package no.nav.pensjon.brev.skribenten.letter.v2

import no.nav.pensjon.brev.skribenten.common.EqualityBy
import no.nav.pensjon.brev.skribenten.common.diff.Change
import no.nav.pensjon.brev.skribenten.common.diff.DiffEntry
import no.nav.pensjon.brev.skribenten.common.diff.EditScript
import no.nav.pensjon.brev.skribenten.common.diff.ReplaceAwareEditScriptCursor
import no.nav.pensjon.brev.skribenten.letter.v2.ContentIndexV2.BlockIndex
import no.nav.pensjon.brev.skribenten.letter.v2.ContentIndexV2.ItemIndex
import no.nav.pensjon.brev.skribenten.letter.v2.ContentIndexV2.TableCellIndex
import no.nav.pensjon.brev.skribenten.letter.v2.ContentIndexV2.TableRowIndex
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2.FontType

class EditLetterWordTokenizerV2 : EditLetterTokenizerV2<EditLetterWordTokenizerV2.Token> {

    sealed interface Token {
        class Block(val id: Int?, val type: EditV2.Block.Type) : Token, EqualityBy<Block>(Block::type)

        sealed interface BlockChild : Token {
            val id: Int?
        }

        sealed interface TextContent : BlockChild

        class Item(override val id: Int?) : BlockChild, EqualityBy<Item>()

        class TableHeader(val id: Int?) : Token, EqualityBy<TableHeader>()
        class ColumnSpec(val id: Int?, val alignment: EditV2.Block.Table.ColumnAlignment, val span: Int) :
            Token, EqualityBy<ColumnSpec>(ColumnSpec::alignment, ColumnSpec::span)

        class Row(override val id: Int?) : BlockChild, EqualityBy<Row>()
        class Cell(val id: Int?) : Token, EqualityBy<Cell>()

        sealed class Text : TextContent, EqualityBy<Text>(Text::fontType) {
            abstract val fontType: FontType

            class Literal(override val id: Int?, override val fontType: FontType) : Text()
            class Variable(override val id: Int?, override val fontType: FontType) : Text()
        }

        class NewLine(override val id: Int?) : TextContent, EqualityBy<NewLine>()
        data class Word(val word: String) : Token
    }

    override fun tokenize(letter: EditV2.Letter): List<Token> = Tokenizer(letter).build()

