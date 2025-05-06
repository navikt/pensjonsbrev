import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val apiModelJavaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    alias(libs.plugins.binary.compatibility.validator) apply true
    id("java-library")
    id("java-test-fixtures")
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
    testImplementation(libs.bundles.junit)
    testImplementation(libs.hamkrest)
    testImplementation(kotlin("reflect"))

    testImplementation(testFixtures(project(":brevbaker-api-model-common")))
    testFixturesImplementation(libs.brevbaker.common)
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(apiModelJavaTarget))
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
    compileKotlin {
        compilerOptions.optIn.add("no.nav.brev.InternKonstruktoer")
    }
    compileTestKotlin {
        compilerOptions.optIn.add("no.nav.brev.InternKonstruktoer")
    }
}
