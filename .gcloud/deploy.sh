#!/bin/bash

# note: requires jdk11
# note: requires gcloud beta

export CLOUDSDK_CORE_DISABLE_PROMPTS=1

declare project=$GOOGLE_CLOUD_PROJECT
declare service=$K_SERVICE
declare region=$GOOGLE_CLOUD_REGION

gcloud services enable run.googleapis.com --project=$project
gcloud services enable containerregistry.googleapis.com --project=$project

# Once an instance is created with a name, you can never create another with the same name, even after it is deleted.
# So we pick a random 8 numbers to append to the instance name.
declare instance="$service-$(cat /dev/urandom | tr -dc '0-9' | fold -w 8 | head -n 1)"


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
    --database-version=POSTGRES_9_6 \
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