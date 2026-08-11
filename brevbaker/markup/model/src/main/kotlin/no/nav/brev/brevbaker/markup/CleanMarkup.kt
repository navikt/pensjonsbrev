package no.nav.brev.brevbaker.markup

import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Text

/**
 * Rydder opp i markup-en før den rendres: fjerner tomme blokker og ugyldige linjeskift.
 */
fun LetterMarkup.clean(): LetterMarkup =
    copy(blocks = blocks.cleanBlocks())

fun List<Attachment>.clean(): List<Attachment> =
    map { it.clean() }

fun Attachment.clean(): Attachment =
    copy(blocks = blocks.cleanBlocks())

private fun List<Block>.cleanBlocks(): List<Block> =
    mapNotNull { clean(it) }.removeEmptyBlocks()

private fun List<Block>.removeEmptyBlocks(): List<Block> =
    filter { !isEmpty(it) }

private fun isEmpty(block: Block): Boolean =
    when (block) {
        is Block.Title2 -> block.content.all { isEmpty(it) }
        is Block.Title3 -> block.content.all { isEmpty(it) }
        is Block.Title4 -> block.content.all { isEmpty(it) }
        is Block.Paragraph -> block.content.all { isEmpty(it) }
        is Block.ItemList -> block.items.isEmpty()
        is Block.NumberedList -> block.items.isEmpty()
        is Block.Table -> block.rows.isEmpty() && block.header.colSpec.isEmpty()
        // FormText and FormChoice are never removed as empty.
        is Block.FormText -> false
        is Block.FormChoice -> false
    }

private fun isEmpty(text: Text): Boolean =
    when (text) {
        is Text.NewLine -> true
        is Text.Literal, is Text.Variable -> text.text.isBlank()
    }

private fun clean(block: Block): Block? =
    when (block) {
        is Block.Title2 -> block.copy(content = block.content.filter { it !is Text.NewLine }).takeIf { !isEmpty(it) }
        is Block.Title3 -> block.copy(content = block.content.filter { it !is Text.NewLine }).takeIf { !isEmpty(it) }
        is Block.Title4 -> block.copy(content = block.content.filter { it !is Text.NewLine }).takeIf { !isEmpty(it) }
        is Block.Paragraph -> block.copy(content = block.content.removeInvalidNewLines())
        is Block.ItemList -> block.takeIf { !isEmpty(it) }
        is Block.NumberedList -> block.takeIf { !isEmpty(it) }
        is Block.Table -> block.takeIf { !isEmpty(it) }
        is Block.FormText -> block
        is Block.FormChoice -> block
    }

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
