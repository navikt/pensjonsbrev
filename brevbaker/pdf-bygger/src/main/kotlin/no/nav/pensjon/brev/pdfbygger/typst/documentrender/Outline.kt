package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.TypstAppendable
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Table
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text

/**
 * Render all blocks in the letter/attachment.
 */
internal fun TypstAppendable.renderBlocks(blocks: List<LetterMarkup.Block>) {
    blocks.forEachIndexed { index, block ->
        val previous = blocks.getOrNull(index - 1)
        val next = blocks.getOrNull(index + 1)
        renderBlock(block, previous, next)
    }
}

/**
 * Render a single block element.
 *
 * Block types:
 * - Title1/2/3: Section headings rendered as title1[...], title2[...], title3[...]
 * - Paragraph: Content paragraphs that may contain text, lists, tables, forms
 *
 * Special handling: If a title is immediately followed by a table, the title is not rendered
 * separately - instead it's passed to the table as its title (handled in Table.kt).
 */
private fun TypstAppendable.renderBlock(
    block: LetterMarkup.Block,
    previous: LetterMarkup.Block?,
    next: LetterMarkup.Block?
) {
    when (block) {
        is LetterMarkup.Block.Paragraph -> renderParagraph(block, previous)

        is LetterMarkup.Block.Title1 -> renderTitleIfNonEmptyText(block.content) { titleText ->
            if (!next.startsWithTable()) {
                appendCodeFunction("title1") {
                    content { append(titleText, escape = false) } // Already escaped
                }
            }
        }

        is LetterMarkup.Block.Title2 -> renderTitleIfNonEmptyText(block.content) { titleText ->
            if (!next.startsWithTable()) {
                appendCodeFunction("title2") {
                    content { append(titleText, escape = false) } // Already escaped
                }
            }
        }

        is LetterMarkup.Block.Title3 -> renderTitleIfNonEmptyText(block.content) { titleText ->
            if (!next.startsWithTable()) {
                appendCodeFunction("title3") {
                    content { append(titleText, escape = false) } // Already escaped
                }
            }
        }
    }
}

/**
 * Check if a block starts with a table.
 * Used to determine if a title should be rendered separately or passed to the table.
 */
private fun LetterMarkup.Block?.startsWithTable(): Boolean =
    (this is LetterMarkup.Block.Paragraph) && this.content.firstOrNull() is Table

/**
 * Render a title only if it has non-empty text content.
 */
private fun TypstAppendable.renderTitleIfNonEmptyText(content: List<Text>, render: TypstAppendable.(String) -> Unit) {
    val text = content.renderToString()
    if (text.isNotEmpty()) {
        render(text)
    }
}

