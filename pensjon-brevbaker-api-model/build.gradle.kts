import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val apiModelJavaTarget: String by System.getProperties()
val kotlinVersion: String by System.getProperties()

plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "no.nav.pensjon.brev"
version = "78"

java {
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(kotlin("stdlib"))
    api("no.nav.pensjon.brevbaker:brevbaker-api-model-common:1.5.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}

publishing {
    repositories {

        maven {
            name = "GitHubPackages"
            url = uri("https://github-package-registry-mirror.gc.nav.no/cached/maven-release")
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(apiModelJavaTarget))
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
