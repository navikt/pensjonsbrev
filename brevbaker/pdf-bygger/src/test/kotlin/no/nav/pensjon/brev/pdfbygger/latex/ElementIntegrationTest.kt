package no.nav.pensjon.brev.pdfbygger.latex

import no.nav.brev.brevbaker.LaTeXCompilerService
import no.nav.brev.brevbaker.PDFByggerTestContainer
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.TypstCompilerService
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

    private val pdfCompileService = TypstCompilerService(PDFByggerTestContainer.mappedUrl())

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

    @Test
    fun `tom title3`() {
        outlineTestTemplate<EmptyAutobrevdata> {
            title3 { }
            title3 { text(bokmal { +"" }) }
            paragraph { text(bokmal { +"Test" }) }
            title3 { text(bokmal { +"med tekst" }) }
        }.renderTestPDF("elementTest tom title3", pdfByggerService = pdfCompileService)
    }

    @Test
    fun `tom paragraph`() {
        outlineTestTemplate<EmptyAutobrevdata> {
            title2 { text(bokmal { +"Test" }) }
            paragraph { }
        }.renderTestPDF("elementTest tom paragraph", pdfByggerService = pdfCompileService)
    }

    @Test
    fun `newline uten tekst forran`() {
        outlineTestTemplate<EmptyAutobrevdata> {
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
        }.renderTestPDF("elementTest newline uten tekst forran", pdfByggerService = pdfCompileService)
    }

    @Test
    fun `paragraph med newline etter tom paragraph`() {
        outlineTestTemplate<EmptyAutobrevdata> {
            title2 { text(bokmal { +"Test" }) }
            paragraph { }
            paragraph {
                newline()
                text(bokmal { +"Etter newline" })
            }
        }.renderTestPDF("elementTest paragraph med newline etter tom paragraph", pdfByggerService = pdfCompileService)
    }

    @Test
    fun `newline etter itemlist`() {
        outlineTestTemplate<EmptyAutobrevdata> {
            paragraph {
                list {
                    item { text(bokmal { +"Punkt 1" }) }
                    item { text(bokmal { +"111" }) }
                }
                newline()
                text(bokmal { +"Etter newline" })
            }
        }.renderTestPDF("elementTest kan ha newline etter itemlist", pdfByggerService = pdfCompileService)
    }

    @Test
    fun `newline etter table`() {
        outlineTestTemplate<EmptyAutobrevdata> {
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
        }.renderTestPDF("elementTest kan ha newline etter table", pdfByggerService = pdfCompileService)
    }
}