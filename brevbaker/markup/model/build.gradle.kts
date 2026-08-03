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
                description.set("Datamodellen for Nav-brev. Publiseres fordi typene inngår i signaturen til både brevbaker-api og pdf-bygger-api.")
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
