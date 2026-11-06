package no.nav.pensjon.brev.skribenten.letter.v2

import no.nav.pensjon.brev.skribenten.common.diff.Change
import no.nav.pensjon.brev.skribenten.common.diff.shortestEditScript
import no.nav.pensjon.brev.skribenten.letter.v2.EditLetterWordTokenizerV2.Token
import no.nav.pensjon.brev.skribenten.letter.v2.UnifiedDiffV2.BlockEdit
import no.nav.pensjon.brev.skribenten.letter.v2.UnifiedDiffV2.DeletedTextSegment
import no.nav.pensjon.brev.skribenten.letter.v2.UnifiedDiffV2.RowEdit
import no.nav.pensjon.brev.skribenten.letter.v2.UnifiedDiffV2.TextEdit
import no.nav.pensjon.brev.skribenten.letter.v2.UnifiedDiffV2.TextOnlyEdit
import no.nav.pensjon.brev.skribenten.letter.v2.UnifiedDiffV2.TextSegment

class EditLetterWordDiffV2 {

    private val tokenizer = EditLetterWordTokenizerV2()

    fun tokenize(letter: EditV2.Letter): List<Token> = tokenizer.tokenize(letter)

    fun diff(old: EditV2.Letter, new: EditV2.Letter): SplitDiffV2 =
        tokenizer.parseTokens(shortestEditScript(tokenizer.tokenize(old), tokenizer.tokenize(new)), SplitDiffProducer())

    fun unifiedDiff(old: EditV2.Letter, new: EditV2.Letter): UnifiedDiffV2 =
        tokenizer.parseTokens(shortestEditScript(tokenizer.tokenize(old), tokenizer.tokenize(new)), UnifiedDiffProducer(old))

    private class SplitDiffProducer : DiffProducerV2<SplitDiffV2> {
        private val inserts = mutableListOf<DiffSegmentV2>()
        private val deletes = mutableListOf<DiffSegmentV2>()

        override fun textSegment(insertIndex: ContentIndexV2, deleteIndex: ContentIndexV2, change: Change<DiffProducerV2.TextSegment>) {
            when (change) {
                is Change.Delete -> deletes.add(DiffSegmentV2(deleteIndex, change.old.startOffset, change.old.endOffset))
                is Change.Insert -> inserts.add(DiffSegmentV2(insertIndex, change.new.startOffset, change.new.endOffset))
                is Change.Replace -> {
                    inserts.add(DiffSegmentV2(insertIndex, change.new.startOffset, change.new.endOffset))
                    deletes.add(DiffSegmentV2(deleteIndex, change.old.startOffset, change.old.endOffset))
                }
            }
        }

        override fun build() = SplitDiffV2(inserts.toList(), deletes.toList())
    }

    private class UnifiedDiffProducer(private val old: EditV2.Letter) : DiffProducerV2<UnifiedDiffV2> {
        private val editedBlocks = mutableMapOf<Int, MutableBlockEdit>()
        private val deletedBlocks = mutableMapOf<Int, MutableList<EditV2.Block>>()
        private val deletedPositions = mutableSetOf<ContentIndexV2>()
        private val oldBlocksReplacedByOtherKind = mutableSetOf<Int>()

        private fun isAncestorDeleted(deleteIndex: ContentIndexV2): Boolean =
            generateSequence(deleteIndex.parent(), ContentIndexV2::parent).any { it in deletedPositions }

        private fun isOldSideIgnored(deleteIndex: ContentIndexV2): Boolean =
            isAncestorDeleted(deleteIndex) || deleteIndex.blockIndex in oldBlocksReplacedByOtherKind

        private fun isNewSideIgnored(deleteIndex: ContentIndexV2): Boolean =
            isAncestorDeleted(deleteIndex) && deleteIndex.blockIndex !in oldBlocksReplacedByOtherKind

        private inline fun <reified E : MutableBlockEdit> blockEdit(blockIndex: Int, create: () -> E): E =
            editedBlocks.getOrPut(blockIndex, create) as E

        private fun textBlockEdit(blockIndex: Int) = blockEdit(blockIndex) { MutableBlockEdit.TextBlockEdit() }
        private fun listBlockEdit(blockIndex: Int) = blockEdit(blockIndex) { MutableBlockEdit.ListBlockEdit() }
        private fun tableEdit(blockIndex: Int) = blockEdit(blockIndex) { MutableBlockEdit.TableEdit() }

        private fun rowEdit(blockIndex: Int, rowIndex: Int): MutableRowEdit =
            tableEdit(blockIndex).rowEdits.getOrPut(rowIndex) { MutableRowEdit() }

