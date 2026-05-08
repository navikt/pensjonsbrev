package no.nav.pensjon.brev.pdfbygger.typst

import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.PdfByggerTestService
import no.nav.brev.brevbaker.outlineTestTemplate
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.template.dsl.text
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode

@Tag(TestTags.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
class ElementIntegrationTest {

    private val pdfCompileService = PdfByggerTestService()

    @Test
    fun `tom title1`() {
        outlineTestTemplate<EmptyAutobrevdata> {
            title1 { text(bokmal { +"" }) }
            title1 { }
            paragraph { text(bokmal { +"Test" }) }
            title1 { text(bokmal { +"med tekst" }) }
        }.renderTestPDF("elementTest tom title1", pdfByggerService = pdfCompileService)
    }

    @Test
    fun `tom title2`() {
        outlineTestTemplate<EmptyAutobrevdata> {
            title2 { }
            title2 { text(bokmal { +"" }) }
            paragraph { text(bokmal { +"Test" }) }
            title2 { text(bokmal { +"med tekst" }) }
        }.renderTestPDF("elementTest tom title2", pdfByggerService = pdfCompileService)
    }
}

