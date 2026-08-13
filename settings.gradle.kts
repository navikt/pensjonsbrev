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
include("pensjon:pensjon-api-model")

include("ufoere:maler")
include("ufoere:api-model")

include("alder:maler")
include("alder:api-model")

include("etterlattemaler")

include("planlegge-pensjon-maler")