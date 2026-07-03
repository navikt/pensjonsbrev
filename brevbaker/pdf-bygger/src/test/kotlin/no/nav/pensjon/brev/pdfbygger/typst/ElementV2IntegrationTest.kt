package no.nav.pensjon.brev.pdfbygger.typst

import no.nav.brev.brevbaker.PdfByggerTestService
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.outlineTestTemplate
import no.nav.brev.brevbaker.renderTestPDFV2
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.template.dsl.text
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode

/**
 * Ende-til-ende-test som rendrer maler til LetterMarkupV2 og produserer PDF mot den kjørende
 * pdf-bygger-containeren via `/v2/produserBrev`. Dekker de viktigste v2-blokktypene (tittel, avsnitt,
 * lister, tabell, skjema) slik at hele v2 Typst-kjeden testes ende-til-ende.
 */
@Tag(TestTags.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
class ElementV2IntegrationTest {

    private val pdfCompileService = PdfByggerTestService()

    @Test
    fun `titler og avsnitt`() {
        outlineTestTemplate<EmptyAutobrevdata> {
            title1 { text(bokmal { +"Hovedtittel" }) }
            paragraph { text(bokmal { +"Et vanlig avsnitt." }) }
            title2 { text(bokmal { +"Undertittel" }) }
            paragraph { text(bokmal { +"Enda et avsnitt." }) }
        }.renderTestPDFV2("elementV2Test titler og avsnitt", pdfByggerService = pdfCompileService)
    }

    @Test
    fun `punktliste og nummerert liste`() {
        outlineTestTemplate<EmptyAutobrevdata> {
            paragraph {
                list {
                    item { text(bokmal { +"Punkt en" }) }
                    item { text(bokmal { +"Punkt to" }) }
                }
            }
            paragraph {
                numberedList {
                    item { text(bokmal { +"Første" }) }
                    item { text(bokmal { +"Andre" }) }
                }
            }
        }.renderTestPDFV2("elementV2Test lister", pdfByggerService = pdfCompileService)
    }

    @Test
    fun `tabell`() {
        outlineTestTemplate<EmptyAutobrevdata> {
            paragraph {
                table(
                    header = {
                        column { text(bokmal { +"Kolonne A" }) }
                        column { text(bokmal { +"Kolonne B" }) }
                    }
                ) {
                    row {
                        cell { text(bokmal { +"A1" }) }
                        cell { text(bokmal { +"B1" }) }
                    }
                    row {
                        cell { text(bokmal { +"A2" }) }
                        cell { text(bokmal { +"B2" }) }
                    }
                }
            }
        }.renderTestPDFV2("elementV2Test tabell", pdfByggerService = pdfCompileService)
    }
}
