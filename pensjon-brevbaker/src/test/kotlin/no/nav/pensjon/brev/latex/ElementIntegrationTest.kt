package no.nav.pensjon.brev.latex

import no.nav.brev.brevbaker.PDF_BUILDER_URL
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.outlineTestTemplate
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.text
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.INTEGRATION_TEST)
class ElementIntegrationTest {

    private val laTeXCompilerService = LaTeXCompilerService(PDF_BUILDER_URL, maxRetries = 0)


    @Test
    fun `tom title1 burde ikke feile`() {
        outlineTestTemplate<EmptyBrevdata> {
            title1 { }
            paragraph { text(Bokmal to "Test") }
            title1 { text(Bokmal to "med tekst") }
        }.renderTestPDF("elementTest tom title1", pdfByggerService = laTeXCompilerService)
    }

    @Test
    fun `tom title2 burde ikke feile`() {
        outlineTestTemplate<EmptyBrevdata> {
            title2 { }
            paragraph { text(Bokmal to "Test") }
            title2 { text(Bokmal to "med tekst") }
        }.renderTestPDF("elementTest tom title2", pdfByggerService = laTeXCompilerService)
    }

    @Test
    fun `tom paragraph burde ikke feile`() {
        outlineTestTemplate<EmptyBrevdata> {
            title2 { text(Bokmal to "Test") }
            paragraph { }
        }.renderTestPDF("elementTest tom paragraph", pdfByggerService = laTeXCompilerService)
    }
}