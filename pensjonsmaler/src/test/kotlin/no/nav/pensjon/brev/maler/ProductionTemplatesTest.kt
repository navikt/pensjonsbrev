package no.nav.pensjon.brev.maler

import no.nav.brev.brevbaker.AllTemplates
import no.nav.brev.brevbaker.BrevmodulTest
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto
import no.nav.pensjon.brev.maler.example.EksempelbrevRedigerbart
import no.nav.pensjon.brev.maler.example.LetterExample

class ProductionTemplatesTest : BrevmodulTest(
    templates = object : AllTemplates {
        override fun hentAutobrevmaler() = ProductionTemplates.hentAutobrevmaler() + LetterExample
        override fun hentRedigerbareMaler() =
            ProductionTemplates.hentRedigerbareMaler() + EksempelbrevRedigerbart
    },
    auto = Pesysbrevkoder.AutoBrev.entries,
    redigerbare = Pesysbrevkoder.Redigerbar.entries,
    fixtures = Fixtures,
    filterForPDF = listOf(SamletMeldingOmPensjonsvedtak.kode)
)