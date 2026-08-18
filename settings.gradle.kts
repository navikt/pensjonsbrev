rootProject.name = "pensjonsbrev"

fun publishedVersion(artifact: String): String =
    settingsDir.resolve("gradle/published/$artifact.properties")
        .inputStream()
        .use { java.util.Properties().apply { load(it) } }
        .getProperty("version")
        ?: throw GradleException("Mangler 'version' i gradle/published/$artifact.properties")

dependencyResolutionManagement {
    versionCatalogs {
        create("publishedLibs") {
            version("brevdataVersion", publishedVersion("brevdata"))
            version("markupVersion", publishedVersion("markup"))
            version("brevbakerApiVersion", publishedVersion("brevbaker-api"))

            library("brevdata", "no.nav.brev.brevbaker", "brevdata").versionRef("brevdataVersion")
            library("brevbaker-api", "no.nav.brev.brevbaker", "brevbaker-api").versionRef("brevbakerApiVersion")
            library("markup-model", "no.nav.brev.brevbaker", "markup-model").versionRef("markupVersion")
            library("markup-dsl", "no.nav.brev.brevbaker", "markup-dsl").versionRef("markupVersion")
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
include("pensjon:pensjon-api-model")

include("ufoere:maler")
include("ufoere:ufoere-api-model")

include("alder:maler")
include("alder:alder-api-model")

include("etterlattemaler")

include("planlegge-pensjon-maler")