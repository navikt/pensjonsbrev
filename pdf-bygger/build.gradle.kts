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
    register<Test>("integrationTest") {
        testClassesDirs = files(test.map { it.sources.output.classesDirs })
        classpath = files(test.map { it.sources.runtimeClasspath })
        outputs.doNotCacheIf("Output of this task is pdf from pdf-bygger which is not cached") { true }
        systemProperties["junit.jupiter.execution.parallel.enabled"] = true
        systemProperties["junit.jupiter.execution.parallel.mode.default"] = "concurrent"
        systemProperties["junit.jupiter.execution.parallel.config.strategy"] = "dynamic"
        systemProperties["junit.jupiter.execution.parallel.config.dynamic.factor"] = 0.5
        group = LifecycleBasePlugin.VERIFICATION_GROUP
        useJUnitPlatform {
            includeTags = setOf("integration-test")
        }
    }

    register<Test>("manualTest") {
        testClassesDirs = files(test.map { it.sources.output.classesDirs })
        classpath = files(test.map { it.sources.runtimeClasspath })
        group = LifecycleBasePlugin.VERIFICATION_GROUP
        useJUnitPlatform {
            includeTags = setOf("manual-test")
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.bundles.logging)
    implementation(libs.kotlinx.coroutines.slf4j)
    implementation(libs.ktor.serialization.jackson)
    implementation(libs.ktor.server.callId)
    implementation(libs.ktor.server.callLogging)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.compression.jvm)
    implementation(libs.bundles.metrics)

    implementation(project(":brevbaker-dsl"))
    implementation(libs.brevbaker.common)

    implementation(libs.jackson.datatype.jsr310) {
        because("we require deserialization/serialization of java.time.LocalDate")
    }

    testImplementation(libs.bundles.junit)
    testImplementation(libs.ktor.server.test.host) {
        exclude("org.jetbrains.kotlin", "kotlin-test")
    }
    testImplementation(testFixtures(project(":brevbaker")))
    testImplementation(testFixtures(project(":brevbaker-dsl")))
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}