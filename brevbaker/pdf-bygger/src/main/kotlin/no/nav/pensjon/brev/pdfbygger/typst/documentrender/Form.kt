package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.TypstCodeScope
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Form

internal fun TypstCodeScope.renderForm(element: Form) {
    when (element) {
        is Form.MultipleChoice -> renderMultipleChoice(element)
        is Form.Text -> renderFormText(element)
    }
}


private fun TypstCodeScope.renderMultipleChoice(element: Form.MultipleChoice) {
    if (element.vspace) {
        appendCodeln("v(1em)")
    }

    appendCodeFunction("formChoice") {
        content { renderTextContent(element.prompt) }
        element.choices.forEach { choice ->
            content { renderTextContent(choice.text) }
        }
    }
}

private fun TypstCodeScope.renderFormText(element: Form.Text) {
    if (element.vspace) {
        appendCodeln("v(1em)")
    }

    appendCodeFunction("formText") {
        content { renderTextContent(element.prompt) }
        content {
            when (element.size) {
                Form.Text.Size.NONE -> {}
                Form.Text.Size.SHORT -> appendContent(". ".repeat(25))
                Form.Text.Size.LONG -> appendContent(". ".repeat(60))
                Form.Text.Size.FILL -> appendCode("#box(width: 1fr, repeat[.#h(1.5pt)])")
            }
        }
    }
}
