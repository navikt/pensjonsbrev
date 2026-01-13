package no.nav.pensjon.brev.pdfbygger.latex.documentRender

import no.nav.pensjon.brev.pdfbygger.latex.LatexAppendable
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text
import kotlin.collections.forEach

internal fun LatexAppendable.renderTextAsPlain(elements: List<Text>): Unit =
    elements.forEach {
        renderPlainTextContent(it)
    }

internal fun LatexAppendable.renderTextContent(elements: List<Text>): Unit =
    elements.forEach { renderTextContent(it) }

private fun LatexAppendable.renderPlainTextContent(element: Text) {
    appendCmd("lettertext") {
        arg {
            when (element) {
                is Text.Literal, is Text.Variable -> append(element.text)
                is Text.NewLine -> appendCmd("newline")
            }
        }
    }
}

internal fun LatexAppendable.renderTextContent(element: Text) {
    appendCmd("lettertext") {
        arg {
            when (element) {
                is Text.Literal, is Text.Variable -> {
                    when (element.fontType) {
                        Text.FontType.PLAIN -> append(element.text)
                        Text.FontType.BOLD -> appendCmd("textbf") { arg { append(element.text) } }
                        Text.FontType.ITALIC -> appendCmd("textit") { arg { append(element.text) } }
                    }
                }

                is Text.NewLine -> appendCmd("newline")
            }
        }
    }

}

internal fun List<Text>.renderToString(): String =
    String(StringBuilder().also { LatexAppendable(it).renderTextAsPlain(this) })
