 package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.TypstCodeScope
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Block
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Text

/**
 * Render all blocks in a v2 letter/attachment.
 */
internal fun TypstCodeScope.renderBlocksV2(blocks: List<Block>) {
    blocks.forEach { renderBlockV2(it) }
}

/**
 * Render a single v2 block element.
 *
 * Block types:
 * - Title2/3/4: Section headings. These correspond directly to v1's Title1/2/3
 *   blocks (v2 shifts titles by one level relative to v1 because the letter's
 *   own top-level title moved from a dedicated `title` field into `title1`).
 *   They map onto the existing typst title1/title2/title3 functions (heading
 *   depths 2/3/4) unchanged.
 * - Paragraph: Text-only content.
 * - ItemList/NumberedList: Top-level list blocks (nested inside a paragraph in v1).
 * - Table: Top-level table block (nested inside a paragraph in v1).
 * - FormText/FormChoice: Top-level form blocks (nested inside a paragraph in v1).
 *   Not persisted anywhere yet (no v2 editing model exists).
 */
private fun TypstCodeScope.renderBlockV2(block: Block) {
    when (block) {
        is Block.Paragraph -> renderParagraphV2(block)
        is Block.Title.Title2 -> renderTitleV2("title1", block.content)
        is Block.Title.Title3 -> renderTitleV2("title2", block.content)
        is Block.Title.Title4 -> renderTitleV2("title3", block.content)
        is Block.ListContent -> renderListV2(block)
        is Block.Table -> renderTableV2(block)
        is Block.FormText -> renderFormTextV2(block)
        is Block.FormChoice -> renderFormChoiceV2(block)
    }
}

/**
 * Render a title only if it has non-empty text content.
 */
private fun TypstCodeScope.renderTitleV2(functionName: String, content: List<Text>) {
    val text = content.renderToEscapedContentV2()
    if (text.value.isNotEmpty()) {
        appendCodeFunction(functionName) {
            content { appendContent(text) }
        }
    }
}
