val jupiterVersion: String by project
val jacksonVersion: String by project

plugins {
    kotlin("jvm") version "1.3.72"
    `maven-publish`
}

group = "no.nav.pensjon.brev"
version = "1.2.1"

java {
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")

    // JUnit 5
    testImplementation(platform("org.junit:junit-bom:$jupiterVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("com.natpryce:hamkrest:1.8.0.1")

    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
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
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    test {
        useJUnitPlatform()
    }
}