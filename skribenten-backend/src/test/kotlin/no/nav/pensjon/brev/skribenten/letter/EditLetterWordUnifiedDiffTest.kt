@file:OptIn(InterneDataklasser::class)

package no.nav.pensjon.brev.skribenten.letter

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.BlockContentIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.ItemContentIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.TableCellContentIndex
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EditLetterWordUnifiedDiffTest {

    private val wordDiff = EditLetterWordDiff()

    @Test
    fun `no change produces empty inserts and deletes`() {
        val letter = editedLetter { paragraph { literal(text = "hello world") } }
        val (inserts, deletes) = wordDiff.unifiedDiff(letter, letter)
        assertEquals(emptyList<DiffSegment>(), inserts)
        assertEquals(emptyList<UnifiedDeleteSegment>(), deletes)
    }

    @Test
    fun `changed word produces insert DiffSegment and delete with text`() {
        val old = editedLetter { paragraph { literal(text = "hello world") } }
        val new = editedLetter { paragraph { literal(text = "hello goodbye") } }
        val (inserts, deletes) = wordDiff.unifiedDiff(old, new)
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 0), 6, 13)), inserts)
        assertEquals(listOf(UnifiedDeleteSegment(BlockContentIndex(0, 0), 6, 11, "world")), deletes)
    }

    @Test
    fun `added word produces insert DiffSegment and no delete`() {
        val old = editedLetter { paragraph { literal(text = "hello") } }
        val new = editedLetter { paragraph { literal(text = "hello world") } }
        val (inserts, deletes) = wordDiff.unifiedDiff(old, new)
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 0), 6, 11)), inserts)
        assertEquals(emptyList<UnifiedDeleteSegment>(), deletes)
    }

    @Test
    fun `removed word produces no insert and delete with text`() {
        val old = editedLetter { paragraph { literal(text = "hello world") } }
        val new = editedLetter { paragraph { literal(text = "hello") } }
        val (inserts, deletes) = wordDiff.unifiedDiff(old, new)
        assertEquals(emptyList<DiffSegment>(), inserts)
        assertEquals(listOf(UnifiedDeleteSegment(BlockContentIndex(0, 0), 6, 11, "world")), deletes)
    }

    @Test
    fun `consecutive changed words are merged into one delete segment with combined text`() {
        val old = editedLetter { paragraph { literal(text = "hello foo bar") } }
        val new = editedLetter { paragraph { literal(text = "hello aaa bbb") } }
        val (inserts, deletes) = wordDiff.unifiedDiff(old, new)
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 0), 6, 13)), inserts)
        assertEquals(listOf(UnifiedDeleteSegment(BlockContentIndex(0, 0), 6, 13, "foo bar")), deletes)
    }

    @Test
    fun `changed word in item list produces delete with ItemContentIndex and text`() {
        val old = editedLetter { paragraph { itemList { item { literal(text = "hello world") } } } }
        val new = editedLetter { paragraph { itemList { item { literal(text = "hello goodbye") } } } }
        val (inserts, deletes) = wordDiff.unifiedDiff(old, new)
        assertEquals(listOf(DiffSegment(ItemContentIndex(0, 0, 0, 0), 6, 13)), inserts)
        assertEquals(listOf(UnifiedDeleteSegment(ItemContentIndex(0, 0, 0, 0), 6, 11, "world")), deletes)
    }

    @Test
    fun `changed word in table body cell produces delete with TableCellContentIndex and text`() {
        val old = editedLetter {
            paragraph {
                table {
                    header { colSpec { literal(text = "col") } }
                    row { cell { literal(text = "hello world") } }
                }
            }
        }
        val new = editedLetter {
            paragraph {
                table {
                    header { colSpec { literal(text = "col") } }
                    row { cell { literal(text = "hello goodbye") } }
                }
            }
        }
        val (inserts, deletes) = wordDiff.unifiedDiff(old, new)
        assertEquals(listOf(DiffSegment(TableCellContentIndex(0, 0, 0, 0, 0), 6, 13)), inserts)
        assertEquals(listOf(UnifiedDeleteSegment(TableCellContentIndex(0, 0, 0, 0, 0), 6, 11, "world")), deletes)
    }
}
