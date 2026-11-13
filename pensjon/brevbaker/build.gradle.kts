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
        //brukes for StartupLatencyTest. Øker faktisk hastigheten til testene med 15%~ De kjører fortsatt i parallell, bare ikke egen jvm fork ofte. Default er 8.
        maxParallelForks = 1
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
    implementation(platform(libs.log4j.bom))
    implementation(platform(libs.netty.bom)) // Vi opplever ofte at netty har sårbarheter, så vi ønsker å enkelt kunne patche denne separat
    implementation(libs.bundles.logging)
    implementation(ktorLibs.client.callId)
    implementation(ktorLibs.client.cio)
    implementation(ktorLibs.client.contentNegotiation)
    implementation(ktorLibs.serialization.jackson)
    implementation(ktorLibs.server.auth)
    implementation(ktorLibs.server.auth.jwt)
    implementation(ktorLibs.server.cachingHeaders)
    implementation(ktorLibs.server.callId)
    implementation(ktorLibs.server.callLogging)
    implementation(ktorLibs.server.conditionalHeaders)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.netty)
    implementation(ktorLibs.server.statusPages)
    implementation(ktorLibs.server.swagger)

    implementation(project(":pensjon:maler"))
    implementation(project(":alder:maler"))
    implementation(project(":planlegge-pensjon-maler"))
    implementation(project(":ufoere:maler"))
    implementation(project(":etterlattemaler"))
    implementation(project(":brevbaker:core"))
    implementation(publishedLibs.brevbaker.api)
    implementation(project(":brevbaker:serialization")) {
        because("bruker internalObjectMapper for serialisering av intern trafikk")
    }
    ksp(project(":brevbaker:template-model-generator"))
    kspTest(project(":brevbaker:template-model-generator"))

    testImplementation(libs.pdfbox)

    implementation(platform(libs.jackson.bom))
    implementation(libs.jackson.datatype.jsr310) {
        because("we require deserialization/serialization of java.time.LocalDate")
    }

    implementation(libs.unleash)

    implementation(libs.micrometer.prometheus)
    implementation(ktorLibs.server.metrics)
    implementation(ktorLibs.server.metrics.micrometer)

    testImplementation(libs.bundles.junit)
    testImplementation(ktorLibs.server.testHost) {
        exclude("org.jetbrains.kotlin", "kotlin-test")
    }

    testImplementation(testFixtures(project(":brevbaker:core")))
    testImplementation(testFixtures(project(":brevbaker:dsl")))
    testImplementation(libs.jsoup)
}
