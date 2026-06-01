# libpebble3 standalone-consumption check

This is a **separate Gradle build** (its own `settings.gradle.kts`, not part of the
`libpebbleroot` monorepo) that consumes `libpebble3` purely as a published Maven
artifact resolved from `mavenLocal()`. It exists to prove that `libpebble3` works as
a standalone Kotlin Multiplatform library for **Android and iOS**.

It is a KMP app:
- `androidTarget` + `com.android.application` → builds a debug **APK**.
- `iosArm64` / `iosSimulatorArm64` → builds an embeddable **iOS framework**.

`src/commonMain` references the published `io.rebble.libpebblecommon.connection.LibPebble`
API, so a successful build means the artifact resolved and compiled on both platforms.

## Prerequisites

1. JDK 17 selected (the repo pins it via `.tool-versions`):
   ```sh
   export JAVA_HOME="$(asdf where java)"
   ```
   Gradle 8.14.4 crashes on JDK 25 (`IllegalArgumentException: 25`), so do **not** run on the global JDK.

2. Publish the library + its `blobannotations` dependency to `~/.m2` from the monorepo root:
   ```sh
   cd ..   # libpebbleroot
   ./gradlew :blobannotations:publishToMavenLocal :libpebble3:publishToMavenLocal
   ```

## Verify

```sh
export JAVA_HOME="$(asdf where java)"
# Android: resolve + compile + package an APK
./gradlew assembleDebug
# iOS: compile + link a framework (includes the LibPebbleSwift cinterop)
./gradlew linkDebugFrameworkIosSimulatorArm64
```

Outputs:
- `build/outputs/apk/debug/libpebble3-consumer-debug.apk`
- `build/bin/iosSimulatorArm64/debugFramework/ConsumerKit.framework`

## iOS: no separate framework required

`libpebble3` implements its iOS-specific bits (location, via `CoreLocation`) in **pure
Kotlin/Native** using the `platform.CoreLocation` bindings that ship with Kotlin/Native
(see `libpebble3/src/iosMain/.../util/IOSLocation.kt`). `CoreLocation` is a system framework
that Kotlin/Native links automatically, so the consumer's iOS framework block needs **no**
extra `linkerOpts` and **no** hand-built `LibPebbleSwift` framework — just the Maven dependency:

```kotlin
listOf(iosArm64(), iosSimulatorArm64()).forEach { target ->
    target.binaries.framework { baseName = "ConsumerKit" }
}
// commonMain: implementation("io.rebble.libpebble3:libpebble3:0.0.1-SNAPSHOT")
```
