package no.nav.pensjon.brev.template.render

import no.nav.brev.brevbaker.Fixtures
import no.nav.brev.brevbaker.PDF_BUILDER_URL
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.copy
import no.nav.brev.brevbaker.renderTestPdfOutline
import no.nav.brev.brevbaker.renderTestVedleggPdf
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.maler.example.lipsums
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Form.Text.Size
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlereImpl
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@Tag(TestTags.INTEGRATION_TEST)
class LatexVisualITest {

    private val laTeXCompilerService = LaTeXCompilerService(PDF_BUILDER_URL, maxRetries = 0)

    private fun render(
        overrideName: String? = null,
        title: String? = null,
        felles: Felles? = null,
        brevtype: LetterMetadata.Brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        outlineInit: OutlineOnlyScope<LangBokmal, EmptyBrevdata>.() -> Unit,
    ) {
        val testName = overrideName ?: StackWalker.getInstance()
            .walk { frames -> frames.skip(2).findFirst().map { it.methodName }.orElse("") }
        renderTestPdfOutline(
            "test_visual/pdf",
            testName = testName,
            felles = felles,
            brevtype = brevtype,
            outlineInit = outlineInit,
            title = title ?: testName,
            pdfByggerService = laTeXCompilerService
        )
    }

    private fun renderTestVedlegg(
        includeSakspart: Boolean,
        testName: String? = null,
        felles: Felles? = null,
        vedleggOutlineInit: OutlineOnlyScope<LangBokmal, EmptyBrevdata>.() -> Unit,
    ) {
        renderTestVedleggPdf(
            outputFolder = "test_visual/pdf",
            testName = testName ?: StackWalker.getInstance()
                .walk { frames -> frames.skip(2).findFirst().map { it.methodName }.orElse("") },
            includeSakspart = includeSakspart,
            outlineInit = vedleggOutlineInit,
            pdfByggerService = laTeXCompilerService,
            felles = felles,
        )
    }


    private fun ParagraphOnlyScope<LangBokmal, EmptyBrevdata>.ipsumText() = text(Bokmal to lipsums[1])

    @Test
    fun `two paragraphs in a row`() {
        render {
            paragraph { ipsumText() }
            paragraph { ipsumText() }
        }
    }

    @Test
    fun fonts() {
        render {
            paragraph {
                text(Bokmal to "The quick brown fox jumps over the lazy dog. ", FontType.PLAIN)
                text(Bokmal to "The quick brown fox jumps over the lazy dog. ", FontType.ITALIC)
                text(Bokmal to "The quick brown fox jumps over the lazy dog. ", FontType.BOLD)
            }
        }
    }

    @Test
    fun `verge foersteside`() {
        render(
            felles = Fixtures.felles.copy(
                vergeNavn = "Verge vergeson"
            )
        ) {
            testTitle1()
            paragraph { ipsumText() }
        }
    }

    @Test
    fun `form choice med vspace`() {
        render {
            title1 { text(Bokmal to "Form choice") }
            paragraph {
                formChoice(prompt = newText(Bokmal to "Hvor lenge har du jobba?"), true) {
                    choice(Bokmal to "0 år")
                    choice(Bokmal to "1 år")
                    choice(Bokmal to "2 til 5 år")
                    choice(Bokmal to "6 år eller mer")
                }
            }
        }
    }

    @Test
    fun `title should not be put on a separate page`() {
        render {
            repeat(14) {
                paragraph { text(Bokmal to "Padding") }
            }
            title1 { text(Bokmal to "Title 1") }
            title2 { text(Bokmal to "Title 2") }
        }
    }

    @Test
    fun `form choice uten vspace`() {
        render {
            title1 { text(Bokmal to "Form choice uten vspace") }
            paragraph {
                formChoice(prompt = newText(Bokmal to "Hvor lenge vil du jobbe?"), false) {
                    choice(Bokmal to "0 år")
                    choice(Bokmal to "1 år")
                    choice(Bokmal to "2 til 5 år")
                    choice(Bokmal to "6 år eller mer")
                }
            }
        }
    }

    @Test
    fun `short form text med vspace`() {
        render {
            title1 { text(Bokmal to "Form text short med vspace") }
            paragraph { formText(Size.SHORT, newText(Bokmal to "test"), true) }
        }
    }

    @Test
    fun `long form text med vspace`() {
        render {
            title1 { text(Bokmal to "Form text long med vspace") }
            paragraph { formText(Size.LONG, newText(Bokmal to "test"), true) }
        }
    }

    @Test
    fun `short form text uten vspace`() {
        render {
            title1 { text(Bokmal to "Form text short uten vspace") }
            paragraph { formText(Size.SHORT, newText(Bokmal to "test"), false) }
        }
    }

    @Test
    fun `long form text uten vspace`() {
        render {
            title1 { text(Bokmal to "Form text long uten vspace") }
            paragraph { formText(Size.LONG, newText(Bokmal to "test"), false) }
        }
    }

    @Test
    fun `verge vedlegg med saksinfo`() {
        renderTestVedlegg(
            includeSakspart = true,
            felles = Fixtures.felles.copy(
                vergeNavn = "Verge vergeson"
            ),
        ) {
            testTitle1()
            paragraph { ipsumText() }
        }
    }