        private fun textOnlyEditFor(index: ContentIndexV2): Pair<MutableTextOnlyEdit, Int> = when (index) {
            is ContentIndexV2.BlockContentIndex ->
                textBlockEdit(index.blockIndex).edit to index.contentIndex
            is ContentIndexV2.ItemContentIndex ->
                listBlockEdit(index.blockIndex).itemEdits.getOrPut(index.itemIndex) { MutableTextOnlyEdit() } to index.itemContentIndex
            is ContentIndexV2.ColumnSpecContentIndex ->
                tableEdit(index.blockIndex).columnSpecEdits.getOrPut(index.columnIndex) { MutableTextOnlyEdit() } to index.columnContentIndex
            is ContentIndexV2.TableCellContentIndex ->
                rowEdit(index.blockIndex, index.rowIndex).cellEdits.getOrPut(index.cellIndex) { MutableTextOnlyEdit() } to index.cellContentIndex
            else -> error("textOnlyEditFor called with unexpected index: $index")
        }

        override fun block(insertIndex: ContentIndexV2.BlockIndex, deleteIndex: ContentIndexV2.BlockIndex, change: Change<DiffProducerV2.BlockInfo>) {
            when (change) {
                is Change.Delete -> {
                    deletedPositions += deleteIndex
                    deletedBlocks.getOrPut(insertIndex.blockIndex) { mutableListOf() }.add(old.blocks[deleteIndex.blockIndex])
                }
                is Change.Replace -> if (change.old.type.kind() != change.new.type.kind()) {
                    oldBlocksReplacedByOtherKind += deleteIndex.blockIndex
                    deletedBlocks.getOrPut(insertIndex.blockIndex) { mutableListOf() }.add(old.blocks[deleteIndex.blockIndex])
                }
                is Change.Insert -> Unit
            }
        }

        override fun item(insertIndex: ContentIndexV2.ItemIndex, deleteIndex: ContentIndexV2.ItemIndex, change: Change<DiffProducerV2.ItemInfo>) {
            if (isOldSideIgnored(deleteIndex)) return
            if (change is Change.Delete) {
                deletedPositions += deleteIndex
                val oldList = old.blocks[deleteIndex.blockIndex] as EditV2.Block.ListBlock
                listBlockEdit(insertIndex.blockIndex).deletedItems
                    .getOrPut(insertIndex.itemIndex) { mutableListOf() }
                    .add(oldList.items[deleteIndex.itemIndex])
            }
        }

        override fun columnSpec(insertIndex: ContentIndexV2.ColumnSpecIndex, deleteIndex: ContentIndexV2.ColumnSpecIndex, change: Change<DiffProducerV2.ColumnSpecInfo>) {
            if (isOldSideIgnored(deleteIndex)) return
            if (change is Change.Delete) {
                deletedPositions += deleteIndex
                val oldTable = old.blocks[deleteIndex.blockIndex] as EditV2.Block.Table
                tableEdit(insertIndex.blockIndex).deletedColumnSpecs
                    .getOrPut(insertIndex.columnIndex) { mutableListOf() }
                    .add(oldTable.header.colSpec[deleteIndex.columnIndex])
            }
        }

        override fun row(insertIndex: ContentIndexV2.TableRowIndex, deleteIndex: ContentIndexV2.TableRowIndex, change: Change<DiffProducerV2.RowInfo>) {
            if (isOldSideIgnored(deleteIndex)) return
            if (change is Change.Delete) {
                deletedPositions += deleteIndex
                val oldTable = old.blocks[deleteIndex.blockIndex] as EditV2.Block.Table
                tableEdit(insertIndex.blockIndex).deletedRows
                    .getOrPut(insertIndex.rowIndex) { mutableListOf() }
                    .add(oldTable.rows[deleteIndex.rowIndex])
            }
        }

        override fun cell(insertIndex: ContentIndexV2.TableCellIndex, deleteIndex: ContentIndexV2.TableCellIndex, change: Change<DiffProducerV2.CellInfo>) {
            if (isOldSideIgnored(deleteIndex)) return
            if (change is Change.Delete) {
                deletedPositions += deleteIndex
                val oldTable = old.blocks[deleteIndex.blockIndex] as EditV2.Block.Table
                rowEdit(insertIndex.blockIndex, insertIndex.rowIndex).deletedCells
                    .getOrPut(insertIndex.cellIndex) { mutableListOf() }
                    .add(oldTable.rows[deleteIndex.rowIndex].cells[deleteIndex.cellIndex])
            }
        }

        override fun textContent(insertIndex: ContentIndexV2, deleteIndex: ContentIndexV2, change: Change<DiffProducerV2.TextContentInfo>) {
            if (change !is Change.Delete) return
            if (isOldSideIgnored(deleteIndex)) return

            val (edit, contentIndex) = textOnlyEditFor(insertIndex)
            edit.deletedContent.getOrPut(contentIndex) { mutableListOf() }.add(textAt(deleteIndex))
        }

