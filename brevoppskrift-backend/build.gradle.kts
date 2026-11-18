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

    testImplementation(libs.bundles.junit)
}
