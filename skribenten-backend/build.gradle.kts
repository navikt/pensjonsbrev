import com.github.gradle.node.npm.task.NpmTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaTarget: String by System.getProperties()

plugins {
    application
    kotlin("jvm")
    kotlin("plugin.serialization") version libs.versions.kotlinVersion
    id("pensjonsbrev.openapi-typescript")
}

openApiTypes {
    npmDirectory.set(rootProject.layout.projectDirectory.dir("skribenten-web/frontend"))
    outputFile.set(rootProject.layout.projectDirectory.file("skribenten-web/frontend/src/types/skribenten-api.ts"))
}

val typeCheckFrontend = tasks.register<NpmTask>("typeCheckFrontend") {
    description = "Runs TypeScript type checking on the frontend after API type generation"
    dependsOn(tasks.named("generateApiTypes"))
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
    implementation(platform(libs.netty.bom)) // Vi opplever ofte at netty har sårbarheter, så vi ønsker å enkelt kunne patche denne separat
    implementation(ktorLibs.client.auth)
    implementation(ktorLibs.client.callId)
    implementation(ktorLibs.client.cio)
    implementation(ktorLibs.client.contentNegotiation)
    implementation(ktorLibs.serialization.jackson)
    implementation(ktorLibs.server.auth)
    implementation(ktorLibs.server.auth.jwt)
    implementation(ktorLibs.server.cachingHeaders)
    implementation(ktorLibs.server.callId)
    implementation(ktorLibs.server.di)
    implementation(ktorLibs.server.callLogging)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.cors)
    implementation(ktorLibs.server.netty)
    implementation(ktorLibs.server.statusPages)
    implementation(ktorLibs.server.swagger)
    implementation(ktorLibs.openapiSchema.reflect)

    // Exposed
    implementation(exposedLibs.core)
    implementation(exposedLibs.dao)
    implementation(exposedLibs.jdbc)
    implementation(exposedLibs.json)
    implementation(exposedLibs.java.time)
    implementation(libs.postgresql)
    implementation(libs.hikari.cp)

    // Databasemigrering
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)

    // Unleash
    implementation(libs.unleash)

    // Domenemodell
    implementation(publishedLibs.brevbaker.api)
    implementation(publishedLibs.markup.model)
    implementation(project(":brevbaker:serialization"))

    // For å merge førsteside med resten av pdf-en
    implementation(libs.pdfbox)

    implementation(platform(libs.log4j.bom))
    implementation(libs.bundles.logging)

    // Necessary for java.time.LocalDate
    // TODO transitivt?
    implementation(platform(libs.jackson.bom))
    implementation(libs.jackson.datatype.jsr310)

    // Hashing
    implementation(libs.commons.codec)

    // Metrics
    implementation(libs.micrometer.prometheus)
    implementation(ktorLibs.server.metrics)
    implementation(ktorLibs.server.metrics.micrometer)
    implementation(platform(libs.opentelemetry.bom))
    implementation(libs.opentelemetry.api)
    implementation(libs.opentelemetry.extension.kotlin)

    // Caching
    implementation(libs.valkey)

    // Test
    testImplementation(libs.bundles.junit)
    testImplementation(libs.opentelemetry.sdk.testing)
    testImplementation(ktorLibs.server.testHost) {
        exclude("org.jetbrains.kotlin", "kotlin-test")
    }
    testImplementation(ktorLibs.client.mock)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.jackson.yaml)
    testImplementation(project(":brevbaker:dsl"))

}