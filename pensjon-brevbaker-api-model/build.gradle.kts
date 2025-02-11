import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val apiModelJavaTarget: String by System.getProperties()
val commonVersion: String by project
val kotlinVersion: String by System.getProperties()

plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "no.nav.pensjon.brev"
version = "122"

java {
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly(kotlin("stdlib"))
    api("no.nav.pensjon.brevbaker:brevbaker-api-model-common:$commonVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
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
