# Kotlin Bars

<!-- [![Run on Google Cloud](https://deploy.cloud.run/button.png)](https://deploy.cloud.run) -->

## API Server

Run the api server:
```
./gradlew :server:bootRun
```

Create a bar:
```
curl -X POST http://localhost:8080/api/bars \
   -H 'Content-Type: application/json' \
   -d '{"name": "Test"}'
```

Fetch the bars: [localhost:8080/api/bars](http://localhost:8080/api/bars)


Create container & run with docker:
```
./gradlew :server:bootBuildImage --imageName=kotlin-bars-server

docker run -it --network host \
  -eSPRING_R2DBC_URL=r2dbc:postgresql://localhost/postgres \
  -eSPRING_R2DBC_USERNAME=postgres \
  -eSPRING_R2DBC_PASSWORD=password \
  kotlin-bars-server
```


## Web Server

Run the web asset server:
```
./gradlew -t :web:run
```


## Android Client

*NOTE: You must use JDK 11 or higher*

Start the server:

  ```sh
  ./gradlew :server:bootRun
  ```

Run the client:

1. [Download Android Command Line Tools](https://developer.android.com/studio)

1. Install the SDK:

    ```sh
    mkdir android-sdk
    cd android-sdk
    unzip PATH_TO_SDK_ZIP/sdk-tools-linux-VERSION.zip
    mv cmdline-tools latest
    mkdir cmdline-tools
    mv latest cmdline-tools
    cmdline-tools/latest/bin/sdkmanager --update
    cmdline-tools/latest/bin/sdkmanager "platforms;android-31" "build-tools;31.0.0" "extras;google;m2repository" "extras;android;m2repository"
    cmdline-tools/latest/bin/sdkmanager --licenses
    ```

1. Set an env var pointing to the `android-sdk`

    ```sh
    export ANDROID_SDK_ROOT=PATH_TO_SDK/android-sdk
    echo "sdk.dir=$(realpath $ANDROID_SDK_ROOT)" > local.properties
    ```

1. Run the build from this project's dir:

    ```sh
    ./gradlew :android:build
    ```

1. You can either run on an emulator or a physical device and you can either
   connect to the server running on your local machine, or connect to a server
   you deployed on the cloud.

   * Emulator + Local Server:

      * From the command line:

        ```sh
        ./gradlew :android:installDebug
        ```

      * From Android Studio / IntelliJ, navigate to
        `android/src/main/kotlin/kotlinbars/android` and right-click on
        `MainActivity` and select `Run`.

   * Physical Device + Local Server:

      * From the command line:

         1. [Setup adb](https://developer.android.com/studio/run/device)
         1. `./gradlew :android:installDebug -PserverUrl=http://YOUR_MACHINE_IP:50051/`

      * From Android Studio / IntelliJ:

         1. Create a `gradle.properties` file in your root project directory containing:

             ```sh
             serverUrl=http://YOUR_MACHINE_IP:50051/
             ```

         1. Navigate to `android/src/main/kotlin/kotlinbars/android` and right-click on `MainActivity` and select `Run`.

   * Emulator or Physical Device + Cloud:

      * From the command line:

         1. [setup adb](https://developer.android.com/studio/run/device)
         1. `./gradlew :android:installDebug -PserverUrl=https://YOUR_SERVER/`

      * From Android Studio / IntelliJ:

         1. Create a `gradle.properties` file in your root project directory containing:

             ```sh
             serverUrl=https://YOUR_SERVER/
             ```

         1. Navigate to `android/src/main/kotlin/kotlinbars/android` and right-click on `MainActivity` and select `Run`.


## Desktop Client

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

## CLI Client

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

## GitHub Actions

Testing GitHub Actions:
```
git tag -d v1.0.0; git push --delete origin v1.0.0; git commit -a --allow-empty-message --no-edit; git tag v1.0.0; git push; git push origin v1.0.0
```