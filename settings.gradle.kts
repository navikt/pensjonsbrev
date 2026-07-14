rootProject.name = "pensjonsbrev"

include("ktlint-rules")

include("brevbaker:core")
include("brevbaker:api-model-common")
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

dependencyResolutionManagement {
    versionCatalogs {
        create("jackson") {
            from(files("gradle/libs.versions.toml"))
            library("bom", "com.fasterxml.jackson", "jackson-bom").versionRef("jacksonVersion")
        }
        create("ktorBom") {
            from(files("gradle/libs.versions.toml"))
            library("bom", "io.ktor", "ktor-bom").versionRef("ktorVersion")
        }
        create("log4jBom") {
            from(files("gradle/libs.versions.toml"))
            library("bom", "org.apache.logging.log4j", "log4j-bom").versionRef("log4j2Version")
        }
        create("exposedBom") {
            from(files("gradle/libs.versions.toml"))
            library("bom", "org.jetbrains.exposed", "exposed-bom").versionRef("exposedVersion")
        }
    }
}