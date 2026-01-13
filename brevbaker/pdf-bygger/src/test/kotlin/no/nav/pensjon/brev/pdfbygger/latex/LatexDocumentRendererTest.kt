package no.nav.pensjon.brev.pdfbygger.latex

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.pdfbygger.EksempelbrevRedigerbart
import no.nav.pensjon.brev.pdfbygger.LetterMarkupBlocksBuilder
import no.nav.pensjon.brev.pdfbygger.ParagraphBuilder
import no.nav.pensjon.brev.pdfbygger.latex.documentRender.LatexDocumentRenderer
import no.nav.pensjon.brev.pdfbygger.letterMarkup
import no.nav.pensjon.brev.template.render.DocumentFile
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text.FontType
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

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
    fun `title should be combined once with adjacent table`() {
        val titleText = "Test tittel 1234"
        assertNumberOfTextOccurrences(1, titleText) {
            title1 { text(titleText) }
            paragraph { testTable() }
        }
    }

    @Test
    fun `table should not grab the title of previous title if there are other elements between them in the same paragraph`() {
        val titleText = "Test tittel 1234"
        assertNumberOfTextOccurrences(1, titleText) {
            title1 { text(titleText) }
            paragraph {
                testText()
                testTable()
            }
        }
    }

    @Test
    fun `table should not grab the title of previous title if there are other elements between them`() {
        val titleText = "Test tittel 1234"
        assertNumberOfTextOccurrences(1, titleText) {
            title1 { text(titleText) }
            paragraph { testText() }
            paragraph {
                testText()
                testTable()
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

    @Test
    fun `renderPDF redigertBrev uses letterMarkup from argument and includes attachments`(): Unit = runBlocking {
        val pdfRequest = PDFRequest(
            letterMarkup = EksempelbrevRedigerbart.brev,
            attachments = listOf(EksempelbrevRedigerbart.vedlegg),
            language = LanguageCode.BOKMAL,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
        val rendered = LatexDocumentRenderer.render(pdfRequest)

        assertThat(rendered.files.first { it.fileName == "letter.tex" }.content).contains("Du har fått innvilget pensjon")
        assertThat(rendered.files.first { it.fileName == "attachment_0.tex" }.content).contains("Test vedlegg")
    }

    @Test
    fun `illegal bold title should have font type discarded`(){
        val markup = letterMarkup {
            title {
                text(
                    "Denne teksten skal ikke faktisk bli bold!",
                    fontType = FontType.BOLD
                )
            }

            // Main letter content
            outline {
                // section title
                title1 {
                    text("Denne teksten skal ikke faktisk bli bold!", fontType = FontType.BOLD)
                }
            }
        }

        val pdfRequest = PDFRequest(
            letterMarkup = markup,
            attachments = emptyList(),
            language = LanguageCode.BOKMAL,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )

        val rendered = LatexDocumentRenderer.render(pdfRequest)

        assertThat(rendered.files.first { it.fileName == "letter.tex" }.content).doesNotContain("textbf")
    }

    private fun assertNumberOfParagraphs(
        expectedParagraphs: Int,
        outline: LetterMarkupBlocksBuilder.() -> Unit
    ) = assertNumberOfTextOccurrences(expectedParagraphs, "templateparagraph", outline)

    private fun assertNumberOfTextOccurrences(
        expectedOccurrences: Int,
        expectedText: String,
        outline: LetterMarkupBlocksBuilder.() -> Unit
    ) {
        val markup = letterMarkup {
            outline {
                outline()
            }
        }

        val latexDocument = LatexDocumentRenderer.render(
            PDFRequest(
                letterMarkup = markup,
                attachments = emptyList(),
                language = LanguageCode.BOKMAL,
                brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
            )
        )
        val tex = latexDocument.files.find { it.fileName == "letter.tex" } as DocumentFile
        assertThat(tex.content.split("\\").count { it.contains(expectedText) }).isEqualTo(expectedOccurrences)
    }


    private fun ParagraphBuilder.testItemList() {
        list {
            item { text("test") }
        }
    }

    private fun ParagraphBuilder.testTable() {
        table(
            header = {
                column { text("Column A") }
                column { text("Column B") }
            }
        ) {
            row {
                cell { text("Cell A-1") }
                cell { text("Cell B-1") }
            }
        }
    }

    private fun ParagraphBuilder.testText() {
        text("test")
    }
}
