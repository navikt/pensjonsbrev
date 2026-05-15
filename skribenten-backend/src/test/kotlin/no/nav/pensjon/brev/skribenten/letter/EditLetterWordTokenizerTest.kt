@file:OptIn(InterneDataklasser::class)

package no.nav.pensjon.brev.skribenten.letter

import no.nav.brev.InterneDataklasser
import no.nav.brev.Listetype
import no.nav.pensjon.brev.skribenten.common.diff.Change
import no.nav.pensjon.brev.skribenten.common.diff.shortestEditScript
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.BlockContentIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.ItemContentIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.TableCellContentIndex
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Type.PARAGRAPH
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Table.ColumnAlignment.LEFT
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.skribenten.letter.EditLetterWordTokenizer.Token
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EditLetterWordTokenizerTest {

    private val tokenizer = EditLetterWordTokenizer()
    private val wordDiff = EditLetterWordDiff()

    // --- tokenize ---

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
        val tokens = wordDiff.tokenize(editedLetter { paragraph { literal(text = "a"); newLine(); literal(text = "b") } })
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
                paragraph {
                    itemList {
                        item { literal(text = "item one") }
                        item { literal(text = "item two") }
                    }
                }
            }
        )
        assertEquals(
            listOf(
                Token.Block(null, PARAGRAPH),
                Token.ItemList(null, Listetype.PUNKTLISTE),
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
    fun `tokenize Table produces correct token sequence including header`() {
        val tokens = wordDiff.tokenize(
            editedLetter {
                paragraph {
                    table {
                        header { colSpec { literal(text = "col header") } }
                        row { cell { literal(text = "cell body") } }
                    }
                }
            }
        )
        assertEquals(
            listOf(
                Token.Block(null, PARAGRAPH),
                Token.Table(null),
                Token.TableHeader(null),
                Token.ColumnSpec(null, LEFT, 1),
                Token.Cell(null),
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

    // --- parseTokens ---

    private sealed class ProducerCall {
        data class Block(val blockIndex: Int, val change: Change<DiffProducer.BlockInfo>) : ProducerCall()
        data class ItemList(val blockIndex: Int, val contentIndex: Int, val change: Change<DiffProducer.ItemListInfo>) : ProducerCall()
        data class Item(val blockIndex: Int, val contentIndex: Int, val itemIndex: Int, val change: Change<DiffProducer.ItemInfo>) : ProducerCall()
        data class Table(val blockIndex: Int, val contentIndex: Int, val change: Change<DiffProducer.TableInfo>) : ProducerCall()
        data class Row(val blockIndex: Int, val contentIndex: Int, val rowIndex: Int, val change: Change<DiffProducer.RowInfo>) : ProducerCall()
        data class Cell(val blockIndex: Int, val contentIndex: Int, val rowIndex: Int, val cellIndex: Int, val change: Change<DiffProducer.CellInfo>) : ProducerCall()
        data class TextSegment(val change: Change<DiffProducer.TextSegment>) : ProducerCall()
    }

    private fun recordCalls(old: List<Token>, new: List<Token>): List<ProducerCall> {
        return tokenizer.parseTokens(shortestEditScript(old, new), object : DiffProducer<List<ProducerCall>> {
            private val calls = mutableListOf<ProducerCall>()
            override fun block(blockIndex: Int, change: Change<DiffProducer.BlockInfo>) {
                calls.add(ProducerCall.Block(blockIndex, change))
            }
            override fun itemList(blockIndex: Int, contentIndex: Int, change: Change<DiffProducer.ItemListInfo>) {
                calls.add(ProducerCall.ItemList(blockIndex, contentIndex, change))
            }
            override fun item(blockIndex: Int, contentIndex: Int, itemIndex: Int, change: Change<DiffProducer.ItemInfo>) {
                calls.add(ProducerCall.Item(blockIndex, contentIndex, itemIndex, change))
            }
            override fun table(blockIndex: Int, contentIndex: Int, change: Change<DiffProducer.TableInfo>) {
                calls.add(ProducerCall.Table(blockIndex, contentIndex, change))
            }
            override fun row(blockIndex: Int, contentIndex: Int, rowIndex: Int, change: Change<DiffProducer.RowInfo>) {
                calls.add(ProducerCall.Row(blockIndex, contentIndex, rowIndex, change))
            }
            override fun cell(blockIndex: Int, contentIndex: Int, rowIndex: Int, cellIndex: Int, change: Change<DiffProducer.CellInfo>) {
                calls.add(ProducerCall.Cell(blockIndex, contentIndex, rowIndex, cellIndex, change))
            }
            override fun textSegment(change: Change<DiffProducer.TextSegment>) {
                calls.add(ProducerCall.TextSegment(change))
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
                ProducerCall.Block(0, Change.Insert(DiffProducer.BlockInfo(null, PARAGRAPH))),
                ProducerCall.TextSegment(Change.Insert(DiffProducer.TextSegment(BlockContentIndex(0, 0), 0, 5))),
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
                ProducerCall.Block(0, Change.Delete(DiffProducer.BlockInfo(null, PARAGRAPH))),
                ProducerCall.TextSegment(Change.Delete(DiffProducer.TextSegment(BlockContentIndex(0, 0), 0, 5))),
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
                ProducerCall.TextSegment(Change.Insert(DiffProducer.TextSegment(BlockContentIndex(0, 0), 6, 13))),
                ProducerCall.TextSegment(Change.Delete(DiffProducer.TextSegment(BlockContentIndex(0, 0), 6, 11))),
            ),
            recordCalls(old, new),
        )
    }

    @Test
    fun `parseTokens inserted itemList calls itemList producer`() {
        val old = listOf(
            Token.Block(null, PARAGRAPH),
        )
        val new = listOf(
            Token.Block(null, PARAGRAPH),
            Token.ItemList(null, Listetype.PUNKTLISTE),
            Token.Item(null),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("hello"),
        )
        assertEquals(
            listOf(
                ProducerCall.ItemList(0, 0, Change.Insert(DiffProducer.ItemListInfo(null, Listetype.PUNKTLISTE))),
                ProducerCall.Item(0, 0, 0, Change.Insert(DiffProducer.ItemInfo(null))),
                ProducerCall.TextSegment(Change.Insert(DiffProducer.TextSegment(ItemContentIndex(0, 0, 0, 0), 0, 5))),
            ),
            recordCalls(old, new),
        )
    }

    @Test
    fun `parseTokens inserted item in unchanged itemList calls item producer`() {
        val old = listOf(
            Token.Block(null, PARAGRAPH),
            Token.ItemList(null, Listetype.PUNKTLISTE),
            Token.Item(null),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("hello"),
        )
        val new = listOf(
            Token.Block(null, PARAGRAPH),
            Token.ItemList(null, Listetype.PUNKTLISTE),
            Token.Item(null),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("hello"),
            Token.Item(null),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("world"),
        )
        assertEquals(
            listOf(
                ProducerCall.Item(0, 0, 1, Change.Insert(DiffProducer.ItemInfo(null))),
                ProducerCall.TextSegment(Change.Insert(DiffProducer.TextSegment(ItemContentIndex(0, 0, 1, 0), 0, 5))),
            ),
            recordCalls(old, new),
        )
    }

    @Test
    fun `parseTokens inserted table calls table, cell, row and cell producers`() {
        val old = listOf(
            Token.Block(null, PARAGRAPH),
        )
        val new = listOf(
            Token.Block(null, PARAGRAPH),
            Token.Table(null),
            Token.TableHeader(null),
            Token.ColumnSpec(null, LEFT, 1),
            Token.Cell(null),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("header"),
            Token.Row(null),
            Token.Cell(null),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("body"),
        )
        assertEquals(
            listOf(
                ProducerCall.Table(0, 0, Change.Insert(DiffProducer.TableInfo(null))),
                ProducerCall.Cell(0, 0, -1, 0, Change.Insert(DiffProducer.CellInfo(null))),
                ProducerCall.TextSegment(Change.Insert(DiffProducer.TextSegment(TableCellContentIndex(0, 0, -1, 0, 0), 0, 6))),
                ProducerCall.Row(0, 0, 0, Change.Insert(DiffProducer.RowInfo(null))),
                ProducerCall.Cell(0, 0, 0, 0, Change.Insert(DiffProducer.CellInfo(null))),
                ProducerCall.TextSegment(Change.Insert(DiffProducer.TextSegment(TableCellContentIndex(0, 0, 0, 0, 0), 0, 4))),
            ),
            recordCalls(old, new),
        )
    }

    @Test
    fun `parseTokens inserted row in unchanged table calls row and cell producers`() {
        val tableHeaderAndFirstRow = listOf(
            Token.TableHeader(null),
            Token.ColumnSpec(null, LEFT, 1),
            Token.Cell(null),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("col"),
            Token.Row(null),
            Token.Cell(null),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("body"),
        )
        val old = listOf(Token.Block(null, PARAGRAPH), Token.Table(null)) + tableHeaderAndFirstRow
        val new = old + listOf(
            Token.Row(null),
            Token.Cell(null),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("extra"),
        )
        assertEquals(
            listOf(
                ProducerCall.Row(0, 0, 1, Change.Insert(DiffProducer.RowInfo(null))),
                ProducerCall.Cell(0, 0, 1, 0, Change.Insert(DiffProducer.CellInfo(null))),
                ProducerCall.TextSegment(Change.Insert(DiffProducer.TextSegment(TableCellContentIndex(0, 0, 1, 0, 0), 0, 5))),
            ),
            recordCalls(old, new),
        )
    }

    @Test
    fun `parseTokens inserted cell in unchanged row calls cell producer`() {
        val old = listOf(
            Token.Block(null, PARAGRAPH),
            Token.Table(null),
            Token.TableHeader(null),
            Token.ColumnSpec(null, LEFT, 1),
            Token.Cell(null),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("col"),
            Token.Row(null),
            Token.Cell(null),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("body"),
        )
        val new = old + listOf(
            Token.Cell(null),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("extra"),
        )
        assertEquals(
            listOf(
                ProducerCall.Cell(0, 0, 0, 1, Change.Insert(DiffProducer.CellInfo(null))),
                ProducerCall.TextSegment(Change.Insert(DiffProducer.TextSegment(TableCellContentIndex(0, 0, 0, 1, 0), 0, 5))),
            ),
            recordCalls(old, new),
        )
    }
}
