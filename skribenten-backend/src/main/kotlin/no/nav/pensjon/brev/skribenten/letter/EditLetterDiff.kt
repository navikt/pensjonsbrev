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
}

data class DiffSegment(val index: ContentIndex, val startOffset: Int, val endOffset: Int)

data class UnifiedDeleteSegment(val index: ContentIndex, val startOffset: Int, val endOffset: Int, val text: String)

data class SplitDiff(val inserts: List<DiffSegment>, val deletes: List<DiffSegment>)

data class UnifiedDiff(val inserts: List<DiffSegment>, val deletes: List<UnifiedDeleteSegment>)

