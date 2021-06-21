plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    defaultConfig {
        minSdk = 21
        targetSdk = 30
        compileSdk = 30

        testInstrumentationRunner = "com.karumi.shot.ShotTestRunner"
        testApplicationId = "com.vanpra.composematerialdialogs.test"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packagingOptions.excludes.addAll(
        listOf(
            "META-INF/LICENSE",
            "META-INF/AL2.0",
            "META-INF/**",
            "META-INF/*.kotlin_module",
            "META-INF/*.kotlin_module"
        )
    )

    buildFeatures {
        buildConfig = false
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.AndroidX.Compose.version
    }
}

dependencies {
    api(project(":core"))
    implementation(Dependencies.Kotlin.stdlib)
    implementation(Dependencies.AndroidX.coreKtx)
    implementation(Dependencies.AndroidX.appcompat)
    implementation(Dependencies.material)

    implementation(Dependencies.AndroidX.Compose.ui)
    implementation(Dependencies.AndroidX.Compose.material)
    implementation(Dependencies.AndroidX.Compose.materialIconsExtended)
    implementation(Dependencies.AndroidX.Compose.activity)

    implementation(Dependencies.AndroidX.Compose.testing)
    implementation(Dependencies.Shot.shot)
}
