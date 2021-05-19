# Kotlin Bars

<!-- [![Run on Google Cloud](https://deploy.cloud.run/button.png)](https://deploy.cloud.run) -->

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
./gradlew :server:bootBuildImage --imageName=kotlin-bars-server

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
    ./gradlew :android:installDebug -PbarsUrl=https://YOUR_URL/api/bars
    ```

1. Or to run from Android Studio / IntelliJ, create a `local.properties` file in your root project directory containing:
    ```
    barsUrl=http://YOUR_URL:8080/api/bars
    ```

    And setup the activity to first run *Gradle-aware Make* with a task of `:android:assembleDebug`

Run the desktop client:

Run, connecting to the default `http://localhost:8080/api/bars` url (if `barsUrl` is not set in the `local.properties` file):
```
./gradlew :desktop:run
```

Run, connecting to the specified url:
```
./gradlew :desktop:run -PbarsUrl=http://YOUR_URL:8080/api/bars
```

Start a server with Testcontainers and connect to it:
```
./gradlew :desktop:dev
```

Package a native app and run it:
```
./gradlew :desktop:runDistributable
```

Package a native app (for the current platform):
```
./gradlew :desktop:package
```

Run the CLI client:

Run, connecting to the default `http://localhost:8080/api/bars` url (if `barsUrl` is not set in the `local.properties` file):
```
./gradlew :cli:run -q --console=plain
```

Run, connecting to the specified url:
```
./gradlew :cli:run -q --console=plain -PbarsUrl=http://YOUR_URL:8080/api/bars
```

Start a server with Testcontainers and connect to it:
```
./gradlew :cli:dev -q --console=plain
```

Create a CLI Native Image:

Export the location of your GraalVM install:
```
export GRAALVM_HOME=YOUR/graalvm-ce-java11-21.1.0
```

Build the native image:
```
./gradlew :cli:nativeImage
```

Run it:
```
cli/build/native-image/kotlin-bars-cli
```

Testing GitHub Actions:
```
git tag -d v0.0.0; git push --delete origin v0.0.0; git tag v0.0.0; git commit -a --allow-empty-message --no-edit; git push; git push --tags
```