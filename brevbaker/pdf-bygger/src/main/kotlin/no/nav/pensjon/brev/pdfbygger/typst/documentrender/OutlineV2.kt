 package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.TypstCodeScope
import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Text


internal fun TypstCodeScope.renderBlocksV2(blocks: List<Block>) {
    blocks.forEach { renderBlockV2(it) }
}


private fun TypstCodeScope.renderBlockV2(block: Block) {
    when (block) {
        is Block.Paragraph -> renderParagraphV2(block)
        is Block.Title2 -> renderTitleV2("title1", block.content)
        is Block.Title3 -> renderTitleV2("title2", block.content)
        is Block.Title4 -> renderTitleV2("title3", block.content)
        is Block.ItemList -> renderItemListV2(block)
        is Block.NumberedList -> renderNumberedListV2(block)
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
