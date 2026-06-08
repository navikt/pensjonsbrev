@file:OptIn(InterneDataklasser::class)

package no.nav.pensjon.brev.skribenten.letter

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.BlockContentIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.ItemContentIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.TableCellContentIndex
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EditLetterWordDiffTest {

    private val wordDiff = EditLetterWordDiff()

    @Test
    fun `first and only word has no phantom leading space in offset`() {
        // "foo" is 3 chars; the segment should be 0-3, not 0-4
        val old = editedLetter { paragraph { literal(text = "foo") } }
        val new = editedLetter { paragraph { literal(text = "bar") } }
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 0), 0, 3)), inserts)
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 0), 0, 3)), deletes)
    }

    @Test
    fun `second word offset reflects actual space position in text`() {
        // "foo bar": "foo"=3, space at 3, "bar" at 4-6
        // The segment for the changed word should start at 4 (after the space), not 3
        val old = editedLetter { paragraph { literal(text = "foo bar") } }
        val new = editedLetter { paragraph { literal(text = "foo baz") } }
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 0), 4, 7)), inserts)
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 0), 4, 7)), deletes)
    }


    @Test
    fun `no change produces empty diff segments`() {
        val letter = editedLetter { paragraph { literal(text = "hello world") } }
        val (inserts, deletes) = wordDiff.diff(letter, letter)
        assertEquals(emptyList<DiffSegment>(), inserts)
        assertEquals(emptyList<DiffSegment>(), deletes)
    }

    @Test
    fun `changed first word produces correct BlockContentIndex and offsets`() {
        val old = editedLetter { paragraph { literal(text = "hello world") } }
        val new = editedLetter { paragraph { literal(text = "goodbye world") } }
        val (inserts, deletes) = wordDiff.diff(old, new)
        // "goodbye"=7, "hello"=5 (no leading space on first word)
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 0), 0, 7)), inserts)
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 0), 0, 5)), deletes)
    }

    @Test
    fun `changed second word produces correct offsets after first word`() {
        val old = editedLetter { paragraph { literal(text = "hello world") } }
        val new = editedLetter { paragraph { literal(text = "hello goodbye") } }
        val (inserts, deletes) = wordDiff.diff(old, new)
        // "hello"=5, then " goodbye"=8: word starts at 6 → [6,13) and " world"=6: word starts at 6 → [6,11)
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 0), 6, 13)), inserts)
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 0), 6, 11)), deletes)
    }

    @Test
    fun `added word produces insert segment and empty delete segment`() {
        val old = editedLetter { paragraph { literal(text = "hello") } }
        val new = editedLetter { paragraph { literal(text = "hello world") } }
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 0), 6, 11)), inserts)
        assertEquals(emptyList<DiffSegment>(), deletes)
    }

    @Test
    fun `removed word produces delete segment and empty insert segment`() {
        val old = editedLetter { paragraph { literal(text = "hello world") } }
        val new = editedLetter { paragraph { literal(text = "hello") } }
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(emptyList<DiffSegment>(), inserts)
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 0), 6, 11)), deletes)
    }

    @Test
    fun `consecutive changed words are merged into one DiffSegment`() {
        val old = editedLetter { paragraph { literal(text = "hello world foo") } }
        val new = editedLetter { paragraph { literal(text = "goodbye there foo") } }
        val (inserts, _) = wordDiff.diff(old, new)
        // "goodbye there" = 7 + 1 + 5 = 13 (space between is included when words merge)
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 0), 0, 13)), inserts)
    }

    @Test
    fun `non-consecutive changed words produce separate DiffSegments`() {
        val old = editedLetter { paragraph { literal(text = "hello world foo") } }
        val new = editedLetter { paragraph { literal(text = "goodbye world bar") } }
        val (inserts, _) = wordDiff.diff(old, new)
        // "goodbye"=[0,7), "bar" starts at 14 (after "goodbye world "=13) → [14,17)
        assertEquals(
            listOf(
                DiffSegment(BlockContentIndex(0, 0), 0, 7),
                DiffSegment(BlockContentIndex(0, 0), 14, 17),
            ),
            inserts
        )
    }

    @Test
    fun `changed word in second literal of block uses correct contentIndex and resets offsets`() {
        // Literal(contentIndex=0), NewLine(contentIndex=1), Literal(contentIndex=2)
        val old = editedLetter {
            paragraph {
                literal(text = "first")
                newLine()
                literal(text = "hello world")
            }
        }
        val new = editedLetter {
            paragraph {
                literal(text = "first")
                newLine()
                literal(text = "hello goodbye")
            }
        }
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 2), 6, 13)), inserts)
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 2), 6, 11)), deletes)
    }

    @Test
    fun `changed word in item list produces ItemContentIndex`() {
        val old = editedLetter { paragraph { itemList { item { literal(text = "hello world") } } } }
        val new = editedLetter { paragraph { itemList { item { literal(text = "hello goodbye") } } } }
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegment(ItemContentIndex(0, 0, 0, 0), 6, 13)), inserts)
        assertEquals(listOf(DiffSegment(ItemContentIndex(0, 0, 0, 0), 6, 11)), deletes)
    }

    @Test
    fun `changed word in second item produces correct itemIndex`() {
        val old = editedLetter {
            paragraph {
                itemList {
                    item { literal(text = "unchanged") }
                    item { literal(text = "hello world") }
                }
            }
        }
        val new = editedLetter {
            paragraph {
                itemList {
                    item { literal(text = "unchanged") }
                    item { literal(text = "hello goodbye") }
                }
            }
        }
        val (inserts, _) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegment(ItemContentIndex(0, 0, 1, 0), 6, 13)), inserts)
    }

    @Test
    fun `changed word in table body cell produces TableCellContentIndex`() {
        val old = editedLetter {
            paragraph {
                table {
                    header { colSpec() }
                    row { cell { literal(text = "hello world") } }
                }
            }
        }
        val new = editedLetter {
            paragraph {
                table {
                    header { colSpec() }
                    row { cell { literal(text = "hello goodbye") } }
                }
            }
        }
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegment(TableCellContentIndex(0, 0, 0, 0, 0), 6, 13)), inserts)
        assertEquals(listOf(DiffSegment(TableCellContentIndex(0, 0, 0, 0, 0), 6, 11)), deletes)
    }

    @Test
    fun `changed word in table header cell produces TableCellContentIndex with rowIndex -1`() {
        val old = editedLetter {
            paragraph {
                table {
                    header { colSpec { literal(text = "hello world") } }
                    row { cell() }
                }
            }
        }
        val new = editedLetter {
            paragraph {
                table {
                    header { colSpec { literal(text = "hello goodbye") } }
                    row { cell() }
                }
            }
        }
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegment(TableCellContentIndex(0, 0, -1, 0, 0), 6, 13)), inserts)
        assertEquals(listOf(DiffSegment(TableCellContentIndex(0, 0, -1, 0, 0), 6, 11)), deletes)
    }

    @Test
    fun `inserted block before existing block with changed word uses correct blockIndex per side`() {
        // New has an extra block at index 0; the shared block with the changed word is at index 1 in new but 0 in old.
        val old = editedLetter {
            paragraph { literal(text = "hello world") }
            paragraph { literal(text = "foo bar") }
        }
        val new = editedLetter {
            paragraph { literal(text = "EXTRA") }
            paragraph { literal(text = "hello goodbye") }
            paragraph { literal(text = "foo bar") }
        }
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(
            listOf(
                DiffSegment(BlockContentIndex(0, 0), 0, 5), // "EXTRA" in new block 0
                DiffSegment(BlockContentIndex(1, 0), 6, 13), // "goodbye" in new block 1
            ),
            inserts,
        )
        assertEquals(
            listOf(DiffSegment(BlockContentIndex(1, 0), 6, 11)), // "world" in old block 0
            deletes,
        )
    }

}
