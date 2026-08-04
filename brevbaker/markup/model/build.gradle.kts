import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val markupJavaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "no.nav.brev.brevbaker"
version = libs.versions.markupVersion.get()

base {
    archivesName.set("markup-model")
}

java {
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

// Modellen skal være helt uten avhengigheter: den er datamodellen flere publiserte artefakter uttrykker
// signaturen sin i, og konsumenter skal ikke arve verken et serialiseringsbibliotek eller genererte
// serializers gjennom den. All serialisering ligger i brevbaker:jackson.
//
// Kontrakten mot pdf-bygger (LetterPDFRequest, PDFCompilationOutput, HttpStatusCodes) bor her og ikke i
// et eget artefakt: den er uttrykt utelukkende i markup-modellen, så et eget artefakt ville bare vært
// en ny versjonsakse å holde i sync uten at noen kan bruke det ene uten det andre.
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
            artifactId = "markup-model"
            from(components["java"])
            pom {
                name.set("brevbaker-markup-model")
                description.set("Datamodellen for Nav-brev, inkludert request/response-kontrakten mot pdf-bygger.")
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

@OptIn(org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation::class)
kotlin {
    abiValidation {}
}
