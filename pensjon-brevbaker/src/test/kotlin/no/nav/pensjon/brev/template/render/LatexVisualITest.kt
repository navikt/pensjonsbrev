package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.maler.example.lipsums
import no.nav.pensjon.brev.renderTestPdfOutline
import no.nav.pensjon.brev.renderTestVedleggPdf
import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import org.apache.commons.codec.language.bm.Lang
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@Tag(TestTags.INTEGRATION_TEST)
class LatexVisualITest {
    private fun render(
        overrideName: String? = null,
        title: String? = null,
        felles: Felles? = null,
        brevtype: LetterMetadata.Brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        attachments: List<AttachmentTemplate<LangBokmal, EmptyBrevdata>> = emptyList(),
        outlineInit: OutlineOnlyScope<LangBokmal, EmptyBrevdata>.() -> Unit,
    ) {
        val testName = overrideName ?: StackWalker.getInstance().walk { frames -> frames.skip(2).findFirst().map { it.methodName }.orElse("") }
        renderTestPdfOutline(
            "test_visual/pdf",
            testName = testName,
            felles = felles,
            brevtype = brevtype,
            attachments = attachments,
            outlineInit = outlineInit,
            title = title ?: testName
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
            testName = testName ?: StackWalker.getInstance().walk { frames -> frames.skip(2).findFirst().map { it.methodName }.orElse("") },
            includeSakspart = includeSakspart,
            outlineInit = vedleggOutlineInit,
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
                signerendeSaksbehandlere = SignerendeSaksbehandlere(
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
                signerendeSaksbehandlere = SignerendeSaksbehandlere(
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
                signerendeSaksbehandlere = SignerendeSaksbehandlere(
                    saksbehandler = "Ole Saksbehandler",
                    attesterendeSaksbehandler = "Per Attesterende"
                )
            )
        ) {
            paragraph { ipsumText() }
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

    @Test
    fun `Test unique content combinations`() {
        val combinations: List<Pair<ElementType, ElementType>> = ElementType.entries.flatMapIndexed { index, type ->
            ElementType.entries.drop(index + 1).flatMap { otherType ->
                listOf(type to otherType, otherType to type, type to type)
            }
        }

        // add combinations to separate attachments to save on compile time
        val attachments = combinations.map {(elementA, elementB) ->
            createAttachment<LangBokmal, EmptyBrevdata>(
                title = newText(Bokmal to "${elementA.description} then ${elementB.description}"),
                includeSakspart = false,
            ){
                renderOutlineElementOfType(elementA)
                renderOutlineElementOfType(elementB)
            }
        }

        render(attachments = attachments) {

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
                    listOf(Arguments.of(type, otherType), Arguments.of(otherType, type))
                }
            }
    }
}