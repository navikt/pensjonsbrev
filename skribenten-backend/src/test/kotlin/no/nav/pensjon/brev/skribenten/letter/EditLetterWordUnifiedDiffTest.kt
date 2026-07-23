@file:OptIn(InterneDataklasser::class)

package no.nav.pensjon.brev.skribenten.letter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.skribenten.letter.UnifiedDiff.BlockEdit
import no.nav.pensjon.brev.skribenten.letter.UnifiedDiff.ContentEdit
import no.nav.pensjon.brev.skribenten.letter.UnifiedDiff.DeletedTextSegment
import no.nav.pensjon.brev.skribenten.letter.UnifiedDiff.RowEdit
import no.nav.pensjon.brev.skribenten.letter.UnifiedDiff.TextEdit
import no.nav.pensjon.brev.skribenten.letter.UnifiedDiff.TextOnlyEdit
import no.nav.pensjon.brev.skribenten.letter.UnifiedDiff.TextSegment
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EditLetterWordUnifiedDiffTest {

    private val wordDiff = EditLetterWordDiff()

    @Test
    fun `no change produces empty editedBlocks and deletedBlocks`() {
        val letter = editedLetter { paragraph { literal(text = "hello world") } }
        val diff = wordDiff.unifiedDiff(letter, letter)
        assertEquals(emptyMap<Int, BlockEdit>(), diff.editedBlocks)
        assertEquals(emptyMap<Int, List<Edit.Block>>(), diff.deletedBlocks)
    }

    @Test
    fun `changed word produces insert and delete in the TextEdit for that content`() {
        val old = editedLetter { paragraph { literal(text = "hello world") } }
        val new = editedLetter { paragraph { literal(text = "hello goodbye") } }
        val diff = wordDiff.unifiedDiff(old, new)
        assertEquals(
            mapOf(0 to BlockEdit(contentEdits = mapOf(0 to textContentEdit(inserts = listOf(TextSegment(6, 13)), deletes = listOf(DeletedTextSegment(6, 11, "world")))), deletedContent = emptyMap())),
            diff.editedBlocks,
        )
        assertEquals(emptyMap<Int, List<Edit.Block>>(), diff.deletedBlocks)
    }

    @Test
    fun `added word produces insert and no delete`() {
        val old = editedLetter { paragraph { literal(text = "hello") } }
        val new = editedLetter { paragraph { literal(text = "hello world") } }
        val diff = wordDiff.unifiedDiff(old, new)
        assertEquals(
            mapOf(0 to BlockEdit(contentEdits = mapOf(0 to textContentEdit(inserts = listOf(TextSegment(6, 11)))), deletedContent = emptyMap())),
            diff.editedBlocks,
        )
    }

    @Test
    fun `removed word produces no insert and delete with text`() {
        val old = editedLetter { paragraph { literal(text = "hello world") } }
        val new = editedLetter { paragraph { literal(text = "hello") } }
        val diff = wordDiff.unifiedDiff(old, new)
        assertEquals(
            mapOf(0 to BlockEdit(contentEdits = mapOf(0 to textContentEdit(deletes = listOf(DeletedTextSegment(6, 11, "world")))), deletedContent = emptyMap())),
            diff.editedBlocks,
        )
    }

    @Test
    fun `consecutive changed words are merged into one delete segment with combined text`() {
        val old = editedLetter { paragraph { literal(text = "hello foo bar") } }
        val new = editedLetter { paragraph { literal(text = "hello aaa bbb") } }
        val diff = wordDiff.unifiedDiff(old, new)
        assertEquals(
            mapOf(0 to BlockEdit(contentEdits = mapOf(0 to textContentEdit(inserts = listOf(TextSegment(6, 13)), deletes = listOf(DeletedTextSegment(6, 13, "foo bar")))), deletedContent = emptyMap())),
            diff.editedBlocks,
        )
    }

    @Test
    fun `changed word in item list produces delete in the item's TextOnlyEdit`() {
        val old = editedLetter { paragraph { itemList { item { literal(text = "hello world") } } } }
        val new = editedLetter { paragraph { itemList { item { literal(text = "hello goodbye") } } } }
        val diff = wordDiff.unifiedDiff(old, new)
        assertEquals(
            mapOf(
                0 to BlockEdit(
                    contentEdits = mapOf(
                        0 to ContentEdit.ItemListEdit(
                            itemEdits = mapOf(0 to textOnlyEdit(inserts = listOf(TextSegment(6, 13)), deletes = listOf(DeletedTextSegment(6, 11, "world")))),
                            deletedItems = emptyMap(),
                        ),
                    ),
                    deletedContent = emptyMap(),
                ),
            ),
            diff.editedBlocks,
        )
    }

    @Test
    fun `changed word in table body cell produces delete in the cell's TextOnlyEdit`() {
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
        val diff = wordDiff.unifiedDiff(old, new)
        assertEquals(
            mapOf(
                0 to BlockEdit(
                    contentEdits = mapOf(
                        0 to ContentEdit.TableEdit(
                            rowEdits = mapOf(
                                0 to RowEdit(
                                    cellEdits = mapOf(0 to textOnlyEdit(inserts = listOf(TextSegment(6, 13)), deletes = listOf(DeletedTextSegment(6, 11, "world")))),
                                    deletedCells = emptyMap(),
                                ),
                            ),
                            deletedRows = emptyMap(),
                        ),
                    ),
                    deletedContent = emptyMap(),
                ),
            ),
            diff.editedBlocks,
        )
    }

    @Test
    fun `entirely deleted block is embedded in deletedBlocks at its unified position`() {
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
        val diff = wordDiff.unifiedDiff(old, new)

        // block id=1 is entirely deleted; it retains its unified position (blockIndex 0), since it's the
        // first block in the merged view. block id=2 is unchanged, so it produces no editedBlocks entry.
        assertEquals(mapOf(0 to listOf(old.blocks[0])), diff.deletedBlocks)
        // block id=3 survives at blockIndex 1 in `new`, with "vwxyz" removed from its text.
        assertEquals(
            mapOf(1 to BlockEdit(contentEdits = mapOf(0 to textContentEdit(deletes = listOf(DeletedTextSegment(8, 13, "vwxyz")))), deletedContent = emptyMap())),
            diff.editedBlocks,
        )
    }

    @Test
    fun `entirely deleted content within a block is embedded in deletedContent at its unified position`() {
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
        val diff = wordDiff.unifiedDiff(old, new)
        val oldContent = (old.blocks[0] as Edit.Block.Paragraph).content

        // "beta epsilon" is the only content in the new block, so it - and everything sharing its unified
        // position - must use contentIndex 0, since there is no contentIndex 1 in the new document.
        assertEquals(
            mapOf(
                0 to BlockEdit(
                    // "gamma" -> "epsilon" is a word-level change within the still-existing "beta ..." literal
                    contentEdits = mapOf(0 to textContentEdit(inserts = listOf(TextSegment(5, 12)), deletes = listOf(DeletedTextSegment(5, 10, "gamma")))),
                    // "alpha" and the NewLine are entirely deleted content, embedded in full, retaining their
                    // unified position (contentIndex 0) rather than their old-document position (0 and 1)
                    deletedContent = mapOf(0 to listOf(oldContent[0], oldContent[1])),
                ),
            ),
            diff.editedBlocks,
        )
    }

    @Test
    fun `entirely deleted content within an item is embedded in deletedContent at its unified position`() {
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
        val diff = wordDiff.unifiedDiff(old, new)
        val oldItemContent = ((old.blocks[0] as Edit.Block.Paragraph).content[0] as Edit.ParagraphContent.ItemList).items[0].content

        assertEquals(
            mapOf(
                0 to BlockEdit(
                    contentEdits = mapOf(
                        0 to ContentEdit.ItemListEdit(
                            itemEdits = mapOf(
                                0 to textOnlyEdit(
                                    inserts = listOf(TextSegment(5, 12)),
                                    deletes = listOf(DeletedTextSegment(5, 10, "gamma")),
                                    deletedContent = mapOf(0 to listOf(oldItemContent[0], oldItemContent[1])),
                                ),
                            ),
                            deletedItems = emptyMap(),
                        ),
                    ),
                    deletedContent = emptyMap(),
                ),
            ),
            diff.editedBlocks,
        )
    }

    @Test
    fun `entirely deleted content within a table cell is embedded in deletedContent at its unified position`() {
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
        val diff = wordDiff.unifiedDiff(old, new)
        val oldTable = (old.blocks[0] as Edit.Block.Paragraph).content[0] as Edit.ParagraphContent.Table
        val oldCellContent = oldTable.rows[0].cells[0].text

        assertEquals(
            mapOf(
                0 to BlockEdit(
                    contentEdits = mapOf(
                        0 to ContentEdit.TableEdit(
                            rowEdits = mapOf(
                                0 to RowEdit(
                                    cellEdits = mapOf(
                                        0 to textOnlyEdit(
                                            inserts = listOf(TextSegment(5, 12)),
                                            deletes = listOf(DeletedTextSegment(5, 10, "gamma")),
                                            deletedContent = mapOf(0 to listOf(oldCellContent[0], oldCellContent[1])),
                                        ),
                                    ),
                                    deletedCells = emptyMap(),
                                ),
                            ),
                            deletedRows = emptyMap(),
                        ),
                    ),
                    deletedContent = emptyMap(),
                ),
            ),
            diff.editedBlocks,
        )
    }

    @Test
    fun `succeeding entire container deletions are correctly tracked at their unified positions`() {
        val old = editedLetter {
            title1 { literal(text = "alpha") }
            paragraph {
                literal(text = "beta")
                literal(text = "epsilon")
            }
            paragraph { literal(text = "hello") }
        }
        val new = editedLetter {
            paragraph {
                literal(text = "beta")
            }
            paragraph { literal(text = "hello") }
        }
        val diff = wordDiff.unifiedDiff(old, new)
        assertThat(diff.deletedBlocks[0]).contains(old.blocks[0])
        // TODO: this is currently 0 (not 1) because forEachIndexedStable attributes a trailing delete to the last
        // established insert index ("beta" at 0), rather than distinguishing "before"/"after" that position. This
        // may need to change once the trailing-delete-ordering issue (deletedContent losing before/after ordering
        // relative to a surviving sibling) is resolved - see plan discussion.
        assertThat(diff.editedBlocks[0]!!.deletedContent[0]).contains(old.blocks[1].content[1])
    }

    private fun textContentEdit(inserts: List<TextSegment> = emptyList(), deletes: List<DeletedTextSegment> = emptyList()): ContentEdit.TextContentEdit =
        ContentEdit.TextContentEdit(TextEdit(inserts, deletes))

    private fun textOnlyEdit(
        inserts: List<TextSegment> = emptyList(),
        deletes: List<DeletedTextSegment> = emptyList(),
        deletedContent: Map<Int, List<Edit.ParagraphContent.Text>> = emptyMap(),
    ): TextOnlyEdit = TextOnlyEdit(textEdits = mapOf(0 to TextEdit(inserts, deletes)), deletedContent = deletedContent)
}
