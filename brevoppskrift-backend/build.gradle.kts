import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version libs.versions.kotlinVersion
}

group = "no.nav.pensjon.brev.brevoppskrift"
version = "0.0.1"

val authoringGitExecutable = providers.gradleProperty("authoringGitExecutable").orElse("/usr/bin/git")

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(javaTarget))
    }
}

tasks {
    compileJava {
        targetCompatibility = javaTarget
    }
    compileTestJava {
        targetCompatibility = javaTarget
    }
    test {
        maxParallelForks = 1
        systemProperty("authoring.repository", rootProject.projectDir.absolutePath)
        systemProperty("authoring.gitExecutable", authoringGitExecutable.get())
        useJUnitPlatform()
    }
}

sourceSets {
    create("authoringTools") {
        compileClasspath += sourceSets.main.get().output.classesDirs
        runtimeClasspath += sourceSets.main.get().output.classesDirs
    }
}
val authoringTools = sourceSets.named("authoringTools").get()

configurations {
    named(authoringTools.implementationConfigurationName) {
        extendsFrom(configurations.implementation.get())
    }
    named(authoringTools.runtimeOnlyConfigurationName) {
        extendsFrom(configurations.runtimeOnly.get())
    }
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(platform(libs.jackson.bom))
    implementation(libs.jackson.module.kotlin)
    implementation(libs.jackson.datatype.jsr310)

    implementation(exposedLibs.core)
    implementation(exposedLibs.jdbc)
    implementation(exposedLibs.json)
    implementation(exposedLibs.java.time)
    implementation(libs.hikari.cp)
    implementation(libs.postgresql)
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)
    implementation(libs.micrometer.prometheus)

    testImplementation(libs.bundles.junit)
    testImplementation(libs.testcontainers.postgresql)

    "authoringToolsImplementation"(libs.kotlin.compiler.embeddable)
    testImplementation(files(authoringTools.output.classesDirs))
    testRuntimeOnly(files(configurations.named(authoringTools.runtimeClasspathConfigurationName)))
}

val catalogueResources = layout.buildDirectory.dir("generated/authoring/resources")
sourceSets.main {
    resources.srcDir(catalogueResources)
}

val generateSourceCatalogue = tasks.register<JavaExec>("generateSourceCatalogue") {
    group = "build"
    description = "Import visible Kotlin templates from the build's Git commit into an immutable resource."
    dependsOn(tasks.named(authoringTools.classesTaskName))
    classpath = authoringTools.runtimeClasspath
    mainClass.set("no.nav.pensjon.brev.brevoppskrift.katalog.SourceCatalogueBuilderKt")
    systemProperty("authoring.gitExecutable", authoringGitExecutable.get())
    args(rootProject.projectDir.absolutePath, catalogueResources.get().file("authoring/source-catalogue.json").asFile.absolutePath)
    outputs.dir(catalogueResources)
    outputs.upToDateWhen { false }
}

tasks.processResources {
    dependsOn(generateSourceCatalogue)
}
