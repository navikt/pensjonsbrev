package no.nav.pensjon.brev.skribenten.letter.v2

import no.nav.pensjon.brev.skribenten.common.diff.Change
import no.nav.pensjon.brev.skribenten.letter.v2.ContentIndexV2.BlockIndex
import no.nav.pensjon.brev.skribenten.letter.v2.ContentIndexV2.ColumnSpecIndex
import no.nav.pensjon.brev.skribenten.letter.v2.ContentIndexV2.ItemIndex
import no.nav.pensjon.brev.skribenten.letter.v2.ContentIndexV2.TableCellIndex
import no.nav.pensjon.brev.skribenten.letter.v2.ContentIndexV2.TableRowIndex

interface DiffProducerV2<R> {
    data class BlockInfo(val id: Int?, val type: EditV2.Block.Type)
    data class ItemInfo(val id: Int?)
    data class ColumnSpecInfo(val id: Int?, val alignment: EditV2.Block.Table.ColumnAlignment, val span: Int)
    data class RowInfo(val id: Int?)
    data class CellInfo(val id: Int?)
    data class TextContentInfo(val id: Int?)
    data class TextSegment(val startOffset: Int, val endOffset: Int, val text: String)

    fun block(insertIndex: BlockIndex, deleteIndex: BlockIndex, change: Change<BlockInfo>) {}
    fun item(insertIndex: ItemIndex, deleteIndex: ItemIndex, change: Change<ItemInfo>) {}
    fun columnSpec(insertIndex: ColumnSpecIndex, deleteIndex: ColumnSpecIndex, change: Change<ColumnSpecInfo>) {}
    fun row(insertIndex: TableRowIndex, deleteIndex: TableRowIndex, change: Change<RowInfo>) {}
    fun cell(insertIndex: TableCellIndex, deleteIndex: TableCellIndex, change: Change<CellInfo>) {}
    fun textContent(insertIndex: ContentIndexV2, deleteIndex: ContentIndexV2, change: Change<TextContentInfo>) {}
    fun textSegment(insertIndex: ContentIndexV2, deleteIndex: ContentIndexV2, change: Change<TextSegment>) {}

    fun build(): R
}
