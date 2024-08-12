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
        val letter = Letter(template, Unit, Bokmal, Fixtures.fellesAuto)
        letter.renderTestPDF(testName, Path.of("build/test_visual/pdf"))
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
            title1 { text(Bokmal to "Title 1") }
        }
    }

    @Test
    fun `Title2 after letter title`() {
        render {
            title2 { text(Bokmal to "Title 2") }
        }
    }

    @Test
    fun `Title1 after letter title followed by paragraph`() {
        render {
            title1 { text(Bokmal to "Title 1") }
            paragraph { ipsumText() }
        }
    }

    @Test
    fun `Title2 after letter title followed by paragraph`() {
        render {
            title2 { text(Bokmal to "Title 2") }
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
    fun `Title1 then list`() {
        render {
            title1 { text(Bokmal to "Title 1") }
            paragraph {
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

    @Test
    fun `Title2 then list`() {
        render {
            title2 { text(Bokmal to "Title2 before point list") }
            paragraph {
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

    @Test
    fun `list after text`() {
        render {
            paragraph {
                text(Bokmal to "Text before point list")
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
    fun `Title2 then table`() {
        render {
            title2 { text(Bokmal to "A table title") }
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