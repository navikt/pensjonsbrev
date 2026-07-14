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
            // Egen catalog for jackson-bom. Vi importerer gradle/libs.versions.toml på nytt her
            // (i tillegg til standard "libs"-catalogen) kun for å kunne gjenbruke jacksonVersion
            // via versionRef, slik at bom-versjonen holdes i sync med resten av jackson-versjonene.
            from(files("gradle/libs.versions.toml"))
            library("bom", "com.fasterxml.jackson", "jackson-bom").versionRef("jacksonVersion")
        }
        create("ktorBom") {
            // Kalles ikke "ktor" siden io.ktor.plugin allerede registrerer en prosjekt-extension
            // med det navnet, som kolliderer med version catalog-accessoren.
            from(files("gradle/libs.versions.toml"))
            library("bom", "io.ktor", "ktor-bom").versionRef("ktorVersion")
        }
        create("log4jBom") {
            // Egen catalog for log4j-bom, samme mønster som jackson/ktor over.
            from(files("gradle/libs.versions.toml"))
            library("bom", "org.apache.logging.log4j", "log4j-bom").versionRef("log4j2Version")
        }
    }
}