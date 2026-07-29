import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val markupJavaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "no.nav.brev.brevbaker"

java {
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

// Markup skal være helt uten avhengigheter: den er den offentlige modellen/DSL-en for pdf-bygger, og
// konsumenter skal ikke arve verken et serialiseringsbibliotek eller genererte serializers gjennom den.
// All serialisering ligger i brevbaker:internal (se MarkupJacksonModule).
dependencies {
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
        // Markups egen DSL er den tiltenkte brukeren av sine egne konstruksjons-seams.
        optIn.add("no.nav.brev.brevbaker.markup.MarkupInternalApi")
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
    abiValidation {
        filters {
            // Konstruksjons-seams for brevbaker:internal er public i bytekoden, men ikke en del av
            // markups støttede kontrakt. Samme mønster som @InternKonstruktoer i api-model-common.
            exclude {
                annotatedWith.add("no.nav.brev.brevbaker.markup.MarkupInternalApi")
            }
        }
    }
}
