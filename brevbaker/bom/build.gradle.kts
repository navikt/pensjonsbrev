val brevbakerVersion: String by project

plugins {
    `java-platform`
    `maven-publish`
}

group = "no.nav.brev.brevbaker"
version = brevbakerVersion

// Alle brevbaker-artefaktene slippes i lockstep fra samme pipeline. BOM-en finnes for at en konsument
// som drar inn flere av dem – direkte eller transitivt – ikke skal kunne havne i versjonssprik på
// delte typer som markup:model.
dependencies {
    constraints {
        api(project(":brevbaker:brevdata"))
        api(project(":brevbaker:brevbaker-api"))
        api(project(":brevbaker:markup-model"))
        api(project(":brevbaker:markup-dsl"))
        api(project(":brevbaker:pdf-bygger-api"))
        api(project(":brevbaker:pdf-bygger-dsl"))
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
            artifactId = "brevbaker-bom"
            from(components["javaPlatform"])
            pom {
                name.set("brevbaker-bom")
                description.set("BOM som holder alle brevbaker-artefaktene på samme versjon.")
                url.set("https://github.com/navikt/pensjonsbrev")
                scm {
                    url.set("https://github.com/navikt/pensjonsbrev")
                    connection.set("scm:git:https://github.com/navikt/pensjonsbrev.git")
                }
            }
        }
    }
}
