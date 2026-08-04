rootProject.name = "pensjonsbrev"

include("ktlint-rules")

include("brevbaker:core")
include("brevbaker:brevbaker-api")
include("brevbaker:brevdata")
include("brevbaker:jackson")
// Modulnavnene er unike paa tvers av hele bygget med vilje: Gradle identifiserer et prosjekt som
// group:name, saa to prosjekter som begge het "dsl" under samme group ville kollidert og blitt
// substituert mot hverandre.
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