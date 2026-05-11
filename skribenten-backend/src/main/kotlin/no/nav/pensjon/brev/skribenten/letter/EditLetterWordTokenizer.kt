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

class EditLetterWordTokenizer : EditLetterTokenizer<EditLetterWordTokenizer.Token> {

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

    override fun tokenize(letter: Edit.Letter): List<Token> = Tokenizer(letter).build()

    override fun <R> parseTokens(editScript: EditScript<Token>, producer: DiffProducer<R>): R {
        parseTokenStream(editScript.new, editScript.inserts, producer)
        parseTokenStream(editScript.old, editScript.deletes, producer)
        return producer.build()
    }

    private fun parseTokenStream(tokens: List<Token>, edits: List<EditOperation<Token>>, producer: DiffProducer<*>) {
        val cursor = EditScriptCursor(tokens, edits)
        cursor.forEachIndexed<Token.Block> { blockIndex, block, blockEdit ->
            if (blockEdit != null) {
                producer.block(blockIndex, makeChangeFor(blockEdit, DiffProducer.BlockInfo(block.id, block.type)))
            }
            BlockParser(blockIndex, cursor, producer).parse()
        }
        require(!cursor.hasNext) { "Not all tokens were consumed, next: ${cursor.peek()}" }
    }

    // This is a general block parser which will not fail for Title-blocks that contain invalid content types.
    private class BlockParser(
        private val blockIndex: Int,
        private val cursor: EditScriptCursor<Token>,
        private val producer: DiffProducer<*>,
    ) {
        fun parse() =
            cursor.forEachIndexed<Token.BlockContent> { currentPosition, token, edit ->
                when (token) {
                    is Token.Text -> consumeText(BlockContentIndex(blockIndex, currentPosition))
                    is Token.NewLine -> {}
                    is Token.ItemList -> consumeItemList(currentPosition, token, edit)
                    is Token.Table -> consumeTable(currentPosition, token, edit)
                }
            }

        private fun consumeText(contentIndex: ContentIndex) {
            data class RangeState(
                private val contentIndex: ContentIndex,
                private val current: DiffProducer.TextSegment? = null,
                private val completed: List<DiffProducer.TextSegment> = emptyList(),
            ) {
                fun extend(wordStart: Int, newLength: Int, textLength: Int): RangeState =
                    if (current?.endOffset == textLength)
                        copy(current = current.copy(endOffset = newLength))
                    else
                        copy(
                            current = DiffProducer.TextSegment(contentIndex, wordStart, newLength),
                            completed = completed + listOfNotNull(current),
                        )

                fun segments(): List<DiffProducer.TextSegment> = completed + listOfNotNull(current)
            }

            data class State(
                val textLength: Int = 0,
                val inserts: RangeState = RangeState(contentIndex),
                val deletes: RangeState = RangeState(contentIndex),
            ) {
                fun insert(wordStart: Int, newLength: Int) = copy(textLength = newLength, inserts = inserts.extend(wordStart, newLength, textLength))
                fun delete(wordStart: Int, newLength: Int) = copy(textLength = newLength, deletes = deletes.extend(wordStart, newLength, textLength))
                fun noChange(newLength: Int) = copy(textLength = newLength)
                fun changes() =
                    inserts.segments().map { Change.Insert(it) } +
                    deletes.segments().map { Change.Delete(it) }
            }

            val finalState = cursor.fold<Token.Word, State>(State()) { state, current, edit ->
                val spaceLength = if (state.textLength == 0) 0 else 1
                val wordStart = state.textLength + spaceLength
                val newLength = wordStart + current.word.length
                when (edit) {
                    is EditOperation.Insert -> state.insert(wordStart, newLength)
                    is EditOperation.Delete -> state.delete(wordStart, newLength)
                    null -> state.noChange(newLength)
                }
            }

            finalState.changes().forEach { producer.textSegment(it) }
        }

        private fun consumeItemList(listContentIndex: Int, token: Token.ItemList, edit: EditOperation<*>?) {
            if (edit != null) {
                producer.itemList(blockIndex, listContentIndex, makeChangeFor(edit, DiffProducer.ItemListInfo(token.id, token.listType)))
            }
            cursor.forEachIndexed<Token.Item> { itemIndex, itemToken, itemEdit ->
                if (itemEdit != null) {
                    producer.item(blockIndex, listContentIndex, itemIndex, makeChangeFor(itemEdit, DiffProducer.ItemInfo(itemToken.id)))
                }
                consumeTextOnlyContent { ItemContentIndex(blockIndex, listContentIndex, itemIndex, it) }
            }
        }

        private fun consumeTable(tableContentIndex: Int, token: Token.Table, edit: EditOperation<*>?) {
            if (edit != null) {
                producer.table(blockIndex, tableContentIndex, makeChangeFor(edit, DiffProducer.TableInfo(token.id)))
            }
            consumeTableHeader(tableContentIndex)
            cursor.forEachIndexed<Token.Row> { rowIndex, rowToken, rowEdit ->
                if (rowEdit != null) {
                    producer.row(blockIndex, tableContentIndex, rowIndex, makeChangeFor(rowEdit, DiffProducer.RowInfo(rowToken.id)))
                }
                consumeRow(tableContentIndex, rowIndex)
            }
        }

        private fun consumeTableHeader(tableContentIndex: Int) {
            cursor.requireAndConsume<Token.TableHeader>()
            cursor.forEachIndexed<Token.ColumnSpec> { cellIndex, _, _ ->
                val (cellToken, cellEdit) = cursor.consumeIf<Token.Cell>()
                    ?: error("Expected Token.Cell after Token.ColumnSpec in table header, got: ${cursor.peek()}")
                if (cellEdit != null) {
                    producer.cell(blockIndex, tableContentIndex, -1, cellIndex, makeChangeFor(cellEdit, DiffProducer.CellInfo(cellToken.id)))
                }
                consumeCell(tableContentIndex, -1, cellIndex)
            }
        }

        private fun consumeRow(tableContentIndex: Int, rowIndex: Int) =
            cursor.forEachIndexed<Token.Cell> { cellIndex, cellToken, cellEdit ->
                if (cellEdit != null) {
                    producer.cell(blockIndex, tableContentIndex, rowIndex, cellIndex, makeChangeFor(cellEdit, DiffProducer.CellInfo(cellToken.id)))
                }
                consumeCell(tableContentIndex, rowIndex, cellIndex)
            }

        private fun consumeCell(tableContentIndex: Int, rowIndex: Int, cellIndex: Int) =
            consumeTextOnlyContent { TableCellContentIndex(blockIndex, tableContentIndex, rowIndex, cellIndex, it) }

        private fun consumeTextOnlyContent(makeIndex: (Int) -> ContentIndex) =
            cursor.forEachIndexed<Token.TextContent> { currentPosition, token, _ ->
                when (token) {
                    is Token.Text -> consumeText(makeIndex(currentPosition))
                    is Token.NewLine -> {}
                }
            }
    }

