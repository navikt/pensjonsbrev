package no.nav.pensjon.brev

import io.ktor.server.application.Application
import no.nav.pensjon.brev.maler.ProductionTemplates
import no.nav.pensjon.brev.maler.example.EksempelbrevRedigerbart
import no.nav.pensjon.brev.maler.example.LetterExample

val alleAutobrevmaler = ProductionTemplates.hentAutobrevmaler() + LetterExample

val alleRedigerbareMaler = ProductionTemplates.hentRedigerbareMaler() + EksempelbrevRedigerbart

fun Application.brevbakerTestModule() = this.brevbakerModule(
    object : AllTemplates {
        override fun urlPrefiks() = ""
        override fun hentAutobrevmaler() = alleAutobrevmaler

        override fun hentRedigerbareMaler() = alleRedigerbareMaler

    }
)