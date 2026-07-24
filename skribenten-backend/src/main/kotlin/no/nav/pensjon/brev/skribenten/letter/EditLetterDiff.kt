package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.common.diff.EditScript

interface EditLetterTokenizer<Token : Any> {
    fun tokenize(letter: Edit.Letter): List<Token>
    fun <R> parseTokens(editScript: EditScript<Token>, producer: DiffProducer<R>): R
}

sealed class ContentIndex {
    data class BlockIndex(val blockIndex: Int) : ContentIndex() {
        fun withContentIndex(contentIndex: Int) = BlockContentIndex(blockIndex, contentIndex)
    }
    data class BlockContentIndex(val blockIndex: Int, val contentIndex: Int) : ContentIndex() {
        fun withItemIndex(itemIndex: Int) = ItemIndex(blockIndex, contentIndex, itemIndex)
        fun withRowIndex(rowIndex: Int) = TableRowIndex(blockIndex, contentIndex, rowIndex)
    }
    data class ItemIndex(val blockIndex: Int, val contentIndex: Int, val itemIndex: Int) : ContentIndex() {
        fun withTextContentIndex(idx: Int) = ItemContentIndex(blockIndex, contentIndex, itemIndex, idx)
    }
    data class ItemContentIndex(val blockIndex: Int, val contentIndex: Int, val itemIndex: Int, val itemContentIndex: Int) : ContentIndex()
    // rowIndex = -1 addresses the header row
    data class TableRowIndex(val blockIndex: Int, val contentIndex: Int, val rowIndex: Int) : ContentIndex() {
        fun withCellIndex(cellIndex: Int) = TableCellIndex(blockIndex, contentIndex, rowIndex, cellIndex)
    }
    data class TableCellIndex(val blockIndex: Int, val contentIndex: Int, val rowIndex: Int, val cellIndex: Int) : ContentIndex() {
        fun withTextContentIndex(idx: Int) = TableCellContentIndex(blockIndex, contentIndex, rowIndex, cellIndex, idx)
    }
    data class TableCellContentIndex(val blockIndex: Int, val contentIndex: Int, val rowIndex: Int, val cellIndex: Int, val cellContentIndex: Int) : ContentIndex()

    /** The ContentIndex of the immediate containing element, or null if this is a top-level BlockIndex. */
    fun parent(): ContentIndex? = when (this) {
        is BlockIndex -> null
        is BlockContentIndex -> BlockIndex(blockIndex)
        is ItemIndex -> BlockContentIndex(blockIndex, contentIndex)
        is ItemContentIndex -> ItemIndex(blockIndex, contentIndex, itemIndex)
        is TableRowIndex -> BlockContentIndex(blockIndex, contentIndex)
        is TableCellIndex -> TableRowIndex(blockIndex, contentIndex, rowIndex)
        is TableCellContentIndex -> TableCellIndex(blockIndex, contentIndex, rowIndex, cellIndex)
    }
}

data class DiffSegment(val index: ContentIndex, val startOffset: Int, val endOffset: Int)

data class SplitDiff(val inserts: List<DiffSegment>, val deletes: List<DiffSegment>)

/**
 * editedBlocks: a map of partially edited blocks where
 *    - the key is the blockIndex of the blocks with edits
 *    - the value is a BlockEdit
 * deletedBlocks: a map of entirely deleted blocks where
 *    - the key is the blockIndex of where it should be displayed in the unified diff as deleted
 *    - the value is a list of entire blocks that should be displayed in the unified diff
 */
data class UnifiedDiff(val editedBlocks: Map<Int, BlockEdit>, val deletedBlocks: Map<Int, List<Edit.Block>>) {

    /**
     * Represents the edits inside a block.
     *
     * contentEdits: a map of partially edited content where
     *    - the key is the contentIndex of the content with edits
     *    - the value is a ContentEdit
     * deletedContent: a map that is similar to deletedBlocks, just for the content of a block instead, where
     *  - the key is the contentIndex of where it should be displayed in the unified diff of the block
     *  - the value is a list of the entirely deleted content that should be displayed in the unified diff
     */
    data class BlockEdit(val contentEdits: Map<Int, ContentEdit>, val deletedContent: Map<Int, List<Edit.ParagraphContent>>)

    /** Represents an edit to a single piece of ParagraphContent within a block. */
    sealed class ContentEdit {
        data class TextContentEdit(val edit: TextEdit) : ContentEdit()

        data class ItemListEdit(val itemEdits: Map<Int, TextOnlyEdit>, val deletedItems: Map<Int, List<Edit.ParagraphContent.ItemList.Item>>) : ContentEdit()

        data class TableEdit(val rowEdits: Map<Int, RowEdit>, val deletedRows: Map<Int, List<Edit.ParagraphContent.Table.Row>>) : ContentEdit()
    }

    /** A single word-level range within a still-existing piece of text. */
    data class TextSegment(val startOffset: Int, val endOffset: Int)

    /** A single word-level range that was removed from a still-existing piece of text, carrying the removed text itself. */
    data class DeletedTextSegment(val startOffset: Int, val endOffset: Int, val text: String)

    /** Word-level edits within a single, still-existing Text (Literal/Variable/NewLine) content node. */
    data class TextEdit(val inserts: List<TextSegment>, val deletes: List<DeletedTextSegment>)

    /**
     * Edits within a content list that only ever holds Text content (ItemList.Item content, Table.Cell content).
     *
     * textEdits: a map of word-level edits inside still-existing Text content, where
     *    - the key is the itemContentIndex/cellContentIndex of the Text content with edits
     *    - the value is the TextEdit
     * deletedContent: a map of entirely deleted Text content, where
     *    - the key is the itemContentIndex/cellContentIndex of where it should be displayed in the unified diff
     *    - the value is a list of the entirely deleted Text content that should be displayed in the unified diff
     */
    data class TextOnlyEdit(val textEdits: Map<Int, TextEdit>, val deletedContent: Map<Int, List<Edit.ParagraphContent.Text>>)

    /**
     * Edits within a single row of a table.
     *
     * cellEdits: similar to contentEdits in BlockEdit, just for the cells of a row instead
     * deletedCells: similar to deletedContent in BlockEdit, just for the cells of a row instead
     */
    data class RowEdit(val cellEdits: Map<Int, TextOnlyEdit>, val deletedCells: Map<Int, List<Edit.ParagraphContent.Table.Cell>>)

}