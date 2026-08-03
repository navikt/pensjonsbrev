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
    archivesName.set("pdf-bygger-dsl")
}

java {
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

// Dette er artefaktet en konsument deklarerer selv: DSL-en for å bygge en komplett bestilling til
// pdf-bygger. markup:model, markup:dsl og pdf-bygger:api kommer transitivt herfra, og siden alt slippes
// i lockstep kan de ikke havne i versjonssprik.
dependencies {
    api(project(":brevbaker:pdf-bygger-api"))
    api(project(":brevbaker:markup-dsl"))
    testImplementation(libs.bundles.junit)
}

kotlin {
    compilerOptions {
        optIn.add("no.nav.brev.brevbaker.markup.MarkupModelApi")
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
            artifactId = "pdf-bygger-dsl"
            from(components["java"])
            pom {
                name.set("brevbaker-pdf-bygger-dsl")
                description.set("DSL for å bygge en komplett PDF-bestilling til pdf-bygger.")
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
