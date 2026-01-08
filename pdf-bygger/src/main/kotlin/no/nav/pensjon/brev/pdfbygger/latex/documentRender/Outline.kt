package no.nav.pensjon.brev.pdfbygger.latex.documentRender

import no.nav.pensjon.brev.pdfbygger.latex.LatexAppendable
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Table
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text

internal fun LatexAppendable.renderBlocks(blocks: List<LetterMarkup.Block>) {
    blocks.forEachIndexed { index, block ->
        val previous = blocks.getOrNull(index - 1)
        val next = blocks.getOrNull(index + 1)
        renderBlock(block, previous, next)
    }
}

private fun LatexAppendable.renderBlock(
    block: LetterMarkup.Block,
    previous: LetterMarkup.Block?,
    next: LetterMarkup.Block?
): Unit =
    when (block) {
        is LetterMarkup.Block.Paragraph -> renderParagraph(block, previous)

        is LetterMarkup.Block.Title1 -> renderTitleIfNonEmptyText(block.content) { titleText ->
            if (!next.startsWithTable()) {
                appendCmd("lettersectiontitleone", titleText, escape = false) // allerede escapet over.
            }
        }

        is LetterMarkup.Block.Title2 -> renderTitleIfNonEmptyText(block.content) { titleText ->
            if (!next.startsWithTable()) {
                appendCmd("lettersectiontitletwo", titleText, escape = false) // allerede escapet over.
            }
        }

        is LetterMarkup.Block.Title3 -> renderTitleIfNonEmptyText(block.content) { titleText ->
            if (!next.startsWithTable()) {
                appendCmd("lettersectiontitlethree", titleText, escape = false) // allerede escapet over.
            }
        }

    }


private fun LetterMarkup.Block?.startsWithTable(): Boolean =
    (this is LetterMarkup.Block.Paragraph) && this.content.firstOrNull() is Table

private fun LatexAppendable.renderTitleIfNonEmptyText(content: List<Text>, render: LatexAppendable.(String) -> Unit) {
    val text = content.renderToString()
    if (text.isNotEmpty()) {
        render(text)
    }
}



