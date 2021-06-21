plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-kapt")
}

android {
    compileSdk = 30
    buildToolsVersion = "30.0.3"

    defaultConfig {

        applicationId = "com.vanpra.composematerialdialogs"
        minSdk = 23
        targetSdk = 30

        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packagingOptions.excludes.addAll(
        listOf(
            "META-INF/LICENSE",
            "META-INF/AL2.0",
            "META-INF/**",
            "META-INF/*.kotlin_module"
        )
    )

    buildFeatures {
        viewBinding = true
        compose = true
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.AndroidX.Compose.version
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":datetime"))
    implementation(project(":color"))

//    implementation(Dependencies.ComposeMaterialDialogs.core)
//    implementation(Dependencies.ComposeMaterialDialogs.datetime)
//    implementation(Dependencies.ComposeMaterialDialogs.color)

    implementation(Dependencies.Kotlin.stdlib)

    implementation(Dependencies.AndroidX.Compose.ui)
    implementation(Dependencies.AndroidX.Compose.material)
    implementation(Dependencies.AndroidX.Compose.materialIconsExtended)
    implementation(Dependencies.AndroidX.Compose.activity)
    implementation(Dependencies.AndroidX.Compose.navigation)

    implementation(Dependencies.AndroidX.coreKtx)
    implementation(Dependencies.AndroidX.appcompat)

    implementation(Dependencies.material)

    implementation(kotlin("stdlib-jdk8"))

    androidTestImplementation(Dependencies.AndroidX.Compose.testing)
    coreLibraryDesugaring(Dependencies.desugar)
}
