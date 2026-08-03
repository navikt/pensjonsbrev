import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val markupJavaTarget: String by System.getProperties()
val brevbakerVersion: String by project

plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "no.nav.brev.brevbaker"
version = brevbakerVersion

base {
    archivesName.set("pdf-bygger-api")
}

java {
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

// Kontrakten mot pdf-bygger. Uttrykt i markup-modellen, ikke i DSL-en: den som bare skal lese eller
// sende en ferdig request trenger ikke byggerne. DSL-en ligger i :brevbaker:pdf-bygger:dsl.
dependencies {
    api(project(":brevbaker:markup-model"))
    testImplementation(libs.bundles.junit)
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(markupJavaTarget))
    }
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
            artifactId = "pdf-bygger-api"
            from(components["java"])
            pom {
                name.set("brevbaker-pdf-bygger-api")
                description.set("Request- og responsmodellen for pdf-bygger.")
                url.set("https://github.com/navikt/pensjonsbrev")
                scm {
                    url.set("https://github.com/navikt/pensjonsbrev")
                    connection.set("scm:git:https://github.com/navikt/pensjonsbrev.git")
                }
            }
        }
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

@OptIn(org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation::class)
kotlin {
    abiValidation {}
}
