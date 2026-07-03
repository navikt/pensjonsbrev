package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.TypstCodeScope
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Block

/**
 * Render a v2 paragraph block. Unlike v1, v2's Paragraph.content is Text-only
 * (lists/tables are hoisted out to sibling blocks by Letter2MarkupV2), so there
 * is no need to split the paragraph content by type.
 *
 * Output: paragraph[text content here]
 */
internal fun TypstCodeScope.renderParagraphV2(element: Block.Paragraph) {
    if (element.content.isEmpty()) return

    appendCodeFunction("paragraph") {
        content { renderTextContentV2(element.content) }
    }
}
