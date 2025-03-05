import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val apiModelJavaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

group = "no.nav.pensjon.brevbaker"
version = "0.0.1-SNAPSHOT"

java {
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.brevbaker.common)

    // JUnit 5
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.hamkrest)
    testImplementation(kotlin("reflect"))
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(apiModelJavaTarget))
    }
    sourceSets {
        test {
            kotlin.srcDir("build/generated/ksp/test/kotlin")
        }
    }
}
tasks {
    test {
        useJUnitPlatform()
    }
    compileJava {
        targetCompatibility = apiModelJavaTarget
    }
    compileTestJava {
        targetCompatibility = apiModelJavaTarget
    }
}
