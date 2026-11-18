import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version libs.versions.kotlinVersion
}

group = "no.nav.pensjon.brev.brevoppskrift"
version = "0.0.1"

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(javaTarget))
    }
}

tasks {
    compileJava {
        targetCompatibility = javaTarget
    }
    compileTestJava {
        targetCompatibility = javaTarget
    }
    test {
        maxParallelForks = 1
        useJUnitPlatform()
    }
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(platform(libs.jackson.bom))
    implementation(libs.jackson.module.kotlin)
    implementation(libs.jackson.datatype.jsr310)

    implementation(exposedLibs.core)
    implementation(exposedLibs.jdbc)
    implementation(exposedLibs.json)
    implementation(exposedLibs.java.time)
    implementation(libs.hikari.cp)
    implementation(libs.postgresql)
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)
    implementation(libs.micrometer.prometheus)

    testImplementation(libs.bundles.junit)
    testImplementation(libs.testcontainers.postgresql)
}
