import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val apiModelVersion: String by project
val apiModelJavaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

group = "no.nav.etterlatte"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(kotlin("stdlib"))
    api(project(":brevbaker-api-model-mal"))
    api(project(":pensjon-brevbaker-api-model"))
    ksp(project(":template-model-generator"))
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
