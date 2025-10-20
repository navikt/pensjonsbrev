package no.nav.pensjon.brev.maler

import no.nav.brev.brevbaker.TemplatesTest
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.UfoereTemplates
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder

class UfoereProductionTemplatesTest : TemplatesTest(
    templates = UfoereTemplates,
    auto = Ufoerebrevkoder.AutoBrev.entries,
    redigerbare = Ufoerebrevkoder.Redigerbar.entries,
    fixtures = Fixtures,
    filterForPDF = listOf(),
)