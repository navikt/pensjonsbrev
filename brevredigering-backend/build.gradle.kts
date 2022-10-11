import io.ktor.plugin.features.*

val kotlinVersion: String by System.getProperties()
val ktorVersion: String by System.getProperties()
val logbackVersion: String by project
val micrometerVersion: String by project

plugins {
    application
    kotlin("jvm")
    id("io.ktor.plugin")
}

group = "no.nav.pensjon.brev.brevredigering"
version = "0.0.1"
application {
    mainClass.set("no.nav.pensjon.brev.brevredigering.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

ktor {
    fatJar {
        archiveFileName.set("app.jar")
    }
    docker {
        jreVersion.set(JreVersion.JRE_17)
        localImageName.set("pensjon-brevredigering")
        imageTag.set(providers.environmentVariable("IMAGE_BREVREDIGERING_TAG").orElse("latest"))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    implementation("io.ktor:ktor-server-call-id:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")

    // Necessary for java.time.LocalDate
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.4")

    // Metrics
    implementation("io.ktor:ktor-server-metrics:$ktorVersion")
    implementation("io.ktor:ktor-server-metrics-micrometer:$ktorVersion")
    implementation("io.micrometer:micrometer-registry-prometheus:$micrometerVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}