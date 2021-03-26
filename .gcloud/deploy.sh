#!/bin/bash

set -uxo pipefail

# note: requires jdk11
# note: requires gcloud beta
#: <<'END'
#END

if [[ -z "${GOOGLE_CLOUD_PROJECT}" ]]; then
  echo "GOOGLE_CLOUD_PROJECT env var not set"
  exit 1
fi

if [[ -z "${K_SERVICE}" ]]; then
  echo "K_SERVICE env var not set"
  exit 1
fi

if [[ -z "${GOOGLE_CLOUD_REGION}" ]]; then
  echo "GOOGLE_CLOUD_REGION env var not set"
  exit 1
fi

if [[ -z "${DOMAINS}" ]]; then
  echo "DOMAINS env var not set"
  exit 1
fi

declare project=$GOOGLE_CLOUD_PROJECT
declare service=$K_SERVICE
declare region=$GOOGLE_CLOUD_REGION

export CLOUDSDK_CORE_DISABLE_PROMPTS=1

# Once an instance is created with a name, you can never create another with the same name, even after it is deleted.
# So we pick a random 8 numbers to append to the instance name.
#declare instance="$service-$(cat /dev/urandom | tr -dc '0-9' | fold -w 8 | head -n 1)"
declare instance=$service

declare bucket=$project-$region-$instance

# Container

./gradlew :server:bootBuildImage --imageName=gcr.io/$project/$instance
docker push gcr.io/$project/$instance


# VPC

gcloud services enable servicenetworking.googleapis.com --project=$project

gcloud compute networks create $instance \
    --project=$project

gcloud compute addresses create google-managed-services-$instance \
    --global \
    --purpose=VPC_PEERING \
    --prefix-length=16 \
    --network=$instance \
    --project=$project

gcloud services vpc-peerings connect \
    --service=servicenetworking.googleapis.com \
    --ranges=google-managed-services-$instance \
    --network=$instance \
    --project=$project


# SQL

gcloud services enable sqladmin.googleapis.com --project=$project

declare db_user=postgres
declare db_pass=$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 64 | head -n 1)
declare db_name=postgres

echo "Creating Cloud SQL instance named $instance"
gcloud beta sql instances create $instance \
    --database-version=POSTGRES_13 \
    --tier=db-f1-micro \
    --region=$region \
    --project=$project \
    --root-password=$db_pass \
    --network=$instance \
    --no-assign-ip

declare db_host=$(gcloud sql instances describe $instance --project=$project --format='value(ipAddresses.ipAddress)')


# VPC Connector

gcloud services enable vpcaccess.googleapis.com --project=$project

declare range="10.$(( ( RANDOM % 64 ) + 1 )).0.0/28"
gcloud beta compute networks vpc-access connectors create $instance \
    --network=$instance \
    --range=$range \
    --region=$region \
    --project=$project


# Cloud Run

gcloud services enable run.googleapis.com --project=$project
gcloud services enable containerregistry.googleapis.com --project=$project

gcloud run deploy $instance \
    --allow-unauthenticated \
    --image=gcr.io/$project/$instance \
    --vpc-connector=$instance \
    --set-env-vars=SPRING_R2DBC_URL=r2dbc:postgresql://$db_host/$db_name,SPRING_R2DBC_USERNAME=$db_user,SPRING_R2DBC_PASSWORD=$db_pass \
    --platform=managed \
    --region=$region \
    --project=$project


# Init Schema

docker run --rm \
  -v$HOME/.config/gcloud:/root/.config/gcloud \
  -ePROJECT_ID=$project \
  -eSERVICE=$instance \
  -eZONE=$region-a \
  -eMACHINE_TYPE=e2-small \
  -eARGS=init \
  gcr.io/jamesward/one-off-cloud-run


# Asset creation & upload

./gradlew :web:browserDistribution

gsutil mb -p $project -c standard -l $region -b on gs://$bucket

gsutil cp web/build/distributions/** gs://$bucket

gsutil iam ch allUsers:objectViewer gs://$bucket

gsutil web set -m index.html gs://$bucket

# GCLB

gcloud compute addresses create $instance-ip \
    --network-tier=PREMIUM \
    --ip-version=IPV4 \
    --global \
    --project=$project

declare ip=$(gcloud compute addresses describe $instance-ip --format="get(address)" --global --project=$project)

gcloud compute backend-buckets create $instance-bucket \
    --gcs-bucket-name=$bucket \
    --enable-cdn \
    --project=$project

gcloud compute backend-services create $instance-service \
    --global \
    --project=$project

gcloud beta compute network-endpoint-groups create $instance-neg-$region \
    --region=$region \
    --network-endpoint-type=SERVERLESS \
    --cloud-run-service=$instance \
    --project=$project

gcloud compute backend-services add-backend $instance-service \
    --global \
    --network-endpoint-group-region=$region \
    --network-endpoint-group=$instance-neg-$region \
    --project=$project

gcloud compute url-maps create $instance-url-map \
    --global \
    --default-backend-bucket=$instance-bucket \
    --project=$project

gcloud compute url-maps add-path-matcher $instance-url-map \
    --path-matcher-name=api-matcher \
    --path-rules=/api/*=$instance-service \
    --default-backend-bucket=$instance-bucket \
    --project=$project

gcloud beta compute ssl-certificates create $instance-cert \
    --domains=$DOMAINS \
    --project=$project

gcloud compute target-https-proxies create $instance-https-proxy \
    --url-map=$instance-url-map \
    --ssl-certificates=$instance-cert \
    --project=$project

gcloud compute forwarding-rules create $instance-https \
    --address=$ip \
    --global \
    --target-https-proxy=$instance-https-proxy \
    --ports=443 \
    --project=$project

# http to https redirect

gcloud compute url-maps import $instance-httpredirect \
    --global \
    --source=/dev/stdin \
    --project=$project <<EOF
name: $instance-httpredirect
defaultUrlRedirect:
  redirectResponseCode: MOVED_PERMANENTLY_DEFAULT
  httpsRedirect: True
EOF

gcloud compute target-http-proxies create $instance-http-proxy \
    --url-map=$instance-httpredirect \
    --project=$project

gcloud compute forwarding-rules create $instance-http \
    --address=$ip \
    --global \
    --target-http-proxy=$instance-http-proxy \
    --ports=80 \
    --project=$project

readonly LB_IP=$(gcloud compute addresses describe $instance-ip --global --format='value(address)' --project=$project)

echo -e "\nPoint $DOMAINS to $LB_IP\n"
