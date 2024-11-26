package no.nav.pensjon.brev.maler.example

import no.nav.pensjon.brev.AllTemplates

object Testmaler : AllTemplates {
    override fun hentAutobrevmaler() = setOf(LetterExample)

    override fun hentRedigerbareMaler() = setOf(EksempelbrevRedigerbart)
}