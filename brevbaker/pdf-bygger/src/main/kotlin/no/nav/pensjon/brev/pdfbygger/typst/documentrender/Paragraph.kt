package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.TypstCodeScope
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.ItemList
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Table
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text

// Note: renderList, renderTable, and renderTextContent are extension functions
// defined in List.kt, Table.kt, and Text.kt respectively.

internal fun TypstCodeScope.renderParagraph(
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
            is ItemList -> renderList(current)
            is Table -> renderTable(current)
            is Text -> continuousTextContent.add(current)
            else -> {}
        }
    }

    // Render any remaining text content
    if (continuousTextContent.isNotEmpty()) {
        renderTextParagraph(continuousTextContent)
    }
}

/**
 * Output: paragraph[text content here]
 */
private fun TypstCodeScope.renderTextParagraph(text: List<Text>) {
    appendCodeFunction("paragraph") {
        content { renderTextContent(text) }
    }
}
