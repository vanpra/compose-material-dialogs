import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.diffplug.spotless") version "5.14.0"
    id("org.jetbrains.dokka") version "1.4.32"
}

buildscript {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }

    dependencies {
        classpath(Dependencies.Kotlin.gradlePlugin)
        classpath("com.android.tools.build:gradle:7.1.0-alpha02")
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.14.2")
        classpath("com.karumi:shot:5.10.5")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.4.32")
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

    tasks.withType<KotlinCompile>().all {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-Xopt-in=androidx.compose.material.ExperimentalMaterialApi",
                "-Xopt-in=androidx.compose.animation.ExperimentalAnimationApi",
                "-Xopt-in=androidx.compose.ui.test.ExperimentalTestApi",
                "-Xopt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                "-Xopt-in=androidx.compose.ui.ExperimentalComposeUiApi",
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

    tasks.withType<Test> {
        testLogging {
            showStandardStreams = true
        }
    }
}