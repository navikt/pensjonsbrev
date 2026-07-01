package no.nav.pensjon.brev.pdfbygger

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Block
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Text
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2Impl

fun LetterMarkupV2.clean(): LetterMarkupV2 =
    cleanMarkup(this)

fun List<LetterMarkupV2.Attachment>.clean(): List<LetterMarkupV2.Attachment> =
    map { it.clean() }

fun LetterMarkupV2.Attachment.clean(): LetterMarkupV2.Attachment =
    cleanMarkup(this)

@OptIn(InterneDataklasser::class)
private fun cleanMarkup(markup: LetterMarkupV2): LetterMarkupV2 =
    LetterMarkupV2Impl(
        title1 = markup.title1,
        saksinformasjon = markup.saksinformasjon,
        blocks = markup.blocks.mapNotNull { clean(it) }.removeEmptyBlocks(),
        signatur = markup.signatur,
    )

@OptIn(InterneDataklasser::class)
private fun cleanMarkup(attachment: LetterMarkupV2.Attachment): LetterMarkupV2.Attachment =
    LetterMarkupV2Impl.AttachmentImpl(
        title1 = attachment.title1,
        blocks = attachment.blocks.mapNotNull { clean(it) }.removeEmptyBlocks(),
        inkluderSaksinformasjon = attachment.inkluderSaksinformasjon,
    )

private fun List<Block>.removeEmptyBlocks(): List<Block> =
    filter { !isEmpty(it) }

private fun isEmpty(block: Block): Boolean =
    when (block) {
        is Block.Title -> block.content.all { isEmpty(it) }
        is Block.Paragraph -> block.content.all { isEmpty(it) }
        is Block.ListContent -> block.items.isEmpty()
        is Block.Table -> block.rows.isEmpty() && block.header.colSpec.isEmpty()
        // FormText and FormChoice are never removed as empty, mirroring v1's Form handling in CleanseMarkup.kt.
        is Block.FormText -> false
        is Block.FormChoice -> false
    }

private fun isEmpty(text: Text): Boolean =
    when (text) {
        is Text.NewLine -> true
        is Text.Literal, is Text.Variable -> text.text.isBlank()
    }

private fun clean(block: Block): Block? = when (block) {
    is Block.Title.Title2 -> clean(block)
    is Block.Title.Title3 -> clean(block)
    is Block.Title.Title4 -> clean(block)
    is Block.Paragraph -> clean(block)
    is Block.ListContent.ItemList -> block.takeIf { !isEmpty(it) }
    is Block.ListContent.NumberedList -> block.takeIf { !isEmpty(it) }
    is Block.Table -> block.takeIf { !isEmpty(it) }
    is Block.FormText -> block
    is Block.FormChoice -> block
}

@OptIn(InterneDataklasser::class)
private fun clean(title: Block.Title.Title2): Block.Title.Title2? =
    LetterMarkupV2Impl.BlockImpl.Title2Impl(
        id = title.id,
        content = title.content.filter { it !is Text.NewLine },
    ).takeIf { !isEmpty(it) }

@OptIn(InterneDataklasser::class)
private fun clean(title: Block.Title.Title3): Block.Title.Title3? =
    LetterMarkupV2Impl.BlockImpl.Title3Impl(
        id = title.id,
        content = title.content.filter { it !is Text.NewLine },
    ).takeIf { !isEmpty(it) }

@OptIn(InterneDataklasser::class)
private fun clean(title: Block.Title.Title4): Block.Title.Title4? =
    LetterMarkupV2Impl.BlockImpl.Title4Impl(
        id = title.id,
        content = title.content.filter { it !is Text.NewLine },
    ).takeIf { !isEmpty(it) }

@OptIn(InterneDataklasser::class)
private fun clean(paragraph: Block.Paragraph): Block.Paragraph =
    LetterMarkupV2Impl.BlockImpl.ParagraphImpl(
        id = paragraph.id,
        content = paragraph.content.removeInvalidNewLines(),
    )

private fun List<Text>.removeInvalidNewLines(): List<Text> =
    fold(emptyList()) { acc, content ->
        // Keep all content except newlines that are at the start or after non-breakable content
        if (content !is Text.NewLine || acc.endsWithLinebreakableContent()) {
            acc + content
        } else {
            acc
        }
    }

private fun List<Text>.endsWithLinebreakableContent(): Boolean =
    dropLastWhile {
        // Ignore trailing literal/variable texts that are blank
        it !is Text.NewLine && it.text.isBlank()
    }.lastOrNull().let {
        it != null && it !is Text.NewLine && it.text.isNotBlank()
    }
