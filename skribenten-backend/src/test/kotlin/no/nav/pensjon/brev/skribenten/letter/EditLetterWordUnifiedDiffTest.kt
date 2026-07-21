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

    @Test
    fun `produces deletes that take into account previously deleted blocks`() {
        val old = editedLetter {
            paragraph(id = 1) {
                literal(text = "abcdefg")
            }
            paragraph(id = 2) {
                literal(text = "hijklmn")
            }
            paragraph(id = 3) {
                literal(text = "opqrstu vwxyz")
            }
        }
        val new = editedLetter {
            paragraph(id = 2) {
                literal(text = "hijklmn")
            }
            paragraph(id = 3) {
                literal(text = "opqrstu")
            }
        }
        val deletes = wordDiff.unifiedDiff(old, new).deletes
        assertEquals(
            listOf(
                UnifiedDeleteSegment(index = BlockContentIndex(blockIndex = 0, contentIndex = 0), startOffset = 0, endOffset = 7, text = "abcdefg"),
                UnifiedDeleteSegment(index = BlockContentIndex(blockIndex = 1, contentIndex = 0), startOffset = 8, endOffset = 13, text = "vwxyz")
            ),
            deletes
        )
    }

    @Test
    fun `produces deletes that take into account previously deleted content within a block`() {
        // Old contentIndex: 0="alpha" (entirely deleted), 1=NewLine (entirely deleted), 2="beta gamma"
        // New contentIndex: 0="beta epsilon"
        val old = editedLetter {
            paragraph {
                literal(text = "alpha")
                newLine()
                literal(text = "beta gamma")
            }
        }
        val new = editedLetter {
            paragraph {
                literal(text = "beta epsilon")
            }
        }
        val (inserts, deletes) = wordDiff.unifiedDiff(old, new)
        // "beta epsilon" is the only content in the new block, so it - and everything sharing its unified
        // position - must use contentIndex 0, since there is no contentIndex 1 in the new document.
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 0), 5, 12)), inserts)
        assertEquals(
            listOf(
                // "alpha" is entirely deleted content, retaining its unified position (contentIndex 0)
                UnifiedDeleteSegment(BlockContentIndex(0, 0), 0, 5, "alpha"),
                // "gamma" is deleted from the same literal as the "epsilon" insert, so shares its unified
                // contentIndex (0) rather than its old-document contentIndex (2)
                UnifiedDeleteSegment(BlockContentIndex(0, 0), 5, 10, "gamma"),
            ),
            deletes,
        )
    }

    @Test
    fun `produces deletes that take into account previously deleted content within an item`() {
        val old = editedLetter {
            paragraph {
                itemList {
                    item {
                        literal(text = "alpha")
                        newLine()
                        literal(text = "beta gamma")
                    }
                }
            }
        }
        val new = editedLetter {
            paragraph {
                itemList {
                    item {
                        literal(text = "beta epsilon")
                    }
                }
            }
        }
        val (inserts, deletes) = wordDiff.unifiedDiff(old, new)
        assertEquals(listOf(DiffSegment(ItemContentIndex(0, 0, 0, 0), 5, 12)), inserts)
        assertEquals(
            listOf(
                UnifiedDeleteSegment(ItemContentIndex(0, 0, 0, 0), 0, 5, "alpha"),
                UnifiedDeleteSegment(ItemContentIndex(0, 0, 0, 0), 5, 10, "gamma"),
            ),
            deletes,
        )
    }

    @Test
    fun `produces deletes that take into account previously deleted content within a table cell`() {
        val old = editedLetter {
            paragraph {
                table {
                    header { colSpec { literal(text = "col") } }
                    row {
                        cell {
                            literal(text = "alpha")
                            newLine()
                            literal(text = "beta gamma")
                        }
                    }
                }
            }
        }
        val new = editedLetter {
            paragraph {
                table {
                    header { colSpec { literal(text = "col") } }
                    row {
                        cell {
                            literal(text = "beta epsilon")
                        }
                    }
                }
            }
        }
        val (inserts, deletes) = wordDiff.unifiedDiff(old, new)
        assertEquals(listOf(DiffSegment(TableCellContentIndex(0, 0, 0, 0, 0), 5, 12)), inserts)
        assertEquals(
            listOf(
                UnifiedDeleteSegment(TableCellContentIndex(0, 0, 0, 0, 0), 0, 5, "alpha"),
                UnifiedDeleteSegment(TableCellContentIndex(0, 0, 0, 0, 0), 5, 10, "gamma"),
            ),
            deletes,
        )
    }
}
