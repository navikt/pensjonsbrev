@file:OptIn(InterneDataklasser::class)

package no.nav.pensjon.brev.skribenten.letter

import no.nav.brev.InterneDataklasser
import no.nav.brev.Listetype
import no.nav.pensjon.brev.skribenten.common.EqualityBy
import no.nav.pensjon.brev.skribenten.common.diff.EditOperation
import no.nav.pensjon.brev.skribenten.common.diff.EditScript
import no.nav.pensjon.brev.skribenten.common.diff.EditScriptCursor
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.BlockContentIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.ItemContentIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.TableCellContentIndex
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.FontType

class EditLetterWordDiff : EditLetterDiff<EditLetterWordDiff.Token> {

    override fun tokenize(letter: Edit.Letter): List<Token> = object : EditLetterVisitor<Token>(letter) {

        override fun visit(block: Edit.Block) {
            emit(Token.Block(block.id, block.type))
            block.content.forEach { visit(it) }
        }

        override fun visit(content: Edit.ParagraphContent.Text.Literal) {
            emit(Token.Text.Literal(content.id, content.editedFontType ?: content.fontType))
            emitWords(content.editedText ?: content.text)
        }

        override fun visit(content: Edit.ParagraphContent.Text.Variable) {
            emit(Token.Text.Variable(content.id, content.fontType))
            emitWords(content.text)
        }

        override fun visit(content: Edit.ParagraphContent.Text.NewLine) {
            emit(Token.NewLine(content.id))
        }

        override fun visit(itemList: Edit.ParagraphContent.ItemList) {
            emit(Token.ItemList(itemList.id, itemList.listType))
            itemList.items.forEach { visit(it) }
        }

        override fun visit(item: Edit.ParagraphContent.ItemList.Item) {
            emit(Token.Item(item.id))
            item.content.forEach { visit(it) }
        }

        override fun visit(table: Edit.ParagraphContent.Table) {
            emit(Token.Table(table.id))
            visit(table.header)
            table.rows.forEach { visit(it) }
        }

        override fun visit(header: Edit.ParagraphContent.Table.Header) {
            emit(Token.TableHeader(header.id))
            header.colSpec.forEach { visit(it) }
        }

        override fun visit(colSpec: Edit.ParagraphContent.Table.ColumnSpec) {
            emit(Token.ColumnSpec(colSpec.id, colSpec.alignment, colSpec.span))
            visit(colSpec.headerContent)
        }

        override fun visit(row: Edit.ParagraphContent.Table.Row) {
            emit(Token.Row(row.id))
            row.cells.forEach { visit(it) }
        }

        override fun visit(cell: Edit.ParagraphContent.Table.Cell) {
            emit(Token.Cell(cell.id))
            cell.text.forEach { visit(it) }
        }

        private fun emitWords(text: String) =
            text.split(' ').forEach { word -> emit(Token.Word(word)) }

    }.build()

    override fun generateDiffSegments(editScript: EditScript<Token>): Pair<List<DiffSegment>, List<DiffSegment>> = Pair(
        generateDiffSegments(editScript.new, editScript.inserts),
        generateDiffSegments(editScript.old, editScript.deletes),
    )

    private fun generateDiffSegments(tokens: List<Token>, edits: List<EditOperation<Token>>): List<DiffSegment> {
        val cursor = EditScriptCursor(tokens, edits)
        return cursor.flatMapIndexed<Token.Block, DiffSegment> { blockIndex, _, _ ->
            BlockParser(blockIndex, cursor).parse()
        }.also { require(!cursor.hasNext) { "Not all tokens were consumed, next: ${cursor.peek()}" } }
    }

    // Equality intentionally excludes id so that it doesn't affect the diff algorithm
    sealed interface Token {
        class Block(val id: Int?, val type: Edit.Block.Type) : Token, EqualityBy<Block>(Block::type)

        sealed interface BlockContent : Token
        sealed interface TextContent : BlockContent

        class ItemList(val id: Int?, val listType: Listetype) : BlockContent, EqualityBy<ItemList>(ItemList::listType)
        class Item(val id: Int?) : Token, EqualityBy<Item>()

