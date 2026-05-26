@file:OptIn(InterneDataklasser::class)

package no.nav.pensjon.brev.skribenten.letter

import no.nav.brev.InterneDataklasser
import no.nav.brev.Listetype
import no.nav.pensjon.brev.skribenten.common.diff.Change
import no.nav.pensjon.brev.skribenten.common.diff.shortestEditScript
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.BlockContentIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.BlockIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.ItemContentIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.TableCellContentIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.ItemIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.TableRowIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.TableCellIndex
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

    private fun recordCalls(old: List<Token>, new: List<Token>): List<Pair<ContentIndex, Change<*>>> {
        return tokenizer.parseTokens(shortestEditScript(old, new), object : DiffProducer<List<Pair<ContentIndex, Change<*>>>> {
            private val calls = mutableListOf<Pair<ContentIndex, Change<*>>>()
            override fun block(index: BlockIndex, change: Change<DiffProducer.BlockInfo>) {
                calls.add(index to change)
            }
            override fun itemList(index: BlockContentIndex, change: Change<DiffProducer.ItemListInfo>) {
                calls.add(index to change)
            }
            override fun item(index: ItemIndex, change: Change<DiffProducer.ItemInfo>) {
                calls.add(index to change)
            }
            override fun table(index: BlockContentIndex, change: Change<DiffProducer.TableInfo>) {
                calls.add(index to change)
            }
            override fun row(index: TableRowIndex, change: Change<DiffProducer.RowInfo>) {
                calls.add(index to change)
            }
            override fun cell(index: TableCellIndex, change: Change<DiffProducer.CellInfo>) {
                calls.add(index to change)
            }
            override fun textSegment(change: Change<DiffProducer.TextSegment>) {
                val index = when (change) {
                    is Change.Insert -> change.new.index
                    is Change.Delete -> change.old.index
                    is Change.Replace -> change.new.index
                }
                calls.add(index to change)
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
                BlockIndex(0) to Change.Insert(DiffProducer.BlockInfo(null, PARAGRAPH)),
                BlockContentIndex(0, 0) to Change.Insert(DiffProducer.TextSegment(BlockContentIndex(0, 0), 0, 5, "hello")),
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
                BlockIndex(0) to Change.Delete(DiffProducer.BlockInfo(null, PARAGRAPH)),
                BlockContentIndex(0, 0) to Change.Delete(DiffProducer.TextSegment(BlockContentIndex(0, 0), 0, 5, "hello")),
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
                BlockContentIndex(0, 0) to Change.Insert(DiffProducer.TextSegment(BlockContentIndex(0, 0), 6, 13, "goodbye")),
                BlockContentIndex(0, 0) to Change.Delete(DiffProducer.TextSegment(BlockContentIndex(0, 0), 6, 11, "world")),
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
                BlockContentIndex(0, 0) to Change.Insert(DiffProducer.ItemListInfo(null, Listetype.PUNKTLISTE)),
                ContentIndex.ItemIndex(0, 0, 0) to Change.Insert(DiffProducer.ItemInfo(null)),
                ItemContentIndex(0, 0, 0, 0) to Change.Insert(DiffProducer.TextSegment(ItemContentIndex(0, 0, 0, 0), 0, 5, "hello")),
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
                ContentIndex.ItemIndex(0, 0, 1) to Change.Insert(DiffProducer.ItemInfo(null)),
                ItemContentIndex(0, 0, 1, 0) to Change.Insert(DiffProducer.TextSegment(ItemContentIndex(0, 0, 1, 0), 0, 5, "world")),
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
                BlockContentIndex(0, 0) to Change.Insert(DiffProducer.TableInfo(null)),
                ContentIndex.TableCellIndex(0, 0, -1, 0) to Change.Insert(DiffProducer.CellInfo(null)),
                TableCellContentIndex(0, 0, -1, 0, 0) to Change.Insert(DiffProducer.TextSegment(TableCellContentIndex(0, 0, -1, 0, 0), 0, 6, "header")),
                ContentIndex.TableRowIndex(0, 0, 0) to Change.Insert(DiffProducer.RowInfo(null)),
                ContentIndex.TableCellIndex(0, 0, 0, 0) to Change.Insert(DiffProducer.CellInfo(null)),
                TableCellContentIndex(0, 0, 0, 0, 0) to Change.Insert(DiffProducer.TextSegment(TableCellContentIndex(0, 0, 0, 0, 0), 0, 4, "body")),
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
                ContentIndex.TableRowIndex(0, 0, 1) to Change.Insert(DiffProducer.RowInfo(null)),
                ContentIndex.TableCellIndex(0, 0, 1, 0) to Change.Insert(DiffProducer.CellInfo(null)),
                TableCellContentIndex(0, 0, 1, 0, 0) to Change.Insert(DiffProducer.TextSegment(TableCellContentIndex(0, 0, 1, 0, 0), 0, 5, "extra")),
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
                ContentIndex.TableCellIndex(0, 0, 0, 1) to Change.Insert(DiffProducer.CellInfo(null)),
                TableCellContentIndex(0, 0, 0, 1, 0) to Change.Insert(DiffProducer.TextSegment(TableCellContentIndex(0, 0, 0, 1, 0), 0, 5, "extra")),
            ),
            recordCalls(old, new),
        )
    }

    @Test
    fun `parseTokens inserted NewLine between unchanged words does not produce spurious text changes`() {
        // Old: one literal with "hello world"
        // New: two literals separated by a NewLine, same words
        // Expected: no textSegment calls (words are unchanged), only silent structural changes
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
            emptyList<Pair<ContentIndex, Change<*>>>(),
            recordCalls(old, new),
        )
    }

    @Test
    fun `parseTokens inserted NewLine with changed word after does not misalign cursors`() {
        // Old: "hello world" in one literal
        // New: "hello" + NewLine + "goodbye" (word changed after inserted NewLine)
        // The word "hello" is unchanged, NewLine is inserted, "world"->"goodbye" is a change
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
        val calls = recordCalls(old, new)
        assertEquals(
            listOf(
                // "world" deleted at content index 0 (first Text in old), offset 6-11
                BlockContentIndex(0, 0) to Change.Delete(DiffProducer.TextSegment(BlockContentIndex(0, 0), 6, 11, "world")),
                // "goodbye" inserted at content index 2 (third BlockContent: the second Text), offset 0-7
                BlockContentIndex(0, 2) to Change.Insert(DiffProducer.TextSegment(BlockContentIndex(0, 2), 0, 7, "goodbye")),
            ),
            calls,
        )
    }

    @Test
    fun `parseTokens deleted NewLine merging two literals with word change produces correct diff`() {
        // Old: "hello" + NewLine + "world" (two literals)
        // New: "hello goodbye" (single literal, word changed)
        // The word "hello" is unchanged, NewLine is deleted, "world"->"goodbye" is a change
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
        val calls = recordCalls(old, new)
        assertEquals(
            listOf(
                // "goodbye" inserted at content index 0 (first Text in new), offset 6-13
                BlockContentIndex(0, 0) to Change.Insert(DiffProducer.TextSegment(BlockContentIndex(0, 0), 6, 13, "goodbye")),
                // "world" deleted at content index 2 (third BlockContent in old: second Text), offset 0-5
                BlockContentIndex(0, 2) to Change.Delete(DiffProducer.TextSegment(BlockContentIndex(0, 2), 0, 5, "world")),
            ),
            calls,
        )
    }

    @Test
    fun `parseTokens inserted block with word shared with following block does not misalign word consumption`() {
        // This test exposes a bug in consumeIf's || branch: when a word token exists on only
        // one cursor side without an edit marker, it gets consumed as "Unchanged" from just that
        // side, desynchronizing the cursors and producing incorrect (missing) diff output.
        //
        // Old: [Block, Text, "aaa", "bbb"]
        // New: [Block, Text, "aaa", Block, Text, "bbb"]
        //
        // The SES sees "aaa" and "bbb" as unchanged in the flat token list (matching across
        // block boundaries). After the first Block is consumed as Unchanged, the nested block
        // parser for block 0 processes words: "aaa" is unchanged on both sides. But "bbb" is
        // only on the delete side (insert cursor is at the second Block token, not a Word).
        // The || branch consumes "bbb" from only the delete side as "Unchanged", losing the
        // fact that "bbb" was deleted from old-block-0.
        //
        // Similarly, inside the inserted block 1, "bbb" on the insert side gets consumed as
        // "Unchanged" from only the insert side, losing the insert information.
        //
        // Correct behavior: "bbb" should be reported as deleted from old-block-0 and
        // inserted in new-block-1.
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
        val calls = recordCalls(old, new)
        assertEquals(
            listOf(
                BlockIndex(1) to Change.Insert(DiffProducer.BlockInfo(null, PARAGRAPH)),
                BlockContentIndex(1, 0) to Change.Delete(DiffProducer.TextSegment(BlockContentIndex(1, 0), 4, 7, "ccc")),
            ),
            calls,
        )
    }
}
