package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.TypstAppendable
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Form

/**
 * Render form elements.
 *
 * Note: The Typst template doesn't have direct equivalents for form elements.
 * This implementation renders them as styled text with visual indicators.
 *
 * For Form.Text: Renders as "Prompt: ............" (dots indicating input area)
 * For Form.MultipleChoice: Renders as a list with checkbox indicators
 */
internal fun TypstAppendable.renderForm(element: Form) {
    when (element) {
        is Form.MultipleChoice -> renderMultipleChoice(element)
        is Form.Text -> renderFormText(element)
    }
}

/**
 * Render a multiple choice form element.
 * Output: A block with the prompt followed by checkbox-style options.
 */
private fun TypstAppendable.renderMultipleChoice(element: Form.MultipleChoice) {
    if (element.vspace) {
        appendln("v(1em)", escape = false)
    }

    // Render prompt
    appendCodeFunction("paragraph") {
        content {
            append("text(weight: \"bold\")[", escape = false)
            renderTextContent(element.prompt)
            append("]", escape = false)
        }
    }

    // Render choices as a list with checkbox symbols
    appendln("block(inset: (left: 1em))[", escape = false)
    element.choices.forEach { choice ->
        append("  box(stroke: 0.5pt, width: 0.8em, height: 0.8em) ", escape = false)
        renderTextContent(choice.text)
        appendln("linebreak()", escape = false)
    }
    appendln("]", escape = false)
}

/**
 * Render a text input form element.
 * Output: "Prompt: ........" where dots indicate the input area size.
 */
private fun TypstAppendable.renderFormText(element: Form.Text) {
    if (element.vspace) {
        appendln("v(1em)", escape = false)
    }

    appendCodeFunction("paragraph") {
        content {
            renderTextContent(element.prompt)
            append(" ", escape = false)

            val dots = when (element.size) {
                Form.Text.Size.NONE -> ""
                Form.Text.Size.SHORT -> ". ".repeat(25)
                Form.Text.Size.LONG -> ". ".repeat(60)
                Form.Text.Size.FILL -> "box(width: 1fr, repeat[.])"
            }

            if (element.size == Form.Text.Size.FILL) {
                append(dots, escape = false)
            } else {
                append(dots)
            }
        }
    }
}

