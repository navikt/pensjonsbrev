import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val apiModelJavaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "no.nav.brev.brevbaker"
version = libs.versions.brevbakerApiVersion.get()

java {
    withSourcesJar()
    withJavadocJar()
}

@OptIn(org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation::class)
kotlin {
    abiValidation {
        filters {
            exclude {
                annotatedWith.add("no.nav.brev.InterneDataklasser")
            }
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":brevbaker:brevdata")) {
        because("brevbaker-api uttrykker HTTP-kontrakten i det samme vokabularet som bestillerne beskriver brevdata med.")
    }
    api(project(":brevbaker:markup-model")) {
        because("BestillRedigertBrevRequestV2 er uttrykt i markup-modellen. markup:model har ingen runtime-avhengigheter, så dette drar ikke inn noe tungt.")
    }
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
            artifactId = "brevbaker-api"
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
