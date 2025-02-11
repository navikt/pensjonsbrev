import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val exposedVersion: String by project
val commonVersion: String by project
val jacksonJsr310Version: String by project
val javaTarget: String by System.getProperties()
val jupiterVersion: String by project
val kotlinVersion: String by System.getProperties()
val ktorVersion: String by System.getProperties()
val logbackVersion: String by project
val logstashVersion: String by project
val micrometerVersion: String by project
val mockkVersion: String by project
val flywayVersion: String by project

plugins {
    application
    kotlin("jvm")
    id("com.gradleup.shadow")
}

group = "no.nav.pensjon.brev.skribenten"
version = "0.0.1"
application {
    mainClass.set("no.nav.pensjon.brev.skribenten.SkribentenAppKt")
}

tasks {
    named<ShadowJar>("shadowJar") {
        /* Lager en FatJar. Dette er en workaround for å få med plugin-konfig som Flyway trenger,
        henta fra https://stackoverflow.com/a/77237118
        Om du endrer denne til FatJar, start først appen i en container og se at databasemigreringa fortsatt fungerer
         */
        mergeServiceFiles()
        archiveFileName.set("app.jar")
    }
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
    test {
        useJUnitPlatform()
    }
}

sourceSets {
    main {
        resources {
            srcDir("secrets")
        }
    }
}

dependencies {
    // Ktor
    implementation("io.ktor:ktor-client-auth:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    implementation("io.ktor:ktor-server-auth:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-server-call-id:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-cors:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")
    implementation("io.ktor:ktor-server-caching-headers:$ktorVersion")
    implementation("io.ktor:ktor-server-request-validation:$ktorVersion")

    // Exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-json:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("com.zaxxer:HikariCP:6.2.1")


    // Databasemigrering
    implementation("org.flywaydb:flyway-core:$flywayVersion")
    implementation("org.flywaydb:flyway-database-postgresql:$flywayVersion")

    // Unleash
    implementation("io.getunleash:unleash-client-java:10.0.0")

    // Domenemodell
    implementation("no.nav.pensjon.brevbaker:brevbaker-api-model-common:$commonVersion")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstashVersion")

    // Necessary for java.time.LocalDate
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonJsr310Version")

    // Hashing
    implementation("commons-codec:commons-codec:1.18.0")

    // Metrics
    implementation("io.ktor:ktor-server-metrics:$ktorVersion")
    implementation("io.ktor:ktor-server-metrics-micrometer:$ktorVersion")
    implementation("io.micrometer:micrometer-registry-prometheus:$micrometerVersion")
    implementation("io.ktor:ktor-server-caching-headers-jvm:$ktorVersion")

    // Test
    testImplementation(platform("org.junit:junit-bom:$jupiterVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    testImplementation("io.mockk:mockk:${mockkVersion}")
    testImplementation("org.assertj:assertj-core:3.27.3")
    testImplementation("org.testcontainers:postgresql:1.20.4")

}