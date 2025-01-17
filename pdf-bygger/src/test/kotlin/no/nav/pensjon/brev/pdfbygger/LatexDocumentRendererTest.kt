package no.nav.pensjon.brev.pdfbygger

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.pdfbygger.Fixtures.felles
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.render.DocumentFile
import no.nav.pensjon.brev.template.render.Letter2Markup
import kotlin.test.Test

class LatexDocumentRendererTest {
    @Test
    fun `paragraph is split in two using lists`() {
        assertNumberOfParagraphs(2) {
            paragraph {
                testText()
                testItemList()
                testText()
            }
        }
    }

    @Test
    fun `paragraph is split in two using tables`() {
        assertNumberOfParagraphs(2) {
            paragraph {
                testTable()
                testText()
                testTable()
                testText()
                testTable()
            }
        }
    }

    @Test
    fun `paragraphs with text is not split`() {
        assertNumberOfParagraphs(2) {
            paragraph {
                testText()
                testText()
            }
            paragraph {
                testText()
                testText()
            }
        }
    }

    @Test
    fun `paragraphs no text does not result in paragraph element`() {
        assertNumberOfParagraphs(0) {
            paragraph { testItemList() }
            paragraph { testTable() }
        }
    }

    @Test
    fun `paragraphs ending with a non text item does not split the paragraph`() {
        assertNumberOfParagraphs(1) {
            paragraph {
                testText()
                testItemList()
            }
        }
    }

    fun assertNumberOfParagraphs(
        expectedParagraphs: Int,
        outline: OutlineOnlyScope<LangBokmal, EmptyBrevdata>.() -> Unit
    ) {
        val letter = Letter(
            LetterExample.template,
            EmptyBrevdata,
            Language.Bokmal,
            Fixtures.fellesAuto,
        )
        runBlocking {
            val markup =
                Letter2Markup.render(Letter(outlineTestTemplate(outline), EmptyBrevdata, Language.Bokmal, felles))

            val latexDocument = LatexDocumentRenderer.render(
                PDFRequest(
                    letterMarkup = markup.letterMarkup,
                    attachments = markup.attachments,
                    language = letter.language,
                    felles = letter.felles,
                    brevtype = letter.template.letterMetadata.brevtype,
                )
            )
            val tex = latexDocument.files.find { it.fileName == "letter.tex" } as DocumentFile.PlainText
            assertThat(tex.content.lines().count { it.contains("templateparagraph") }, equalTo(expectedParagraphs))
        }
    }


    private fun ParagraphOnlyScope<LangBokmal, EmptyBrevdata>.testItemList() {
        list {
            item { text(Language.Bokmal to "test") }
        }
    }

    private fun ParagraphOnlyScope<LangBokmal, EmptyBrevdata>.testTable() {
        table(
            header = {
                column { text(Language.Bokmal to "Column A") }
                column { text(Language.Bokmal to "Column B") }
            }
        ) {
            row {
                cell { text(Language.Bokmal to "Cell A-1") }
                cell { text(Language.Bokmal to "Cell B-1") }
            }
        }
    }

    private fun ParagraphOnlyScope<LangBokmal, EmptyBrevdata>.testText() {
        text(Language.Bokmal to "test")
    }

}