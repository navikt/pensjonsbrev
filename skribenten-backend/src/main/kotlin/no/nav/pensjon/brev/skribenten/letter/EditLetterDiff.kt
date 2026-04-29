package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.common.diff.EditScript
import no.nav.pensjon.brev.skribenten.common.diff.shortestEditScript

interface EditLetterDiff<Token : Any> {
    fun tokenize(letter: Edit.Letter): List<Token>
    fun generateDiffSegments(editScript: EditScript<Token>): Pair<List<DiffSegment>, List<DiffSegment>>

    fun diff(old: Edit.Letter, new: Edit.Letter): Pair<List<DiffSegment>, List<DiffSegment>> =
        generateDiffSegments(
            shortestEditScript(
                old = tokenize(old),
                new = tokenize(new),
            )
        )
}

sealed class ContentIndex {
    data class BlockContentIndex(val blockIndex: Int, val contentIndex: Int) : ContentIndex()
    data class ItemContentIndex(val blockIndex: Int, val contentIndex: Int, val itemIndex: Int, val itemContentIndex: Int) : ContentIndex()
    // rowIndex = -1 addresses the header row
    data class TableCellContentIndex(val blockIndex: Int, val contentIndex: Int, val rowIndex: Int, val cellIndex: Int, val cellContentIndex: Int) : ContentIndex()
}

data class DiffSegment(val index: ContentIndex, val startOffset: Int, val endOffset: Int)
