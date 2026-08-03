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
    archivesName.set("markup-dsl")
}

java {
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

// DSL-en er den eneste tiltenkte veien inn i markup-modellen, og bygger den via den opt-in-merkede
// fabrikkflaten i model-modulen. Ingen andre avhengigheter: konsumenter skal ikke arve et
// serialiseringsbibliotek gjennom markup.
dependencies {
    api(project(":brevbaker:markup-model"))
    testImplementation(libs.bundles.junit)
}

// Både den offentlige og den utvidede DSL-en bor i denne modulen. Skillet håndheves med
// @ExtendedMarkupDsl (RequiresOptIn), ikke med en modulgrense — hadde de vært to moduler måtte hver
// builder-søm (texts, blocks, contentFactory, build()) blitt public for å krysse grensen, og nettopp
// det maskineriet er det vi vil holde utenfor det publiserte API-et.
kotlin {
    compilerOptions {
        optIn.add("no.nav.brev.brevbaker.markup.MarkupModelApi")
        optIn.add("no.nav.brev.brevbaker.markup.dsl.extended.ExtendedMarkupDsl")
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
            artifactId = "markup-dsl"
            from(components["java"])
            pom {
                name.set("brevbaker-markup-dsl")
                description.set("DSL for å bygge markup-modellen for Nav-brev.")
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
