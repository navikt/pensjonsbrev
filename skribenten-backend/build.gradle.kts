import io.ktor.plugin.features.*

val javaTarget: String by System.getProperties()
val kotlinVersion: String by System.getProperties()
val ktorVersion: String by System.getProperties()
val logbackVersion: String by project
val logstashVersion: String by project
val micrometerVersion: String by project
val apiModelVersion: String by project
val jacksonJsr310Version: String by project

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
data class GithubImageRegistry(override val toImage: Provider<String>, override val username: Provider<String>, override val password: Provider<String>) : DockerImageRegistry

ktor {
    fatJar {
        archiveFileName.set("app.jar")
    }
    docker {
        jreVersion.set(JreVersion.JRE_17)
        localImageName.set("pensjon-skribenten")
        imageTag.set(providers.environmentVariable("IMAGE_TAG").orElse("latest"))
        externalRegistry.set(
            GithubImageRegistry(
                toImage = providers.environmentVariable("IMAGE_SKRIBENTEN_BACKEND"),
                username = providers.environmentVariable("GITHUB_REPOSITORY"),
                password = providers.environmentVariable("GITHUB_TOKEN"),
            )
        )
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
    implementation("io.ktor:ktor-client-cio-jvm:2.1.3")
    implementation("io.ktor:ktor-server-caching-headers-jvm:2.3.3")

    // Test
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}