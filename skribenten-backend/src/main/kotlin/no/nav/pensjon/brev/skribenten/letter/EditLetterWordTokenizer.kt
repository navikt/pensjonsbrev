@file:OptIn(InterneDataklasser::class)

package no.nav.pensjon.brev.skribenten.letter

import no.nav.brev.InterneDataklasser
import no.nav.brev.Listetype
import no.nav.pensjon.brev.skribenten.common.EqualityBy
import no.nav.pensjon.brev.skribenten.common.diff.EditScript
import no.nav.pensjon.brev.skribenten.common.diff.EditScriptCursor
import no.nav.pensjon.brev.skribenten.common.diff.Change
import no.nav.pensjon.brev.skribenten.common.diff.ReplaceAwareEditScriptCursor
import no.nav.pensjon.brev.skribenten.common.diff.map
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
        val cursor = ReplaceAwareEditScriptCursor(
            EditScriptCursor(editScript.new, editScript.inserts),
            EditScriptCursor(editScript.old, editScript.deletes),
        )
        cursor.forEachIndexed<Token.Block> { insertBlockIdx, deleteBlockIdx, block, change ->
            if (change != null) {
                producer.block(insertBlockIdx, change.map { DiffProducer.BlockInfo(it.id, it.type) })
            }
            BlockParser(insertBlockIdx, deleteBlockIdx, cursor, producer).parse()
        }
        require(!cursor.hasNext) { "Not all tokens were consumed, next: ${cursor.peek()}" }
        return producer.build()
    }

    // This is a general block parser which will not fail for Title-blocks that contain invalid content types.
    private class BlockParser(
        private val insertBlockIndex: Int,
        private val deleteBlockIndex: Int,
        private val cursor: ReplaceAwareEditScriptCursor<Token>,
        private val producer: DiffProducer<*>,
    ) {
        fun parse() =
            cursor.forEachIndexed<Token.BlockContent> { insertPos, deletePos, token, change ->
                when (token) {
                    is Token.Text -> consumeText(
                        BlockContentIndex(insertBlockIndex, insertPos),
                        BlockContentIndex(deleteBlockIndex, deletePos),
                    )
                    is Token.NewLine -> {}
                    is Token.ItemList -> {
                        @Suppress("UNCHECKED_CAST")
                        consumeItemList(insertPos, deletePos, token, change as Change<Token.ItemList>?)
                    }
                    is Token.Table -> {
                        @Suppress("UNCHECKED_CAST")
                        consumeTable(insertPos, deletePos, token, change as Change<Token.Table>?)
                    }
                }
            }

        private fun consumeText(insertContentIndex: ContentIndex, deleteContentIndex: ContentIndex) {
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
                val insertTextLength: Int = 0,
                val deleteTextLength: Int = 0,
                val inserts: RangeState = RangeState(insertContentIndex),
                val deletes: RangeState = RangeState(deleteContentIndex),
            ) {
                private fun insertSpaceLength() = if (insertTextLength == 0) 0 else 1
                private fun deleteSpaceLength() = if (deleteTextLength == 0) 0 else 1

                fun insert(word: Token.Word): State {
                    val wordStart = insertTextLength + insertSpaceLength()
                    val newLength = wordStart + word.word.length
                    return copy(insertTextLength = newLength, inserts = inserts.extend(wordStart, newLength, insertTextLength))
                }

                fun delete(word: Token.Word): State {
                    val wordStart = deleteTextLength + deleteSpaceLength()
                    val newLength = wordStart + word.word.length
                    return copy(deleteTextLength = newLength, deletes = deletes.extend(wordStart, newLength, deleteTextLength))
                }

                fun replace(oldWord: Token.Word, newWord: Token.Word): State {
                    val insertWordStart = insertTextLength + insertSpaceLength()
                    val insertNewLength = insertWordStart + newWord.word.length
                    val deleteWordStart = deleteTextLength + deleteSpaceLength()
                    val deleteNewLength = deleteWordStart + oldWord.word.length
                    return copy(
                        insertTextLength = insertNewLength,
                        deleteTextLength = deleteNewLength,
                        inserts = inserts.extend(insertWordStart, insertNewLength, insertTextLength),
                        deletes = deletes.extend(deleteWordStart, deleteNewLength, deleteTextLength),
                    )
                }

                fun noChange(word: Token.Word): State = copy(
                    insertTextLength = insertTextLength + insertSpaceLength() + word.word.length,
                    deleteTextLength = deleteTextLength + deleteSpaceLength() + word.word.length,
                )

                fun changes() =
                    inserts.segments().map { Change.Insert(it) } +
                    deletes.segments().map { Change.Delete(it) }
            }

            val finalState = cursor.fold<Token.Word, State>(State()) { state, current, change ->
                when (change) {
                    is Change.Insert -> state.insert(current)
                    is Change.Delete -> state.delete(current)
                    is Change.Replace -> state.replace(change.old, current)
                    null -> state.noChange(current)
                }
            }

            finalState.changes().forEach { producer.textSegment(it) }
        }

        private fun consumeItemList(insertContentIdx: Int, deleteContentIdx: Int, token: Token.ItemList, change: Change<Token.ItemList>?) {
            if (change != null) {
                producer.itemList(insertBlockIndex, insertContentIdx, change.map { DiffProducer.ItemListInfo(it.id, it.listType) })
            }
            cursor.forEachIndexed<Token.Item> { insertItemIdx, deleteItemIdx, itemToken, itemChange ->
                if (itemChange != null) {
                    producer.item(insertBlockIndex, insertContentIdx, insertItemIdx, itemChange.map { DiffProducer.ItemInfo(it.id) })
                }
                consumeTextOnlyContent(
                    makeInsertIndex = { ItemContentIndex(insertBlockIndex, insertContentIdx, insertItemIdx, it) },
                    makeDeleteIndex = { ItemContentIndex(deleteBlockIndex, deleteContentIdx, deleteItemIdx, it) },
                )
            }
        }

        private fun consumeTable(insertContentIdx: Int, deleteContentIdx: Int, token: Token.Table, change: Change<Token.Table>?) {
            if (change != null) {
                producer.table(insertBlockIndex, insertContentIdx, change.map { DiffProducer.TableInfo(it.id) })
            }
            consumeTableHeader(insertContentIdx, deleteContentIdx)
            cursor.forEachIndexed<Token.Row> { insertRowIdx, deleteRowIdx, rowToken, rowChange ->
                if (rowChange != null) {
                    producer.row(insertBlockIndex, insertContentIdx, insertRowIdx, rowChange.map { DiffProducer.RowInfo(it.id) })
                }
                consumeRow(insertContentIdx, deleteContentIdx, insertRowIdx, deleteRowIdx)
            }
        }

        private fun consumeTableHeader(insertContentIdx: Int, deleteContentIdx: Int) {
            cursor.requireAndConsume<Token.TableHeader>()
            cursor.forEachIndexed<Token.ColumnSpec> { insertCellIdx, deleteCellIdx, _, _ ->
                val (cellToken, cellChange) = cursor.consumeIf<Token.Cell>()
                    ?: error("Expected Token.Cell after Token.ColumnSpec in table header, got: ${cursor.peek()}")
                if (cellChange != null) {
                    producer.cell(insertBlockIndex, insertContentIdx, -1, insertCellIdx, cellChange.map { DiffProducer.CellInfo(it.id) })
                }
                consumeCell(insertContentIdx, deleteContentIdx, -1, -1, insertCellIdx, deleteCellIdx)
            }
        }

        private fun consumeRow(insertContentIdx: Int, deleteContentIdx: Int, insertRowIdx: Int, deleteRowIdx: Int) =
            cursor.forEachIndexed<Token.Cell> { insertCellIdx, deleteCellIdx, cellToken, cellChange ->
                if (cellChange != null) {
                    producer.cell(insertBlockIndex, insertContentIdx, insertRowIdx, insertCellIdx, cellChange.map { DiffProducer.CellInfo(it.id) })
                }
                consumeCell(insertContentIdx, deleteContentIdx, insertRowIdx, deleteRowIdx, insertCellIdx, deleteCellIdx)
            }

        private fun consumeCell(insertContentIdx: Int, deleteContentIdx: Int, insertRowIdx: Int, deleteRowIdx: Int, insertCellIdx: Int, deleteCellIdx: Int) =
            consumeTextOnlyContent(
                makeInsertIndex = { TableCellContentIndex(insertBlockIndex, insertContentIdx, insertRowIdx, insertCellIdx, it) },
                makeDeleteIndex = { TableCellContentIndex(deleteBlockIndex, deleteContentIdx, deleteRowIdx, deleteCellIdx, it) },
            )

        private fun consumeTextOnlyContent(makeInsertIndex: (Int) -> ContentIndex, makeDeleteIndex: (Int) -> ContentIndex) =
            cursor.forEachIndexed<Token.TextContent> { insertPos, deletePos, token, _ ->
                when (token) {
                    is Token.Text -> consumeText(makeInsertIndex(insertPos), makeDeleteIndex(deletePos))
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
