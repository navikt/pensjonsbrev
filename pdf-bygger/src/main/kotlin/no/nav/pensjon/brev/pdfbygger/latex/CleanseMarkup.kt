package no.nav.pensjon.brev.pdfbygger.latex

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Block
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl

fun LetterMarkup.clean(): LetterMarkup = cleanMarkup(this)

@OptIn(InterneDataklasser::class)
private fun cleanMarkup(markup: LetterMarkup): LetterMarkup = LetterMarkupImpl(
    title = markup.title,
    sakspart = markup.sakspart,
    blocks = markup.blocks.mapNotNull { clean(it) }.removeSucceedingEmptyBlocks(),
    signatur = markup.signatur
)

private fun List<Block>.removeSucceedingEmptyBlocks(): List<Block> = fold(emptyList()) { acc, current ->
    val prev = acc.lastOrNull()
    if (prev != null && isEmpty(prev) && isEmpty(current)) {
        acc
    } else {
        acc + current
    }
}

private fun isEmpty(block: Block): Boolean = when (block) {
    is Block.Title1 -> block.content
    is Block.Title2 -> block.content
    is Block.Paragraph -> block.content
}.all { isEmpty(it) }

private fun isEmpty(content: LetterMarkup.ParagraphContent) =
    content is LetterMarkup.ParagraphContent.Text.NewLine || (content is LetterMarkup.ParagraphContent.Text && content.text.isBlank())

private fun clean(block: Block): Block? = when (block) {
    is Block.Title1 -> clean(block)
    is Block.Title2 -> clean(block)
    is Block.Paragraph -> clean(block)
}

@OptIn(InterneDataklasser::class)
private fun clean(title1: Block.Title1): Block.Title1? =
    LetterMarkupImpl.BlockImpl.Title1Impl(
        id = title1.id,
        editable = title1.editable,
        content = title1.content.filter { it !is LetterMarkup.ParagraphContent.Text.NewLine },
    ).takeIf { !isEmpty(it) }

@OptIn(InterneDataklasser::class)
private fun clean(title2: Block.Title2): Block.Title2? =
    LetterMarkupImpl.BlockImpl.Title2Impl(
        id = title2.id,
        editable = title2.editable,
        content = title2.content.filter { it !is LetterMarkup.ParagraphContent.Text.NewLine },
    ).takeIf { !isEmpty(it) }

@OptIn(InterneDataklasser::class)
private fun clean(paragraph: Block.Paragraph): Block.Paragraph =
    LetterMarkupImpl.BlockImpl.ParagraphImpl(
        id = paragraph.id,
        editable = paragraph.editable,
        content = paragraph.content.removeInvalidNewLines(),
    )

private fun List<LetterMarkup.ParagraphContent>.removeInvalidNewLines(): List<LetterMarkup.ParagraphContent> =
    fold(emptyList()) { acc, content ->
        if (content !is LetterMarkup.ParagraphContent.Text.NewLine || acc.endsWithLinebreakableContent()) {
            acc + content
        } else {
            acc
        }
    }

private fun List<LetterMarkup.ParagraphContent>.endsWithLinebreakableContent(): Boolean =
    if (isEmpty()) {
        false
    } else {
        val lastBreakableContent = lastOrNull {
            (it is LetterMarkup.ParagraphContent.Text.NewLine)
                    || (it is LetterMarkup.ParagraphContent.Text && it.text.isNotBlank())
                    || (it is LetterMarkup.ParagraphContent.ItemList)
                    || (it is LetterMarkup.ParagraphContent.Table)
        }
        lastBreakableContent != null && lastBreakableContent !is LetterMarkup.ParagraphContent.Text.NewLine
    }