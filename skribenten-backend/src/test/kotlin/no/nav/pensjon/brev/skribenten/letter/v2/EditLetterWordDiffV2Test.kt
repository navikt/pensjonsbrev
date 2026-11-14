package no.nav.pensjon.brev.skribenten.letter.v2

import no.nav.pensjon.brev.skribenten.letter.v2.ContentIndexV2.BlockContentIndex
import no.nav.pensjon.brev.skribenten.letter.v2.ContentIndexV2.ColumnSpecContentIndex
import no.nav.pensjon.brev.skribenten.letter.v2.ContentIndexV2.ItemContentIndex
import no.nav.pensjon.brev.skribenten.letter.v2.ContentIndexV2.TableCellContentIndex
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EditLetterWordDiffV2Test {

    private val wordDiff = EditLetterWordDiffV2()

    @Test
    fun `first and only word has no phantom leading space in offset`() {
        val old = editedLetter { paragraph { literal(text = "foo") } }
        val new = editedLetter { paragraph { literal(text = "bar") } }
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegmentV2(BlockContentIndex(0, 0), 0, 3)), inserts)
        assertEquals(listOf(DiffSegmentV2(BlockContentIndex(0, 0), 0, 3)), deletes)
    }

    @Test
    fun `second word offset reflects actual space position in text`() {
        val old = editedLetter { paragraph { literal(text = "foo bar") } }
        val new = editedLetter { paragraph { literal(text = "foo baz") } }
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegmentV2(BlockContentIndex(0, 0), 4, 7)), inserts)
        assertEquals(listOf(DiffSegmentV2(BlockContentIndex(0, 0), 4, 7)), deletes)
    }

    @Test
    fun `no change produces empty diff segments`() {
        val letter = editedLetter { paragraph { literal(text = "hello world") } }
        val (inserts, deletes) = wordDiff.diff(letter, letter)
        assertEquals(emptyList<DiffSegmentV2>(), inserts)
        assertEquals(emptyList<DiffSegmentV2>(), deletes)
    }

    @Test
    fun `changed first word produces correct BlockContentIndex and offsets`() {
        val old = editedLetter { paragraph { literal(text = "hello world") } }
        val new = editedLetter { paragraph { literal(text = "goodbye world") } }
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegmentV2(BlockContentIndex(0, 0), 0, 7)), inserts)
        assertEquals(listOf(DiffSegmentV2(BlockContentIndex(0, 0), 0, 5)), deletes)
    }

    @Test
    fun `changed second word produces correct offsets after first word`() {
        val old = editedLetter { paragraph { literal(text = "hello world") } }
        val new = editedLetter { paragraph { literal(text = "hello goodbye") } }
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegmentV2(BlockContentIndex(0, 0), 6, 13)), inserts)
        assertEquals(listOf(DiffSegmentV2(BlockContentIndex(0, 0), 6, 11)), deletes)
    }

    @Test
    fun `added word produces insert segment and empty delete segment`() {
        val old = editedLetter { paragraph { literal(text = "hello") } }
        val new = editedLetter { paragraph { literal(text = "hello world") } }
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegmentV2(BlockContentIndex(0, 0), 6, 11)), inserts)
        assertEquals(emptyList<DiffSegmentV2>(), deletes)
    }

    @Test
    fun `removed word produces delete segment and empty insert segment`() {
        val old = editedLetter { paragraph { literal(text = "hello world") } }
        val new = editedLetter { paragraph { literal(text = "hello") } }
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(emptyList<DiffSegmentV2>(), inserts)
        assertEquals(listOf(DiffSegmentV2(BlockContentIndex(0, 0), 6, 11)), deletes)
    }

    @Test
    fun `consecutive changed words are merged into one DiffSegment`() {
        val old = editedLetter { paragraph { literal(text = "hello world foo") } }
        val new = editedLetter { paragraph { literal(text = "goodbye there foo") } }
        val (inserts, _) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegmentV2(BlockContentIndex(0, 0), 0, 13)), inserts)
    }

    @Test
    fun `non-consecutive changed words produce separate DiffSegments`() {
        val old = editedLetter { paragraph { literal(text = "hello world foo") } }
        val new = editedLetter { paragraph { literal(text = "goodbye world bar") } }
        val (inserts, _) = wordDiff.diff(old, new)
        assertEquals(
            listOf(
                DiffSegmentV2(BlockContentIndex(0, 0), 0, 7),
                DiffSegmentV2(BlockContentIndex(0, 0), 14, 17),
            ),
            inserts
        )
    }

    @Test
    fun `changed word in second literal of block uses correct contentIndex and resets offsets`() {
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
        assertEquals(listOf(DiffSegmentV2(BlockContentIndex(0, 2), 6, 13)), inserts)
        assertEquals(listOf(DiffSegmentV2(BlockContentIndex(0, 2), 6, 11)), deletes)
    }

    @Test
    fun `changed word in title4 produces BlockContentIndex`() {
        val old = editedLetter { title4 { literal(text = "hello world") } }
        val new = editedLetter { title4 { literal(text = "hello goodbye") } }
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegmentV2(BlockContentIndex(0, 0), 6, 13)), inserts)
        assertEquals(listOf(DiffSegmentV2(BlockContentIndex(0, 0), 6, 11)), deletes)
    }

    @Test
    fun `changed word in item list produces ItemContentIndex`() {
        val old = editedLetter { itemList { item { literal(text = "hello world") } } }
        val new = editedLetter { itemList { item { literal(text = "hello goodbye") } } }
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegmentV2(ItemContentIndex(0, 0, 0), 6, 13)), inserts)
        assertEquals(listOf(DiffSegmentV2(ItemContentIndex(0, 0, 0), 6, 11)), deletes)
    }

    @Test
    fun `changed word in numbered list produces ItemContentIndex`() {
        val old = editedLetter { numberedList { item { literal(text = "hello world") } } }
        val new = editedLetter { numberedList { item { literal(text = "hello goodbye") } } }
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegmentV2(ItemContentIndex(0, 0, 0), 6, 13)), inserts)
        assertEquals(listOf(DiffSegmentV2(ItemContentIndex(0, 0, 0), 6, 11)), deletes)
    }

    @Test
    fun `changed word in second item produces correct itemIndex`() {
        val old = editedLetter {
            itemList {
                item { literal(text = "unchanged") }
                item { literal(text = "hello world") }
            }
        }
        val new = editedLetter {
            itemList {
                item { literal(text = "unchanged") }
                item { literal(text = "hello goodbye") }
            }
        }
        val (inserts, _) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegmentV2(ItemContentIndex(0, 1, 0), 6, 13)), inserts)
    }

    @Test
    fun `changed word in table body cell produces TableCellContentIndex`() {
        val old = editedLetter {
            table {
                header { colSpec() }
                row { cell { literal(text = "hello world") } }
            }
        }
        val new = editedLetter {
            table {
                header { colSpec() }
                row { cell { literal(text = "hello goodbye") } }
            }
        }
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegmentV2(TableCellContentIndex(0, 0, 0, 0), 6, 13)), inserts)
        assertEquals(listOf(DiffSegmentV2(TableCellContentIndex(0, 0, 0, 0), 6, 11)), deletes)
    }

    @Test
    fun `changed word in table header produces ColumnSpecContentIndex`() {
        val old = editedLetter {
            table {
                header { colSpec { literal(text = "hello world") } }
                row { cell() }
            }
        }
        val new = editedLetter {
            table {
                header { colSpec { literal(text = "hello goodbye") } }
                row { cell() }
            }
        }
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegmentV2(ColumnSpecContentIndex(0, 0, 0), 6, 13)), inserts)
        assertEquals(listOf(DiffSegmentV2(ColumnSpecContentIndex(0, 0, 0), 6, 11)), deletes)
    }

    @Test
    fun `inserted block before existing block with changed word uses correct blockIndex per side`() {
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
                DiffSegmentV2(BlockContentIndex(0, 0), 0, 5),
                DiffSegmentV2(BlockContentIndex(1, 0), 6, 13),
            ),
            inserts,
        )
        assertEquals(
            listOf(DiffSegmentV2(BlockContentIndex(1, 0), 6, 11)),
            deletes,
        )
    }

    @Test
    fun `inserted list block between paragraphs produces inserts with ItemContentIndex`() {
        val old = editedLetter {
            paragraph { literal(text = "before") }
            paragraph { literal(text = "after") }
        }
        val new = editedLetter {
            paragraph { literal(text = "before") }
            itemList { item { literal(text = "new item") } }
            paragraph { literal(text = "after") }
        }
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegmentV2(ItemContentIndex(1, 0, 0), 0, 8)), inserts)
        assertEquals(emptyList<DiffSegmentV2>(), deletes)
    }
}
