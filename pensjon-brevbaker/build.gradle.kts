val logback_version: String by project
val ktor_version: String by project
val jupiter_version: String by project

plugins {
    application
    kotlin("jvm") version "1.5.20"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "no.nav.pensjon.brev"
version = "0.0.1-SNAPSHOT"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenLocal()
    mavenCentral()
}

sourceSets {
    test {
        resources {
            srcDir("src/main/resources")
        }
    }
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "15"
    }
    shadowJar {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")
        archiveVersion.set("")
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "15"
    }

    test {
        useJUnitPlatform()
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("io.ktor:ktor-client-jackson:$ktor_version")
    implementation("io.ktor:ktor-metrics:$ktor_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    implementation("io.ktor:ktor-metrics-micrometer:$ktor_version")
    implementation("io.micrometer:micrometer-registry-prometheus:1.7.0")

    // JUnit 5
//    testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiter_version")
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jupiter_version")
    testImplementation(platform("org.junit:junit-bom:$jupiter_version"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

