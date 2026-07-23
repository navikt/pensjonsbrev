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

        sealed interface BlockContent : Token {
            val id: Int?
        }
        sealed interface TextContent : BlockContent

        class ItemList(override val id: Int?, val listType: Listetype) : BlockContent, EqualityBy<ItemList>(ItemList::listType)
        class Item(val id: Int?) : Token, EqualityBy<Item>()

        class Table(override val id: Int?) : BlockContent, EqualityBy<Table>()
        class TableHeader(val id: Int?) : Token, EqualityBy<TableHeader>()
        class ColumnSpec(val id: Int?, val alignment: Edit.ParagraphContent.Table.ColumnAlignment, val span: Int) : Token, EqualityBy<ColumnSpec>(ColumnSpec::alignment, ColumnSpec::span)
        class Row(val id: Int?) : Token, EqualityBy<Row>()
        class Cell(val id: Int?) : Token, EqualityBy<Cell>()

        sealed class Text : TextContent, EqualityBy<Text>(Text::fontType) {
            abstract val fontType: FontType

            class Literal(override val id: Int?, override val fontType: FontType) : Text()
            class Variable(override val id: Int?, override val fontType: FontType) : Text()
        }

        class NewLine(override val id: Int?) : TextContent, EqualityBy<NewLine>()
        data class Word(val word: String) : Token
    }

    override fun tokenize(letter: Edit.Letter): List<Token> = Tokenizer(letter).build()

    override fun <R> parseTokens(editScript: EditScript<Token>, producer: DiffProducer<R>): R {
        val cursor = ReplaceAwareEditScriptCursor(editScript)
        cursor.forEachIndexed<Token.Block> { insertBlockIdx, deleteBlockIdx, entry ->
            val insertBlockIndex = BlockIndex(insertBlockIdx)
            val deleteBlockIndex = BlockIndex(deleteBlockIdx)
            entry.toChange { DiffProducer.BlockInfo(it.id, it.type) }?.let {
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
        private val producer: DiffProducer<*>,
    ) {
        fun parse() =
            cursor.forEachIndexedStable<Token.BlockContent> { indices, entry ->
                val insertContentIndex = insertIndex.withContentIndex(indices.insertIndex)
                val deleteContentIndex = deleteIndex.withContentIndex(indices.rawDeleteIndex)
                val stableDeleteContentIndex = deleteIndex.withContentIndex(indices.stableDeleteIndex)

                when (entry.token) {
                    is Token.TextContent -> {
                        consumeTextContent(insertContentIndex, deleteContentIndex, stableDeleteContentIndex, entry.narrow())
                    }
                    is Token.ItemList -> {
                        consumeItemList(insertContentIndex, deleteContentIndex, entry.narrow())
                        false
                    }
                    is Token.Table -> {
                        consumeTable(insertContentIndex, deleteContentIndex, entry.narrow())
                        false
                    }
                }
            }

        // Fires textContent for the whole node (Text or NewLine); for Text nodes, also diffs the words within it.
        //
        // A Text node's word-level diff must always be folded from the cursor (it can't be skipped, or its
        // "genuinely fully deleted" status decided, based on the content-level Change alone): Token.Text equality
        // only compares fontType (see Token.Text's EqualityBy), so the shortest edit script may pair this node
        // with an unrelated node elsewhere purely because their fontType matches, then classify it as content-level
        // Unchanged/Replace even though none of its actual words survive - or, conversely, classify it as a
        // content-level Delete while one of its words is, word-for-word, reused by an unrelated new node elsewhere
        // (e.g. old "beta gamma" being classified as a content-level Delete while its "beta" word is reused by new
        // "beta epsilon"). Only the resulting word-level changes reveal whether this node's text is genuinely,
        // entirely gone (in which case reporting its words individually would only duplicate the full node already
        // conveyed via the textContent Delete, so we suppress them) or whether some of its words survived elsewhere
        // (in which case the word-level segments are the only place that information is captured, so we emit them
        // instead of a textContent Delete). Similarly, a node that keeps some of its own words as-is (e.g. "hello
        // world" -> "hello", where "hello" survives Unchanged within this very node's fold) is not genuinely gone
        // either, even though the resulting changes are delete-only - hence the additional collector.hasUnchanged
        // check.
        //
        // insertIndex is decoy-corrected (see ReplaceAwareEditScriptCursor.StableIndices), so it's safe to use both
        // for the node's own textContent report (distinguishing leading/trailing siblings) and for nested
        // textSegment reporting (anchoring to an already-established position). deleteIndex/stableDeleteIndex keep
        // their original distinction: deleteIndex (this entry's own position) for the node's own report,
        // stableDeleteIndex (the last real position) for nested reporting - relevant when entry is a genuine Insert,
        // which doesn't itself consume any delete-side position.
        //
        // Returns whether this entry's insert-side pairing turned out to be a decoy (isWholeNodeGone despite the
        // entry's own classification suggesting otherwise, e.g. an ambiguous Unchanged/Replace match) - see
        // ReplaceAwareEditScriptCursor.forEachIndexedStable's doc for why the caller needs to know this.
        private fun consumeTextContent(
            insertIndex: ContentIndex,
            deleteIndex: ContentIndex,
            stableDeleteIndex: ContentIndex,
            entry: DiffEntry<Token.TextContent>,
        ): Boolean {
            when (entry.token) {
                is Token.NewLine -> {
                    entry.toChange { DiffProducer.TextContentInfo(it.id) }?.let { producer.textContent(insertIndex, deleteIndex, it) }
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
                        producer.textContent(insertIndex, deleteIndex, Change.Delete(DiffProducer.TextContentInfo(entry.token.id)))
                    } else {
                        changes.forEach { producer.textSegment(insertIndex, stableDeleteIndex, it) }
                    }
                    return isWholeNodeGone && entry !is DiffEntry.Delete
                }
            }
        }


        private fun consumeItemList(insertIndex: BlockContentIndex, deleteIndex: BlockContentIndex, entry: DiffEntry<Token.ItemList>) {
            entry.toChange { DiffProducer.ItemListInfo(it.id, it.listType) }?.let {
                producer.itemList(insertIndex, deleteIndex, it)
            }
            cursor.forEachIndexed<Token.Item> { insertItemIdx, deleteItemIdx, itemEntry ->
                val insertItemIndex = insertIndex.withItemIndex(insertItemIdx)
                val deleteItemIndex = deleteIndex.withItemIndex(deleteItemIdx)
                itemEntry.toChange { DiffProducer.ItemInfo(it.id) }?.let {
                    producer.item(insertItemIndex, deleteItemIndex, it)
                }
                consumeTextOnlyContent(
                    makeInsertIndex = insertItemIndex::withTextContentIndex,
                    makeDeleteIndex = deleteItemIndex::withTextContentIndex,
                )
            }
        }

        private fun consumeTable(insertIndex: BlockContentIndex, deleteIndex: BlockContentIndex, entry: DiffEntry<Token.Table>) {
            entry.toChange { DiffProducer.TableInfo(it.id) }?.let {
                producer.table(insertIndex, deleteIndex, it)
            }
            consumeTableHeader(insertIndex, deleteIndex)
            cursor.forEachIndexed<Token.Row> { insertRowIdx, deleteRowIdx, rowEntry ->
                val insertRowIndex = insertIndex.withRowIndex(insertRowIdx)
                val deleteRowIndex = deleteIndex.withRowIndex(deleteRowIdx)
                rowEntry.toChange { DiffProducer.RowInfo(it.id) }?.let {
                    producer.row(insertRowIndex, deleteRowIndex, it)
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
                    producer.cell(insertCellIndex, deleteCellIndex, it)
                }
                consumeCell(insertCellIndex, deleteCellIndex)
            }
        }

        private fun consumeRow(insertIndex: TableRowIndex, deleteIndex: TableRowIndex) =
            cursor.forEachIndexed<Token.Cell> { insertCellIdx, deleteCellIdx, cellEntry ->
                val insertCellIndex = insertIndex.withCellIndex(insertCellIdx)
                val deleteCellIndex = deleteIndex.withCellIndex(deleteCellIdx)
                cellEntry.toChange { DiffProducer.CellInfo(it.id) }?.let {
                    producer.cell(insertCellIndex, deleteCellIndex, it)
                }
                consumeCell(insertCellIndex, deleteCellIndex)
            }

        private fun consumeCell(insertIndex: TableCellIndex, deleteIndex: TableCellIndex) =
            consumeTextOnlyContent(
                makeInsertIndex = insertIndex::withTextContentIndex,
                makeDeleteIndex = deleteIndex::withTextContentIndex,
            )

        private fun consumeTextOnlyContent(makeInsertIndex: (Int) -> ContentIndex, makeDeleteIndex: (Int) -> ContentIndex) =
            cursor.forEachIndexedStable<Token.TextContent> { indices, entry ->
                consumeTextContent(
                    makeInsertIndex(indices.insertIndex),
                    makeDeleteIndex(indices.rawDeleteIndex),
                    makeDeleteIndex(indices.stableDeleteIndex),
                    entry,
                )
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
            emit(Token.ItemList(itemList.id, itemList.editedListType ?: itemList.listType))
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
        // Tracks whether any of this node's own words were seen as Unchanged (i.e. genuinely still present,
        // as-is, in both old and new) - see consumeTextContent's doc for why this matters.
        val hasUnchanged: Boolean,
    ) {
        constructor() : this(RangeState(), RangeState(), false)

        fun addEntry(entry: DiffEntry<Token.Word>): WordDiffCollector = when (entry) {
            is DiffEntry.Insert -> copy(inserts = inserts.extend(entry.new.word))
            is DiffEntry.Delete -> copy(deletes = deletes.extend(entry.old.word))
            is DiffEntry.Replace -> copy(inserts = inserts.extend(entry.new.word), deletes = deletes.extend(entry.old.word))
            is DiffEntry.Unchanged -> copy(inserts = inserts.skip(entry.value.word), deletes = deletes.skip(entry.value.word), hasUnchanged = true)
        }

        fun changes(): List<Change<DiffProducer.TextSegment>> =
            inserts.segments().map { Change.Insert(it) } +
                    deletes.segments().map { Change.Delete(it) }

        private data class RangeState(
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
                        current = DiffProducer.TextSegment(wordStart, newLength, word),
                        completed = completed + listOfNotNull(current),
                    )
            }

            fun skip(word: String): RangeState =
                copy(textLength = textLength + spaceLength() + word.length)

            fun segments(): List<DiffProducer.TextSegment> = completed + listOfNotNull(current)
        }
    }
}
