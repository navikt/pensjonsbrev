import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val apiModelVersion: String by project
val apiModelJavaTarget: String by System.getProperties()
val jacksonJsr310Version: String by project
val jupiterVersion: String by project
val logstashVersion: String by project
val ktorVersion: String by System.getProperties()
val mockkVersion: String by project

plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

group = "no.nav.pensjon.brev"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly(kotlin("stdlib"))
    api(project(":brevbaker"))
    ksp(project(":template-model-generator"))
    api(project(":pensjon-brevbaker-api-model"))


    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonJsr310Version") {
        because("we require deserialization/serialization of java.time.LocalDate")
    }

    // JUnit 5
    testImplementation(platform("org.junit:junit-bom:$jupiterVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.mockk:mockk:${mockkVersion}")

    testImplementation("net.logstash.logback:logstash-logback-encoder:$logstashVersion")
    testImplementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    testImplementation("io.ktor:ktor-client-cio:$ktorVersion")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    testImplementation("io.ktor:ktor-client-encoding:$ktorVersion")
    testImplementation("io.ktor:ktor-server-call-id:$ktorVersion")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(apiModelJavaTarget))
    }
    sourceSets {
        main {
            kotlin.srcDir("build/generated/ksp/main/kotlin")
        }
        test {
            kotlin.srcDir("build/generated/ksp/test/kotlin")
        }
    }
}

tasks {
    compileJava {
        targetCompatibility = apiModelJavaTarget
    }
    compileTestJava {
        targetCompatibility = apiModelJavaTarget
    }
}

tasks {
    test {
        useJUnitPlatform {
            excludeTags = setOf("integration-test", "manual-test")
        }
        testLogging {
            events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED, TestLogEvent.STANDARD_ERROR)
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }

    task<Test>("integrationTest") {
        group = LifecycleBasePlugin.VERIFICATION_GROUP
        useJUnitPlatform {
            includeTags = setOf("integration-test")
        }
        testLogging {
            events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED, TestLogEvent.STANDARD_ERROR)
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }
    task<Test>("manualTest") {
        group = LifecycleBasePlugin.VERIFICATION_GROUP
        useJUnitPlatform {
            includeTags = setOf("manual-test")
        }
        testLogging {
            events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED, TestLogEvent.STANDARD_ERROR)
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }
}