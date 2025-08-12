import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

val javaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    alias(libs.plugins.ksp) apply true
}

group = "no.nav.pensjon.brev"
version = "0.0.1-SNAPSHOT"

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
}

tasks {
    test {
        useJUnitPlatform()
    }
}

dependencies {
    implementation(libs.ksp.symbol.processing.api)
    implementation(project(":brevbaker-dsl"))

    testImplementation(kotlin("reflect"))
    testImplementation(libs.brevbaker.common)
    testImplementation(libs.bundles.junit)
    testImplementation(libs.hamkrest)
    testImplementation(libs.ksp.symbol.processing.aa)
    testImplementation(libs.ksp.symbol.processing.common)
    testImplementation(libs.io.github.classgraph)
    kspTest(project)
}
