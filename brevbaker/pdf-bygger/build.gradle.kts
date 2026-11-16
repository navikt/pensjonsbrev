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
    implementation(platform(libs.netty.bom)) // Vi opplever ofte at netty har sårbarheter, så vi ønsker å enkelt kunne patche denne separat
    implementation(libs.bundles.logging)
    implementation(ktorLibs.serialization.jackson)
    implementation(ktorLibs.server.auth)
    implementation(ktorLibs.server.auth.jwt)
    implementation(ktorLibs.server.callId)
    implementation(ktorLibs.server.callLogging)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.netty)
    implementation(ktorLibs.server.statusPages)
    implementation(libs.micrometer.prometheus)
    implementation(ktorLibs.server.metrics)
    implementation(ktorLibs.server.metrics.micrometer)

    implementation(publishedLibs.brevbaker.api) // trengs fortsatt fordi gammel letterMarkup ligger her. Kan fjernes når den er borte.
    implementation(publishedLibs.markup.model)
    implementation(project(":brevbaker:serialization"))
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.bundles.junit)
    testImplementation(ktorLibs.server.testHost) {
        exclude("org.jetbrains.kotlin", "kotlin-test")
    }
    testImplementation(ktorLibs.client.cio)
    testImplementation(ktorLibs.client.contentNegotiation)
    testImplementation(publishedLibs.markup.dsl)
    testImplementation(libs.testcontainers.core)
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}