    override fun <R> parseTokens(editScript: EditScript<Token>, producer: DiffProducerV2<R>): R {
        val cursor = ReplaceAwareEditScriptCursor(editScript)
        cursor.forEachIndexed<Token.Block> { insertBlockIdx, deleteBlockIdx, entry ->
            val insertBlockIndex = BlockIndex(insertBlockIdx)
            val deleteBlockIndex = BlockIndex(deleteBlockIdx)
            entry.toChange { DiffProducerV2.BlockInfo(it.id, it.type) }?.let {
                producer.block(insertBlockIndex, deleteBlockIndex, it)
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
        private val producer: DiffProducerV2<*>,
    ) {
        fun parse() {
            consumeTableHeader()
            cursor.forEachIndexedStable<Token.BlockChild> { indices, entry ->
                when (entry.token) {
                    is Token.TextContent ->
                        consumeTextContent(
                            insertIndex.withContentIndex(indices.insertIndex),
                            deleteIndex.withContentIndex(indices.rawDeleteIndex),
                            deleteIndex.withContentIndex(indices.stableDeleteIndex),
                            entry.narrow(),
                        )

                    is Token.Item -> {
                        consumeItem(insertIndex.withItemIndex(indices.insertIndex), deleteIndex.withItemIndex(indices.rawDeleteIndex), entry.narrow())
                        false
                    }

                    is Token.Row -> {
                        consumeRow(insertIndex.withRowIndex(indices.insertIndex), deleteIndex.withRowIndex(indices.rawDeleteIndex), entry.narrow())
                        false
                    }
                }
            }
        }

        private fun consumeTextContent(
            insertIndex: ContentIndexV2,
            deleteIndex: ContentIndexV2,
            stableDeleteIndex: ContentIndexV2,
            entry: DiffEntry<Token.TextContent>,
        ): Boolean {
            when (entry.token) {
                is Token.NewLine -> {
                    entry.toChange { DiffProducerV2.TextContentInfo(it.id) }?.let { producer.textContent(insertIndex, deleteIndex, it) }
                    return false
                }

                is Token.Text -> {
                    val collector = cursor.fold(WordDiffCollector(), WordDiffCollector::addEntry)
                    val changes = collector.changes()
                    val isWholeNodeGone = when {
                        entry is DiffEntry.Insert || collector.hasUnchanged -> false
                        changes.isEmpty() -> entry is DiffEntry.Delete
                        else -> changes.all { it is Change.Delete }
                    }
                    if (isWholeNodeGone) {
                        producer.textContent(insertIndex, deleteIndex, Change.Delete(DiffProducerV2.TextContentInfo(entry.token.id)))
                    } else {
                        changes.forEach { producer.textSegment(insertIndex, stableDeleteIndex, it) }
                    }
                    return isWholeNodeGone && entry !is DiffEntry.Delete
                }
            }
        }

        private fun consumeItem(insertItemIndex: ItemIndex, deleteItemIndex: ItemIndex, entry: DiffEntry<Token.Item>) {
            entry.toChange { DiffProducerV2.ItemInfo(it.id) }?.let {
                producer.item(insertItemIndex, deleteItemIndex, it)
            }
            consumeTextOnlyContent(
                makeInsertIndex = insertItemIndex::withTextContentIndex,
                makeDeleteIndex = deleteItemIndex::withTextContentIndex,
            )
        }

        private fun consumeTableHeader() {
            cursor.consumeIf<Token.TableHeader>() ?: return
            cursor.forEachIndexed<Token.ColumnSpec> { insertColumnIdx, deleteColumnIdx, columnEntry ->
                val insertColumnIndex = insertIndex.withColumnSpecIndex(insertColumnIdx)
                val deleteColumnIndex = deleteIndex.withColumnSpecIndex(deleteColumnIdx)
                columnEntry.toChange { DiffProducerV2.ColumnSpecInfo(it.id, it.alignment, it.span) }?.let {
                    producer.columnSpec(insertColumnIndex, deleteColumnIndex, it)
                }
                consumeTextOnlyContent(
                    makeInsertIndex = insertColumnIndex::withTextContentIndex,
                    makeDeleteIndex = deleteColumnIndex::withTextContentIndex,
                )
            }
        }

        private fun consumeRow(insertRowIndex: TableRowIndex, deleteRowIndex: TableRowIndex, entry: DiffEntry<Token.Row>) {
            entry.toChange { DiffProducerV2.RowInfo(it.id) }?.let {
                producer.row(insertRowIndex, deleteRowIndex, it)
            }
            cursor.forEachIndexed<Token.Cell> { insertCellIdx, deleteCellIdx, cellEntry ->
                val insertCellIndex = insertRowIndex.withCellIndex(insertCellIdx)
                val deleteCellIndex = deleteRowIndex.withCellIndex(deleteCellIdx)
                cellEntry.toChange { DiffProducerV2.CellInfo(it.id) }?.let {
                    producer.cell(insertCellIndex, deleteCellIndex, it)
                }
                consumeCell(insertCellIndex, deleteCellIndex)
            }
        }

        private fun consumeCell(insertIndex: TableCellIndex, deleteIndex: TableCellIndex) =
            consumeTextOnlyContent(
                makeInsertIndex = insertIndex::withTextContentIndex,
                makeDeleteIndex = deleteIndex::withTextContentIndex,
            )

        private fun consumeTextOnlyContent(makeInsertIndex: (Int) -> ContentIndexV2, makeDeleteIndex: (Int) -> ContentIndexV2) =
            cursor.forEachIndexedStable<Token.TextContent> { indices, entry ->
                consumeTextContent(
                    makeInsertIndex(indices.insertIndex),
                    makeDeleteIndex(indices.rawDeleteIndex),
                    makeDeleteIndex(indices.stableDeleteIndex),
                    entry,
                )
            }
    }

    private class Tokenizer(letter: EditV2.Letter) : EditLetterVisitorV2<Token>(letter) {

        override fun visit(block: EditV2.Block) {
            emit(Token.Block(block.id, block.type))
            super.visit(block)
        }

        override fun visit(item: EditV2.Item) {
            emit(Token.Item(item.id))
            super.visit(item)
        }

        override fun visit(header: EditV2.Block.Table.Header) {
            emit(Token.TableHeader(header.id))
            super.visit(header)
        }

        override fun visit(colSpec: EditV2.Block.Table.ColumnSpec) {
            emit(Token.ColumnSpec(colSpec.id, colSpec.alignment, colSpec.span))
            super.visit(colSpec)
        }

        override fun visit(row: EditV2.Block.Table.Row) {
            emit(Token.Row(row.id))
            super.visit(row)
        }

        override fun visit(cell: EditV2.Block.Table.Cell) {
            emit(Token.Cell(cell.id))
            super.visit(cell)
        }

        override fun visit(text: EditV2.Text.Literal) {
            emit(Token.Text.Literal(text.id, text.editedFontType ?: text.fontType))
            emitWords(text.editedText ?: text.text)
            super.visit(text)
        }

        override fun visit(text: EditV2.Text.Variable) {
            emit(Token.Text.Variable(text.id, text.fontType))
            emitWords(text.text)
            super.visit(text)
        }

        override fun visit(text: EditV2.Text.NewLine) {
            emit(Token.NewLine(text.id))
            super.visit(text)
        }

        private fun emitWords(text: String) =
            text.split(' ').forEach { word -> emit(Token.Word(word)) }
    }

    @ConsistentCopyVisibility
    private data class WordDiffCollector private constructor(
        private val inserts: RangeState,
        private val deletes: RangeState,
        val hasUnchanged: Boolean,
    ) {
        constructor() : this(RangeState(), RangeState(), false)

        fun addEntry(entry: DiffEntry<Token.Word>): WordDiffCollector = when (entry) {
            is DiffEntry.Insert -> copy(inserts = inserts.extend(entry.new.word))
            is DiffEntry.Delete -> copy(deletes = deletes.extend(entry.old.word))
            is DiffEntry.Replace -> copy(inserts = inserts.extend(entry.new.word), deletes = deletes.extend(entry.old.word))
            is DiffEntry.Unchanged -> copy(inserts = inserts.skip(entry.value.word), deletes = deletes.skip(entry.value.word), hasUnchanged = true)
        }

        fun changes(): List<Change<DiffProducerV2.TextSegment>> =
            inserts.segments().map { Change.Insert(it) } +
                    deletes.segments().map { Change.Delete(it) }

        private data class RangeState(
            private val textLength: Int = 0,
            private val current: DiffProducerV2.TextSegment? = null,
            private val completed: List<DiffProducerV2.TextSegment> = emptyList(),
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
                        current = DiffProducerV2.TextSegment(wordStart, newLength, word),
                        completed = completed + listOfNotNull(current),
                    )
            }

            fun skip(word: String): RangeState =
                copy(textLength = textLength + spaceLength() + word.length)

            fun segments(): List<DiffProducerV2.TextSegment> = completed + listOfNotNull(current)
        }
    }
}
