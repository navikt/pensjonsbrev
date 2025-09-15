import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaTarget: String by System.getProperties()

plugins {
    application
    kotlin("jvm")
    alias(libs.plugins.ksp) apply true
    alias(libs.plugins.ktor) apply true
}

group = "no.nav.pensjon.brev"
version = "0.0.1-SNAPSHOT"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

ktor {
    fatJar {
        archiveFileName.set("${project.name}.jar")
    }
}

repositories {
    mavenLocal()
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
}

tasks.compileTestKotlin {
    compilerOptions.optIn.add("no.nav.brev.InterneDataklasser")
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

kotlin {
    sourceSets {
        main {
            kotlin.srcDir("build/generated/ksp/main/kotlin")
        }
        test {
            kotlin.srcDir("build/generated/ksp/test/kotlin")
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.bundles.logging)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.jackson)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.callId)
    implementation(libs.ktor.server.callLogging)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.swagger)
    implementation(libs.ktor.client.encoding)

    implementation(project(":pensjonsmaler"))
    implementation(project(":aldersmaler"))
    implementation(project(":ufoeremaler"))
    implementation(project(":etterlattemaler"))
    implementation(project(":brevbaker"))
    ksp(project(":template-model-generator"))

    implementation(libs.jackson.datatype.jsr310) {
        because("we require deserialization/serialization of java.time.LocalDate")
    }

    implementation(libs.unleash)

    implementation(libs.bundles.metrics)

    // JUnit 5
    testImplementation(libs.bundles.junit)
    testImplementation(libs.hamkrest)
    testImplementation(libs.ktor.server.test.host)

    testImplementation(libs.pdfbox)

    testImplementation(testFixtures(project(":brevbaker")))
}

