plugins {
    id("org.jetbrains.compose") version "0.4.0-build180"
    id("com.android.application")
    kotlin("android")
}

group = "com.vanpra.composematerialdialogs.sample.android"
version = "1.0"

repositories {
    google()
}

dependencies {
    implementation(project(":sample:common"))
    implementation("androidx.activity:activity-compose:1.3.0-alpha06")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "com.vanpra.composematerialdialogs"
        minSdkVersion(24)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}