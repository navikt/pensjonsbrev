@file:OptIn(InterneDataklasser::class)

package no.nav.pensjon.brev.skribenten.letter

import no.nav.brev.InterneDataklasser
import no.nav.brev.Listetype
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.BlockContentIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.ItemContentIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.TableCellContentIndex
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Type.PARAGRAPH
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Table.ColumnAlignment.LEFT
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.skribenten.letter.EditLetterWordDiff.Token
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EditLetterWordDiffTest {

    private val wordDiff = EditLetterWordDiff()

    // --- Tokenize ---

    @Test
    fun `tokenize paragraph with literal produces Block, Text_Literal, and Word tokens`() {
        val tokens = wordDiff.tokenize(letter(paragraph(literal(text = "hello world")))).toList()
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
        val tokens = wordDiff.tokenize(letter(paragraph(literal(text = "original", editedText = "edited")))).toList()
        assertEquals(
            listOf(Token.Block(null, PARAGRAPH), Token.Text.Literal(null, FontType.PLAIN), Token.Word("edited")),
            tokens
        )
    }

    @Test
    fun `tokenize uses editedFontType instead of fontType for Literal token`() {
        val tokens = wordDiff.tokenize(
            letter(paragraph(literal(text = "hello", fontType = FontType.PLAIN, editedFontType = FontType.BOLD)))
        ).toList()
        assertEquals(
            listOf(Token.Block(null, PARAGRAPH), Token.Text.Literal(null, FontType.BOLD), Token.Word("hello")),
            tokens
        )
    }

    @Test
    fun `tokenize Variable produces Variable token with fontType`() {
        val tokens = wordDiff.tokenize(letter(paragraph(variable(text = "val", fontType = FontType.ITALIC)))).toList()
        assertEquals(
            listOf(Token.Block(null, PARAGRAPH), Token.Text.Variable(null, FontType.ITALIC), Token.Word("val")),
            tokens
        )
    }

    @Test
    fun `tokenize NewLine produces NewLine token between literals`() {
        val tokens = wordDiff.tokenize(letter(paragraph(literal(text = "a"), newLine(), literal(text = "b")))).toList()
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
        val tokens = wordDiff.tokenize(letter(
            paragraph(itemList(
                item(literal(text = "item one")),
                item(literal(text = "item two")),
            ))
        )).toList()
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
        val tokens = wordDiff.tokenize(letter(
            paragraph(table(
                header(colSpec(headerContent = cell(literal(text = "col header")))),
                row(cell(literal(text = "cell body"))),
            ))
        )).toList()
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

    // --- Diff ---

    @Test
    fun `no change produces empty diff segments`() {
        val letter = letter(paragraph(literal(text = "hello world")))
        val (inserts, deletes) = wordDiff.diff(letter, letter)
        assertEquals(emptyList<DiffSegment>(), inserts)
        assertEquals(emptyList<DiffSegment>(), deletes)
    }

    @Test
    fun `changed first word produces correct BlockContentIndex and offsets`() {
        val old = letter(paragraph(literal(text = "hello world")))
        val new = letter(paragraph(literal(text = "goodbye world")))
        val (inserts, deletes) = wordDiff.diff(old, new)
        // " goodbye" = 8, " hello" = 6
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 0), 0, 8)), inserts)
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 0), 0, 6)), deletes)
    }

    @Test
    fun `changed second word produces correct offsets after first word`() {
        val old = letter(paragraph(literal(text = "hello world")))
        val new = letter(paragraph(literal(text = "hello goodbye")))
        val (inserts, deletes) = wordDiff.diff(old, new)
        // " hello"=6, then " goodbye"=8 → [6,14) and " world"=6 → [6,12)
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 0), 6, 14)), inserts)
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 0), 6, 12)), deletes)
    }

    @Test
    fun `added word produces insert segment and empty delete segment`() {
        val old = letter(paragraph(literal(text = "hello")))
        val new = letter(paragraph(literal(text = "hello world")))
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 0), 6, 12)), inserts)
        assertEquals(emptyList<DiffSegment>(), deletes)
    }

    @Test
    fun `removed word produces delete segment and empty insert segment`() {
        val old = letter(paragraph(literal(text = "hello world")))
        val new = letter(paragraph(literal(text = "hello")))
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(emptyList<DiffSegment>(), inserts)
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 0), 6, 12)), deletes)
    }

    @Test
    fun `consecutive changed words are merged into one DiffSegment`() {
        val old = letter(paragraph(literal(text = "hello world foo")))
        val new = letter(paragraph(literal(text = "goodbye there foo")))
        val (inserts, _) = wordDiff.diff(old, new)
        // " goodbye there" = 14
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 0), 0, 14)), inserts)
    }

    @Test
    fun `non-consecutive changed words produce separate DiffSegments`() {
        val old = letter(paragraph(literal(text = "hello world foo")))
        val new = letter(paragraph(literal(text = "goodbye world bar")))
        val (inserts, _) = wordDiff.diff(old, new)
        // " goodbye"=[0,8), " bar"=[14,18)
        assertEquals(
            listOf(
                DiffSegment(BlockContentIndex(0, 0), 0, 8),
                DiffSegment(BlockContentIndex(0, 0), 14, 18),
            ),
            inserts
        )
    }

    @Test
    fun `changed word in second literal of block uses correct contentIndex and resets offsets`() {
        // Literal(contentIndex=0), NewLine(contentIndex=1), Literal(contentIndex=2)
        val old = letter(paragraph(literal(text = "first"), newLine(), literal(text = "hello world")))
        val new = letter(paragraph(literal(text = "first"), newLine(), literal(text = "hello goodbye")))
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 2), 6, 14)), inserts)
        assertEquals(listOf(DiffSegment(BlockContentIndex(0, 2), 6, 12)), deletes)
    }

    @Test
    fun `changed word in item list produces ItemContentIndex`() {
        val old = letter(paragraph(itemList(item(literal(text = "hello world")))))
        val new = letter(paragraph(itemList(item(literal(text = "hello goodbye")))))
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegment(ItemContentIndex(0, 0, 0, 0), 6, 14)), inserts)
        assertEquals(listOf(DiffSegment(ItemContentIndex(0, 0, 0, 0), 6, 12)), deletes)
    }

    @Test
    fun `changed word in second item produces correct itemIndex`() {
        val old = letter(paragraph(itemList(item(literal(text = "unchanged")), item(literal(text = "hello world")))))
        val new = letter(paragraph(itemList(item(literal(text = "unchanged")), item(literal(text = "hello goodbye")))))
        val (inserts, _) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegment(ItemContentIndex(0, 0, 1, 0), 6, 14)), inserts)
    }

    @Test
    fun `changed word in table body cell produces TableCellContentIndex`() {
        val old = letter(paragraph(table(header(colSpec()), row(cell(literal(text = "hello world"))))))
        val new = letter(paragraph(table(header(colSpec()), row(cell(literal(text = "hello goodbye"))))))
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegment(TableCellContentIndex(0, 0, 0, 0, 0), 6, 14)), inserts)
        assertEquals(listOf(DiffSegment(TableCellContentIndex(0, 0, 0, 0, 0), 6, 12)), deletes)
    }

    @Test
    fun `changed word in table header cell produces TableCellContentIndex with rowIndex -1`() {
        val old = letter(paragraph(table(header(colSpec(headerContent = cell(literal(text = "hello world")))), row(cell()))))
        val new = letter(paragraph(table(header(colSpec(headerContent = cell(literal(text = "hello goodbye")))), row(cell()))))
        val (inserts, deletes) = wordDiff.diff(old, new)
        assertEquals(listOf(DiffSegment(TableCellContentIndex(0, 0, -1, 0, 0), 6, 14)), inserts)
        assertEquals(listOf(DiffSegment(TableCellContentIndex(0, 0, -1, 0, 0), 6, 12)), deletes)
    }

    // --- Helpers ---

    private fun letter(vararg blocks: Edit.Block) = editedLetter(*blocks, fixParentIds = false)

    private fun paragraph(vararg content: Edit.ParagraphContent) =
        Edit.Block.Paragraph(id = null, editable = true, content = content.toList())

    private fun literal(
        id: Int? = null,
        text: String,
        fontType: FontType = FontType.PLAIN,
        editedText: String? = null,
        editedFontType: FontType? = null,
    ) = Edit.ParagraphContent.Text.Literal(id, text, fontType, editedText, editedFontType)

    private fun variable(id: Int? = null, text: String, fontType: FontType = FontType.PLAIN) =
        Edit.ParagraphContent.Text.Variable(id, text, fontType)

    private fun newLine(id: Int? = null) = Edit.ParagraphContent.Text.NewLine(id)

    private fun itemList(vararg items: Edit.ParagraphContent.ItemList.Item) =
        Edit.ParagraphContent.ItemList(id = null, items = items.toList())

    private fun item(vararg content: Edit.ParagraphContent.Text) =
        Edit.ParagraphContent.ItemList.Item(id = null, content = content.toList())

    private fun table(header: Edit.ParagraphContent.Table.Header, vararg rows: Edit.ParagraphContent.Table.Row) =
        Edit.ParagraphContent.Table(id = null, rows = rows.toList(), header = header)

    private fun header(vararg colSpecs: Edit.ParagraphContent.Table.ColumnSpec) =
        Edit.ParagraphContent.Table.Header(id = null, colSpec = colSpecs.toList())

    private fun colSpec(headerContent: Edit.ParagraphContent.Table.Cell = cell()) =
        Edit.ParagraphContent.Table.ColumnSpec(id = null, headerContent = headerContent, alignment = LEFT, span = 1)

    private fun row(vararg cells: Edit.ParagraphContent.Table.Cell) =
        Edit.ParagraphContent.Table.Row(id = null, cells = cells.toList())

    private fun cell(vararg text: Edit.ParagraphContent.Text) =
        Edit.ParagraphContent.Table.Cell(id = null, text = text.toList())
}
