import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    alias(libs.plugins.kotlin.serialization)
    application
}

group="no.nav.pensjon.brev"
version="0.0.1-SNAPSHOT"

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(javaTarget))
    }
}

sourceSets {
    main {
        resources.srcDir(rootProject.layout.projectDirectory.dir("resources"))
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
            excludeTags = setOf("integration-test")
        }
    }
    val test by testing.suites.existing(JvmTestSuite::class)
    named<Test>("integrationTest") {
        testClassesDirs = files(test.map { it.sources.output.classesDirs })
        classpath = files(test.map { it.sources.runtimeClasspath })
    }
}

dependencies {
    implementation(platform(libs.log4j.bom))
    implementation(libs.bundles.logging)
    implementation(platform(libs.ktor.bom))
    implementation(libs.ktor.serialization.jackson)
    implementation(libs.ktor.server.callId)
    implementation(libs.ktor.server.callLogging)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.bundles.metrics)

    implementation(publishedLibs.brevbaker.api) // trengs fortsatt fordi gammel letterMarkup ligger her. Kan fjernes når den er borte.
    implementation(publishedLibs.markup.model)
    implementation(project(":brevbaker:serialization"))
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.bundles.junit)
    testImplementation(libs.ktor.server.test.host) {
        exclude("org.jetbrains.kotlin", "kotlin-test")
    }
    testImplementation(libs.ktor.client.cio)
    testImplementation(libs.ktor.client.content.negotiation)
    testImplementation(publishedLibs.markup.dsl)
    testImplementation(libs.testcontainers.core)
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}