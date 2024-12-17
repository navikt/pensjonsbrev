package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.maler.example.lipsums
import no.nav.pensjon.brev.renderTestPDF
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.nio.file.Path

@Tag(TestTags.INTEGRATION_TEST)
class LatexVisualITest {

    private fun render(overrideName: String? = null, outlineInit: OutlineOnlyScope<LangBokmal, Unit>.() -> Unit) {
        val testName = overrideName ?: StackWalker.getInstance().walk { frames -> frames.skip(2).findFirst().map { it.methodName }.orElse("") }

        val template = createTemplate(
            testName, Unit::class, languages(Bokmal), LetterMetadata(
                testName,
                false,
                LetterMetadata.Distribusjonstype.VEDTAK,
                LetterMetadata.Brevtype.VEDTAKSBREV
            )
        ) {
            title {
                text(Bokmal to testName)
            }
            outline { outlineInit() }
        }
        val letter = Letter(template, Unit, Bokmal, Fixtures.fellesAuto)
        letter.renderTestPDF(testName, Path.of("build/test_visual/pdf"))
    }

    private fun ParagraphOnlyScope<LangBokmal, Unit>.ipsumText() = text(Bokmal to lipsums.first())

    @Test
    fun `Two paragraphs in a row`() {
        render {
            paragraph { ipsumText() }
            paragraph { ipsumText() }
        }
    }

    @Test
    fun `Title1 with ingress text then table`() {
        render {
            title1 { text(Bokmal to "Title 1") }
            paragraph {
                text(Bokmal to "Some further descriptive text.")
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
        }
    }


    @Test
    fun `Two pages`() {
        render {
            title1 { text(Bokmal to "First title") }
            paragraph { ipsumText() }
            title1 { text(Bokmal to "Second title") }
            paragraph { ipsumText() }
        }
    }

    @ParameterizedTest
    @MethodSource("elementCombinations")
    fun `Test unique content combinations`(elementA: ElementType, elementB: ElementType) {
        render(overrideName = "${elementA.description} then ${elementB.description}") {
            renderElementOfType(elementA)
            renderElementOfType(elementB)
        }
    }

    private fun OutlineOnlyScope<LangBokmal, Unit>.renderElementOfType(elementA: ElementType) {
        when(elementA) {
            ElementType.T1 -> title1 { text(Bokmal to "First title") }
            ElementType.T2 -> title2 { text(Bokmal to "Second title") }
            ElementType.PAR -> paragraph { ipsumText() }

            ElementType.TABLE -> paragraph {
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

            ElementType.LIST -> paragraph {
                list {
                    item {
                        text(Bokmal to "Text point 1")
                    }
                    item {
                        text(Bokmal to "Text point 2")
                    }
                }
            }
        }
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
        fun elementCombinations(): List<Arguments> = ElementType.entries.flatMapIndexed { index, type ->
            ElementType.entries.drop(index + 1).flatMap { otherType ->
                listOf(Arguments.of(type, otherType), Arguments.of(otherType, type))
            }
        }
    }
}