import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler

class CommonModulePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            applyPlugins()
            dependenciesConf()
        }
    }

    private fun Project.applyPlugins() {
        plugins.run {
            apply("com.android.library")
            apply("kotlin-android")
            apply("com.vanniktech.maven.publish")
            apply("shot")
        }
    }

    private fun Project.dependenciesConf() {
        dependencies.apply {
            implementation(Dependencies.Kotlin.stdlib)
            implementation(Dependencies.AndroidX.coreKtx)
            implementation(Dependencies.AndroidX.appcompat)
            implementation(Dependencies.material)

            implementation(Dependencies.AndroidX.Compose.ui)
            implementation(Dependencies.AndroidX.Compose.material)
            implementation(Dependencies.AndroidX.Compose.materialIconsExtended)

            androidTestImplementation(Dependencies.AndroidX.Compose.activity)
            androidTestImplementation(Dependencies.AndroidX.Compose.testing)
            androidTestImplementation(Dependencies.AndroidX.Testing.core)
            androidTestImplementation(Dependencies.AndroidX.Testing.rules)
            androidTestImplementation(Dependencies.AndroidX.Testing.runner)
            add("androidTestImplementation", project(":test-utils"))
        }
    }

    private fun DependencyHandler.implementation(dependency: String) {
        add("implementation", dependency)
    }

    private fun DependencyHandler.androidTestImplementation(dependency: String) {
        add("androidTestImplementation", dependency)
    }
}