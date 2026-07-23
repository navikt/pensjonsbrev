package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.EscapedTypstContent
import no.nav.pensjon.brev.pdfbygger.typst.TypstMarkupScope
import no.nav.brev.brevbaker.markup.outline.Text

internal fun TypstMarkupScope.renderTextAsPlainV2(elements: List<Text>) {
    elements.forEach { renderPlainTextContentV2(it) }
}

internal fun TypstMarkupScope.renderTextContentV2(elements: List<Text>) {
    elements.forEach { renderTextContentV2(it) }
}

private fun TypstMarkupScope.renderPlainTextContentV2(element: Text) {
    when (element) {
        is Text.Literal, is Text.Variable -> appendContent(element.text)
        is Text.NewLine -> appendCode("#linebreak()")
    }
}


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

internal fun List<Text>.renderToEscapedContentV2(): EscapedTypstContent {
    val output = StringBuilder()
    TypstMarkupScope(output).renderTextAsPlainV2(this)
    return EscapedTypstContent(output.toString())
}

internal fun List<Text>.renderToPlainStringV2(): String =
    this.joinToString("") { it.text }
