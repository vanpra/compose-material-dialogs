plugins {
    id("com.diffplug.spotless") version "5.9.0"
}

buildscript {
    repositories {
        google()
        maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }

    dependencies {
        classpath(Dependencies.Kotlin.gradlePlugin)
        classpath("com.android.tools.build:gradle:7.0.0-alpha14")
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.14.2")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.4.30")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
        maven { url = uri("https://kotlin.bintray.com/kotlinx/") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }

    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
        kotlinOptions {
            jvmTarget = "1.8"
            useIR = true
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-Xopt-in=androidx.compose.material.ExperimentalMaterialApi",
                "-Xopt-in=androidx.compose.animation.ExperimentalAnimationApi",
                "-Xopt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                "-Xopt-in=com.google.accompanist.pager.ExperimentalPagerApi"
            )
        }
    }
}

subprojects {
    plugins.apply("com.diffplug.spotless")
    spotless {
        kotlin {
            target("**/*.kt")
            ktlint(Dependencies.Ktlint.version)
        }
    }
}

