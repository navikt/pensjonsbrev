package no.nav.pensjon.brev.skribenten.letter

import no.nav.brev.Listetype
import no.nav.pensjon.brev.skribenten.common.diff.Change
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.BlockContentIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.BlockIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.ItemIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.TableCellIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.TableRowIndex

interface DiffProducer<R> {
    data class BlockInfo(val id: Int?, val type: Edit.Block.Type)
    data class ItemListInfo(val id: Int?, val listType: Listetype)
    data class ItemInfo(val id: Int?)
    data class TableInfo(val id: Int?)
    data class RowInfo(val id: Int?)
    data class CellInfo(val id: Int?)
    data class TextSegment(val startOffset: Int, val endOffset: Int, val text: String)

    fun block(insertIndex: BlockIndex, deleteIndex: BlockIndex, change: Change<BlockInfo>) {}
    fun itemList(insertIndex: BlockContentIndex, deleteIndex: BlockContentIndex, change: Change<ItemListInfo>) {}
    fun item(insertIndex: ItemIndex, deleteIndex: ItemIndex, change: Change<ItemInfo>) {}
    fun table(insertIndex: BlockContentIndex, deleteIndex: BlockContentIndex, change: Change<TableInfo>) {}
    fun row(insertIndex: TableRowIndex, deleteIndex: TableRowIndex, change: Change<RowInfo>) {}
    fun cell(insertIndex: TableCellIndex, deleteIndex: TableCellIndex, change: Change<CellInfo>) {}
    fun textSegment(insertIndex: ContentIndex, deleteIndex: ContentIndex, change: Change<TextSegment>) {}

    fun build(): R
}
