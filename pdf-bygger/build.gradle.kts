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

tasks {
    test {
        useJUnitPlatform {
            excludeTags = setOf("integration-test")
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

    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.assertJ)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(testFixtures(project(":brevbaker")))
    testImplementation(testFixtures(project(":brevbaker-dsl")))
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

ktor {
    fatJar {
        archiveFileName.set("${project.name}.jar")
    }
}