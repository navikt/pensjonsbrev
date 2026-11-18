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
        useJUnitPlatform()
    }
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(platform(libs.jackson.bom))
    implementation(libs.jackson.module.kotlin)
    implementation(libs.jackson.datatype.jsr310)

    testImplementation(libs.bundles.junit)
}

val authoringTools = sourceSets.create("authoringTools")
configurations[authoringTools.implementationConfigurationName].extendsFrom(configurations.implementation.get())
configurations[authoringTools.runtimeOnlyConfigurationName].extendsFrom(configurations.runtimeOnly.get())
authoringTools.compileClasspath += sourceSets.main.get().output.classesDirs
authoringTools.runtimeClasspath += sourceSets.main.get().output.classesDirs

dependencies {
    add(authoringTools.implementationConfigurationName, kotlin("reflect"))
    testImplementation(files(authoringTools.output.classesDirs))
    testRuntimeOnly(files(configurations[authoringTools.runtimeClasspathConfigurationName]))
}

val generateAuthoringApi by tasks.registering(JavaExec::class) {
    group = "build"
    description = "Generate OpenAPI component schemas and TypeScript from serialized Kotlin properties and Jackson subtypes."
    dependsOn(tasks.named(authoringTools.classesTaskName))
    classpath = authoringTools.runtimeClasspath
    mainClass.set("no.nav.pensjon.brev.brevoppskrift.kotlin.ApiContractGeneratorKt")
    args(
        sourceSets.main.get().output.classesDirs.asPath,
        rootProject.file("brevoppskrift-web/frontend/src/api/brevoppskrift-api.generated.ts").absolutePath,
        layout.buildDirectory.file("generated/authoring/openapi.json").get().asFile.absolutePath,
    )
    inputs.files(sourceSets.main.get().output.classesDirs)
    outputs.file(rootProject.file("brevoppskrift-web/frontend/src/api/brevoppskrift-api.generated.ts"))
    outputs.file(layout.buildDirectory.file("generated/authoring/openapi.json"))
}
