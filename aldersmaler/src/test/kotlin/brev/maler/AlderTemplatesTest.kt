package no.nav.pensjon.brev.maler

import no.nav.brev.brevbaker.TemplatesTest
import no.nav.pensjon.brev.AlderTemplates
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.Aldersbrevkoder

class AlderTemplatesTest : TemplatesTest(
    templates = AlderTemplates,
    auto = Aldersbrevkoder.AutoBrev.entries,
    redigerbare = Aldersbrevkoder.Redigerbar.entries,
    fixtures = Fixtures,
    filterForPDF = listOf()
)