        override fun textSegment(insertIndex: ContentIndexV2, deleteIndex: ContentIndexV2, change: Change<DiffProducerV2.TextSegment>) {
            val insert = when (change) {
                is Change.Insert -> change.new
                is Change.Replace -> change.new
                is Change.Delete -> null
            }?.takeUnless { isNewSideIgnored(deleteIndex) }
            val delete = when (change) {
                is Change.Delete -> change.old
                is Change.Replace -> change.old
                is Change.Insert -> null
            }?.takeUnless { isOldSideIgnored(deleteIndex) }
            if (insert == null && delete == null) return

            val (edit, contentIndex) = textOnlyEditFor(insertIndex)
            val textEdit = edit.textEdits.getOrPut(contentIndex) { MutableTextEdit() }
            insert?.let { textEdit.inserts.add(TextSegment(it.startOffset, it.endOffset)) }
            delete?.let { textEdit.deletes.add(DeletedTextSegment(it.startOffset, it.endOffset, it.text)) }
        }

        private fun textAt(index: ContentIndexV2): EditV2.Text = when (index) {
            is ContentIndexV2.BlockContentIndex ->
                (old.blocks[index.blockIndex] as EditV2.Block.TextBlock).content[index.contentIndex]
            is ContentIndexV2.ItemContentIndex ->
                (old.blocks[index.blockIndex] as EditV2.Block.ListBlock).items[index.itemIndex].content[index.itemContentIndex]
            is ContentIndexV2.ColumnSpecContentIndex ->
                (old.blocks[index.blockIndex] as EditV2.Block.Table).header.colSpec[index.columnIndex].content[index.columnContentIndex]
            is ContentIndexV2.TableCellContentIndex ->
                (old.blocks[index.blockIndex] as EditV2.Block.Table).rows[index.rowIndex].cells[index.cellIndex].content[index.cellContentIndex]
            else -> error("textAt called with unexpected index: $index")
        }

        override fun build(): UnifiedDiffV2 = UnifiedDiffV2(
            editedBlocks = editedBlocks.mapValues { it.value.build() },
            deletedBlocks = deletedBlocks.mapValues { it.value.toList() },
        )

        private enum class BlockKind { TEXT, LIST, TABLE }

        private fun EditV2.Block.Type.kind(): BlockKind = when (this) {
            EditV2.Block.Type.TITLE2, EditV2.Block.Type.TITLE3, EditV2.Block.Type.TITLE4, EditV2.Block.Type.PARAGRAPH -> BlockKind.TEXT
            EditV2.Block.Type.ITEM_LIST, EditV2.Block.Type.NUMBERED_LIST -> BlockKind.LIST
            EditV2.Block.Type.TABLE -> BlockKind.TABLE
        }

        private sealed class MutableBlockEdit {
            abstract fun build(): BlockEdit

            class TextBlockEdit : MutableBlockEdit() {
                val edit = MutableTextOnlyEdit()

                override fun build() = edit.build().let { BlockEdit.TextBlockEdit(textEdits = it.textEdits, deletedContent = it.deletedContent) }
            }

            class ListBlockEdit : MutableBlockEdit() {
                val itemEdits = mutableMapOf<Int, MutableTextOnlyEdit>()
                val deletedItems = mutableMapOf<Int, MutableList<EditV2.Item>>()

                override fun build() = BlockEdit.ListBlockEdit(
                    itemEdits = itemEdits.mapValues { it.value.build() },
                    deletedItems = deletedItems.mapValues { it.value.toList() },
                )
            }

            class TableEdit : MutableBlockEdit() {
                val columnSpecEdits = mutableMapOf<Int, MutableTextOnlyEdit>()
                val deletedColumnSpecs = mutableMapOf<Int, MutableList<EditV2.Block.Table.ColumnSpec>>()
                val rowEdits = mutableMapOf<Int, MutableRowEdit>()
                val deletedRows = mutableMapOf<Int, MutableList<EditV2.Block.Table.Row>>()

                override fun build() = BlockEdit.TableEdit(
                    columnSpecEdits = columnSpecEdits.mapValues { it.value.build() },
                    deletedColumnSpecs = deletedColumnSpecs.mapValues { it.value.toList() },
                    rowEdits = rowEdits.mapValues { it.value.build() },
                    deletedRows = deletedRows.mapValues { it.value.toList() },
                )
            }
        }

        private class MutableRowEdit {
            val cellEdits = mutableMapOf<Int, MutableTextOnlyEdit>()
            val deletedCells = mutableMapOf<Int, MutableList<EditV2.Block.Table.Cell>>()

            fun build() = RowEdit(
                cellEdits = cellEdits.mapValues { it.value.build() },
                deletedCells = deletedCells.mapValues { it.value.toList() },
            )
        }

        private class MutableTextOnlyEdit {
            val textEdits = mutableMapOf<Int, MutableTextEdit>()
            val deletedContent = mutableMapOf<Int, MutableList<EditV2.Text>>()

            fun build() = TextOnlyEdit(
                textEdits = textEdits.mapValues { it.value.build() },
                deletedContent = deletedContent.mapValues { it.value.toList() },
            )
        }

        private class MutableTextEdit {
            val inserts = mutableListOf<TextSegment>()
            val deletes = mutableListOf<DeletedTextSegment>()

            fun build() = TextEdit(inserts.toList(), deletes.toList())
        }
    }
}
