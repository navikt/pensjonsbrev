package no.nav.pensjon.brev.maler

import no.nav.brev.brevbaker.BrevmodulTest
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.UfoereTemplates
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder

class UfoereProductionTemplatesTest : BrevmodulTest(
    templates = UfoereTemplates,
    auto = Ufoerebrevkoder.AutoBrev.entries,
    redigerbare = Ufoerebrevkoder.Redigerbar.entries,
    fixtures = Fixtures,
    filterForPDF = listOf(),
)