import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    id("com.android.application")
}

val libpebble3Version = "0.0.1-SNAPSHOT"

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions { jvmTarget.set(JvmTarget.JVM_17) }
    }

    listOf(iosArm64(), iosSimulatorArm64()).forEach { target ->
        target.binaries.framework {
            baseName = "ConsumerKit"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation("io.rebble.libpebble3:libpebble3:$libpebble3Version")
        }
    }
}

android {
    namespace = "dev.sal.libpebble3consumer"
    compileSdk = 36
    defaultConfig {
        applicationId = "dev.sal.libpebble3consumer"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
