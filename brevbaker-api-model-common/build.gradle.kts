import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val apiModelJavaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "no.nav.pensjon.brevbaker"
version = "1.5.0"

java {
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(kotlin("stdlib"))
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