    @Test
    fun `vedlegg med saksinfo`() {
        renderTestVedlegg(
            includeSakspart = true,
        ) {
            testTitle1()
            paragraph { ipsumText() }
        }
    }

    @Test
    fun `vedlegg uten saksinfo`() {
        renderTestVedlegg(
            includeSakspart = false,
        ) {
            testTitle1()
            paragraph { ipsumText() }
        }
    }

    @Test
    fun `brev med saksbehandler underskrift`() {
        render(
            felles = Fixtures.felles.copy(
                signerendeSaksbehandlere = SignerendeSaksbehandlereImpl(
                    saksbehandler = "Ole Saksbehandler"
                )
            )
        ) {
            paragraph { ipsumText() }
        }
    }

    @Test
    fun `brev med saksbehandler og attestant underskrift`() {
        render(
            felles = Fixtures.felles.copy(
                signerendeSaksbehandlere = SignerendeSaksbehandlereImpl(
                    saksbehandler = "Ole Saksbehandler",
                    attesterendeSaksbehandler = "Per Saksbehandler"
                )
            )
        ) {
            paragraph { ipsumText() }
        }
    }

    @Test
    fun `test av ulike `() {
        render(
            felles = Fixtures.felles.copy(
                signerendeSaksbehandlere = SignerendeSaksbehandlereImpl(
                    saksbehandler = "Ole Saksbehandler",
                    attesterendeSaksbehandler = "Per Saksbehandler"
                )
            )
        ) {
            paragraph { ipsumText() }
        }
    }

    @Test
    fun `vedtaksbrev med saksbehandler underskrift`() {
        render(
            felles = Fixtures.felles.copy(
                signerendeSaksbehandlere = SignerendeSaksbehandlereImpl(
                    saksbehandler = "Ole Saksbehandler",
                    attesterendeSaksbehandler = "Per Attesterende"
                )
            )
        ) {
            paragraph { ipsumText() }
        }
    }

    @Test
    fun `should not add extra line change when nearing end of line before shipping content to new line`() {

        render {
            paragraph { text(Bokmal to "Denne testen skal vise at det ikke fremstår en bug hvor det kommer ekstra linjeskift under innhold som tar opp hele bredden av linjen.") }
            repeat(30) {
                paragraph {
                    text(
                        Bokmal to "Denne linjen skal ikke ha større mellomrom til neste setning enn de andre. Dette er littegranne filler ˌˌˌˌˌˌ"
                                + "ˌ".repeat(it)
                    )
                }
            }
        }
    }

    @Test
    fun `Table across multiple pages`() {
        render {
            paragraph {
                table(header = {
                    column(columnSpan = 2) { text(Bokmal to "Tekst") }
                    column(alignment = RIGHT) { text(Bokmal to "Kroner") }
                }) {
                    for (i in 1..100) {
                        row {
                            cell { text(Bokmal to "Rad $i") }
                            cell { text(Bokmal to "$i Kroner") }
                        }
                    }
                }
            }
        }
    }

    @ParameterizedTest
    @MethodSource("allElementCombinations")
    fun `Test unique content combinations`(elementA: ElementType, elementB: ElementType) {
        render(overrideName = "${elementA.description} then ${elementB.description}") {
            renderOutlineElementOfType(elementA)
            renderOutlineElementOfType(elementB)

        }
    }

    private fun OutlineOnlyScope<LangBokmal, EmptyBrevdata>.renderOutlineElementOfType(elementA: ElementType) {
        when (elementA) {
            ElementType.T1 -> testTitle1()
            ElementType.T2 -> testTitle2()
            ElementType.PAR -> {
                paragraph { ipsumText() }
            }

            ElementType.TABLE -> {
                paragraph {
                    testTable()
                }
            }

            ElementType.LIST -> {
                paragraph {
                    testList()
                }
            }
        }
    }

    private fun ParagraphOnlyScope<LangBokmal, EmptyBrevdata>.testList() {
        list {
            item {
                text(Bokmal to "Text point 1")
            }
            item {
                text(Bokmal to "Text point 2")
            }
        }
    }

    private fun ParagraphOnlyScope<LangBokmal, EmptyBrevdata>.testTable() {
        table(
            header = {
                column { text(Bokmal to "Column A") }
                column { text(Bokmal to "Column B") }
            }
        ) {
            row {
                cell { text(Bokmal to "Cell A-1") }
                cell { text(Bokmal to "Cell B-1") }
            }
        }
    }

    private fun OutlineOnlyScope<LangBokmal, EmptyBrevdata>.testTitle2() {
        title2 { text(Bokmal to "Second title") }
    }

    private fun OutlineOnlyScope<LangBokmal, EmptyBrevdata>.testTitle1() {
        title1 { text(Bokmal to "First title") }
    }

    enum class ElementType(val description: String) {
        T1("Title 1"),
        T2("Title 2"),
        PAR("Paragraph"),
        TABLE("Table"),
        LIST("Item list")
    }

    companion object {
        @JvmStatic
        fun allElementCombinations(): List<Arguments> =
            ElementType.entries.flatMapIndexed { index, type ->
                ElementType.entries.drop(index + 1).flatMap { otherType ->
                    listOf(Arguments.of(type, otherType), Arguments.of(otherType, type), Arguments.of(type, type))
                }
            }
    }
}