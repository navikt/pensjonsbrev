plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

val catalogVersions = mapOf(
    "ktorVersion" to libs.versions.ktorVersion.get(),
    "exposedVersion" to libs.versions.exposedVersion.get(),
)

tasks.processResources {
    inputs.properties(catalogVersions)
    filesMatching("catalog-versions.properties") {
        expand(catalogVersions)
    }
}
