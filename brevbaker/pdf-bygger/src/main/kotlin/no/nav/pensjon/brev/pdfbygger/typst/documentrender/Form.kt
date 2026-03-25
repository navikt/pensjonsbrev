package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.TypstAppendable
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Form

/**
 * Render form elements using form.typ functions.
 *
 * For Form.Text: Uses formText(prompt, dots)
 * For Form.MultipleChoice: Uses formChoice(prompt, ..choices)
 */
internal fun TypstAppendable.renderForm(element: Form) {
    when (element) {
        is Form.MultipleChoice -> renderMultipleChoice(element)
        is Form.Text -> renderFormText(element)
    }
}

/**
 * Render a multiple choice form element.
 * Output: formChoice[prompt][choice1][choice2]...
 */
private fun TypstAppendable.renderMultipleChoice(element: Form.MultipleChoice) {
    if (element.vspace) {
        appendln("v(1em)", escape = false)
    }

    appendCodeFunction("formChoice") {
        content { renderTextContent(element.prompt) }
        element.choices.forEach { choice ->
            content { renderTextContent(choice.text) }
        }
    }
}

/**
 * Render a text input form element.
 * Output: formText[prompt][dots]
 */
private fun TypstAppendable.renderFormText(element: Form.Text) {
    if (element.vspace) {
        appendln("v(1em)", escape = false)
    }

    appendCodeFunction("formText") {
        content { renderTextContent(element.prompt) }
        content {
            when (element.size) {
                Form.Text.Size.NONE -> {}
                Form.Text.Size.SHORT -> append(". ".repeat(25))
                Form.Text.Size.LONG -> append(". ".repeat(60))
                Form.Text.Size.FILL -> append("#box(width: 1fr, repeat[.])", escape = false)
            }
        }
    }
}

