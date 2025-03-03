import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val apiModelJavaTarget: String by System.getProperties()
val hamkrestVersion: String by project
val commonVersion: String by project
val logstashVersion: String by project
val ktorVersion: String by System.getProperties()
val jacksonJsr310Version: String by project

plugins {
    kotlin("jvm")
    id("java-library")
    id("java-test-fixtures")
    id("com.google.devtools.ksp")
}

group = "no.nav.brev.brevbaker"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
}

dependencies {
    api(project(":brevbaker-dsl"))
    api("no.nav.pensjon.brevbaker:brevbaker-api-model-common:$commonVersion")
    ksp(project(":template-model-generator"))
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.12.0")

    testImplementation(kotlin("test"))
    testImplementation("com.natpryce:hamkrest:$hamkrestVersion")


    testFixturesImplementation("net.logstash.logback:logstash-logback-encoder:$logstashVersion")
    testFixturesImplementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    testFixturesImplementation("io.ktor:ktor-client-cio:$ktorVersion")
    testFixturesImplementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    testFixturesImplementation("io.ktor:ktor-client-encoding:$ktorVersion")
    testFixturesImplementation("io.ktor:ktor-server-call-id:$ktorVersion")

    testFixturesImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonJsr310Version") {
        because("we require deserialization/serialization of java.time.LocalDate")
    }
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
    kotlin {
        jvmToolchain(apiModelJavaTarget.toInt())
        compileTestKotlin {
            compilerOptions.optIn.add("no.nav.brev.InterneDataklasser")
        }
        compileTestFixturesKotlin {
            compilerOptions.optIn.add("no.nav.brev.InterneDataklasser")
        }
    }
    compileJava {
        targetCompatibility = apiModelJavaTarget
    }
    compileTestJava {
        targetCompatibility = apiModelJavaTarget
    }
}
