package no.nav.pensjon.brev.maler.example

import no.nav.brev.brevbaker.AllTemplates
import no.nav.pensjon.brev.template.AlltidValgbartVedlegg

object Testmaler : AllTemplates {
    override fun hentAutobrevmaler() = setOf(LetterExample)

    override fun hentRedigerbareMaler() = setOf(EksempelbrevRedigerbart, EnkeltRedigerbartTestbrev)

    override fun hentAlltidValgbareVedlegg(): Set<AlltidValgbartVedlegg<*>> = setOf()
}