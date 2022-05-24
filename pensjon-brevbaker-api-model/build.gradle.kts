val jupiterVersion: String by project
val jacksonVersion: String by project

plugins {
    kotlin("jvm") version "1.6.10"
    `maven-publish`
}

group = "no.nav.pensjon.brev"
version = "2.0.0"

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

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}