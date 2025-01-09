import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val apiModelVersion: String by project
val apiModelJavaTarget: String by System.getProperties()
val templateModelGeneratorVersion: String by project

plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

group = "no.nav.etterlatte"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly(kotlin("stdlib"))
    implementation("no.nav.pensjon.brev:template-model-generator:$templateModelGeneratorVersion")
    api("no.nav.pensjon.brevbaker:brevbaker-api-model-mal:1.0.0")
    ksp("no.nav.pensjon.brevbaker:brevbaker-api-model-mal:1.0.0")
    api(project(":pensjon-brevbaker-api-model"))
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
