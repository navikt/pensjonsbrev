rootProject.name = "pensjonsbrev"

dependencyResolutionManagement {
    versionCatalogs {
        create("publishedLibs") {
            from(files("gradle/published-versions.toml"))
        }
    }
}

include("ktlint-rules")

include("brevbaker:core")
include("brevbaker:brevbaker-api")
include("brevbaker:brevdata")
include("brevbaker:serialization")
include("brevbaker:markup-model")
project(":brevbaker:markup-model").projectDir = file("brevbaker/markup/model")
include("brevbaker:markup-dsl")
project(":brevbaker:markup-dsl").projectDir = file("brevbaker/markup/dsl")
include("brevbaker:dsl")
include("brevbaker:pdf-bygger")
include("brevbaker:template-model-generator")

include("skribenten-backend")

include("pensjon:brevbaker")
include("pensjon:maler")
include("pensjon:api-model")

include("ufoere:maler")
include("ufoere:api-model")

include("alder:maler")
include("alder:api-model")

include("etterlattemaler")

include("planlegge-pensjon-maler")

// Substitute published artifacts with local project modules so the IDE navigates
// to source files in this repo rather than decompiled classes from a sources.jar.
gradle.allprojects {
    if (System.getenv("CI")?.toBoolean() == true) return@allprojects
    plugins.withType<JavaBasePlugin> {
        extensions.getByType<JavaPluginExtension>().sourceSets.all {
            configurations.matching {
                it.name in setOf(compileClasspathConfigurationName, runtimeClasspathConfigurationName, annotationProcessorConfigurationName)
            }.configureEach {
                resolutionStrategy.dependencySubstitution {
                    substitute(module("no.nav.pensjon.brev:pensjon-api-model")).using(project(":pensjon:api-model"))
                    substitute(module("no.nav.pensjon.alder.brev:alder-api-model")).using(project(":alder:api-model"))
                    substitute(module("no.nav.pensjon.ufoere.brev:ufoere-api-model")).using(project(":ufoere:api-model"))
                    substitute(module("no.nav.brev.brevbaker:brevdata")).using(project(":brevbaker:brevdata"))
                    substitute(module("no.nav.brev.brevbaker:brevbaker-api")).using(project(":brevbaker:brevbaker-api"))
                    substitute(module("no.nav.brev.brevbaker:markup-model")).using(project(":brevbaker:markup-model"))
                    substitute(module("no.nav.brev.brevbaker:markup-dsl")).using(project(":brevbaker:markup-dsl"))
                }
            }
        }
    }
}