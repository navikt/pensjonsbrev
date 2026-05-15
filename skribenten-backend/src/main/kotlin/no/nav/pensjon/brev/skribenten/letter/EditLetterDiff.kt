package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.common.diff.EditScript

interface EditLetterTokenizer<Token : Any> {
    fun tokenize(letter: Edit.Letter): List<Token>
    fun <R> parseTokens(editScript: EditScript<Token>, producer: DiffProducer<R>): R
}

sealed class ContentIndex {
    data class BlockIndex(val blockIndex: Int) : ContentIndex()
    data class BlockContentIndex(val blockIndex: Int, val contentIndex: Int) : ContentIndex()
    data class ItemContentIndex(val blockIndex: Int, val contentIndex: Int, val itemIndex: Int, val itemContentIndex: Int) : ContentIndex()
    // rowIndex = -1 addresses the header row
    data class TableCellContentIndex(val blockIndex: Int, val contentIndex: Int, val rowIndex: Int, val cellIndex: Int, val cellContentIndex: Int) : ContentIndex()
}

data class DiffSegment(val index: ContentIndex, val startOffset: Int, val endOffset: Int)

