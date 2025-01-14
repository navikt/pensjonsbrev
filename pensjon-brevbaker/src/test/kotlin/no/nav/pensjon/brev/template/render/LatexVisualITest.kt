package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.maler.example.lipsums
import no.nav.pensjon.brev.renderTestPDF
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.nio.file.Path

@Tag(TestTags.INTEGRATION_TEST)
class LatexVisualITest {

    private fun render(overrideName: String? = null, outlineInit: OutlineOnlyScope<LangBokmal, Unit>.() -> Unit) {
        val testName = overrideName ?: StackWalker.getInstance().walk { frames -> frames.skip(2).findFirst().map { it.methodName }.orElse("") }

        val template = createTemplate(testName, Unit::class, languages(Bokmal), LetterMetadata(
            testName,
            false,
            LetterMetadata.Distribusjonstype.VEDTAK,
            LetterMetadata.Brevtype.VEDTAKSBREV
        )) {
            title {
                text(Bokmal to testName)
            }
            outline { outlineInit() }
        }
        renderTestPDF(template, Unit, Bokmal, Fixtures.fellesAuto, testName, Path.of("build/test_visual/pdf"))
    }

    private fun ParagraphOnlyScope<LangBokmal, Unit>.ipsumText() = text(Bokmal to lipsums.first())

    @Test
    fun `Paragraph after letter title`() {
        render {
            paragraph { ipsumText() }
        }
    }

    @Test
    fun `Title1 after letter title`() {
        render {
            title1 { text(Bokmal to "A title1 text") }
        }
    }

    @Test
    fun `Title2 after letter title`() {
        render {
            title2 { text(Bokmal to "A title2 text") }
        }
    }

    @Test
    fun `Title1 after letter title followed by paragraph`() {
        render {
            title1 { text(Bokmal to "A title1 text") }
            paragraph { ipsumText() }
        }
    }

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
            title1 { text(Bokmal to "A table title") }
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
    fun `Title1 then table`() {
        render {
            title1 { text(Bokmal to "A table title") }
            paragraph {
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
}