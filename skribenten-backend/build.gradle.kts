import com.github.gradle.node.npm.task.NpmTask
import com.github.gradle.node.npm.task.NpxTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaTarget: String by System.getProperties()

plugins {
    application
    kotlin("jvm")
    id("io.ktor.plugin") version "3.5.0"
    alias(libs.plugins.gradle.node)
}

ktor {
    openApi {
        enabled = true
        codeInferenceEnabled = true
    }
}

node {
    nodeProjectDir.set(rootProject.file("skribenten-web/frontend"))
    download.set(true)
    version.set("24.14.1")
}

val generateApiTypes by tasks.registering(NpxTask::class) {
    description = "Generates TypeScript types from the OpenAPI spec into skribenten-web/frontend/src/types/skribenten-api.ts"
    dependsOn(tasks.test, tasks.npmInstall)
    command.set("openapi-typescript")
    val specFile = layout.buildDirectory.file("openapi-spec.yaml")
    val outputFile = rootProject.file("skribenten-web/frontend/src/types/skribenten-api.ts")
    args.set(
        listOf(
            specFile.get().asFile.absolutePath,
            "--output", outputFile.absolutePath,
            "--root-types",
            "--root-types-no-schema-prefix",
        )
    )
    inputs.file(specFile)
    outputs.file(outputFile)
}

val typeCheckFrontend by tasks.registering(NpmTask::class) {
    description = "Runs TypeScript type checking on the frontend after API type generation"
    dependsOn(generateApiTypes)
    npmCommand.set(listOf("run", "check-types"))
    inputs.files(rootProject.fileTree("skribenten-web/frontend/src"))
    outputs.upToDateWhen { true }
}


group = "no.nav.pensjon.brev.skribenten"
version = "0.0.1"
application {
    mainClass.set("no.nav.pensjon.brev.skribenten.SkribentenAppKt")
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
        useJUnitPlatform()
    }
    kotlin {
        compileTestKotlin {
            compilerOptions.optIn.add("no.nav.brev.InterneDataklasser")
        }
    }
    build {
        dependsOn(installDist)
        dependsOn(typeCheckFrontend)
    }
}

sourceSets {
    main {
        resources {
            srcDir("secrets")
        }
    }
}

dependencies {
    // Ktor
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.jackson)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.caching.headers)
    implementation(libs.ktor.server.callId)
    implementation(libs.ktor.server.callLogging)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.swagger)
    implementation(libs.bundles.ktor.openapi)

    // Exposed
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.json)
    implementation(libs.exposed.java.time)
    implementation(libs.postgresql)
    implementation(libs.hikari.cp)

    // Databasemigrering
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)

    // Unleash
    implementation(libs.unleash)

    // Domenemodell
    implementation(libs.brevbaker.common)

    implementation(libs.bundles.logging)

    // Necessary for java.time.LocalDate
    implementation(libs.jackson.datatype.jsr310)

    // Hashing
    implementation(libs.commons.codec)

    // Metrics
    implementation(libs.bundles.metrics)

    // Caching
    implementation(libs.valkey)

    // Test
    testImplementation(libs.bundles.junit)
    testImplementation(libs.ktor.server.test.host) {
        exclude("org.jetbrains.kotlin", "kotlin-test")
    }
    testImplementation(libs.ktor.client.mock)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.jackson.yaml)

}