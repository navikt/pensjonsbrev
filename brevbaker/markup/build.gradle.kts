import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val markupJavaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    alias(libs.plugins.kotlin.serialization)
    `maven-publish`
    alias(libs.plugins.binary.compatibility.validator) apply true
}

group = "no.nav.brev.brevbaker"

java {
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {
    api(libs.kotlinx.serialization.json)

    testImplementation(libs.bundles.junit)
}

tasks.test {
    useJUnitPlatform()
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
            pom {
                name.set("brevbaker-markup")
                description.set("Markup-modell og DSL for å bygge og serialisere Nav-brev.")
                url.set("https://github.com/navikt/pensjonsbrev")
                scm {
                    url.set("https://github.com/navikt/pensjonsbrev")
                    connection.set("scm:git:https://github.com/navikt/pensjonsbrev.git")
                }
            }
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(markupJavaTarget))
    }
}
tasks {
    compileJava {
        targetCompatibility = markupJavaTarget
    }
    compileTestJava {
        targetCompatibility = markupJavaTarget
    }
}
