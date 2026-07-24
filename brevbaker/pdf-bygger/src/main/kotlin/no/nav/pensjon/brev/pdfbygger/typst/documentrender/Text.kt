package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.EscapedTypstContent
import no.nav.pensjon.brev.pdfbygger.typst.TypstMarkupScope
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text

internal fun TypstMarkupScope.renderTextAsPlain(elements: List<Text>) {
    elements.forEach { renderPlainTextContent(it) }
}

internal fun TypstMarkupScope.renderTextContent(elements: List<Text>) {
    elements.forEach { renderTextContent(it) }
}

private fun TypstMarkupScope.renderPlainTextContent(element: Text) {
    when (element) {
        is Text.Literal, is Text.Variable -> appendContent(element.text)
        is Text.NewLine -> appendCode("#linebreak()")
    }
}


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

internal fun List<Text>.renderToEscapedContent(): EscapedTypstContent {
    val output = StringBuilder()
    TypstMarkupScope(output).renderTextAsPlain(this)
    return EscapedTypstContent(output.toString())
}

internal fun List<Text>.renderToPlainString(): String =
    this.joinToString("") { it.text }
