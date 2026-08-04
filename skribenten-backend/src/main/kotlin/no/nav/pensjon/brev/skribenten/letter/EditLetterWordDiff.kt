package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.common.diff.Change
import no.nav.pensjon.brev.skribenten.common.diff.shortestEditScript
import no.nav.pensjon.brev.skribenten.letter.EditLetterWordTokenizer.Token
import no.nav.pensjon.brev.skribenten.letter.UnifiedDiff.BlockEdit
import no.nav.pensjon.brev.skribenten.letter.UnifiedDiff.ContentEdit
import no.nav.pensjon.brev.skribenten.letter.UnifiedDiff.DeletedTextSegment
import no.nav.pensjon.brev.skribenten.letter.UnifiedDiff.RowEdit
import no.nav.pensjon.brev.skribenten.letter.UnifiedDiff.TextEdit
import no.nav.pensjon.brev.skribenten.letter.UnifiedDiff.TextOnlyEdit
import no.nav.pensjon.brev.skribenten.letter.UnifiedDiff.TextSegment

class EditLetterWordDiff {

    private val tokenizer = EditLetterWordTokenizer()

    fun tokenize(letter: Edit.Letter): List<Token> = tokenizer.tokenize(letter)

    fun diff(old: Edit.Letter, new: Edit.Letter): SplitDiff =
        tokenizer.parseTokens(shortestEditScript(tokenizer.tokenize(old), tokenizer.tokenize(new)), SplitDiffProducer())

    fun unifiedDiff(old: Edit.Letter, new: Edit.Letter): UnifiedDiff =
        tokenizer.parseTokens(shortestEditScript(tokenizer.tokenize(old), tokenizer.tokenize(new)), UnifiedDiffProducer(old))

    private class SplitDiffProducer : DiffProducer<SplitDiff> {
        private val inserts = mutableListOf<DiffSegment>()
        private val deletes = mutableListOf<DiffSegment>()

        override fun textSegment(insertIndex: ContentIndex, deleteIndex: ContentIndex, change: Change<DiffProducer.TextSegment>) {
            when (change) {
                is Change.Delete -> deletes.add(DiffSegment(deleteIndex, change.old.startOffset, change.old.endOffset))
                is Change.Insert -> inserts.add(DiffSegment(insertIndex, change.new.startOffset, change.new.endOffset))
                is Change.Replace -> {
                    inserts.add(DiffSegment(insertIndex, change.new.startOffset, change.new.endOffset))
                    deletes.add(DiffSegment(deleteIndex, change.old.startOffset, change.old.endOffset))
                }
            }
        }

        override fun build() = SplitDiff(inserts.toList(), deletes.toList())
    }

    /**
     * Builds a [UnifiedDiff]. All positions used to key the resulting maps use insertIndex (not deleteIndex),
     * since that reflects the position in the merged (unified/`new`) view, unlike deleteIndex which is the
     * position in `old` and drifts whenever entirely deleted content precedes it.
     *
     * Entirely deleted content is looked up in [old] via deleteIndex (which correctly indexes into `old` for
     * pure deletes) and embedded directly, so the frontend doesn't need `old` sent alongside the diff. Once a
     * container (block/itemList/item/table/row) is known to be entirely deleted, further nested callbacks for
     * its content are ignored, since that content is already embedded recursively in the container itself.
     */
    private class UnifiedDiffProducer(private val old: Edit.Letter) : DiffProducer<UnifiedDiff> {
        private val editedBlocks = mutableMapOf<Int, MutableBlockEdit>()
        private val deletedBlocks = mutableMapOf<Int, MutableList<Edit.Block>>()

        // Keyed by deleteIndex (not insertIndex): deleteIndex uniquely identifies each node in `old`, since the
        // delete-side cursor advances exactly once per old-side node regardless of whether it's a delete or not.
        // insertIndex can't be used here, since it does not advance for pure deletes - so a wholly deleted
        // container and the very next surviving sibling can end up sharing the same insertIndex (they're both
        // "at" the same unified position), which would make isAncestorDeleted wrongly conflate them.
        private val deletedPositions = mutableSetOf<ContentIndex>()

