package no.nav.pensjon.brev.pdfbygger.latex.documentRender

import no.nav.pensjon.brev.pdfbygger.latex.LatexAppendable
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Form
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.ItemList
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Table
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text

internal fun LatexAppendable.renderParagraph(
    element: LetterMarkup.Block.Paragraph,
    previous: LetterMarkup.Block?,
) {
    var continousTextContent = mutableListOf<Text>()

    element.content.forEachIndexed { index, current ->
        if (current !is Text && continousTextContent.isNotEmpty()) {
            renderTextParagraph(continousTextContent)
            continousTextContent = mutableListOf()
        }

        when (current) {
            is Form -> renderForm(current)
            is ItemList -> renderList(current)
            is Table -> renderTable(current, previous.takeIf { index == 0})
            is Text -> continousTextContent.add(current)
        }
    }
    if (continousTextContent.isNotEmpty()) {
        renderTextParagraph(continousTextContent)
    }
}

private fun LatexAppendable.renderTextParagraph(text: List<Text>): Unit =
    appendCmd("templateparagraph") {
        arg { renderTextContent(text) }
    }