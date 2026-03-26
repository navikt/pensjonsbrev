package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.TypstAppendable
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text

/**
 * Render a list of text elements as plain text (no formatting).
 * Used for titles and places where only plain text is needed.
 */
internal fun TypstAppendable.renderTextAsPlain(elements: List<Text>) {
    elements.forEach { renderPlainTextContent(it) }
}

/**
 * Render a list of text elements with full formatting support.
 */
internal fun TypstAppendable.renderTextContent(elements: List<Text>) {
    elements.forEach { renderTextContent(it) }
}

/**
 * Render a single text element as plain text.
 */
private fun TypstAppendable.renderPlainTextContent(element: Text) {
    when (element) {
        is Text.Literal, is Text.Variable -> append(element.text)
        is Text.NewLine -> append("\n", escape = false)
    }
}

/**
 * Render a single text element with formatting (bold, italic).
 *
 * In Typst:
 * - Bold: Uses #text(weight: "bold")[text] for reliable bold
 * - Italic: Uses #emph[text] for reliable italic
 * - Line break: Uses #linebreak()
 */
internal fun TypstAppendable.renderTextContent(element: Text) {
    when (element) {
        is Text.Literal, is Text.Variable -> {
            when (element.fontType) {
                Text.FontType.PLAIN -> append(element.text)
                Text.FontType.BOLD -> {
                    // Use #text(weight: "bold") for reliable bold that doesn't interfere with escaping
                    append("#text(weight: \"bold\")[", escape = false)
                    append(element.text)
                    append("]", escape = false)
                }
                Text.FontType.ITALIC -> {
                    // Use #emph for reliable italic that doesn't interfere with escaping
                    append("#emph[", escape = false)
                    append(element.text)
                    append("]", escape = false)
                }
            }
        }
        is Text.NewLine -> append("#linebreak()", escape = false)
    }
}

/**
 * Render text elements to a plain string (for use in function arguments).
 * Returns escaped text suitable for use in Typst content.
 */
internal fun List<Text>.renderToString(): String =
    StringBuilder().also { sb ->
        TypstAppendable(sb).renderTextAsPlain(this)
    }.toString()

/**
 * Render text elements to an unescaped plain string.
 * Used for places where the raw text value is needed (e.g., dictionary values).
 */
internal fun List<Text>.renderToPlainString(): String =
    this.joinToString("") { it.text }

