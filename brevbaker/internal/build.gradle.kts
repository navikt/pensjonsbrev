import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    id("java-library")
    id("java-test-fixtures")
}

group = "no.nav.brev.brevbaker"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(libs.brevbaker.common)
    api(libs.brevbaker.markup)
    api(libs.jackson.databind)
    api(libs.jackson.annotations)
    api(libs.jackson.datatype.jsr310) {
        because("we require deserialization/serialization of java.time.LocalDate")
    }
    api(libs.jackson.module.kotlin) {
        because("markup bruker value classes og default-verdier i konstruktører, som Jackson kun ser med kotlin-modulen")
    }

    testImplementation(libs.bundles.junit)

    testFixturesApi(libs.testcontainers.core)
    testFixturesImplementation(libs.bundles.logging)
    testFixturesImplementation(libs.ktor.client.cio)
    testFixturesImplementation(libs.ktor.client.content.negotiation)
    testFixturesImplementation(libs.ktor.serialization.jackson)
    testFixturesImplementation(libs.jackson.datatype.jsr310)
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(javaTarget))
        // Modulen er den eneste tiltenkte brukeren av markups interne konstruksjons-seams.
        optIn.add("no.nav.brev.brevbaker.markup.MarkupInternalApi")
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
}
