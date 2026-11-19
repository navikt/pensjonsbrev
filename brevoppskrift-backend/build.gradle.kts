import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version libs.versions.kotlinVersion
}

group = "no.nav.pensjon.brev.brevoppskrift"
version = "0.0.1"

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
        useJUnitPlatform()
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
}

val authoringTools = sourceSets.create("authoringTools")
configurations[authoringTools.implementationConfigurationName].extendsFrom(configurations.implementation.get())
configurations[authoringTools.runtimeOnlyConfigurationName].extendsFrom(configurations.runtimeOnly.get())
authoringTools.compileClasspath += sourceSets.main.get().output.classesDirs
authoringTools.runtimeClasspath += sourceSets.main.get().output.classesDirs

dependencies {
    add(authoringTools.implementationConfigurationName, "org.jetbrains.kotlin:kotlin-compiler-embeddable:${libs.versions.kotlinVersion.get()}")
    testImplementation(files(authoringTools.output.classesDirs))
    testRuntimeOnly(files(configurations[authoringTools.runtimeClasspathConfigurationName]))
}

val catalogueResources = layout.buildDirectory.dir("generated/authoring/resources")
sourceSets.main {
    resources.srcDir(catalogueResources)
}

val generateSourceCatalogue by tasks.registering(JavaExec::class) {
    group = "build"
    description = "Import visible Kotlin templates from the build's Git commit into an immutable resource."
    dependsOn(tasks.named(authoringTools.classesTaskName))
    classpath = authoringTools.runtimeClasspath
    mainClass.set("no.nav.pensjon.brev.brevoppskrift.katalog.SourceCatalogueBuilderKt")
    args(rootProject.projectDir.absolutePath, catalogueResources.get().file("authoring/source-catalogue.json").asFile.absolutePath)
    outputs.dir(catalogueResources)
    outputs.upToDateWhen { false }
}

tasks.processResources {
    dependsOn(generateSourceCatalogue)
}
