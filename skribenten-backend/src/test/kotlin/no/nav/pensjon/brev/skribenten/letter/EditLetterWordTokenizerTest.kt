@file:OptIn(InterneDataklasser::class)

package no.nav.pensjon.brev.skribenten.letter

import no.nav.brev.InterneDataklasser
import no.nav.brev.Listetype
import no.nav.pensjon.brev.skribenten.common.diff.Change
import no.nav.pensjon.brev.skribenten.common.diff.shortestEditScript
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.BlockContentIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.ItemContentIndex
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

    private fun recordChanges(old: List<Token>, new: List<Token>): List<Pair<ContentIndex, Change<*>>> {
        return tokenizer.parseTokens(shortestEditScript(old, new), object : DiffProducer<List<Pair<ContentIndex, Change<*>>>> {
            private val changes = mutableListOf<Pair<ContentIndex, Change<*>>>()
            override fun block(blockIndex: Int, change: Change<DiffProducer.BlockInfo>) {
                changes.add(ContentIndex.BlockIndex(blockIndex) to change)
            }
            override fun itemList(blockIndex: Int, contentIndex: Int, change: Change<DiffProducer.ItemListInfo>) {
                changes.add(BlockContentIndex(blockIndex, contentIndex) to change)
            }
            override fun textSegment(change: Change<DiffProducer.TextSegment>) {
                val index = when (change) {
                    is Change.Insert -> change.new.index
                    is Change.Delete -> change.old.index
                    is Change.Replace -> change.new.index
                }
                changes.add(index to change)
            }
            override fun build() = changes.toList()
        })
    }

    @Test
    fun `parseTokens inserted block calls block with BlockIndex and Change Insert`() {
        val old = listOf<Token>()
        val new = listOf(
            Token.Block(null, PARAGRAPH),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("hello"),
        )
        assertEquals(
            listOf(
                ContentIndex.BlockIndex(0) to Change.Insert(DiffProducer.BlockInfo(null, PARAGRAPH)),
                BlockContentIndex(0, 0) to Change.Insert(DiffProducer.TextSegment(BlockContentIndex(0, 0), 0, 5)),
            ),
            recordChanges(old, new),
        )
    }

    @Test
    fun `parseTokens deleted block calls block with BlockIndex and Change Delete`() {
        val old = listOf(
            Token.Block(null, PARAGRAPH),
            Token.Text.Literal(null, FontType.PLAIN),
            Token.Word("hello"),
        )
        val new = listOf<Token>()
        assertEquals(
            listOf(
                ContentIndex.BlockIndex(0) to Change.Delete(DiffProducer.BlockInfo(null, PARAGRAPH)),
                BlockContentIndex(0, 0) to Change.Delete(DiffProducer.TextSegment(BlockContentIndex(0, 0), 0, 5)),
            ),
            recordChanges(old, new),
        )
    }

    @Test
    fun `parseTokens unchanged block does not call block but changed word still produces textSegment`() {
        val tokens = listOf(
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
                BlockContentIndex(0, 0) to Change.Insert(DiffProducer.TextSegment(BlockContentIndex(0, 0), 6, 13)),
                BlockContentIndex(0, 0) to Change.Delete(DiffProducer.TextSegment(BlockContentIndex(0, 0), 6, 11)),
            ),
            recordChanges(tokens, new),
        )
    }

    @Test
    fun `parseTokens inserted itemList calls itemList with BlockContentIndex and Change Insert`() {
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
                ItemContentIndex(0, 0, 0, 0) to Change.Insert(DiffProducer.TextSegment(ItemContentIndex(0, 0, 0, 0), 0, 5)),
            ),
            recordChanges(old, new),
        )
    }
}
