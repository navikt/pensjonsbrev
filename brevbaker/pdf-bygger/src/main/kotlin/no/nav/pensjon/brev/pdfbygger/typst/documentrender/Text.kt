package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.EscapedTypstContent
import no.nav.pensjon.brev.pdfbygger.typst.TypstMarkupScope
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text

/**
 * Render a list of text elements as plain text (no formatting).
 * Used for titles and places where only plain text is needed.
 */
internal fun TypstMarkupScope.renderTextAsPlain(elements: List<Text>) {
    elements.forEach { renderPlainTextContent(it) }
}

/**
 * Render a list of text elements with full formatting support.
 */
internal fun TypstMarkupScope.renderTextContent(elements: List<Text>) {
    elements.forEach { renderTextContent(it) }
}

/**
 * Render a single text element as plain text.
 */
private fun TypstMarkupScope.renderPlainTextContent(element: Text) {
    when (element) {
        is Text.Literal, is Text.Variable -> appendContent(element.text)
        is Text.NewLine -> appendCode("#linebreak()")
    }
}

/**
 * Render a single text element with formatting (bold, italic).
 *
 * In Typst:
 * - Bold: `#text(weight: "bold")[text]`
 * - Italic: `#emph[text]`
 * - Line break: `#linebreak()`
 */
internal fun TypstMarkupScope.renderTextContent(element: Text) {
    when (element) {
        is Text.Literal, is Text.Variable -> {
            when (element.fontType) {
                Text.FontType.PLAIN -> appendContent(element.text)
                Text.FontType.BOLD -> {
                    appendCode("#text(weight: \"bold\")[")
                    appendContent(element.text)
                    appendCode("]")
                }
                Text.FontType.ITALIC -> {
                    appendCode("#emph[")
                    appendContent(element.text)
                    appendCode("]")
                }
            }
        }
        is Text.NewLine -> appendCode("#linebreak()")
    }
}

/**
 * Render text elements to a plain string.
 * Returns escaped text suitable for use in Typst content.
 */
internal fun List<Text>.renderToEscapedContent(): EscapedTypstContent {
    val output = StringBuilder()
    TypstMarkupScope(output).renderTextAsPlain(this)
    return EscapedTypstContent(output.toString())
}

/**
 * Render text elements to an unescaped plain string.
 * Used for places where the raw text value is needed (e.g., dictionary values).
 */
internal fun List<Text>.renderToPlainString(): String =
    this.joinToString("") { it.text }
