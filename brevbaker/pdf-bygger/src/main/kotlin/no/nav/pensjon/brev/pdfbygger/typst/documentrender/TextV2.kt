package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.EscapedTypstContent
import no.nav.pensjon.brev.pdfbygger.typst.TypstMarkupScope
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Text

/**
 * Render a list of text elements as plain text (no formatting).
 * Used for titles and places where only plain text is needed.
 */
internal fun TypstMarkupScope.renderTextAsPlainV2(elements: List<Text>) {
    elements.forEach { renderPlainTextContentV2(it) }
}

/**
 * Render a list of text elements with full formatting support.
 */
internal fun TypstMarkupScope.renderTextContentV2(elements: List<Text>) {
    elements.forEach { renderTextContentV2(it) }
}

/**
 * Render a single text element as plain text.
 */
private fun TypstMarkupScope.renderPlainTextContentV2(element: Text) {
    when (element) {
        is Text.Literal, is Text.Variable -> appendContent(element.text)
        is Text.NewLine -> appendCode("#linebreak()")
    }
}

/**
 * Render a single text element with formatting (bold, italic).
 */
internal fun TypstMarkupScope.renderTextContentV2(element: Text) {
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
internal fun List<Text>.renderToEscapedContentV2(): EscapedTypstContent {
    val output = StringBuilder()
    TypstMarkupScope(output).renderTextAsPlainV2(this)
    return EscapedTypstContent(output.toString())
}

/**
 * Render text elements to an unescaped plain string.
 * Used for places where the raw text value is needed (e.g., dictionary values).
 */
internal fun List<Text>.renderToPlainStringV2(): String =
    this.joinToString("") { it.text }
