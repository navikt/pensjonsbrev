package no.nav.pensjon.brev.skribenten.letter

internal object ListAlignment {

    /**
     * Returns the elements of [rendered] that should be merged with [edited]: elements without id are dropped,
     * and so are occurrences deleted by the caseworker. [deleted] is a multiset (one entry per actually deleted
     * occurrence, not a "this id is deleted" flag). Deletion counts are consumed only from rendered occurrences
     * that are not aligned with [edited] (see [matchedRenderedIndices]), preserving the surrounding content order.
     */
    fun <E : Edit.Identifiable> withoutDeleted(edited: List<Edit.Identifiable>, rendered: List<E>, deleted: List<Int>): List<E> {
        if (deleted.isEmpty()) return rendered.filter { it.id != null }

        val deletedCounts = deleted.groupingBy { it }.eachCount().toMutableMap()
        val matchedRenderedIndices = matchedRenderedIndices(edited, rendered)
        return rendered.mapIndexedNotNull { index, element ->
            val id = element.id ?: return@mapIndexedNotNull null
            val remaining = deletedCounts[id]
            if (index !in matchedRenderedIndices && remaining != null && remaining > 0) {
                deletedCounts[id] = remaining - 1
                null
            } else {
                element
            }
        }
    }

    /**
     * Caps a multiset of deleted ids (one entry per deleted occurrence, see [withoutDeleted]) at the
     * number of rendered occurrences of that id that are not aligned with [edited]. This uses the
     * same alignment as [withoutDeleted], so an edited occurrence that is no longer in the template
     * cannot use up capacity belonging to a deleted occurrence that is still rendered.
     */
    fun prunedDeleted(deleted: List<Int>, edited: List<Edit.Identifiable>, rendered: List<Edit.Identifiable>): List<Int> {
        if (deleted.isEmpty()) return emptyList()

        val matchedRenderedIndices = matchedRenderedIndices(edited, rendered)
        val unmatchedRenderedCounts = rendered
            .filterIndexed { index, element -> element.id != null && index !in matchedRenderedIndices }
            .groupingBy { it.id }
            .eachCount()
        val deletedCounts = deleted.groupingBy { it }.eachCount()

        return deletedCounts.flatMap { (id, deletedCount) ->
            List(minOf(deletedCount, unmatchedRenderedCounts[id] ?: 0)) { id }
        }
    }

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
