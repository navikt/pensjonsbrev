import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val markupJavaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    `maven-publish`
}

dependencyLocking {
    lockAllConfigurations()
    ignoredDependencies.add("no.nav.brev.brevbaker:markup-model")
}

group = "no.nav.brev.brevbaker"
version = publishedLibs.versions.markupVersion.get()

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

dependencies {
    api(publishedLibs.markup.model)
    testImplementation(libs.bundles.junit)
}

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
                description.set("DSL for å bygge markup-modellen og en komplett bestilling til pdf-bygger.")
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
