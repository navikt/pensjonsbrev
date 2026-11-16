package no.nav.pensjon.brev.skribenten.letter.v2

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import no.nav.pensjon.brev.skribenten.common.diff.EditScript

interface EditLetterTokenizerV2<Token : Any> {
    fun tokenize(letter: EditV2.Letter): List<Token>
    fun <R> parseTokens(editScript: EditScript<Token>, producer: DiffProducerV2<R>): R
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = ContentIndexV2.BlockIndex::class, name = "BLOCK"),
    JsonSubTypes.Type(value = ContentIndexV2.BlockContentIndex::class, name = "BLOCK_CONTENT"),
    JsonSubTypes.Type(value = ContentIndexV2.ItemIndex::class, name = "ITEM"),
    JsonSubTypes.Type(value = ContentIndexV2.ItemContentIndex::class, name = "ITEM_CONTENT"),
    JsonSubTypes.Type(value = ContentIndexV2.ColumnSpecIndex::class, name = "COLUMN_SPEC"),
    JsonSubTypes.Type(value = ContentIndexV2.ColumnSpecContentIndex::class, name = "COLUMN_SPEC_CONTENT"),
    JsonSubTypes.Type(value = ContentIndexV2.TableRowIndex::class, name = "TABLE_ROW"),
    JsonSubTypes.Type(value = ContentIndexV2.TableCellIndex::class, name = "TABLE_CELL"),
    JsonSubTypes.Type(value = ContentIndexV2.TableCellContentIndex::class, name = "TABLE_CELL_CONTENT"),
)
sealed class ContentIndexV2(val type: Type) {
    enum class Type {
        BLOCK, BLOCK_CONTENT, ITEM, ITEM_CONTENT, COLUMN_SPEC, COLUMN_SPEC_CONTENT, TABLE_ROW, TABLE_CELL, TABLE_CELL_CONTENT,
    }

    abstract val blockIndex: Int

    data class BlockIndex(override val blockIndex: Int) : ContentIndexV2(Type.BLOCK) {
        fun withContentIndex(contentIndex: Int) = BlockContentIndex(blockIndex, contentIndex)
        fun withItemIndex(itemIndex: Int) = ItemIndex(blockIndex, itemIndex)
        fun withColumnSpecIndex(columnIndex: Int) = ColumnSpecIndex(blockIndex, columnIndex)
        fun withRowIndex(rowIndex: Int) = TableRowIndex(blockIndex, rowIndex)
    }

    data class BlockContentIndex(override val blockIndex: Int, val contentIndex: Int) : ContentIndexV2(Type.BLOCK_CONTENT)

    data class ItemIndex(override val blockIndex: Int, val itemIndex: Int) : ContentIndexV2(Type.ITEM) {
        fun withTextContentIndex(idx: Int) = ItemContentIndex(blockIndex, itemIndex, idx)
    }

    data class ItemContentIndex(override val blockIndex: Int, val itemIndex: Int, val itemContentIndex: Int) : ContentIndexV2(Type.ITEM_CONTENT)

    data class ColumnSpecIndex(override val blockIndex: Int, val columnIndex: Int) : ContentIndexV2(Type.COLUMN_SPEC) {
        fun withTextContentIndex(idx: Int) = ColumnSpecContentIndex(blockIndex, columnIndex, idx)
    }

    data class ColumnSpecContentIndex(override val blockIndex: Int, val columnIndex: Int, val columnContentIndex: Int) : ContentIndexV2(Type.COLUMN_SPEC_CONTENT)

    data class TableRowIndex(override val blockIndex: Int, val rowIndex: Int) : ContentIndexV2(Type.TABLE_ROW) {
        fun withCellIndex(cellIndex: Int) = TableCellIndex(blockIndex, rowIndex, cellIndex)
    }

    data class TableCellIndex(override val blockIndex: Int, val rowIndex: Int, val cellIndex: Int) : ContentIndexV2(Type.TABLE_CELL) {
        fun withTextContentIndex(idx: Int) = TableCellContentIndex(blockIndex, rowIndex, cellIndex, idx)
    }

    data class TableCellContentIndex(override val blockIndex: Int, val rowIndex: Int, val cellIndex: Int, val cellContentIndex: Int) : ContentIndexV2(Type.TABLE_CELL_CONTENT)

    fun parent(): ContentIndexV2? = when (this) {
        is BlockIndex -> null
        is BlockContentIndex -> BlockIndex(blockIndex)
        is ItemIndex -> BlockIndex(blockIndex)
        is ItemContentIndex -> ItemIndex(blockIndex, itemIndex)
        is ColumnSpecIndex -> BlockIndex(blockIndex)
        is ColumnSpecContentIndex -> ColumnSpecIndex(blockIndex, columnIndex)
        is TableRowIndex -> BlockIndex(blockIndex)
        is TableCellIndex -> TableRowIndex(blockIndex, rowIndex)
        is TableCellContentIndex -> TableCellIndex(blockIndex, rowIndex, cellIndex)
    }
}

data class DiffSegmentV2(val index: ContentIndexV2, val startOffset: Int, val endOffset: Int)

data class SplitDiffV2(val inserts: List<DiffSegmentV2>, val deletes: List<DiffSegmentV2>)

data class UnifiedDiffV2(val editedBlocks: Map<Int, BlockEdit>, val deletedBlocks: Map<Int, List<EditV2.Block>>) {

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    @JsonSubTypes(
        JsonSubTypes.Type(value = BlockEdit.TextBlockEdit::class, name = "TEXT"),
        JsonSubTypes.Type(value = BlockEdit.ListBlockEdit::class, name = "LIST"),
        JsonSubTypes.Type(value = BlockEdit.TableEdit::class, name = "TABLE"),
    )
    sealed class BlockEdit(val type: Type) {
        enum class Type {
            TEXT, LIST, TABLE,
        }

        data class TextBlockEdit(val textEdits: Map<Int, TextEdit>, val deletedContent: Map<Int, List<EditV2.Text>>) : BlockEdit(Type.TEXT)

        data class ListBlockEdit(val itemEdits: Map<Int, TextOnlyEdit>, val deletedItems: Map<Int, List<EditV2.Item>>) : BlockEdit(Type.LIST)

        data class TableEdit(
            val columnSpecEdits: Map<Int, TextOnlyEdit>,
            val deletedColumnSpecs: Map<Int, List<EditV2.Block.Table.ColumnSpec>>,
            val rowEdits: Map<Int, RowEdit>,
            val deletedRows: Map<Int, List<EditV2.Block.Table.Row>>,
        ) : BlockEdit(Type.TABLE)
    }

    data class TextSegment(val startOffset: Int, val endOffset: Int)

    data class DeletedTextSegment(val startOffset: Int, val endOffset: Int, val text: String)

    data class TextEdit(val inserts: List<TextSegment>, val deletes: List<DeletedTextSegment>)

    data class TextOnlyEdit(val textEdits: Map<Int, TextEdit>, val deletedContent: Map<Int, List<EditV2.Text>>)

    data class RowEdit(val cellEdits: Map<Int, TextOnlyEdit>, val deletedCells: Map<Int, List<EditV2.Block.Table.Cell>>)
}
