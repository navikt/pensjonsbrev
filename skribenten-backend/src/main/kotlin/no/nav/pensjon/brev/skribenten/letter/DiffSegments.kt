package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brevbaker.api.model.LetterMarkup

fun diffBrev(redigertBrev: Edit.Letter, rendret: LetterMarkup): Pair<List<DiffSegment>, List<DiffSegment>> =
    TextOnlyWordDiff().diff(old = rendret.toEdit(), new = redigertBrev)

sealed class ContentIndex {
    data class BlockContentIndex(val blockIndex: Int, val contentIndex: Int) : ContentIndex()
    data class ItemContentIndex(val blockIndex: Int, val contentIndex: Int, val itemIndex: Int, val itemContentIndex: Int) : ContentIndex()
    // rowIndex = -1 addresses the header row
    data class TableCellContentIndex(val blockIndex: Int, val contentIndex: Int, val rowIndex: Int, val cellIndex: Int, val cellContentIndex: Int) : ContentIndex()
}

data class DiffSegment(val index: ContentIndex, val startOffset: Int, val endOffset: Int)