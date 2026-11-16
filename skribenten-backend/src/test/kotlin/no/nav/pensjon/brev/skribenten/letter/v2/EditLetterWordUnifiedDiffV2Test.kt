package no.nav.pensjon.brev.skribenten.letter.v2

import no.nav.pensjon.brev.skribenten.letter.v2.UnifiedDiffV2.BlockEdit
import no.nav.pensjon.brev.skribenten.letter.v2.UnifiedDiffV2.DeletedTextSegment
import no.nav.pensjon.brev.skribenten.letter.v2.UnifiedDiffV2.RowEdit
import no.nav.pensjon.brev.skribenten.letter.v2.UnifiedDiffV2.TextEdit
import no.nav.pensjon.brev.skribenten.letter.v2.UnifiedDiffV2.TextOnlyEdit
import no.nav.pensjon.brev.skribenten.letter.v2.UnifiedDiffV2.TextSegment
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EditLetterWordUnifiedDiffV2Test {

    private val wordDiff = EditLetterWordDiffV2()

    @Test
    fun `no change produces empty editedBlocks and deletedBlocks`() {
        val letter = editedLetter { paragraph { literal(text = "hello world") } }
        val diff = wordDiff.unifiedDiff(letter, letter)
        assertEquals(emptyMap<Int, BlockEdit>(), diff.editedBlocks)
        assertEquals(emptyMap<Int, List<EditV2.Block>>(), diff.deletedBlocks)
    }

    @Test
    fun `changed word produces insert and delete in the TextEdit for that content`() {
        val old = editedLetter { paragraph { literal(text = "hello world") } }
        val new = editedLetter { paragraph { literal(text = "hello goodbye") } }
        val diff = wordDiff.unifiedDiff(old, new)
        assertEquals(
            mapOf(0 to textBlockEdit(inserts = listOf(TextSegment(6, 13)), deletes = listOf(DeletedTextSegment(6, 11, "world")))),
            diff.editedBlocks,
        )
        assertEquals(emptyMap<Int, List<EditV2.Block>>(), diff.deletedBlocks)
    }

    @Test
    fun `added word produces insert and no delete`() {
        val old = editedLetter { paragraph { literal(text = "hello") } }
        val new = editedLetter { paragraph { literal(text = "hello world") } }
        val diff = wordDiff.unifiedDiff(old, new)
        assertEquals(
            mapOf(0 to textBlockEdit(inserts = listOf(TextSegment(6, 11)))),
            diff.editedBlocks,
        )
    }

    @Test
    fun `removed word produces no insert and delete with text`() {
        val old = editedLetter { paragraph { literal(text = "hello world") } }
        val new = editedLetter { paragraph { literal(text = "hello") } }
        val diff = wordDiff.unifiedDiff(old, new)
        assertEquals(
            mapOf(0 to textBlockEdit(deletes = listOf(DeletedTextSegment(6, 11, "world")))),
            diff.editedBlocks,
        )
    }

    @Test
    fun `consecutive changed words are merged into one delete segment with combined text`() {
        val old = editedLetter { paragraph { literal(text = "hello foo bar") } }
        val new = editedLetter { paragraph { literal(text = "hello aaa bbb") } }
        val diff = wordDiff.unifiedDiff(old, new)
        assertEquals(
            mapOf(0 to textBlockEdit(inserts = listOf(TextSegment(6, 13)), deletes = listOf(DeletedTextSegment(6, 13, "foo bar")))),
            diff.editedBlocks,
        )
    }

    @Test
    fun `changed word in item list produces delete in the item's TextOnlyEdit`() {
        val old = editedLetter { itemList { item { literal(text = "hello world") } } }
        val new = editedLetter { itemList { item { literal(text = "hello goodbye") } } }
        val diff = wordDiff.unifiedDiff(old, new)
        assertEquals(
            mapOf(
                0 to BlockEdit.ListBlockEdit(
                    itemEdits = mapOf(0 to textOnlyEdit(inserts = listOf(TextSegment(6, 13)), deletes = listOf(DeletedTextSegment(6, 11, "world")))),
                    deletedItems = emptyMap(),
                ),
            ),
            diff.editedBlocks,
        )
    }

    @Test
    fun `changed word in table body cell produces delete in the cell's TextOnlyEdit`() {
        val old = editedLetter {
            table {
                header { colSpec { literal(text = "col") } }
                row { cell { literal(text = "hello world") } }
            }
        }
        val new = editedLetter {
            table {
                header { colSpec { literal(text = "col") } }
                row { cell { literal(text = "hello goodbye") } }
            }
        }
        val diff = wordDiff.unifiedDiff(old, new)
        assertEquals(
            mapOf(
                0 to tableEdit(
                    rowEdits = mapOf(
                        0 to RowEdit(
                            cellEdits = mapOf(0 to textOnlyEdit(inserts = listOf(TextSegment(6, 13)), deletes = listOf(DeletedTextSegment(6, 11, "world")))),
                            deletedCells = emptyMap(),
                        ),
                    ),
                ),
            ),
            diff.editedBlocks,
        )
    }

    @Test
    fun `changed word in table header produces edit in the column spec's TextOnlyEdit`() {
        val old = editedLetter {
            table {
                header { colSpec { literal(text = "hello world") } }
                row { cell { literal(text = "body") } }
            }
        }
        val new = editedLetter {
            table {
                header { colSpec { literal(text = "hello goodbye") } }
                row { cell { literal(text = "body") } }
            }
        }
        val diff = wordDiff.unifiedDiff(old, new)
        assertEquals(
            mapOf(0 to tableEdit(columnSpecEdits = mapOf(0 to textOnlyEdit(inserts = listOf(TextSegment(6, 13)), deletes = listOf(DeletedTextSegment(6, 11, "world")))))),
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

        assertEquals(mapOf(0 to listOf(old.blocks[0])), diff.deletedBlocks)
        assertEquals(
            mapOf(1 to textBlockEdit(deletes = listOf(DeletedTextSegment(8, 13, "vwxyz")))),
            diff.editedBlocks,
        )
    }

    @Test
    fun `entirely deleted content within a block is embedded in deletedContent at its unified position`() {
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
        val oldContent = (old.blocks[0] as EditV2.Block.Paragraph).content

        assertEquals(
            mapOf(
                0 to textBlockEdit(
                    inserts = listOf(TextSegment(5, 12)),
                    deletes = listOf(DeletedTextSegment(5, 10, "gamma")),
                    deletedContent = mapOf(0 to listOf(oldContent[0], oldContent[1])),
                ),
            ),
            diff.editedBlocks,
        )
    }

    @Test
    fun `entirely deleted content within an item is embedded in deletedContent at its unified position`() {
        val old = editedLetter {
            itemList {
                item {
                    literal(text = "alpha")
                    newLine()
                    literal(text = "beta gamma")
                }
            }
        }
        val new = editedLetter {
            itemList {
                item {
                    literal(text = "beta epsilon")
                }
            }
        }
        val diff = wordDiff.unifiedDiff(old, new)
        val oldItemContent = (old.blocks[0] as EditV2.Block.ItemList).items[0].content

        assertEquals(
            mapOf(
                0 to BlockEdit.ListBlockEdit(
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
            diff.editedBlocks,
        )
    }

    @Test
    fun `entirely deleted content within a table cell is embedded in deletedContent at its unified position`() {
        val old = editedLetter {
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
        val new = editedLetter {
            table {
                header { colSpec { literal(text = "col") } }
                row {
                    cell {
                        literal(text = "beta epsilon")
                    }
                }
            }
        }
        val diff = wordDiff.unifiedDiff(old, new)
        val oldCellContent = (old.blocks[0] as EditV2.Block.Table).rows[0].cells[0].content

        assertEquals(
            mapOf(
                0 to tableEdit(
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
                ),
            ),
            diff.editedBlocks,
        )
    }

    @Test
    fun `succeeding entire container deletions are correctly tracked at their unified positions`() {
        val old = editedLetter {
            title2 { literal(text = "alpha") }
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
        assertThat((diff.editedBlocks[0] as BlockEdit.TextBlockEdit).deletedContent[1])
            .contains((old.blocks[1] as EditV2.Block.Paragraph).content[1])
    }

    @Test
    fun `deletedContent distinguishes a leading delete from a trailing delete relative to the same survivor`() {
        val oldLeading = editedLetter {
            paragraph {
                literal(text = "abc")
                newLine()
                variable(text = "def")
            }
        }
        val oldTrailing = editedLetter {
            paragraph {
                literal(text = "abc")
                variable(text = "def")
                newLine()
            }
        }
        val new = editedLetter {
            paragraph {
                variable(text = "def")
            }
        }

        val leadingDiff = wordDiff.unifiedDiff(oldLeading, new)
        val leadingContent = (oldLeading.blocks[0] as EditV2.Block.Paragraph).content
        assertEquals(
            mapOf(0 to listOf(leadingContent[0], leadingContent[1])),
            (leadingDiff.editedBlocks[0] as BlockEdit.TextBlockEdit).deletedContent,
        )

        val trailingDiff = wordDiff.unifiedDiff(oldTrailing, new)
        val trailingContent = (oldTrailing.blocks[0] as EditV2.Block.Paragraph).content
        assertEquals(
            mapOf(0 to listOf(trailingContent[0]), 1 to listOf(trailingContent[2])),
            (trailingDiff.editedBlocks[0] as BlockEdit.TextBlockEdit).deletedContent,
        )
    }

    @Test
    fun `ItemList changed to NumberedList keeps word level edits in the ListBlockEdit`() {
        val old = editedLetter { itemList(id = 1) { item(id = 2) { literal(id = 3, text = "hello world") } } }
        val new = editedLetter { numberedList(id = 1) { item(id = 2) { literal(id = 3, text = "hello goodbye") } } }
        val diff = wordDiff.unifiedDiff(old, new)
        assertEquals(emptyMap<Int, List<EditV2.Block>>(), diff.deletedBlocks)
        assertEquals(
            mapOf(
                0 to BlockEdit.ListBlockEdit(
                    itemEdits = mapOf(0 to textOnlyEdit(inserts = listOf(TextSegment(6, 13)), deletes = listOf(DeletedTextSegment(6, 11, "world")))),
                    deletedItems = emptyMap(),
                ),
            ),
            diff.editedBlocks,
        )
    }

    @Test
    fun `block changed to a different kind shows the old block as deleted and only inserts in the new block`() {
        val old = editedLetter { paragraph { literal(text = "hello") } }
        val new = editedLetter { itemList { item { literal(text = "world") } } }
        val diff = wordDiff.unifiedDiff(old, new)
        assertEquals(mapOf(0 to listOf(old.blocks[0])), diff.deletedBlocks)
        assertEquals(
            mapOf(
                0 to BlockEdit.ListBlockEdit(
                    itemEdits = mapOf(0 to textOnlyEdit(inserts = listOf(TextSegment(0, 5)))),
                    deletedItems = emptyMap(),
                ),
            ),
            diff.editedBlocks,
        )
    }

    @Test
    fun `inserted list block produces inserts in a ListBlockEdit at its position`() {
        val old = editedLetter {
            paragraph { literal(text = "before") }
            paragraph { literal(text = "after") }
        }
        val new = editedLetter {
            paragraph { literal(text = "before") }
            itemList { item { literal(text = "new item") } }
            paragraph { literal(text = "after") }
        }
        val diff = wordDiff.unifiedDiff(old, new)
        assertEquals(emptyMap<Int, List<EditV2.Block>>(), diff.deletedBlocks)
        assertEquals(
            mapOf(
                1 to BlockEdit.ListBlockEdit(
                    itemEdits = mapOf(0 to textOnlyEdit(inserts = listOf(TextSegment(0, 8)))),
                    deletedItems = emptyMap(),
                ),
            ),
            diff.editedBlocks,
        )
    }

    @Test
    fun `deleted list block is embedded in deletedBlocks at its unified position`() {
        val old = editedLetter {
            paragraph { literal(text = "before") }
            numberedList { item { literal(text = "old item") } }
            paragraph { literal(text = "after") }
        }
        val new = editedLetter {
            paragraph { literal(text = "before") }
            paragraph { literal(text = "after") }
        }
        val diff = wordDiff.unifiedDiff(old, new)
        assertEquals(mapOf(1 to listOf(old.blocks[1])), diff.deletedBlocks)
        assertEquals(emptyMap<Int, BlockEdit>(), diff.editedBlocks)
    }

    @Test
    fun `deleted trailing item is embedded in deletedItems at its unified position`() {
        val old = editedLetter {
            itemList {
                item { literal(text = "first") }
                item { literal(text = "second") }
            }
        }
        val new = editedLetter {
            itemList {
                item { literal(text = "first") }
            }
        }
        val diff = wordDiff.unifiedDiff(old, new)
        assertEquals(
            mapOf(0 to BlockEdit.ListBlockEdit(itemEdits = emptyMap(), deletedItems = mapOf(1 to listOf((old.blocks[0] as EditV2.Block.ItemList).items[1])))),
            diff.editedBlocks,
        )
    }

    @Test
    fun `inserted table row produces inserts in the row's cell edits`() {
        val old = editedLetter {
            table {
                header { colSpec { literal(text = "col") } }
                row { cell { literal(text = "first") } }
            }
        }
        val new = editedLetter {
            table {
                header { colSpec { literal(text = "col") } }
                row { cell { literal(text = "first") } }
                row { cell { literal(text = "second") } }
            }
        }
        val diff = wordDiff.unifiedDiff(old, new)
        assertEquals(
            mapOf(
                0 to tableEdit(
                    rowEdits = mapOf(1 to RowEdit(cellEdits = mapOf(0 to textOnlyEdit(inserts = listOf(TextSegment(0, 6)))), deletedCells = emptyMap())),
                ),
            ),
            diff.editedBlocks,
        )
    }

    @Test
    fun `deleted table row is embedded in deletedRows at its unified position`() {
        val old = editedLetter {
            table {
                header { colSpec { literal(text = "col") } }
                row { cell { literal(text = "first") } }
                row { cell { literal(text = "second") } }
                row { cell { literal(text = "third") } }
            }
        }
        val new = editedLetter {
            table {
                header { colSpec { literal(text = "col") } }
                row { cell { literal(text = "first") } }
                row { cell { literal(text = "third") } }
            }
        }
        val diff = wordDiff.unifiedDiff(old, new)
        assertEquals(
            mapOf(0 to tableEdit(deletedRows = mapOf(1 to listOf((old.blocks[0] as EditV2.Block.Table).rows[1])))),
            diff.editedBlocks,
        )
    }

    private fun textBlockEdit(
        inserts: List<TextSegment> = emptyList(),
        deletes: List<DeletedTextSegment> = emptyList(),
        deletedContent: Map<Int, List<EditV2.Text>> = emptyMap(),
    ): BlockEdit.TextBlockEdit =
        BlockEdit.TextBlockEdit(textEdits = mapOf(0 to TextEdit(inserts, deletes)), deletedContent = deletedContent)

    private fun textOnlyEdit(
        inserts: List<TextSegment> = emptyList(),
        deletes: List<DeletedTextSegment> = emptyList(),
        deletedContent: Map<Int, List<EditV2.Text>> = emptyMap(),
    ): TextOnlyEdit = TextOnlyEdit(textEdits = mapOf(0 to TextEdit(inserts, deletes)), deletedContent = deletedContent)

    private fun tableEdit(
        columnSpecEdits: Map<Int, TextOnlyEdit> = emptyMap(),
        rowEdits: Map<Int, RowEdit> = emptyMap(),
        deletedRows: Map<Int, List<EditV2.Block.Table.Row>> = emptyMap(),
    ): BlockEdit.TableEdit =
        BlockEdit.TableEdit(columnSpecEdits = columnSpecEdits, deletedColumnSpecs = emptyMap(), rowEdits = rowEdits, deletedRows = deletedRows)
}
