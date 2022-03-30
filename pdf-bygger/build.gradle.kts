val ktorVersion: String by project
val logbackVersion: String by project
val logstashVersion: String by project

plugins {
    kotlin("jvm") version "1.6.10"
    application
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

repositories {
    mavenLocal()
    mavenCentral()
}

group="no.nav.pensjon.brev"
version="0.0.1-SNAPSHOT"

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    shadowJar {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")
        archiveVersion.set("")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstashVersion")

    // Metrics
    implementation("io.ktor:ktor-metrics:$ktorVersion")
    implementation("io.ktor:ktor-metrics-micrometer:$ktorVersion")
    implementation("io.micrometer:micrometer-registry-prometheus:1.8.3")
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}