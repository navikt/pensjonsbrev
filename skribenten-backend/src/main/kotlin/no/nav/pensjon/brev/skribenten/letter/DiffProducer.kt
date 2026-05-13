package no.nav.pensjon.brev.skribenten.letter

import no.nav.brev.Listetype
import no.nav.pensjon.brev.skribenten.common.diff.Change

interface DiffProducer<R> {
    data class BlockInfo(val id: Int?, val type: Edit.Block.Type)
    data class ItemListInfo(val id: Int?, val listType: Listetype)
    data class ItemInfo(val id: Int?)
    data class TableInfo(val id: Int?)
    data class RowInfo(val id: Int?)
    data class CellInfo(val id: Int?)
    data class TextSegment(val index: ContentIndex, val startOffset: Int, val endOffset: Int)

    fun block(blockIndex: Int, change: Change<BlockInfo>) {}
    // TODO: These contentIndex parameters don't make sense as it is because we don't know if they are relative to the old or new version of the letter.
    fun itemList(blockIndex: Int, contentIndex: Int, change: Change<ItemListInfo>) {}
    fun item(blockIndex: Int, contentIndex: Int, itemIndex: Int, change: Change<ItemInfo>) {}
    fun table(blockIndex: Int, contentIndex: Int, change: Change<TableInfo>) {}
    fun row(blockIndex: Int, contentIndex: Int, rowIndex: Int, change: Change<RowInfo>) {}
    fun cell(blockIndex: Int, contentIndex: Int, rowIndex: Int, cellIndex: Int, change: Change<CellInfo>) {}
    fun textSegment(change: Change<TextSegment>) {}

    fun build(): R
}
