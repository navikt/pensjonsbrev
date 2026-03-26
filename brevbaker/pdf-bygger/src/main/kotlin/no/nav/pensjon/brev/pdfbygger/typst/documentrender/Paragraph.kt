package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.TypstAppendable
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Form
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.ItemList
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Table
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text

// Note: renderForm, renderList, renderTable, and renderTextContent are extension functions
// defined in Form.kt, List.kt, Table.kt, and Text.kt respectively.

internal fun TypstAppendable.renderParagraph(
    element: LetterMarkup.Block.Paragraph,
) {
    var continuousTextContent = mutableListOf<Text>()

    element.content.forEachIndexed { _, current ->
        // If we encounter non-text content and have accumulated text, render it first
        if (current !is Text && continuousTextContent.isNotEmpty()) {
            renderTextParagraph(continuousTextContent)
            continuousTextContent = mutableListOf()
        }

        when (current) {
            is Form -> renderForm(current)
            is ItemList -> renderList(current)
            is Table -> renderTable(current)
            is Text -> continuousTextContent.add(current)
        }
    }

    // Render any remaining text content
    if (continuousTextContent.isNotEmpty()) {
        renderTextParagraph(continuousTextContent)
    }
}

/**
 * Render accumulated text content as a Typst paragraph.
 * Output: paragraph[text content here]
 */
private fun TypstAppendable.renderTextParagraph(text: List<Text>) {
    appendCodeFunction("paragraph") {
        content { renderTextContent(text) }
    }
}


