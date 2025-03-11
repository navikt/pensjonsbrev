import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaTarget: String by System.getProperties()

plugins {
    application
    kotlin("jvm")
    id("com.gradleup.shadow")
}

group = "no.nav.pensjon.brev.skribenten"
version = "0.0.1"
application {
    mainClass.set("no.nav.pensjon.brev.skribenten.SkribentenAppKt")
}

tasks {
    named<ShadowJar>("shadowJar") {
        /* Lager en FatJar. Dette er en workaround for å få med plugin-konfig som Flyway trenger,
        henta fra https://stackoverflow.com/a/77237118
        Om du endrer denne til FatJar, start først appen i en container og se at databasemigreringa fortsatt fungerer
         */
        mergeServiceFiles()
        archiveFileName.set("app.jar")
    }
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
    implementation(libs.ktor.server.callId)
    implementation(libs.ktor.server.callLogging)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.core.jvm)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.netty.jvm)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.caching.headers)
    implementation(libs.ktor.server.request.validation)

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
    implementation(libs.ktor.server.caching.headers.jvm)

    // Test
    testImplementation(libs.bundles.junit)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.ktor.client.mock)
    testImplementation(libs.mockk)
    testImplementation(libs.assertJ)
    testImplementation(libs.testcontainers.postgresql)

}