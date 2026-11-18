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
    fun `align drops rendered elements without id`() {
        assertEquals(
            ListAlignment.Aligned(elements(1, 2), emptyList()),
            ListAlignment.align(elements(1, 2), elements(null, 1, 2), emptyList()),
        )
    }

    @Test
    fun `align removes the unmatched first occurrence of a duplicated id`() {
        assertEquals(
            ListAlignment.Aligned(elements(2, 1), listOf(1)),
            ListAlignment.align(elements(2, 1), elements(1, 2, 1), listOf(1)),
        )
    }

    @Test
    fun `align removes the unmatched second occurrence of a duplicated id`() {
        assertEquals(
            ListAlignment.Aligned(elements(1, 2), listOf(1)),
            ListAlignment.align(elements(1, 2), elements(1, 2, 1), listOf(1)),
        )
    }

    @Test
    fun `align prunes deleted ids no longer rendered`() {
        assertEquals(
            ListAlignment.Aligned(elements(2), listOf(1)),
            ListAlignment.align(elements(2), elements(1, 2), listOf(1, 3)),
        )
    }

    @Test
    fun `align prunes deleted ids whose only rendered occurrence is matched by edited`() {
        assertEquals(
            ListAlignment.Aligned(elements(1), emptyList()),
            ListAlignment.align(elements(1), elements(1), listOf(1)),
        )
    }
}
