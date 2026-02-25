package no.nav.pensjon.brev.maler

import no.nav.brev.brevbaker.AllTemplates
import no.nav.brev.brevbaker.BrevmodulTest
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.maler.example.EksempelbrevRedigerbart
import no.nav.pensjon.brev.maler.example.LetterExample
import no.nav.pensjon.brev.maler.legacy.redigerbar.DelvisEksportAvUforetrygd
import no.nav.pensjon.brev.template.AlltidValgbartVedlegg
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate

class ProductionTemplatesTest : BrevmodulTest(
    templates = object : AllTemplates {
        override fun hentAutobrevmaler() = setOf<AutobrevTemplate<AutobrevData>>()
        override fun hentRedigerbareMaler() = setOf(DelvisEksportAvUforetrygd)
        override fun hentAlltidValgbareVedlegg(): Set<AlltidValgbartVedlegg<*>> = setOf()
    },
    auto = Pesysbrevkoder.AutoBrev.entries,
    redigerbare = Pesysbrevkoder.Redigerbar.entries,
    fixtures = Fixtures,
    filterForPDF = listOf(LetterExample.kode)
)