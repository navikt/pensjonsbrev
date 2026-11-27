import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaTarget: String by System.getProperties()

plugins {
    application
    kotlin("jvm")
    kotlin("plugin.serialization") version libs.versions.kotlinVersion
    id("brevoppskrift.authoring-tools")
    id("brevoppskrift.verify-generated-templates")
    id("pensjonsbrev.openapi-typescript")
}

openApiTypes {
    npmDirectory.set(layout.projectDirectory.dir("openapi"))
    outputFile.set(rootProject.layout.projectDirectory.file("brevoppskrift-web/frontend/src/verksted/api-types.ts"))
}

group = "no.nav.pensjon.brev.brevoppskrift"
version = "0.0.1"

application {
    mainClass.set("no.nav.pensjon.brev.brevoppskrift.BrevoppskriftAppKt")
}

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
    build {
        dependsOn(installDist)
    }
}

dependencies {
    implementation(platform(libs.netty.bom))
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.client.cio)
    implementation(ktorLibs.server.netty)
    implementation(ktorLibs.server.di)
    implementation(ktorLibs.server.callId)
    implementation(ktorLibs.server.callLogging)
    implementation(ktorLibs.server.auth)
    implementation(ktorLibs.server.auth.jwt)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.bodyLimit)
    implementation(ktorLibs.server.statusPages)
    implementation(ktorLibs.server.metrics.micrometer)
    implementation(ktorLibs.serialization.jackson)
    implementation(ktorLibs.server.swagger)
    implementation(ktorLibs.openapiSchema.reflect)
    implementation(platform(libs.log4j.bom))
    implementation(libs.bundles.logging)
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
    testImplementation(ktorLibs.client.mock)
    testImplementation(ktorLibs.server.testHost) {
        exclude("org.jetbrains.kotlin", "kotlin-test")
    }
}
