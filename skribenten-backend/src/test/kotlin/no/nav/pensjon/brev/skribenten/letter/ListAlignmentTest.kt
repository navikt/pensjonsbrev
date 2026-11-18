package no.nav.pensjon.brev.skribenten.letter

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ListAlignmentTest {

    private fun elements(vararg ids: Int?) = ids.map { Edit.ParagraphContent.Text.NewLine(id = it) }

    @Test
    fun `matches all rendered elements when nothing is removed`() {
        assertEquals(setOf(0, 1, 2), ListAlignment.matchedRenderedIndices(elements(1, 2, 3), elements(1, 2, 3)))
    }

    @Test
    fun `keeps second occurrence of duplicated id when first is removed`() {
        assertEquals(setOf(1, 2), ListAlignment.matchedRenderedIndices(elements(2, 1), elements(1, 2, 1)))
    }

    @Test
    fun `keeps first occurrence of duplicated id when second is removed`() {
        assertEquals(setOf(0, 1), ListAlignment.matchedRenderedIndices(elements(1, 2), elements(1, 2, 1)))
    }

    @Test
    fun `ignores new edited elements and returns indices in original rendered list`() {
        assertEquals(setOf(1, 3), ListAlignment.matchedRenderedIndices(elements(null, 1, 3), elements(null, 1, 2, 3)))
    }

    @Test
    fun `returns empty set when no ids are shared`() {
        assertEquals(emptySet<Int>(), ListAlignment.matchedRenderedIndices(elements(4, 5), elements(1, 2, 3)))
    }

    @Test
    fun `withoutDeleted drops rendered elements without id`() {
        assertEquals(elements(1, 2), ListAlignment.withoutDeleted(elements(1, 2), elements(null, 1, 2), emptyList()))
    }

    @Test
    fun `withoutDeleted removes the unmatched first occurrence of a duplicated id`() {
        assertEquals(elements(2, 1), ListAlignment.withoutDeleted(elements(2, 1), elements(1, 2, 1), listOf(1)))
    }

    @Test
    fun `withoutDeleted removes the unmatched second occurrence of a duplicated id`() {
        assertEquals(elements(1, 2), ListAlignment.withoutDeleted(elements(1, 2), elements(1, 2, 1), listOf(1)))
    }

    @Test
    fun `prunedDeleted drops ids no longer rendered`() {
        assertEquals(listOf(1), ListAlignment.prunedDeleted(listOf(1, 3), elements(2), elements(1, 2)))
    }

    @Test
    fun `prunedDeleted drops ids whose only rendered occurrence is matched by edited`() {
        assertEquals(emptyList<Int>(), ListAlignment.prunedDeleted(listOf(1), elements(1), elements(1)))
    }
}
