package no.nav.pensjon.brev.pdfbygger.latex

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Block
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl

fun LetterMarkup.clean(): LetterMarkup =
    cleanMarkup(this)

@OptIn(InterneDataklasser::class)
private fun cleanMarkup(markup: LetterMarkup): LetterMarkup =
    LetterMarkupImpl(
        title = markup.title,
        sakspart = markup.sakspart,
        blocks = markup.blocks.mapNotNull { clean(it) }.removeEmptyBlocks(),
        signatur = markup.signatur
    )

private fun List<Block>.removeEmptyBlocks(): List<Block> =
    filter { !isEmpty(it) }

private fun isEmpty(block: Block): Boolean =
    when (block) {
        is Block.Title1 -> block.content
        is Block.Title2 -> block.content
        is Block.Title3 -> block.content
        is Block.Paragraph -> block.content
    }.all { isEmpty(it) }

private fun isEmpty(content: LetterMarkup.ParagraphContent) =
    when (content) {
        is LetterMarkup.ParagraphContent.Form,
        is LetterMarkup.ParagraphContent.ItemList,
        is LetterMarkup.ParagraphContent.Table -> false
        is LetterMarkup.ParagraphContent.Text.NewLine -> true
        is LetterMarkup.ParagraphContent.Text.Literal,
        is LetterMarkup.ParagraphContent.Text.Variable -> content.text.isBlank()
    }

private fun clean(block: Block): Block? = when (block) {
    is Block.Title1 -> clean(block)
    is Block.Title2 -> clean(block)
    is Block.Title3 -> clean(block)
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
private fun clean(title3: Block.Title3): Block.Title3? =
    LetterMarkupImpl.BlockImpl.Title3Impl(
        id = title3.id,
        editable = title3.editable,
        content = title3.content.filter { it !is LetterMarkup.ParagraphContent.Text.NewLine },
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
        // Keep all content except newlines that are at the start or after non-breakable content
        if (content !is LetterMarkup.ParagraphContent.Text.NewLine || acc.endsWithLinebreakableContent()) {
            acc + content
        } else {
            acc
        }
    }

private fun List<LetterMarkup.ParagraphContent>.endsWithLinebreakableContent(): Boolean =
    dropLastWhile {
        // Ignore trailing literal/variable texts that are blank
        it is LetterMarkup.ParagraphContent.Text && it !is LetterMarkup.ParagraphContent.Text.NewLine && it.text.isBlank()
    }.lastOrNull().let {
        it is LetterMarkup.ParagraphContent.Text && it.text.isNotBlank()
    }
