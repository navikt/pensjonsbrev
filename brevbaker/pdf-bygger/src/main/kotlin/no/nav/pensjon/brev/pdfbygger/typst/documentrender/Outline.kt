package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.TypstAppendable
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text

/**
 * Render all blocks in the letter/attachment.
 */
internal fun TypstAppendable.renderBlocks(blocks: List<LetterMarkup.Block>) {
    blocks.forEach { renderBlock(it) }
}

/**
 * Render a single block element.
 *
 * Block types:
 * - Title1/2/3: Section headings rendered as title1[...], title2[...], title3[...]
 * - Paragraph: Content paragraphs that may contain text, lists, tables, forms
 */
private fun TypstAppendable.renderBlock(block: LetterMarkup.Block) {
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
private fun TypstAppendable.renderTitle(functionName: String, content: List<Text>) {
    val text = content.renderToString()
    if (text.isNotEmpty()) {
        appendCodeFunction(functionName) {
            content { append(text, escape = false) } // Already escaped
        }
    }
}

