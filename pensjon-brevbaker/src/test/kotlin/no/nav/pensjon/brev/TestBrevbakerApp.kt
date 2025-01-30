package no.nav.pensjon.brev

import io.ktor.server.application.Application
import no.nav.pensjon.brev.maler.AllTemplates
import no.nav.pensjon.brev.maler.ProductionTemplates
import no.nav.pensjon.brev.maler.example.EksempelbrevRedigerbart
import no.nav.pensjon.brev.maler.example.LetterExample

val alleAutobrevmaler = try {
    ProductionTemplates.hentAutobrevmaler() + LetterExample
} catch(e: ExceptionInInitializerError) {
    formaterOgSkrivUtFeil(e, "Feila under initialisering av autobrev-maler: ")
}

val alleRedigerbareMaler = try {
    ProductionTemplates.hentRedigerbareMaler() + EksempelbrevRedigerbart
} catch(e: ExceptionInInitializerError) {
    formaterOgSkrivUtFeil(e, "Feila under initialisering av redigerbare maler: ")
}

private fun formaterOgSkrivUtFeil(e: ExceptionInInitializerError, prefiks: String): Nothing =
    throw RuntimeException(
        "$prefiks\n ${e.cause?.message}, stack trace: \n ${
            e.cause?.stackTrace?.joinToString(
                "\n\t"
            )
        } \n \n", e.cause
    )

fun Application.brevbakerTestModule() = this.brevbakerModule(
    templates = object : AllTemplates {
        override fun hentAutobrevmaler() = alleAutobrevmaler

        override fun hentRedigerbareMaler() = alleRedigerbareMaler
    },
    konfigurerFeatureToggling = { settOppFakeUnleash() }
)