        private fun isAncestorDeleted(deleteIndex: ContentIndex): Boolean =
            generateSequence(deleteIndex.parent(), ContentIndex::parent).any { it in deletedPositions }

        private fun blockEdit(blockIndex: Int): MutableBlockEdit = editedBlocks.getOrPut(blockIndex) { MutableBlockEdit() }

        private fun itemListEdit(blockIndex: Int, contentIndex: Int): MutableContentEdit.ItemListEdit =
            blockEdit(blockIndex).contentEdits.getOrPut(contentIndex) { MutableContentEdit.ItemListEdit() } as MutableContentEdit.ItemListEdit

        private fun tableEdit(blockIndex: Int, contentIndex: Int): MutableContentEdit.TableEdit =
            blockEdit(blockIndex).contentEdits.getOrPut(contentIndex) { MutableContentEdit.TableEdit() } as MutableContentEdit.TableEdit

        private fun textContentEdit(blockIndex: Int, contentIndex: Int): MutableContentEdit.TextContentEdit =
            blockEdit(blockIndex).contentEdits.getOrPut(contentIndex) { MutableContentEdit.TextContentEdit() } as MutableContentEdit.TextContentEdit

        private fun rowEdit(blockIndex: Int, contentIndex: Int, rowIndex: Int): MutableRowEdit =
            tableEdit(blockIndex, contentIndex).rowEdits.getOrPut(rowIndex) { MutableRowEdit() }

        private fun textOnlyEditFor(index: ContentIndex): MutableTextOnlyEdit = when (index) {
            is ContentIndex.ItemContentIndex ->
                itemListEdit(index.blockIndex, index.contentIndex).itemEdits.getOrPut(index.itemIndex) { MutableTextOnlyEdit() }
            is ContentIndex.TableCellContentIndex ->
                rowEdit(index.blockIndex, index.contentIndex, index.rowIndex).cellEdits.getOrPut(index.cellIndex) { MutableTextOnlyEdit() }
            else -> error("textOnlyEditFor called with unexpected index: $index")
        }

        override fun block(insertIndex: ContentIndex.BlockIndex, deleteIndex: ContentIndex.BlockIndex, change: Change<DiffProducer.BlockInfo>) {
            if (change is Change.Delete) {
                deletedPositions += deleteIndex
                deletedBlocks.getOrPut(insertIndex.blockIndex) { mutableListOf() }.add(old.blocks[deleteIndex.blockIndex])
            }
        }

        override fun itemList(insertIndex: ContentIndex.BlockContentIndex, deleteIndex: ContentIndex.BlockContentIndex, change: Change<DiffProducer.ItemListInfo>) {
            if (isAncestorDeleted(deleteIndex)) return
            if (change is Change.Delete) {
                deletedPositions += deleteIndex
                blockEdit(insertIndex.blockIndex).deletedContent.getOrPut(insertIndex.contentIndex) { mutableListOf() }
                    .add(old.blocks[deleteIndex.blockIndex].content[deleteIndex.contentIndex])
            }
        }

        override fun table(insertIndex: ContentIndex.BlockContentIndex, deleteIndex: ContentIndex.BlockContentIndex, change: Change<DiffProducer.TableInfo>) {
            if (isAncestorDeleted(deleteIndex)) return
            if (change is Change.Delete) {
                deletedPositions += deleteIndex
                blockEdit(insertIndex.blockIndex).deletedContent.getOrPut(insertIndex.contentIndex) { mutableListOf() }
                    .add(old.blocks[deleteIndex.blockIndex].content[deleteIndex.contentIndex])
            }
        }

