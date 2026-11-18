package no.nav.pensjon.brev.skribenten.letter

internal object ListAlignment {

    /**
     * Returns the indices in [rendered] that form a longest common subsequence with the template
     * elements still present in [edited]. A deletion marker for a duplicated id is then applied
     * only to rendered occurrences outside this alignment. That preserves whether the caseworker
     * removed the first or the second occurrence in e.g. `[X, separator, X]`.
     */
    fun matchedRenderedIndices(edited: List<Edit.Identifiable>, rendered: List<Edit.Identifiable>): Set<Int> {
        val editedIds = edited.mapNotNull { it.id }
        val renderedWithIds = rendered.mapIndexedNotNull { index, element -> element.id?.let { index to it } }
        val renderedIds = renderedWithIds.map { it.second }
        val lengths = longestCommonSubsequenceLengths(editedIds, renderedIds)

        return collectMatchedRenderedIndices(editedIds, renderedWithIds, lengths)
    }

    private fun longestCommonSubsequenceLengths(editedIds: List<Int>, renderedIds: List<Int>): Array<IntArray> {
        val lengths = Array(editedIds.size + 1) { IntArray(renderedIds.size + 1) }
        for (editedIndex in editedIds.indices.reversed()) {
            for (renderedIndex in renderedIds.indices.reversed()) {
                lengths[editedIndex][renderedIndex] =
                    if (editedIds[editedIndex] == renderedIds[renderedIndex]) {
                        1 + lengths[editedIndex + 1][renderedIndex + 1]
                    } else {
                        maxOf(lengths[editedIndex + 1][renderedIndex], lengths[editedIndex][renderedIndex + 1])
                    }
            }
        }
        return lengths
    }

    private fun collectMatchedRenderedIndices(
        editedIds: List<Int>,
        renderedWithIds: List<Pair<Int, Int>>,
        lengths: Array<IntArray>,
    ): Set<Int> =
        buildSet {
            var editedIndex = 0
            var renderedIndex = 0
            while (editedIndex < editedIds.size && renderedIndex < renderedWithIds.size) {
                if (editedIds[editedIndex] == renderedWithIds[renderedIndex].second) {
                    add(renderedWithIds[renderedIndex].first)
                    editedIndex++
                    renderedIndex++
                } else if (lengths[editedIndex + 1][renderedIndex] > lengths[editedIndex][renderedIndex + 1]) {
                    editedIndex++
                } else {
                    renderedIndex++
                }
            }
    }
}
