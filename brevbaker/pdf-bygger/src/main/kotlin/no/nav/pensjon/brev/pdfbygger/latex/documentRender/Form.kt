package no.nav.pensjon.brev.pdfbygger.latex.documentRender

import no.nav.pensjon.brev.pdfbygger.latex.LatexAppendable
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Form

internal fun LatexAppendable.renderForm(element: Form): Unit =
    when (element) {
        is Form.MultipleChoice -> {
            if (element.vspace) {
                appendCmd("formvspace")
            }

            appendCmd("begin") {
                arg { append("formChoice") }
                arg { renderTextContent(element.prompt) }
            }

            element.choices.forEach {
                appendCmd("formchoiceitem")
                renderTextContent(it.text)
            }

            appendCmd("end", "formChoice")
        }

        is Form.Text -> {
            if (element.vspace) {
                appendCmd("formvspace")
            }

            appendCmd("formText") {
                arg {
                    val size = when (element.size) {
                        Form.Text.Size.NONE -> 0
                        Form.Text.Size.SHORT -> 25
                        Form.Text.Size.LONG -> 60
                        Form.Text.Size.FILL -> null
                    }
                    renderTextContent(element.prompt)
                    if (size != null) {
                        append(" ${". ".repeat(size)}")
                    } else {
                        appendCmd("dotfill")
                    }
                }
            }
        }
    }
