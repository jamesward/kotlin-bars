# Kotlin Bars

[![Run on Google Cloud](https://deploy.cloud.run/button.png)](https://deploy.cloud.run)

## Local Dev

Run the web asset server:
```
./gradlew -t :web:run
```

Run the api server:
```
./gradlew :server:bootRun
```

Create container & run with docker:
```
./gradlew bootBuildImage --imageName=kotlin-bars-server

docker run -it --network host \
  -eSPRING_R2DBC_URL=r2dbc:postgresql://localhost/postgres \
  -eSPRING_R2DBC_USERNAME=postgres \
  -eSPRING_R2DBC_PASSWORD=password \
  kotlin-bars-server
```
