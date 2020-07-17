// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()
        maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72")
        classpath("com.android.tools.build:gradle:4.2.0-alpha04")
        classpath("org.jmailen.gradle:kotlinter-gradle:2.4.1")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

