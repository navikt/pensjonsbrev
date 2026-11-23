plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
    implementation(ktorLibs.plugins.ktor.map { "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}" })
    implementation(libs.plugins.gradle.node.map { "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}" })
}
