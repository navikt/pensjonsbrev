import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

val javaTarget: String by System.getProperties()
val kotlinVersion: String by System.getProperties()
val kspVersion: String by System.getProperties()
val jupiterVersion: String by project
val hamkrestVersion: String by project

plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "no.nav.pensjon.brev"
version = "1.0.0"

java {
    withSourcesJar()
    withJavadocJar()
}

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

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/navikt/pensjonsbrev")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}

dependencies {
    compileOnly(kotlin("reflect"))
    implementation("com.google.devtools.ksp:symbol-processing-api:$kotlinVersion-$kspVersion")
    implementation(project(":brevbaker-api-model-common"))
    implementation(project(":brevbaker-dsl"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiterVersion")
    // Byttet til fork som støtter kotlin > 2.0
    //    testImplementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:1.6.0")
    testImplementation("dev.zacsweers.kctfork:ksp:0.7.0")
    testImplementation("com.natpryce:hamkrest:$hamkrestVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")
}
