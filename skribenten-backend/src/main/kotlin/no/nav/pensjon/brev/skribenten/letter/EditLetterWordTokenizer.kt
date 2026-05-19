package no.nav.pensjon.brev.skribenten.letter

import no.nav.brev.Listetype
import no.nav.pensjon.brev.skribenten.common.EqualityBy
import no.nav.pensjon.brev.skribenten.common.diff.EditScript
import no.nav.pensjon.brev.skribenten.common.diff.Change
import no.nav.pensjon.brev.skribenten.common.diff.DiffEntry
import no.nav.pensjon.brev.skribenten.common.diff.ReplaceAwareEditScriptCursor
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.BlockContentIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.BlockIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.TableCellIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.TableRowIndex
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
        val cursor = ReplaceAwareEditScriptCursor(editScript)
        cursor.forEachIndexed<Token.Block> { insertBlockIdx, deleteBlockIdx, entry ->
            val insertBlockIndex = BlockIndex(insertBlockIdx)
            val deleteBlockIndex = BlockIndex(deleteBlockIdx)
            entry.toChange { DiffProducer.BlockInfo(it.id, it.type) }?.let {
                producer.block(insertBlockIndex, it)
            }
            BlockParser(insertBlockIndex, deleteBlockIndex, cursor, producer).parse()
        }
        require(!cursor.hasNext) { "Not all tokens were consumed, next: ${cursor.peek()}" }
        return producer.build()
    }

    private class BlockParser(
        private val insertIndex: BlockIndex,
        private val deleteIndex: BlockIndex,
        private val cursor: ReplaceAwareEditScriptCursor<Token>,
        private val producer: DiffProducer<*>,
    ) {
        fun parse() =
            cursor.forEachIndexed<Token.BlockContent> { insertPos, deletePos, entry ->
                val insertContentIndex = insertIndex.withContentIndex(insertPos)
                val deleteContentIndex = deleteIndex.withContentIndex(deletePos)

                when (entry.token) {
                    is Token.Text -> consumeText(insertContentIndex, deleteContentIndex)
                    is Token.NewLine -> {}
                    is Token.ItemList -> {
                        @Suppress("UNCHECKED_CAST")
                        consumeItemList(insertContentIndex, deleteContentIndex, entry as DiffEntry<Token.ItemList>)
                    }
                    is Token.Table -> {
                        @Suppress("UNCHECKED_CAST")
                        consumeTable(insertContentIndex, deleteContentIndex, entry as DiffEntry<Token.Table>)
                    }
                }
            }

        private fun consumeText(insertIndex: ContentIndex, deleteIndex: ContentIndex) =
            cursor.fold(WordDiffCollector(insertIndex, deleteIndex), WordDiffCollector::addEntry)
                .changes()
                .forEach { producer.textSegment(it) }

        private fun consumeItemList(insertIndex: BlockContentIndex, deleteIndex: BlockContentIndex, entry: DiffEntry<Token.ItemList>) {
            entry.toChange { DiffProducer.ItemListInfo(it.id, it.listType) }?.let {
                producer.itemList(insertIndex, it)
            }
            cursor.forEachIndexed<Token.Item> { insertItemIdx, deleteItemIdx, itemEntry ->
                val insertItemIndex = insertIndex.withItemIndex(insertItemIdx)
                val deleteItemIndex = deleteIndex.withItemIndex(deleteItemIdx)
                itemEntry.toChange { DiffProducer.ItemInfo(it.id) }?.let {
                    producer.item(insertItemIndex, it)
                }
                consumeTextOnlyContent(
                    makeInsertIndex = insertItemIndex::withTextContentIndex,
                    makeDeleteIndex = deleteItemIndex::withTextContentIndex,
                )
            }
        }

        private fun consumeTable(insertIndex: BlockContentIndex, deleteIndex: BlockContentIndex, entry: DiffEntry<Token.Table>) {
            entry.toChange { DiffProducer.TableInfo(it.id) }?.let {
                producer.table(insertIndex, it)
            }
            consumeTableHeader(insertIndex, deleteIndex)
            cursor.forEachIndexed<Token.Row> { insertRowIdx, deleteRowIdx, rowEntry ->
                val insertRowIndex = insertIndex.withRowIndex(insertRowIdx)
                val deleteRowIndex = deleteIndex.withRowIndex(deleteRowIdx)
                rowEntry.toChange { DiffProducer.RowInfo(it.id) }?.let {
                    producer.row(insertRowIndex, it)
                }
                consumeRow(insertRowIndex, deleteRowIndex)
            }
        }

        private fun consumeTableHeader(insertIndex: BlockContentIndex, deleteIndex: BlockContentIndex) {
            cursor.requireAndConsume<Token.TableHeader>()
            cursor.forEachIndexed<Token.ColumnSpec> { insertCellIdx, deleteCellIdx, _ ->
                val insertCellIndex = insertIndex.withRowIndex(-1).withCellIndex(insertCellIdx)
                val deleteCellIndex = deleteIndex.withRowIndex(-1).withCellIndex(deleteCellIdx)
                val cellEntry = cursor.consumeIf<Token.Cell>()
                    ?: error("Expected Token.Cell after Token.ColumnSpec in table header, got: ${cursor.peek()}")
                cellEntry.toChange { DiffProducer.CellInfo(it.id) }?.let {
                    producer.cell(insertCellIndex, it)
                }
                consumeCell(insertCellIndex, deleteCellIndex)
            }
        }

        private fun consumeRow(insertIndex: TableRowIndex, deleteIndex: TableRowIndex) =
            cursor.forEachIndexed<Token.Cell> { insertCellIdx, deleteCellIdx, cellEntry ->
                val insertCellIndex = insertIndex.withCellIndex(insertCellIdx)
                val deleteCellIndex = deleteIndex.withCellIndex(deleteCellIdx)
                cellEntry.toChange { DiffProducer.CellInfo(it.id) }?.let {
                    producer.cell(insertCellIndex, it)
                }
                consumeCell(insertCellIndex, deleteCellIndex)
            }

        private fun consumeCell(insertIndex: TableCellIndex, deleteIndex: TableCellIndex) =
            consumeTextOnlyContent(
                makeInsertIndex = insertIndex::withTextContentIndex,
                makeDeleteIndex = deleteIndex::withTextContentIndex,
            )

        private fun consumeTextOnlyContent(makeInsertIndex: (Int) -> ContentIndex, makeDeleteIndex: (Int) -> ContentIndex) =
            cursor.forEachIndexed<Token.TextContent> { insertContentIndex, deleteContentIndex, entry ->
                when (entry.token) {
                    is Token.Text -> consumeText(makeInsertIndex(insertContentIndex), makeDeleteIndex(deleteContentIndex))
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

    @ConsistentCopyVisibility
    private data class WordDiffCollector private constructor(
        private val inserts: RangeState,
        private val deletes: RangeState,
    ) {
        constructor(insertIndex: ContentIndex, deleteIndex: ContentIndex) :
                this(RangeState(insertIndex), RangeState(deleteIndex))

        fun addEntry(entry: DiffEntry<Token.Word>): WordDiffCollector = when (entry) {
            is DiffEntry.Insert    -> copy(inserts = inserts.extend(entry.new.word))
            is DiffEntry.Delete    -> copy(deletes = deletes.extend(entry.old.word))
            is DiffEntry.Replace   -> copy(inserts = inserts.extend(entry.new.word), deletes = deletes.extend(entry.old.word))
            is DiffEntry.Unchanged -> copy(inserts = inserts.skip(entry.value.word), deletes = deletes.skip(entry.value.word))
        }

        fun changes(): List<Change<DiffProducer.TextSegment>> =
            inserts.segments().map { Change.Insert(it) } +
                    deletes.segments().map { Change.Delete(it) }

        private data class RangeState(
            private val contentIndex: ContentIndex,
            private val textLength: Int = 0,
            private val current: DiffProducer.TextSegment? = null,
            private val completed: List<DiffProducer.TextSegment> = emptyList(),
        ) {
            private fun spaceLength() = if (textLength == 0) 0 else 1

            fun extend(word: String): RangeState {
                val wordStart = textLength + spaceLength()
                val newLength = wordStart + word.length
                return if (current?.endOffset == textLength)
                    copy(textLength = newLength, current = current.copy(endOffset = newLength, text = current.text + " " + word))
                else
                    copy(
                        textLength = newLength,
                        current = DiffProducer.TextSegment(contentIndex, wordStart, newLength, word),
                        completed = completed + listOfNotNull(current),
                    )
            }

            fun skip(word: String): RangeState =
                copy(textLength = textLength + spaceLength() + word.length)

            fun segments(): List<DiffProducer.TextSegment> = completed + listOfNotNull(current)
        }
    }
}
