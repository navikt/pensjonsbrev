package no.nav.pensjon.brev.skribenten.letter.v2

import no.nav.pensjon.brev.skribenten.common.diff.Change
import no.nav.pensjon.brev.skribenten.common.diff.shortestEditScript
import no.nav.pensjon.brev.skribenten.letter.v2.ContentIndexV2.BlockContentIndex
import no.nav.pensjon.brev.skribenten.letter.v2.ContentIndexV2.BlockIndex
import no.nav.pensjon.brev.skribenten.letter.v2.ContentIndexV2.ColumnSpecContentIndex
import no.nav.pensjon.brev.skribenten.letter.v2.ContentIndexV2.ColumnSpecIndex
import no.nav.pensjon.brev.skribenten.letter.v2.ContentIndexV2.ItemContentIndex
import no.nav.pensjon.brev.skribenten.letter.v2.ContentIndexV2.ItemIndex
import no.nav.pensjon.brev.skribenten.letter.v2.ContentIndexV2.TableCellContentIndex
import no.nav.pensjon.brev.skribenten.letter.v2.ContentIndexV2.TableCellIndex
import no.nav.pensjon.brev.skribenten.letter.v2.ContentIndexV2.TableRowIndex
import no.nav.pensjon.brev.skribenten.letter.v2.EditLetterWordTokenizerV2.Token
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2.Block.Table.ColumnAlignment.LEFT
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2.Block.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2.Block.Type.ITEM_LIST
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2.Block.Type.NUMBERED_LIST
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2.Block.Type.PARAGRAPH
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2.Block.Type.TABLE
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2.Block.Type.TITLE2
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2.FontType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EditLetterWordTokenizerV2Test {

    private val tokenizer = EditLetterWordTokenizerV2()
    private val wordDiff = EditLetterWordDiffV2()

    @Test
    fun `tokenize paragraph with literal produces Block, Text_Literal, and Word tokens`() {
        val tokens = wordDiff.tokenize(editedLetter { paragraph { literal(text = "hello world") } })
        assertEquals(
            listOf(
                Token.Block(null, PARAGRAPH),
                Token.Text.Literal(null, FontType.PLAIN),
                Token.Word("hello"),
                Token.Word("world"),
            ),
            tokens
        )
    }

    @Test
    fun `tokenize title2 produces Block token with TITLE2 type`() {
        val tokens = wordDiff.tokenize(editedLetter { title2 { literal(text = "hello") } })
        assertEquals(
            listOf(Token.Block(null, TITLE2), Token.Text.Literal(null, FontType.PLAIN), Token.Word("hello")),
            tokens
        )
    }

    @Test
    fun `tokenize uses editedText instead of text for word tokens`() {
        val tokens = wordDiff.tokenize(editedLetter { paragraph { literal(text = "original", editedText = "edited") } })
        assertEquals(
            listOf(Token.Block(null, PARAGRAPH), Token.Text.Literal(null, FontType.PLAIN), Token.Word("edited")),
            tokens
        )
    }

    @Test
    fun `tokenize uses editedFontType instead of fontType for Literal token`() {
        val tokens = wordDiff.tokenize(
            editedLetter { paragraph { literal(text = "hello", fontType = FontType.PLAIN, editedFontType = FontType.BOLD) } }
        )
        assertEquals(
            listOf(Token.Block(null, PARAGRAPH), Token.Text.Literal(null, FontType.BOLD), Token.Word("hello")),
            tokens
        )
    }

    @Test
    fun `tokenize Variable produces Variable token with fontType`() {
        val tokens = wordDiff.tokenize(editedLetter { paragraph { variable(text = "val", fontType = FontType.ITALIC) } })
        assertEquals(
            listOf(Token.Block(null, PARAGRAPH), Token.Text.Variable(null, FontType.ITALIC), Token.Word("val")),
            tokens
        )
    }

    @Test
    fun `tokenize NewLine produces NewLine token between literals`() {
        val tokens = wordDiff.tokenize(editedLetter {
            paragraph {
                literal(text = "a")
                newLine()
                literal(text = "b")
            }
        })
        assertEquals(
            listOf(
                Token.Block(null, PARAGRAPH),
                Token.Text.Literal(null, FontType.PLAIN),
                Token.Word("a"),
                Token.NewLine(null),
                Token.Text.Literal(null, FontType.PLAIN),
                Token.Word("b"),
            ),
            tokens
        )
    }

    @Test
    fun `tokenize ItemList produces correct token sequence`() {
        val tokens = wordDiff.tokenize(
            editedLetter {
                itemList {
                    item { literal(text = "item one") }
                    item { literal(text = "item two") }
                }
            }
        )
        assertEquals(
            listOf(
                Token.Block(null, ITEM_LIST),
                Token.Item(null),
                Token.Text.Literal(null, FontType.PLAIN),
                Token.Word("item"),
                Token.Word("one"),
                Token.Item(null),
                Token.Text.Literal(null, FontType.PLAIN),
                Token.Word("item"),
                Token.Word("two"),
            ),
            tokens
        )
    }

    @Test
    fun `tokenize NumberedList produces Block token with NUMBERED_LIST type`() {
        val tokens = wordDiff.tokenize(editedLetter { numberedList { item { literal(text = "one") } } })
        assertEquals(
            listOf(
                Token.Block(null, NUMBERED_LIST),
                Token.Item(null),
                Token.Text.Literal(null, FontType.PLAIN),
                Token.Word("one"),
            ),
            tokens
        )
    }

    @Test
    fun `tokenize Table produces correct token sequence including header`() {
        val tokens = wordDiff.tokenize(
            editedLetter {
                table {
                    header { colSpec { literal(text = "col header") } }
                    row { cell { literal(text = "cell body") } }
                }
            }
        )
        assertEquals(
            listOf(
                Token.Block(null, TABLE),
                Token.TableHeader(null),
                Token.ColumnSpec(null, LEFT, 1),
                Token.Text.Literal(null, FontType.PLAIN),
                Token.Word("col"),
                Token.Word("header"),
                Token.Row(null),
                Token.Cell(null),
                Token.Text.Literal(null, FontType.PLAIN),
                Token.Word("cell"),
                Token.Word("body"),
            ),
            tokens
        )
    }

    private fun recordCalls(old: List<Token>, new: List<Token>): List<Triple<ContentIndexV2, ContentIndexV2, Change<*>>> {
        return tokenizer.parseTokens(shortestEditScript(old, new), object : DiffProducerV2<List<Triple<ContentIndexV2, ContentIndexV2, Change<*>>>> {
            private val calls = mutableListOf<Triple<ContentIndexV2, ContentIndexV2, Change<*>>>()
            override fun block(insertIndex: BlockIndex, deleteIndex: BlockIndex, change: Change<DiffProducerV2.BlockInfo>) {
                calls.add(Triple(insertIndex, deleteIndex, change))
            }
            override fun item(insertIndex: ItemIndex, deleteIndex: ItemIndex, change: Change<DiffProducerV2.ItemInfo>) {
                calls.add(Triple(insertIndex, deleteIndex, change))
            }
            override fun columnSpec(insertIndex: ColumnSpecIndex, deleteIndex: ColumnSpecIndex, change: Change<DiffProducerV2.ColumnSpecInfo>) {
                calls.add(Triple(insertIndex, deleteIndex, change))
            }
            override fun row(insertIndex: TableRowIndex, deleteIndex: TableRowIndex, change: Change<DiffProducerV2.RowInfo>) {
                calls.add(Triple(insertIndex, deleteIndex, change))
            }
            override fun cell(insertIndex: TableCellIndex, deleteIndex: TableCellIndex, change: Change<DiffProducerV2.CellInfo>) {
                calls.add(Triple(insertIndex, deleteIndex, change))
            }
            override fun textSegment(insertIndex: ContentIndexV2, deleteIndex: ContentIndexV2, change: Change<DiffProducerV2.TextSegment>) {
                calls.add(Triple(insertIndex, deleteIndex, change))
            }
            override fun build() = calls.toList()
        })
    }

    @Test
    fun `parseTokens inserted block calls block producer`() {
        val old = listOf<Token>()
        val new = listOf(
            Token.Block(null, PARAGRAPH),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("hello"),
        )
        assertEquals(
            listOf(
                Triple(BlockIndex(0), BlockIndex(0), Change.Insert(DiffProducerV2.BlockInfo(null, PARAGRAPH))),
                Triple(BlockContentIndex(0, 0), BlockContentIndex(0, 0), Change.Insert(DiffProducerV2.TextSegment(0, 5, "hello"))),
            ),
            recordCalls(old, new),
        )
    }

    @Test
    fun `parseTokens deleted block calls block producer`() {
        val old = listOf(
            Token.Block(null, PARAGRAPH),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("hello"),
        )
        val new = listOf<Token>()
        assertEquals(
            listOf(
                Triple(BlockIndex(0), BlockIndex(0), Change.Delete(DiffProducerV2.BlockInfo(null, PARAGRAPH))),
            ),
            recordCalls(old, new),
        )
    }

    @Test
    fun `parseTokens unchanged block does not call block producer but changed word still calls textSegment`() {
        val old = listOf(
            Token.Block(null, PARAGRAPH),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("hello"),
            Token.Word("world"),
        )
        val new = listOf(
            Token.Block(null, PARAGRAPH),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("hello"),
            Token.Word("goodbye"),
        )
        assertEquals(
            listOf(
                Triple(BlockContentIndex(0, 0), BlockContentIndex(0, 0), Change.Insert(DiffProducerV2.TextSegment(6, 13, "goodbye"))),
                Triple(BlockContentIndex(0, 0), BlockContentIndex(0, 0), Change.Delete(DiffProducerV2.TextSegment(6, 11, "world"))),
            ),
            recordCalls(old, new),
        )
    }

    @Test
    fun `parseTokens changed block type is reported as block Replace`() {
        val old = listOf(
            Token.Block(null, PARAGRAPH),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("hello"),
        )
        val new = listOf(
            Token.Block(null, TITLE2),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("hello"),
        )
        assertEquals(
            listOf(
                Triple(BlockIndex(0), BlockIndex(0), Change.Replace(DiffProducerV2.BlockInfo(null, PARAGRAPH), DiffProducerV2.BlockInfo(null, TITLE2))),
            ),
            recordCalls(old, new),
        )
    }

    @Test
    fun `parseTokens ItemList changed to NumberedList is reported as block Replace`() {
        val items = listOf(
            Token.Item(null),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("hello"),
        )
        val old = listOf<Token>(Token.Block(1, ITEM_LIST)) + items
        val new = listOf<Token>(Token.Block(1, NUMBERED_LIST)) + items
        assertEquals(
            listOf(
                Triple(BlockIndex(0), BlockIndex(0), Change.Replace(DiffProducerV2.BlockInfo(1, ITEM_LIST), DiffProducerV2.BlockInfo(1, NUMBERED_LIST))),
            ),
            recordCalls(old, new),
        )
    }

    @Test
    fun `parseTokens inserted list block calls block and item producer`() {
        val old = listOf(
            Token.Block(null, PARAGRAPH),
        )
        val new = listOf(
            Token.Block(null, PARAGRAPH),
            Token.Block(null, ITEM_LIST),
            Token.Item(null),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("hello"),
        )
        assertEquals(
            listOf(
                Triple(BlockIndex(1), BlockIndex(1), Change.Insert(DiffProducerV2.BlockInfo(null, ITEM_LIST))),
                Triple(ItemIndex(1, 0), ItemIndex(1, 0), Change.Insert(DiffProducerV2.ItemInfo(null))),
                Triple(ItemContentIndex(1, 0, 0), ItemContentIndex(1, 0, 0), Change.Insert(DiffProducerV2.TextSegment(0, 5, "hello"))),
            ),
            recordCalls(old, new),
        )
    }

    @Test
    fun `parseTokens deleted list block calls block producer only`() {
        val old = listOf(
            Token.Block(null, PARAGRAPH),
            Token.Block(null, ITEM_LIST),
            Token.Item(null),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("hello"),
        )
        val new = listOf(
            Token.Block(null, PARAGRAPH),
        )
        assertEquals(
            listOf(
                Triple(BlockIndex(1), BlockIndex(1), Change.Delete(DiffProducerV2.BlockInfo(null, ITEM_LIST))),
                Triple(ItemIndex(1, 0), ItemIndex(1, 0), Change.Delete(DiffProducerV2.ItemInfo(null))),
            ),
            recordCalls(old, new),
        )
    }

    @Test
    fun `parseTokens inserted item in unchanged itemList calls item producer`() {
        val old = listOf(
            Token.Block(null, ITEM_LIST),
            Token.Item(null),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("hello"),
        )
        val new = listOf(
            Token.Block(null, ITEM_LIST),
            Token.Item(null),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("hello"),
            Token.Item(null),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("world"),
        )
        assertEquals(
            listOf(
                Triple(ItemIndex(0, 1), ItemIndex(0, 1), Change.Insert(DiffProducerV2.ItemInfo(null))),
                Triple(ItemContentIndex(0, 1, 0), ItemContentIndex(0, 1, 0), Change.Insert(DiffProducerV2.TextSegment(0, 5, "world"))),
            ),
            recordCalls(old, new),
        )
    }

    @Test
    fun `parseTokens inserted table calls block, columnSpec, row and cell producers`() {
        val old = listOf(
            Token.Block(null, PARAGRAPH),
        )
        val new = listOf(
            Token.Block(null, PARAGRAPH),
            Token.Block(null, TABLE),
            Token.TableHeader(null),
            Token.ColumnSpec(null, LEFT, 1),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("header"),
            Token.Row(null),
            Token.Cell(null),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("body"),
        )
        assertEquals(
            listOf(
                Triple(BlockIndex(1), BlockIndex(1), Change.Insert(DiffProducerV2.BlockInfo(null, TABLE))),
                Triple(ColumnSpecIndex(1, 0), ColumnSpecIndex(1, 0), Change.Insert(DiffProducerV2.ColumnSpecInfo(null, LEFT, 1))),
                Triple(ColumnSpecContentIndex(1, 0, 0), ColumnSpecContentIndex(1, 0, 0), Change.Insert(DiffProducerV2.TextSegment(0, 6, "header"))),
                Triple(TableRowIndex(1, 0), TableRowIndex(1, 0), Change.Insert(DiffProducerV2.RowInfo(null))),
                Triple(TableCellIndex(1, 0, 0), TableCellIndex(1, 0, 0), Change.Insert(DiffProducerV2.CellInfo(null))),
                Triple(TableCellContentIndex(1, 0, 0, 0), TableCellContentIndex(1, 0, 0, 0), Change.Insert(DiffProducerV2.TextSegment(0, 4, "body"))),
            ),
            recordCalls(old, new),
        )
    }

    @Test
    fun `parseTokens changed column alignment calls columnSpec producer with Replace`() {
        val old = listOf(
            Token.Block(null, TABLE),
            Token.TableHeader(null),
            Token.ColumnSpec(null, LEFT, 1),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("col"),
        )
        val new = listOf(
            Token.Block(null, TABLE),
            Token.TableHeader(null),
            Token.ColumnSpec(null, RIGHT, 1),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("col"),
        )
        assertEquals(
            listOf(
                Triple(ColumnSpecIndex(0, 0), ColumnSpecIndex(0, 0), Change.Replace(DiffProducerV2.ColumnSpecInfo(null, LEFT, 1), DiffProducerV2.ColumnSpecInfo(null, RIGHT, 1))),
            ),
            recordCalls(old, new),
        )
    }

    private val tableHeaderAndFirstRow = listOf(
        Token.Block(null, TABLE),
        Token.TableHeader(null),
        Token.ColumnSpec(null, LEFT, 1),
        Token.Text.Literal(null, FontType.PLAIN),
        Token.Word("col"),
        Token.Row(null),
        Token.Cell(null),
        Token.Text.Literal(null, FontType.PLAIN),
        Token.Word("body"),
    )

    @Test
    fun `parseTokens inserted row in unchanged table calls row and cell producers`() {
        val old = tableHeaderAndFirstRow
        val new = tableHeaderAndFirstRow + listOf(
            Token.Row(null),
            Token.Cell(null),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("extra"),
        )
        assertEquals(
            listOf(
                Triple(TableRowIndex(0, 1), TableRowIndex(0, 1), Change.Insert(DiffProducerV2.RowInfo(null))),
                Triple(TableCellIndex(0, 1, 0), TableCellIndex(0, 1, 0), Change.Insert(DiffProducerV2.CellInfo(null))),
                Triple(TableCellContentIndex(0, 1, 0, 0), TableCellContentIndex(0, 1, 0, 0), Change.Insert(DiffProducerV2.TextSegment(0, 5, "extra"))),
            ),
            recordCalls(old, new),
        )
    }

    @Test
    fun `parseTokens deleted row in unchanged table calls row and cell producers`() {
        val old = tableHeaderAndFirstRow + listOf(
            Token.Row(null),
            Token.Cell(null),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("extra"),
        )
        val new = tableHeaderAndFirstRow
        assertEquals(
            listOf(
                Triple(TableRowIndex(0, 1), TableRowIndex(0, 1), Change.Delete(DiffProducerV2.RowInfo(null))),
                Triple(TableCellIndex(0, 1, 0), TableCellIndex(0, 1, 0), Change.Delete(DiffProducerV2.CellInfo(null))),
            ),
            recordCalls(old, new),
        )
    }

    @Test
    fun `parseTokens inserted cell in unchanged row calls cell producer`() {
        val old = tableHeaderAndFirstRow
        val new = tableHeaderAndFirstRow + listOf(
            Token.Cell(null),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("extra"),
        )
        assertEquals(
            listOf(
                Triple(TableCellIndex(0, 0, 1), TableCellIndex(0, 0, 1), Change.Insert(DiffProducerV2.CellInfo(null))),
                Triple(TableCellContentIndex(0, 0, 1, 0), TableCellContentIndex(0, 0, 1, 0), Change.Insert(DiffProducerV2.TextSegment(0, 5, "extra"))),
            ),
            recordCalls(old, new),
        )
    }

    @Test
    fun `parseTokens inserted NewLine between unchanged words does not produce spurious text changes`() {
        val old = listOf(
            Token.Block(null, PARAGRAPH),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("hello"),
            Token.Word("world"),
        )
        val new = listOf(
            Token.Block(null, PARAGRAPH),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("hello"),
            Token.NewLine(null),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("world"),
        )
        assertEquals(
            emptyList<Triple<ContentIndexV2, ContentIndexV2, Change<*>>>(),
            recordCalls(old, new),
        )
    }

    @Test
    fun `parseTokens inserted NewLine with changed word after does not misalign cursors`() {
        val old = listOf(
            Token.Block(null, PARAGRAPH),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("hello"),
            Token.Word("world"),
        )
        val new = listOf(
            Token.Block(null, PARAGRAPH),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("hello"),
            Token.NewLine(null),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("goodbye"),
        )
        assertEquals(
            listOf(
                Triple(BlockContentIndex(0, 0), BlockContentIndex(0, 0), Change.Delete(DiffProducerV2.TextSegment(6, 11, "world"))),
                Triple(BlockContentIndex(0, 2), BlockContentIndex(0, 0), Change.Insert(DiffProducerV2.TextSegment(0, 7, "goodbye"))),
            ),
            recordCalls(old, new),
        )
    }

    @Test
    fun `parseTokens deleted NewLine merging two literals with word change produces correct diff`() {
        val old = listOf(
            Token.Block(null, PARAGRAPH),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("hello"),
            Token.NewLine(null),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("world"),
        )
        val new = listOf(
            Token.Block(null, PARAGRAPH),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("hello"),
            Token.Word("goodbye"),
        )
        assertEquals(
            listOf(
                Triple(BlockContentIndex(0, 0), BlockContentIndex(0, 0), Change.Insert(DiffProducerV2.TextSegment(6, 13, "goodbye"))),
            ),
            recordCalls(old, new),
        )
    }

    @Test
    fun `parseTokens inserted block with word shared with following block does not misalign word consumption`() {
        val old = listOf(
            Token.Block(null, PARAGRAPH),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("aaa"),
            Token.Word("bbb"),
            Token.Word("ccc"),
        )
        val new = listOf(
            Token.Block(null, PARAGRAPH),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("aaa"),
            Token.Block(null, PARAGRAPH),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("bbb"),
        )
        assertEquals(
            listOf(
                Triple(BlockIndex(1), BlockIndex(1), Change.Insert(DiffProducerV2.BlockInfo(null, PARAGRAPH))),
                Triple(BlockContentIndex(1, 0), BlockContentIndex(1, 0), Change.Delete(DiffProducerV2.TextSegment(4, 7, "ccc"))),
            ),
            recordCalls(old, new),
        )
    }
}
