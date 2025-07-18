package no.nav.pensjon.brev.maler.example

import no.nav.brev.brevbaker.Fixtures
import no.nav.brev.brevbaker.PDF_BUILDER_URL
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.fixtures.createLetterExampleDto
import no.nav.pensjon.brev.latex.LaTeXCompilerHttpService
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LetterImpl
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.INTEGRATION_TEST)
class LetterExampleTest {

    @Test
    fun test() {
        LetterImpl(
            LetterExample.template,
            createLetterExampleDto(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("EKSEMPELBREV_BOKMAL", pdfByggerService = LaTeXCompilerHttpService(PDF_BUILDER_URL, maxRetries = 0))
    }
}