        override fun item(insertIndex: ContentIndex.ItemIndex, deleteIndex: ContentIndex.ItemIndex, change: Change<DiffProducer.ItemInfo>) {
            if (isAncestorDeleted(deleteIndex)) return
            if (change is Change.Delete) {
                deletedPositions += deleteIndex
                val oldItemList = old.blocks[deleteIndex.blockIndex].content[deleteIndex.contentIndex] as Edit.ParagraphContent.ItemList
                itemListEdit(insertIndex.blockIndex, insertIndex.contentIndex).deletedItems
                    .getOrPut(insertIndex.itemIndex) { mutableListOf() }
                    .add(oldItemList.items[deleteIndex.itemIndex])
            }
        }

        override fun row(insertIndex: ContentIndex.TableRowIndex, deleteIndex: ContentIndex.TableRowIndex, change: Change<DiffProducer.RowInfo>) {
            if (isAncestorDeleted(deleteIndex)) return
            if (change is Change.Delete) {
                deletedPositions += deleteIndex
                val oldTable = old.blocks[deleteIndex.blockIndex].content[deleteIndex.contentIndex] as Edit.ParagraphContent.Table
                tableEdit(insertIndex.blockIndex, insertIndex.contentIndex).deletedRows
                    .getOrPut(insertIndex.rowIndex) { mutableListOf() }
                    .add(oldTable.rows[deleteIndex.rowIndex])
            }
        }

        override fun cell(insertIndex: ContentIndex.TableCellIndex, deleteIndex: ContentIndex.TableCellIndex, change: Change<DiffProducer.CellInfo>) {
            if (isAncestorDeleted(deleteIndex)) return
            if (change is Change.Delete) {
                deletedPositions += deleteIndex
                val oldTable = old.blocks[deleteIndex.blockIndex].content[deleteIndex.contentIndex] as Edit.ParagraphContent.Table
                val oldCell = if (deleteIndex.rowIndex == -1)
                    oldTable.header.colSpec[deleteIndex.cellIndex].headerContent
                else
                    oldTable.rows[deleteIndex.rowIndex].cells[deleteIndex.cellIndex]
                rowEdit(insertIndex.blockIndex, insertIndex.contentIndex, insertIndex.rowIndex).deletedCells
                    .getOrPut(insertIndex.cellIndex) { mutableListOf() }
                    .add(oldCell)
            }
        }

        override fun textContent(insertIndex: ContentIndex, deleteIndex: ContentIndex, change: Change<DiffProducer.TextContentInfo>) {
            if (change !is Change.Delete) return
            if (isAncestorDeleted(deleteIndex)) return

            val deletedText = textAt(deleteIndex)
            when (insertIndex) {
                is ContentIndex.BlockContentIndex ->
                    blockEdit(insertIndex.blockIndex).deletedContent.getOrPut(insertIndex.contentIndex) { mutableListOf() }.add(deletedText)
                is ContentIndex.ItemContentIndex ->
                    textOnlyEditFor(insertIndex).deletedContent.getOrPut(insertIndex.itemContentIndex) { mutableListOf() }.add(deletedText)
                is ContentIndex.TableCellContentIndex ->
                    textOnlyEditFor(insertIndex).deletedContent.getOrPut(insertIndex.cellContentIndex) { mutableListOf() }.add(deletedText)
                else -> error("textContent fired with unexpected index: $insertIndex")
            }
        }

        override fun textSegment(insertIndex: ContentIndex, deleteIndex: ContentIndex, change: Change<DiffProducer.TextSegment>) {
            if (isAncestorDeleted(deleteIndex)) return

            val textEdit = when (insertIndex) {
                is ContentIndex.BlockContentIndex -> textContentEdit(insertIndex.blockIndex, insertIndex.contentIndex).edit
                is ContentIndex.ItemContentIndex -> textOnlyEditFor(insertIndex).textEdits.getOrPut(insertIndex.itemContentIndex) { MutableTextEdit() }
                is ContentIndex.TableCellContentIndex -> textOnlyEditFor(insertIndex).textEdits.getOrPut(insertIndex.cellContentIndex) { MutableTextEdit() }
                else -> error("textSegment fired with unexpected index: $insertIndex")
            }

            when (change) {
                is Change.Insert -> textEdit.inserts.add(TextSegment(change.new.startOffset, change.new.endOffset))
                is Change.Delete -> textEdit.deletes.add(DeletedTextSegment(change.old.startOffset, change.old.endOffset, change.old.text))
                is Change.Replace -> {
                    textEdit.inserts.add(TextSegment(change.new.startOffset, change.new.endOffset))
                    textEdit.deletes.add(DeletedTextSegment(change.old.startOffset, change.old.endOffset, change.old.text))
                }
            }
        }

