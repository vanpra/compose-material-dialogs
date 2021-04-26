pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }
    
}

rootProject.name = "compose-material-dialogs"

include(":sample:android")
include(":sample:desktop")
include(":sample:common")
include(":core:common")
include(":core:android")
include(":core:desktop")



