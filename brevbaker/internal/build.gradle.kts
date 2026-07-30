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
    // Den utvidede (id-eksplisitte) markup-DSL-en. Ligger i markups `apiInternal`-kildesett, som har
    // friend-tilgang til markups internals og aldri publiseres, og hentes derfor som lokal jar.
    api(project(path = ":brevbaker:markup", configuration = "apiInternalElements"))
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