        private fun textAt(index: ContentIndex): Edit.ParagraphContent.Text = when (index) {
            is ContentIndex.BlockContentIndex ->
                old.blocks[index.blockIndex].content[index.contentIndex] as Edit.ParagraphContent.Text
            is ContentIndex.ItemContentIndex -> {
                val itemList = old.blocks[index.blockIndex].content[index.contentIndex] as Edit.ParagraphContent.ItemList
                itemList.items[index.itemIndex].content[index.itemContentIndex]
            }
            is ContentIndex.TableCellContentIndex -> {
                val table = old.blocks[index.blockIndex].content[index.contentIndex] as Edit.ParagraphContent.Table
                val cell = if (index.rowIndex == -1) table.header.colSpec[index.cellIndex].headerContent else table.rows[index.rowIndex].cells[index.cellIndex]
                cell.text[index.cellContentIndex]
            }
            else -> error("textAt called with unexpected index: $index")
        }

        override fun build(): UnifiedDiff = UnifiedDiff(
            editedBlocks = editedBlocks.mapValues { it.value.build() },
            deletedBlocks = deletedBlocks.mapValues { it.value.toList() },
        )

        private class MutableBlockEdit {
            val contentEdits = mutableMapOf<Int, MutableContentEdit>()
            val deletedContent = mutableMapOf<Int, MutableList<Edit.ParagraphContent>>()

            fun build() = BlockEdit(
                contentEdits = contentEdits.mapValues { it.value.build() },
                deletedContent = deletedContent.mapValues { it.value.toList() },
            )
        }

        private sealed class MutableContentEdit {
            abstract fun build(): ContentEdit

            class TextContentEdit : MutableContentEdit() {
                val edit = MutableTextEdit()
                override fun build() = ContentEdit.TextContentEdit(edit.build())
            }

            class ItemListEdit : MutableContentEdit() {
                val itemEdits = mutableMapOf<Int, MutableTextOnlyEdit>()
                val deletedItems = mutableMapOf<Int, MutableList<Edit.ParagraphContent.ItemList.Item>>()

                override fun build() = ContentEdit.ItemListEdit(
                    itemEdits = itemEdits.mapValues { it.value.build() },
                    deletedItems = deletedItems.mapValues { it.value.toList() },
                )
            }

            class TableEdit : MutableContentEdit() {
                val rowEdits = mutableMapOf<Int, MutableRowEdit>()
                val deletedRows = mutableMapOf<Int, MutableList<Edit.ParagraphContent.Table.Row>>()

                override fun build() = ContentEdit.TableEdit(
                    rowEdits = rowEdits.mapValues { it.value.build() },
                    deletedRows = deletedRows.mapValues { it.value.toList() },
                )
            }
        }

        private class MutableRowEdit {
            val cellEdits = mutableMapOf<Int, MutableTextOnlyEdit>()
            val deletedCells = mutableMapOf<Int, MutableList<Edit.ParagraphContent.Table.Cell>>()

            fun build() = RowEdit(
                cellEdits = cellEdits.mapValues { it.value.build() },
                deletedCells = deletedCells.mapValues { it.value.toList() },
            )
        }

        private class MutableTextOnlyEdit {
            val textEdits = mutableMapOf<Int, MutableTextEdit>()
            val deletedContent = mutableMapOf<Int, MutableList<Edit.ParagraphContent.Text>>()

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
