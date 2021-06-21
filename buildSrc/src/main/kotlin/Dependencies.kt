object Dependencies {
    const val material = "com.google.android.material:material:1.4.0-rc01"
    const val desugar = "com.android.tools:desugar_jdk_libs:1.1.5"

    object ComposeMaterialDialogs {
        const val version = "0.4.3"

        const val core = "io.github.vanpra.compose-material-dialogs:core:$version"
        const val datetime = "io.github.vanpra.compose-material-dialogs:datetime:$version"
        const val color = "io.github.vanpra.compose-material-dialogs:color:$version"
    }

    object Ktlint {
        const val version = "0.41.0"
    }

    object Accompanist {
        private const val version = "0.12.0"
        const val pager = "com.google.accompanist:accompanist-pager:$version"
    }

    object Kotlin {
        private const val version = "1.5.10"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
    }

    object Shot {
        private const val version = "5.10.5"
        const val shot = "com.karumi:shot-android:$version"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.4.0-alpha02"
        const val coreKtx = "androidx.core:core-ktx:1.6.0-rc01"

        object Testing {
            const val version = "1.4.0-alpha06"
            const val core = "androidx.test:core:$version"
            const val rules = "androidx.test:rules:$version"
            const val runner = "androidx.test:runner:$version"
        }

        object Compose {
            const val version = "1.0.0-beta09"

            const val ui = "androidx.compose.ui:ui:$version"
            const val material = "androidx.compose.material:material:$version"
            const val materialIconsExtended =
                "androidx.compose.material:material-icons-extended:$version"

            const val testing = "androidx.compose.ui:ui-test-junit4:$version"
            const val activity = "androidx.activity:activity-compose:1.3.0-beta02"
            const val navigation = "androidx.navigation:navigation-compose:2.4.0-alpha03"
        }
    }
}
