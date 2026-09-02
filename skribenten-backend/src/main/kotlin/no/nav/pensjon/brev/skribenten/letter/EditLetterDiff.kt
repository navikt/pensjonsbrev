package no.nav.pensjon.brev.skribenten.letter

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import no.nav.pensjon.brev.skribenten.common.diff.EditScript

interface EditLetterTokenizer<Token : Any> {
    fun tokenize(letter: Edit.Letter): List<Token>
    fun <R> parseTokens(editScript: EditScript<Token>, producer: DiffProducer<R>): R
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = ContentIndex.BlockIndex::class, name = "BLOCK"),
    JsonSubTypes.Type(value = ContentIndex.BlockContentIndex::class, name = "BLOCK_CONTENT"),
    JsonSubTypes.Type(value = ContentIndex.ItemIndex::class, name = "ITEM"),
    JsonSubTypes.Type(value = ContentIndex.ItemContentIndex::class, name = "ITEM_CONTENT"),
    JsonSubTypes.Type(value = ContentIndex.TableRowIndex::class, name = "TABLE_ROW"),
    JsonSubTypes.Type(value = ContentIndex.TableCellIndex::class, name = "TABLE_CELL"),
    JsonSubTypes.Type(value = ContentIndex.TableCellContentIndex::class, name = "TABLE_CELL_CONTENT"),
)
sealed class ContentIndex(val type: Type) {
    enum class Type {
        BLOCK, BLOCK_CONTENT, ITEM, ITEM_CONTENT, TABLE_ROW, TABLE_CELL, TABLE_CELL_CONTENT,
    }

    data class BlockIndex(val blockIndex: Int) : ContentIndex(Type.BLOCK) {
        fun withContentIndex(contentIndex: Int) = BlockContentIndex(blockIndex, contentIndex)
    }
    data class BlockContentIndex(val blockIndex: Int, val contentIndex: Int) : ContentIndex(Type.BLOCK_CONTENT) {
        fun withItemIndex(itemIndex: Int) = ItemIndex(blockIndex, contentIndex, itemIndex)
        fun withRowIndex(rowIndex: Int) = TableRowIndex(blockIndex, contentIndex, rowIndex)
    }
    data class ItemIndex(val blockIndex: Int, val contentIndex: Int, val itemIndex: Int) : ContentIndex(Type.ITEM) {
        fun withTextContentIndex(idx: Int) = ItemContentIndex(blockIndex, contentIndex, itemIndex, idx)
    }
    data class ItemContentIndex(val blockIndex: Int, val contentIndex: Int, val itemIndex: Int, val itemContentIndex: Int) : ContentIndex(Type.ITEM_CONTENT)
    // rowIndex = -1 addresses the header row
    data class TableRowIndex(val blockIndex: Int, val contentIndex: Int, val rowIndex: Int) : ContentIndex(Type.TABLE_ROW) {
        fun withCellIndex(cellIndex: Int) = TableCellIndex(blockIndex, contentIndex, rowIndex, cellIndex)
    }
    data class TableCellIndex(val blockIndex: Int, val contentIndex: Int, val rowIndex: Int, val cellIndex: Int) : ContentIndex(Type.TABLE_CELL) {
        fun withTextContentIndex(idx: Int) = TableCellContentIndex(blockIndex, contentIndex, rowIndex, cellIndex, idx)
    }
    data class TableCellContentIndex(val blockIndex: Int, val contentIndex: Int, val rowIndex: Int, val cellIndex: Int, val cellContentIndex: Int) : ContentIndex(Type.TABLE_CELL_CONTENT)

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
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    @JsonSubTypes(
        JsonSubTypes.Type(value = ContentEdit.TextContentEdit::class, name = "TEXT"),
        JsonSubTypes.Type(value = ContentEdit.ItemListEdit::class, name = "ITEM_LIST"),
        JsonSubTypes.Type(value = ContentEdit.TableEdit::class, name = "TABLE"),
    )
    sealed class ContentEdit(val type: Type) {
        enum class Type {
            TEXT, ITEM_LIST, TABLE,
        }

        data class TextContentEdit(val edit: TextEdit) : ContentEdit(Type.TEXT)

        data class ItemListEdit(val itemEdits: Map<Int, TextOnlyEdit>, val deletedItems: Map<Int, List<Edit.ParagraphContent.ItemList.Item>>) : ContentEdit(Type.ITEM_LIST)

        data class TableEdit(val rowEdits: Map<Int, RowEdit>, val deletedRows: Map<Int, List<Edit.ParagraphContent.Table.Row>>) : ContentEdit(Type.TABLE)
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