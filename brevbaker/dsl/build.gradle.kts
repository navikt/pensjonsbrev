import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val apiModelJavaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    alias(libs.plugins.binary.compatibility.validator) apply true
    id("java-library")
    id("java-test-fixtures")
}

group = "no.nav.brev.brevbaker"
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
    implementation(libs.brevbaker.common)

    testImplementation(libs.bundles.junit)
    testImplementation(kotlin("reflect"))

    testFixturesImplementation(libs.brevbaker.common)
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(apiModelJavaTarget))
    }
}

java {
    targetCompatibility = JavaVersion.toVersion(apiModelJavaTarget)
}

val brevbakerInternAnnoteringer = listOf(
    "no.nav.brev.InternKonstruktoer",
    "no.nav.brev.InterneDataklasser",
    "no.nav.pensjon.brev.template.BrevbakerDSLInternal",
)

tasks {
    test {
        useJUnitPlatform()
    }
    compileKotlin {
        compilerOptions.optIn.addAll(brevbakerInternAnnoteringer)
    }
    compileTestKotlin {
        compilerOptions.optIn.addAll(brevbakerInternAnnoteringer)
    }
}

apiValidation {
    nonPublicMarkers.addAll(brevbakerInternAnnoteringer)
}