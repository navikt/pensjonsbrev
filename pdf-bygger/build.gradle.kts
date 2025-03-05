import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    application
    alias(libs.plugins.ktor) apply true
}

group="no.nav.pensjon.brev"
version="0.0.1-SNAPSHOT"

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
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.logback.classic)
    implementation(libs.ktor.serialization.jackson)
    implementation(libs.ktor.server.call.id)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.logback.encoder)
    implementation(libs.ktor.server.compression.jvm)

    // Metrics
    implementation(libs.ktor.server.metrics)
    implementation(libs.ktor.server.metrics.micrometer)
    implementation(libs.micrometer.prometheus)

    implementation(project(":brevbaker-dsl"))
    implementation(libs.brevbaker.common)

    implementation(libs.jackson.datatype.jsr310) {
        because("we require deserialization/serialization of java.time.LocalDate")
    }

    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.hamkrest)
    testImplementation(libs.ktor.server.test.host)

    testImplementation(project(":brevbaker"))
    testImplementation(testFixtures(project(":brevbaker")))
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

ktor {
    fatJar {
        archiveFileName.set("${project.name}.jar")
    }
}