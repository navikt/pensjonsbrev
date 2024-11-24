package no.nav.pensjon.brev

import io.ktor.server.application.Application
import no.nav.pensjon.brev.maler.AllTemplates
import no.nav.pensjon.brev.maler.ProductionTemplates
import no.nav.pensjon.brev.maler.example.EksempelbrevRedigerbart
import no.nav.pensjon.brev.maler.example.LetterExample

fun Application.brevbakerTestModule() = this.brevbakerModule(
    templates = object : AllTemplates {
        override fun hentAutobrevmaler() =
            ProductionTemplates.hentAutobrevmaler() + LetterExample

        override fun hentRedigerbareMaler() = ProductionTemplates.hentRedigerbareMaler() + EksempelbrevRedigerbart

    }
)