    private class Tokenizer(letter: Edit.Letter) : EditLetterVisitor<Token>(letter) {

        override fun visit(block: Edit.Block) {
            emit(Token.Block(block.id, block.type))
            super.visit(block)
        }

        override fun visit(content: Edit.ParagraphContent.Text.Literal) {
            emit(Token.Text.Literal(content.id, content.editedFontType ?: content.fontType))
            emitWords(content.editedText ?: content.text)
            super.visit(content)
        }

        override fun visit(content: Edit.ParagraphContent.Text.Variable) {
            emit(Token.Text.Variable(content.id, content.fontType))
            emitWords(content.text)
            super.visit(content)
        }

        override fun visit(content: Edit.ParagraphContent.Text.NewLine) {
            emit(Token.NewLine(content.id))
            super.visit(content)
        }

        override fun visit(itemList: Edit.ParagraphContent.ItemList) {
            emit(Token.ItemList(itemList.id, itemList.listType))
            super.visit(itemList)
        }

        override fun visit(item: Edit.ParagraphContent.ItemList.Item) {
            emit(Token.Item(item.id))
            super.visit(item)
        }

        override fun visit(table: Edit.ParagraphContent.Table) {
            emit(Token.Table(table.id))
            super.visit(table)
        }

        override fun visit(header: Edit.ParagraphContent.Table.Header) {
            emit(Token.TableHeader(header.id))
            super.visit(header)
        }

        override fun visit(colSpec: Edit.ParagraphContent.Table.ColumnSpec) {
            emit(Token.ColumnSpec(colSpec.id, colSpec.alignment, colSpec.span))
            super.visit(colSpec)
        }

        override fun visit(row: Edit.ParagraphContent.Table.Row) {
            emit(Token.Row(row.id))
            super.visit(row)
        }

        override fun visit(cell: Edit.ParagraphContent.Table.Cell) {
            emit(Token.Cell(cell.id))
            super.visit(cell)
        }

        private fun emitWords(text: String) =
            text.split(' ').forEach { word -> emit(Token.Word(word)) }
    }
}

private fun <T> makeChangeFor(edit: EditOperation<*>, value: T): Change<T> = when (edit) {
    is EditOperation.Insert -> Change.Insert(value)
    is EditOperation.Delete -> Change.Delete(value)
}