        class Table(val id: Int?) : BlockContent, EqualityBy<Table>()
        class TableHeader(val id: Int?) : Token, EqualityBy<TableHeader>()
        class ColumnSpec(val id: Int?, val alignment: Edit.ParagraphContent.Table.ColumnAlignment, val span: Int) : Token, EqualityBy<ColumnSpec>(ColumnSpec::alignment, ColumnSpec::span)
        class Row(val id: Int?) : Token, EqualityBy<Row>()
        class Cell(val id: Int?) : Token, EqualityBy<Cell>()

        sealed class Text : TextContent, EqualityBy<Text>(Text::fontType) {
            abstract val id: Int?
            abstract val fontType: FontType

            class Literal(override val id: Int?, override val fontType: FontType) : Text()
            class Variable(override val id: Int?, override val fontType: FontType) : Text()
        }

        class NewLine(val id: Int?) : TextContent, EqualityBy<NewLine>()
        data class Word(val word: String) : Token
    }

    // This is a general block parser which will not fail for Title-blocks that contain invalid content types.
    private class BlockParser(
        private val blockIndex: Int,
        private val cursor: EditScriptCursor<Token>,
    ) {
        fun parse(): List<DiffSegment> =
            cursor.flatMapIndexed<Token.BlockContent, DiffSegment> { currentPosition, token, _ ->
                when (token) {
                    is Token.Text -> consumeText(BlockContentIndex(blockIndex, currentPosition))
                    is Token.NewLine -> emptyList()
                    is Token.ItemList -> consumeItemList(currentPosition)
                    is Token.Table -> consumeTable(currentPosition)
                }
            }

        private fun consumeText(contentIndex: ContentIndex): List<DiffSegment> {
            data class State(val textLength: Int = 0, val currentDiff: DiffSegment? = null, val completed: List<DiffSegment> = emptyList())

            return cursor.fold<Token.Word, State>(State()) { state, current, edit ->
                val toAppend = " ${current.word}"
                val newLength = state.textLength + toAppend.length
                if (edit != null) {
                    when {
                        state.currentDiff == null ->
                            State(newLength, DiffSegment(index = contentIndex, startOffset = state.textLength, endOffset = newLength), state.completed)
                        state.currentDiff.endOffset == state.textLength ->
                            State(newLength, state.currentDiff.copy(endOffset = newLength), state.completed)
                        else ->
                            State(newLength, DiffSegment(index = contentIndex, startOffset = state.textLength, endOffset = newLength), state.completed + state.currentDiff)
                    }
                } else {
                    state.copy(textLength = newLength)
                }
            }.let { it.completed + listOfNotNull(it.currentDiff) }
        }

        private fun consumeItemList(listContentIndex: Int): List<DiffSegment> =
            cursor.flatMapIndexed<Token.Item, DiffSegment> { itemIndex, _, _ ->
                consumeTextOnlyContent { ItemContentIndex(blockIndex, listContentIndex, itemIndex, it) }
            }

        private fun consumeTable(tableContentIndex: Int): List<DiffSegment> =
            consumeTableHeader(tableContentIndex) + cursor.flatMapIndexed<Token.Row, DiffSegment> { rowIndex, _, _ ->
                consumeRow(tableContentIndex, rowIndex)
            }

        private fun consumeTableHeader(tableContentIndex: Int): List<DiffSegment> {
            cursor.requireAndConsume<Token.TableHeader>()
            return cursor.flatMapIndexed<Token.ColumnSpec, DiffSegment> { cellIndex, _, _ ->
                cursor.requireAndConsume<Token.Cell>()
                consumeCell(tableContentIndex, -1, cellIndex)
            }
        }

        private fun consumeRow(tableContentIndex: Int, rowIndex: Int): List<DiffSegment> =
            cursor.flatMapIndexed<Token.Cell, DiffSegment> { cellIndex, _, _ ->
                consumeCell(tableContentIndex, rowIndex, cellIndex)
            }

        private fun consumeCell(tableContentIndex: Int, rowIndex: Int, cellIndex: Int): List<DiffSegment> =
            consumeTextOnlyContent { TableCellContentIndex(blockIndex, tableContentIndex, rowIndex, cellIndex, it) }

        private fun consumeTextOnlyContent(makeIndex: (Int) -> ContentIndex): List<DiffSegment> =
            cursor.flatMapIndexed<Token.TextContent, DiffSegment> { currentPosition, token, _ ->
                when (token) {
                    is Token.Text -> consumeText(makeIndex(currentPosition))
                    is Token.NewLine -> emptyList()
                }
            }
    }
}
