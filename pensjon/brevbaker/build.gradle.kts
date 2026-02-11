import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaTarget: String by System.getProperties()

plugins {
    application
    kotlin("jvm")
    alias(libs.plugins.ksp) apply true
}

group = "no.nav.pensjon.brev"
version = "0.0.1-SNAPSHOT"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
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
    build {
        dependsOn(installDist)
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
    named<Test>("integrationTest") {
        testClassesDirs = files(test.map { it.sources.output.classesDirs })
        classpath = files(test.map { it.sources.runtimeClasspath })
    }
    named<Test>("manualTest") {
        testClassesDirs = files(test.map { it.sources.output.classesDirs })
        classpath = files(test.map { it.sources.runtimeClasspath })
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

    implementation(project(":pensjon:maler"))
    implementation(project(":alder:maler"))
    implementation(project(":ufoere:maler"))
    implementation(project(":etterlattemaler"))
    implementation(project(":brevbaker:core"))
    ksp(project(":brevbaker:template-model-generator"))

    implementation(libs.pdfbox)

    implementation(libs.jackson.datatype.jsr310) {
        because("we require deserialization/serialization of java.time.LocalDate")
    }

    implementation(libs.unleash)

    implementation(libs.bundles.metrics)

    testImplementation(libs.bundles.junit)
    testImplementation(libs.ktor.server.test.host) {
        exclude("org.jetbrains.kotlin", "kotlin-test")
    }

    testImplementation(testFixtures(project(":brevbaker:core")))
    testImplementation(testFixtures(project(":brevbaker:dsl")))
}

