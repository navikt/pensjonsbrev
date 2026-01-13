rootProject.name = "pensjonsbrev"

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