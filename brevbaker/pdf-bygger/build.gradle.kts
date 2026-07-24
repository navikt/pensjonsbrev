import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    application
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
    build {
        dependsOn(installDist)
    }
}

tasks {
    test {
        useJUnitPlatform {
            excludeTags = setOf("integration-test", "manual-test")
        }
    }
    val test by testing.suites.existing(JvmTestSuite::class)
    named<Test>("integrationTest") {
        testClassesDirs = files(test.map { it.sources.output.classesDirs })
        classpath = files(test.map { it.sources.runtimeClasspath })
    }

    named<Test>("manualTest") {
        testClassesDirs = files(test.map { it.sources.output.classesDirs })
        classpath = files(test.map { it.sources.runtimeClasspath })
    }
}

dependencies {
    implementation(libs.bundles.logging)
    implementation(libs.kotlinx.coroutines.slf4j)
    implementation(libs.ktor.serialization.jackson)
    implementation(libs.ktor.server.callId)
    implementation(libs.ktor.server.callLogging)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.bundles.metrics)

    implementation(libs.brevbaker.markup)
    implementation(libs.brevbaker.common)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.jackson.datatype.jsr310) {
        because("we require deserialization/serialization of java.time.LocalDate")
    }

    testImplementation(libs.bundles.junit)
    testImplementation(libs.ktor.server.test.host) {
        exclude("org.jetbrains.kotlin", "kotlin-test")
    }
    testImplementation(libs.ktor.client.cio)
    testImplementation(libs.ktor.client.content.negotiation)
    testImplementation(libs.testcontainers.core)
    testImplementation(libs.assertJ)
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}