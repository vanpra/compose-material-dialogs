plugins {
    id("common-library")
}

android {
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
        compileSdkVersion(30)

        versionCode = 1

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures.compose = true

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.AndroidX.Compose.version
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

val VERSION_NAME: String by project
val mavenCentralRepositoryUsername: String? by project
val mavenCentralRepositoryPassword: String? by project

publishing {
    repositories {
        withType<MavenArtifactRepository> {
            if (name == "local") {
                return@withType
            }

            val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"

            url = if (VERSION_NAME.endsWith("SNAPSHOT")) {
                uri(snapshotsRepoUrl)
            } else {
                uri(releasesRepoUrl)
            }

            credentials {
                username = mavenCentralRepositoryUsername
                password = mavenCentralRepositoryPassword
            }
        }
    }
}