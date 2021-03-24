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

Run the Android client:

1. [Download Android Command Line Tools:](https://developer.android.com/studio)

1. Install the SDK:
    ```
    mkdir android-sdk
    cd android-sdk
    unzip PATH_TO_SDK_ZIP/sdk-tools-linux-VERSION.zip
    tools/bin/sdkmanager --update
    tools/bin/sdkmanager "platforms;android-30" "build-tools;30.0.2" "extras;google;m2repository" "extras;android;m2repository"
    tools/bin/sdkmanager --licenses
    ```

1. Set an env var
    ```
    export ANDROID_SDK_ROOT=PATH_TO_SDK/android-sdk
    echo "sdk.dir=$ANDROID_SDK_ROOT" > local.properties
    ```

1. Run the build from this project's dir:
    ```
    ./gradlew :android:build
    ```

1. For a physical device, [setup adb](https://developer.android.com/studio/run/device)

1. Run on a device using an external server:
    ```
    ./gradlew android:installDebug -PbarsUrl=https://YOUR_URL
    ```

1. Or to run from Android Studio / IntelliJ, create a `gradle.properties` file in your root project directory containing:
    ```
    barsUrl=http://YOUR_URL:8080
    ```

   And setup the activity to first run *Gradle-aware Make* with a task of `:android:assembleDebug`
