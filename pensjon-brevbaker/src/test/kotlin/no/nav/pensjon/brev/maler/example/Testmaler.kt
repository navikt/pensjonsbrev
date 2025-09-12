package no.nav.pensjon.brev.maler.example

import no.nav.brev.brevbaker.AllTemplates
import no.nav.brev.brevbaker.AutoMal
import no.nav.brev.brevbaker.RedigerbarMal

object Testmaler : AllTemplates {
    override fun hentAutobrevmaler() = setOf(AutoMal(LetterExample))

    override fun hentRedigerbareMaler() = setOf(RedigerbarMal(EksempelbrevRedigerbart, null), RedigerbarMal(EnkeltRedigerbartTestbrev, null))
}