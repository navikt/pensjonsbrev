package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.TypstCodeScope
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text

internal fun TypstCodeScope.renderBlocks(blocks: List<LetterMarkup.Block>) {
    blocks.forEach { renderBlock(it) }
}

private fun TypstCodeScope.renderBlock(block: LetterMarkup.Block) {
    when (block) {
        is LetterMarkup.Block.Paragraph -> renderParagraph(block)
        is LetterMarkup.Block.Title1 -> renderTitle("title1", block.content)
        is LetterMarkup.Block.Title2 -> renderTitle("title2", block.content)
        is LetterMarkup.Block.Title3 -> renderTitle("title3", block.content)
    }
}

/**
 * Render a title only if it has non-empty text content.
 */
private fun TypstCodeScope.renderTitle(functionName: String, content: List<Text>) {
    val text = content.renderToEscapedContent()
    if (text.value.isNotEmpty()) {
        appendCodeFunction(functionName) {
            content { appendContent(text) }
        }
    }
}
