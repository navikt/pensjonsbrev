val apiModelVersion: String by project
val exposedVersion: String by project
val hamkrestVersion: String by project
val jacksonJsr310Version: String by project
val javaTarget: String by System.getProperties()
val kotlinVersion: String by System.getProperties()
val ktorVersion: String by System.getProperties()
val logbackVersion: String by project
val logstashVersion: String by project
val micrometerVersion: String by project
val mockkVersion: String by project

plugins {
    application
    kotlin("jvm")
    id("io.ktor.plugin")
}

group = "no.nav.pensjon.brev.skribenten"
version = "0.0.1"
application {
    mainClass.set("no.nav.pensjon.brev.skribenten.SkribentenAppKt")
}

ktor {
    fatJar {
        archiveFileName.set("app.jar")
    }
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = javaTarget
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = javaTarget
    }
    compileJava {
        targetCompatibility = javaTarget
    }
    compileTestJava {
        targetCompatibility = javaTarget
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

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.postgresql:postgresql:42.7.2")
    implementation("com.zaxxer:HikariCP:5.1.0")

    implementation("no.nav.pensjon.brev:pensjon-brevbaker-api-model:$apiModelVersion")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstashVersion")

    // Necessary for java.time.LocalDate
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonJsr310Version")

    // Metrics
    implementation("io.ktor:ktor-server-metrics:$ktorVersion")
    implementation("io.ktor:ktor-server-metrics-micrometer:$ktorVersion")
    implementation("io.micrometer:micrometer-registry-prometheus:$micrometerVersion")
    implementation("io.ktor:ktor-server-caching-headers-jvm:$ktorVersion")

    // Test
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    testImplementation("io.mockk:mockk:${mockkVersion}")
    testImplementation("com.natpryce:hamkrest:$hamkrestVersion")
}