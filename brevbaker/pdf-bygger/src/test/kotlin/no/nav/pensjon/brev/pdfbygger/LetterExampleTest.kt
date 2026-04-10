package no.nav.pensjon.brev.pdfbygger

import no.nav.brev.InterneDataklasser
import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.PdfByggerTestService
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LetterImpl
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.INTEGRATION_TEST)
class LetterExampleTest {

    private val pdfCompileService = PdfByggerTestService()
    //private val pdfCompileService = PdfByggerServiceImpl("http://localhost:8081") // brukes for lokal testing av mal-endringer

    @OptIn(InterneDataklasser::class)
    @Test
    fun test() {
        LetterImpl(
            LetterExample.template,
            createLetterExampleDto(),
            Language.Bokmal,
            FellesFactory.fellesAuto
        ).renderTestPDF("EKSEMPELBREV_BOKMAL", pdfByggerService = pdfCompileService)
    }
}