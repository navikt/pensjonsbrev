package no.nav.pensjon.brev.latex

import no.nav.brev.brevbaker.PDF_BUILDER_URL
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.outlineTestTemplate
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.template.dsl.text
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.INTEGRATION_TEST)
class ElementIntegrationTest {

    private val laTeXCompilerService = LaTeXCompilerService(PDF_BUILDER_URL, maxRetries = 0)


    @Test
    fun `tom title1`() {
        outlineTestTemplate<EmptyBrevdata> {
            title1 { text(bokmal { +"" }) }
            title1 { }
            paragraph { text(bokmal { +"Test" }) }
            title1 { text(bokmal { +"med tekst" }) }
        }.renderTestPDF("elementTest tom title1", pdfByggerService = laTeXCompilerService)
    }

    @Test
    fun `tom title2`() {
        outlineTestTemplate<EmptyBrevdata> {
            title2 { }
            title2 { text(bokmal { +"" }) }
            paragraph { text(bokmal { +"Test" }) }
            title2 { text(bokmal { +"med tekst" }) }
        }.renderTestPDF("elementTest tom title2", pdfByggerService = laTeXCompilerService)
    }

    @Test
    fun `tom title3`() {
        outlineTestTemplate<EmptyBrevdata> {
            title3 { }
            title3 { text(bokmal { +"" }) }
            paragraph { text(bokmal { +"Test" }) }
            title3 { text(bokmal { +"med tekst" }) }
        }.renderTestPDF("elementTest tom title3", pdfByggerService = laTeXCompilerService)
    }

    @Test
    fun `tom paragraph`() {
        outlineTestTemplate<EmptyBrevdata> {
            title2 { text(bokmal { +"Test" }) }
            paragraph { }
        }.renderTestPDF("elementTest tom paragraph", pdfByggerService = laTeXCompilerService)
    }

    @Test
    fun `newline uten tekst forran`() {
        outlineTestTemplate<EmptyBrevdata> {
            paragraph {
                text(bokmal { +"" })
                newline()
                text(bokmal { +"Etter newline" })
            }

            paragraph {
                text(bokmal { +"   " })
                newline()
                text(bokmal { +"Etter newline" })
            }
        }.renderTestPDF("elementTest newline uten tekst forran", pdfByggerService = laTeXCompilerService)
    }

    @Test
    fun `paragraph med newline etter tom paragraph`() {
        outlineTestTemplate<EmptyBrevdata> {
            title2 { text(bokmal { +"Test" }) }
            paragraph { }
            paragraph {
                newline()
                text(bokmal { +"Etter newline" })
            }
        }.renderTestPDF("elementTest paragraph med newline etter tom paragraph", pdfByggerService = laTeXCompilerService)
    }

    @Test
    fun `newline etter itemlist`() {
        outlineTestTemplate<EmptyBrevdata> {
            paragraph {
                list {
                    item { text(bokmal { +"Punkt 1" }) }
                    item { text(bokmal { +"111" }) }
                }
                newline()
                text(bokmal { +"Etter newline" })
            }
        }.renderTestPDF("elementTest kan ha newline etter itemlist", pdfByggerService = laTeXCompilerService)
    }

    @Test
    fun `newline etter table`() {
        outlineTestTemplate<EmptyBrevdata> {
            paragraph {
                table(
                    header = {
                        column { text(bokmal { +"Kolonne 1" }) }
                        column { text(bokmal { +"Kolonne 2" }) }
                    }
                ) {
                    row {
                        cell { text(bokmal { +"Celle 1" }) }
                        cell { text(bokmal { +"Celle 2" }) }
                    }
                    row {
                        cell { text(bokmal { +"Celle 3" }) }
                        cell { text(bokmal { +"Celle 4" }) }
                    }
                }
                newline()
                text(bokmal { +"Etter newline" })
            }
        }.renderTestPDF("elementTest kan ha newline etter table", pdfByggerService = laTeXCompilerService)
    }
}