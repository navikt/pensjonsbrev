package no.nav.pensjon.brev

import io.ktor.server.application.Application
import no.nav.brev.brevbaker.AllTemplates
import no.nav.brev.brevbaker.PDFByggerTestContainer
import no.nav.pensjon.brev.maler.example.EksempelbrevRedigerbart
import no.nav.pensjon.brev.maler.example.EnkeltRedigerbartTestbrev
import no.nav.pensjon.brev.maler.example.LetterExample

val alleAutobrevmaler = try {
    pensjonOgUfoereProductionTemplates.hentAutobrevmaler() + LetterExample
} catch(e: ExceptionInInitializerError) {
    formaterOgSkrivUtFeil(e, "Feila under initialisering av autobrev-maler: ")
}

val alleRedigerbareMaler = try {
    pensjonOgUfoereProductionTemplates.hentRedigerbareMaler() + EksempelbrevRedigerbart + EnkeltRedigerbartTestbrev
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

// Brukes av `testBrevbakerApp` gjennom test/resources/application.conf
@Suppress("unused")
fun Application.brevbakerTestModule() = this.brevbakerModule(
    templates = object : AllTemplates {
        override fun hentAutobrevmaler() = alleAutobrevmaler

        override fun hentRedigerbareMaler() = alleRedigerbareMaler
    },
    pdfByggerUrl = {
        PDFByggerTestContainer.start()
        PDFByggerTestContainer.mappedUrl()
    }
)