package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.maler.example.lipsums
import no.nav.pensjon.brev.renderTestPdfOutline
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@Tag(TestTags.INTEGRATION_TEST)
class LatexVisualITest {

    private fun render(
        overrideName: String? = null,
        overrideFelles: Felles? = null,
        brevtype: LetterMetadata.Brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        outlineInit: OutlineOnlyScope<LangBokmal, Unit>.() -> Unit,
    ) {
        renderTestPdfOutline(
            "test_visual/pdf",
            testName = overrideName ?: StackWalker.getInstance().walk { frames -> frames.skip(2).findFirst().map { it.methodName }.orElse("") },
            felles = overrideFelles,
            brevtype = brevtype,
            outlineInit = outlineInit,
        )
    }

    private fun ParagraphOnlyScope<LangBokmal, Unit>.ipsumText() =
        text(Bokmal to "Etiam porta turpis et eros ullamcorper sodales. Cras et eleifend leo. Aenean vehicula nunc sit amet quam tincidunt, id aliquam arcu cursus.")

    @Test
    fun `Two paragraphs in a row`() {
        render {
            paragraph { ipsumText() }
            paragraph { ipsumText() }
        }
    }

    @Test
    fun verge() {
        render(
            overrideFelles = Fixtures.felles.copy(
                vergeNavn = "Verge vergeson"
            )
        ) {
            testTitle1()
            paragraph { ipsumText() }
        }
    }

    @Test
    fun `infobrev med saksbehandler underskrift`() {
        render(
            overrideFelles = Fixtures.felles.copy(
                signerendeSaksbehandlere = SignerendeSaksbehandlere(
                    saksbehandler = "Ole Saksbehandler"
                )
            )
        ) {
            paragraph { ipsumText() }
        }
    }

    @Test
    fun `vedtaksbrev med saksbehandler underskrift`() {
        render(
            overrideFelles = Fixtures.felles.copy(
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
    fun `all element combinations with and without paragraph wrappers`() {
        val relevantEntries = ElementType.entries.filterNot { it == ElementType.T1 || it == ElementType.T2 }
        val combinations = relevantEntries.flatMapIndexed { index, type ->
            relevantEntries.drop(index + 1).flatMap { otherType ->
                listOf(type to otherType, otherType to type)
            }
        }

        render(overrideName = "all paragraph content inside same paragraph") {
            paragraph {
                combinations.forEach {
                    renderParagraphElementOfType(it.first)
                    renderParagraphElementOfType(it.second)
                }
            }
        }
        render(overrideName = "all paragraph content inside different paragraphs") {
            combinations.forEach {
                paragraph { renderParagraphElementOfType(it.first) }
                paragraph { renderParagraphElementOfType(it.second) }

            }
        }
    }

    @Test
    fun `Two pages`() {
        render {
            testTitle1()
            paragraph { ipsumText() }
            title1 { text(Bokmal to "Second title") }
            paragraph { ipsumText() }
        }
    }

    @ParameterizedTest
    @MethodSource("allParSeparatedElementCombinations")
    fun `Test unique content combinations`(elementA: ElementType, elementB: ElementType) {
        render(overrideName = "${elementA.description} then ${elementB.description}") {
            renderOutlineElementOfType(elementA)
            renderOutlineElementOfType(elementB)
        }
    }

    private fun OutlineOnlyScope<LangBokmal, Unit>.renderOutlineElementOfType(elementA: ElementType) {
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

    private fun ParagraphOnlyScope<LangBokmal, Unit>.renderParagraphElementOfType(elementA: ElementType) {
        when (elementA) {
            ElementType.PAR -> ipsumText()
            ElementType.TABLE -> testTable()
            ElementType.LIST -> testList()
            else -> throw IllegalArgumentException()
        }
    }


    private fun ParagraphOnlyScope<LangBokmal, Unit>.testList() {
        list {
            item {
                text(Bokmal to "Text point 1")
            }
            item {
                text(Bokmal to "Text point 2")
            }
        }
    }

    private fun ParagraphOnlyScope<LangBokmal, Unit>.testTable() {
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

    private fun OutlineOnlyScope<LangBokmal, Unit>.testTitle2() {
        title2 { text(Bokmal to "Second title") }
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
        fun allParSeparatedElementCombinations(): List<Arguments> =
            ElementType.entries.flatMapIndexed { index, type ->
                ElementType.entries.drop(index + 1).flatMap { otherType ->
                    listOf(Arguments.of(type, otherType), Arguments.of(otherType, type))
                }
            }

        @JvmStatic
        fun paragraphContentCombinations(): List<Arguments> {
            val relevantEntries = ElementType.entries.filterNot { it == ElementType.T1 || it == ElementType.T2 }
            return relevantEntries.flatMapIndexed { index, type ->
                relevantEntries.drop(index + 1).flatMap { otherType ->
                    listOf(Arguments.of(type, otherType), Arguments.of(otherType, type))
                }
            }
        }
    }
}

private fun OutlineOnlyScope<LangBokmal, Unit>.testTitle1() {
    title1 { text(Bokmal to "First title") }
}