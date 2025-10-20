package no.nav.pensjon.brev.maler

import no.nav.brev.brevbaker.BrevmodulTest
import no.nav.pensjon.brev.AlderTemplates
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.model.alder.Aldersbrevkoder

class AlderTemplatesTest : BrevmodulTest(
    templates = AlderTemplates,
    auto = Aldersbrevkoder.AutoBrev.entries,
    redigerbare = Aldersbrevkoder.Redigerbar.entries,
    fixtures = Fixtures,
    filterForPDF = listOf()
)