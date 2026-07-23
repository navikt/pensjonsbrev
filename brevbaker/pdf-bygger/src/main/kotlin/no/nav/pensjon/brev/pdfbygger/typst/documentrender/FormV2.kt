package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.TypstCodeScope
import no.nav.brev.brevbaker.markup.outline.Block

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