val javaTarget: String by System.getProperties()
val logbackVersion: String by project
val ktorVersion: String by System.getProperties()
val jupiterVersion: String by project
val hamkrestVersion: String by project
val logstashVersion: String by project
val micrometerVersion: String by project
val apiModelVersion: String by project
val jacksonJsr310Version: String by project

plugins {
    application
    kotlin("jvm")
    id("com.google.devtools.ksp")
    id("com.github.johnrengelman.shadow")
}

group = "no.nav.pensjon.brev"
version = "0.0.1-SNAPSHOT"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
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

    shadowJar {
        archiveBaseName.set(project.name)
        archiveClassifier.set("")
        archiveVersion.set("")
    }

    test {
        useJUnitPlatform {
            excludeTags = setOf("integration-test", "manual-test")
        }
    }

    task<Test>("integrationTest") {
        group = LifecycleBasePlugin.VERIFICATION_GROUP
        useJUnitPlatform {
            includeTags = setOf("integration-test")
        }
    }
    task<Test>("manualTest") {
        group = LifecycleBasePlugin.VERIFICATION_GROUP
        useJUnitPlatform {
            includeTags = setOf("manual-test")
        }
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
    implementation(kotlin("stdlib"))
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    implementation("io.ktor:ktor-server-auth:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-server-call-id:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")
    implementation("io.ktor:ktor-client-encoding:$ktorVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstashVersion")
    implementation("no.nav.pensjon.brev:pensjon-brevbaker-api-model:$apiModelVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.8.0")

    implementation(project(":template-model-generator"))
    ksp(project(":template-model-generator"))

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonJsr310Version") {
        because("we require deserialization/serialization of java.time.LocalDate")
    }

    // Metrics
    implementation("io.ktor:ktor-server-metrics:$ktorVersion")
    implementation("io.ktor:ktor-server-metrics-micrometer:$ktorVersion")
    implementation("io.micrometer:micrometer-registry-prometheus:$micrometerVersion")

    // JUnit 5
    testImplementation(platform("org.junit:junit-bom:$jupiterVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("com.natpryce:hamkrest:$hamkrestVersion")
}

