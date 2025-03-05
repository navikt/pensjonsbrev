import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

val javaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
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

tasks.named("compileTestKotlin", KotlinCompilationTask::class.java) {
    compilerOptions {
        // Denne kreves for å kunne kompilere kotlin i unit tester.
        freeCompilerArgs.add("-opt-in=org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
}

dependencies {
    compileOnly(kotlin("reflect"))
    implementation(libs.ksp.symbol.processing.api)
    implementation(project(":brevbaker-dsl"))

    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.platform.launcher)
    // Byttet til fork som støtter kotlin > 2.0
    //    testImplementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:1.6.0")
    testImplementation(libs.ksp.kotlin.compile.testing)
    testImplementation(libs.hamkrest)
    testRuntimeOnly(libs.junit.jupiter.engine)
}
