import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

val javaTarget: String by System.getProperties()
val kotlinVersion: String by System.getProperties()
val kspVersion: String by System.getProperties()
val commonVersion: String by project
val jupiterVersion: String by project
val hamkrestVersion: String by project

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
    implementation("com.google.devtools.ksp:symbol-processing-api:$kotlinVersion-$kspVersion")
    implementation("no.nav.pensjon.brevbaker:brevbaker-api-model-common:$commonVersion")
    implementation(project(":brevbaker-dsl"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiterVersion")
    // Byttet til fork som støtter kotlin > 2.0
    //    testImplementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:1.6.0")
    testImplementation("dev.zacsweers.kctfork:ksp:0.7.0")
    testImplementation("com.natpryce:hamkrest:$hamkrestVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")
}
