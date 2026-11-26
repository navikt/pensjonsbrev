package no.nav.pensjon.brev.skribenten.letter.v2

internal object ListAlignmentV2 {

    data class Aligned<E>(val remainingRendered: List<E>, val deleted: List<Int>)

    fun <E : EditV2.Identifiable> align(edited: List<EditV2.Identifiable>, rendered: List<E>, deleted: List<Int>): Aligned<E> {
        if (deleted.isEmpty()) return Aligned(rendered.filter { it.id != null }, emptyList())

        val matchedRenderedIndices = matchedRenderedIndices(edited, rendered)
        return Aligned(
            remainingRendered = withoutDeleted(rendered, deleted, matchedRenderedIndices),
            deleted = prunedDeleted(rendered, deleted, matchedRenderedIndices),
        )
    }

    private fun <E : EditV2.Identifiable> withoutDeleted(rendered: List<E>, deleted: List<Int>, matchedRenderedIndices: Set<Int>): List<E> {
        val deletedCounts = deleted.groupingBy { it }.eachCount().toMutableMap()
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

    private fun prunedDeleted(rendered: List<EditV2.Identifiable>, deleted: List<Int>, matchedRenderedIndices: Set<Int>): List<Int> {
        val unmatchedRenderedCounts = rendered
            .filterIndexed { index, element -> element.id != null && index !in matchedRenderedIndices }
            .groupingBy { it.id }
            .eachCount()
        val deletedCounts = deleted.groupingBy { it }.eachCount()

        return deletedCounts.flatMap { entry ->
            List(minOf(entry.value, unmatchedRenderedCounts[entry.key] ?: 0)) { entry.key }
        }
    }

    private fun matchedRenderedIndices(edited: List<EditV2.Identifiable>, rendered: List<EditV2.Identifiable>): Set<Int> {
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
