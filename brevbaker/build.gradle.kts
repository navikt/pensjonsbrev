import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val logback_version: String by project
val ktor_version: String by project
val kotlin_version: String by project
val jupiter_version: String by project

plugins {
    application
    kotlin("jvm") version "1.4.32"
}

group = "no.nav.pensjon.brev"
version = "0.0.1-SNAPSHOT"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
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
    test {
        useJUnitPlatform()
    }
}

dependencies {
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("io.ktor:ktor-client-jackson:$ktor_version")
    implementation("io.ktor:ktor-metrics:$ktor_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")

    // JUnit 5
//    testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiter_version")
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jupiter_version")
    testImplementation(platform("org.junit:junit-bom:$jupiter_version"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

