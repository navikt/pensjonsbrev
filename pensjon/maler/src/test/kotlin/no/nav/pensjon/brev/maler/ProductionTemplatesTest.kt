package no.nav.pensjon.brev.maler

import no.nav.brev.brevbaker.AllTemplates
import no.nav.brev.brevbaker.BrevmodulTest
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.example.EksempelbrevRedigerbart
import no.nav.pensjon.brev.maler.example.LetterExample
import no.nav.pensjon.brev.template.AlltidValgbartVedlegg

class ProductionTemplatesTest : BrevmodulTest(
    templates = object : AllTemplates {
        override fun hentAutobrevmaler() = ProductionTemplates.hentAutobrevmaler() + LetterExample
        override fun hentRedigerbareMaler() =
            ProductionTemplates.hentRedigerbareMaler() + EksempelbrevRedigerbart
        override fun hentAlltidValgbareVedlegg(): Set<AlltidValgbartVedlegg<*>> = ProductionTemplates.hentAlltidValgbareVedlegg()
    },
    auto = Pesysbrevkoder.AutoBrev.entries,
    redigerbare = Pesysbrevkoder.Redigerbar.entries,
    fixtures = Fixtures,
    filterForPDF = listOf(LetterExample.kode)
)