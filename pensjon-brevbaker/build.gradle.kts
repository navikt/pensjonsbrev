val logback_version: String by project
val ktor_version: String by project
val jupiter_version: String by project
val logstash_version: String by project

plugins {
    application
    kotlin("jvm") version "1.6.10"
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
    maven {
        // Create a token at https://github.com/settings/tokens/new with package.read
        // Then create a gradle.properties file in $HOME/.gradle with the following:
        // gpr.user=<your github username>
        // gpr.token=<the token>
        url = uri("https://maven.pkg.github.com/navikt/pensjonsbrev")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }
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
        kotlinOptions.jvmTarget = "17"
    }
    shadowJar {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")
        archiveVersion.set("")
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    val integrationTests = setOf("pdf-bygger")

    test {
        useJUnitPlatform {
            excludeTags = integrationTests
        }
    }

    task<Test>("integrationTest") {
        useJUnitPlatform {
            includeTags = integrationTests
        }
    }

}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstash_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("io.ktor:ktor-client-jackson:$ktor_version")
    implementation("io.ktor:ktor-metrics:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")
    implementation("no.nav.pensjon.brev:pensjon-brevbaker-api-model:1.2.7")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")
    // Necessary for java.time.LocalDate
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.1")
    implementation("io.ktor:ktor-metrics-micrometer:$ktor_version")
    implementation("io.micrometer:micrometer-registry-prometheus:1.8.1")

    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    // JUnit 5
    testImplementation(platform("org.junit:junit-bom:$jupiter_version"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("com.natpryce:hamkrest:1.8.0.1")
}

