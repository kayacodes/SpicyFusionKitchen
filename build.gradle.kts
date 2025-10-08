// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
}
val detektConfig = files("$rootDir/config/detekt/detekt.yml")

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension>("detekt") {
        toolVersion = "1.23.8"
        config = detektConfig
        buildUponDefaultConfig = true
        allRules = false
        autoCorrect = true

        // Set the task group here
        tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
            group = "verification"
        }
    }

    dependencies.add("detektPlugins", "io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
}
