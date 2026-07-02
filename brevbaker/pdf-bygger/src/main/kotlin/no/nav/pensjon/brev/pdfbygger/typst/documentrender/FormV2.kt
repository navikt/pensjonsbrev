package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.TypstCodeScope
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Block

/**
 * Render a v2 form-text block using content/form.typ's `formText` function.
 * Mirrors v1's [no.nav.pensjon.brev.pdfbygger.typst.documentrender.renderForm]'s
 * `Form.Text` handling, but as a top-level sibling block rather than content
 * nested inside a paragraph (per v2's flat block model).
 *
 * Note: this content is not persisted anywhere yet (no v2 editing model exists),
 * so the shape may still change.
 */
internal fun TypstCodeScope.renderFormTextV2(element: Block.FormText) {
    if (element.vspace) {
        appendCodeln("v(1em)")
    }

    appendCodeFunction("formText") {
        content { renderTextContentV2(element.prompt) }
        content {
            when (element.size) {
                Block.FormText.Size.NONE -> {}
                Block.FormText.Size.SHORT -> appendContent(". ".repeat(25))
                Block.FormText.Size.LONG -> appendContent(". ".repeat(60))
                Block.FormText.Size.FILL -> appendCode("#box(width: 1fr, repeat[.#h(1.5pt)])")
            }
        }
    }
}

/**
 * Render a v2 form choice block using content/form.typ's `formChoice` function.
 * Mirrors v1's [no.nav.pensjon.brev.pdfbygger.typst.documentrender.renderForm]'s
 * `Form.MultipleChoice` handling, but as a top-level sibling block rather than
 * content nested inside a paragraph (per v2's flat block model).
 *
 * Note: this content is not persisted anywhere yet (no v2 editing model exists),
 * so the shape may still change.
 */
internal fun TypstCodeScope.renderFormChoiceV2(element: Block.FormChoice) {
    if (element.vspace) {
        appendCodeln("v(1em)")
    }

    appendCodeFunction("formChoice") {
        content { renderTextContentV2(element.prompt) }
        element.choices.forEach { choice ->
            content { renderTextContentV2(choice.text) }
        }
    }
}
