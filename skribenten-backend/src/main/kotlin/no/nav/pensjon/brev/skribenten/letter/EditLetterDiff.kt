@file:OptIn(InterneDataklasser::class)

package no.nav.pensjon.brev.skribenten.letter

import no.nav.brev.InterneDataklasser
import no.nav.brev.Listetype
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.FontType

interface EditLetterDiff<Token : Any> {
    fun tokenize(letter: Edit.Letter): Sequence<Token>
    fun generateDiffSegments(editScript: EditScript<Token>): Pair<List<DiffSegment>, List<DiffSegment>>

    fun diff(old: Edit.Letter, new: Edit.Letter): Pair<List<DiffSegment>, List<DiffSegment>> =
        generateDiffSegments(
            shortestEditScript(
                old = tokenize(old).toList(),
                new = tokenize(new).toList(),
            )
        )
}

class TextOnlyWordDiff : EditLetterDiff<TextOnlyWordDiff.Token> {

    override fun tokenize(letter: Edit.Letter): Sequence<Token> = object : EditLetterSequence<Token>() {

        override suspend fun SequenceScope<Token>.visit(block: Edit.Block) {
            yield(Token.Block(block.id, block.type))
            block.content.forEach { visit(it) }
        }

        override suspend fun SequenceScope<Token>.visit(content: Edit.ParagraphContent.Text.Literal) {
            yield(Token.Literal(content.id, content.editedFontType ?: content.fontType))
            yieldWords(content.editedText ?: content.text)
        }

        override suspend fun SequenceScope<Token>.visit(content: Edit.ParagraphContent.Text.Variable) {
            yield(Token.Variable(content.id, content.fontType))
            yieldWords(content.text)
        }

        override suspend fun SequenceScope<Token>.visit(content: Edit.ParagraphContent.Text.NewLine) {
            yield(Token.NewLine(content.id))
        }

        override suspend fun SequenceScope<Token>.visit(itemList: Edit.ParagraphContent.ItemList) {
            yield(Token.ItemList(itemList.id, itemList.listType))
            itemList.items.forEach { visit(it) }
        }

        override suspend fun SequenceScope<Token>.visit(item: Edit.ParagraphContent.ItemList.Item) {
            yield(Token.Item(item.id))
            item.content.forEach { visit(it) }
        }

        override suspend fun SequenceScope<Token>.visit(table: Edit.ParagraphContent.Table) {
            yield(Token.Table(table.id))
            visit(table.header)
            table.rows.forEach { visit(it) }
        }

        override suspend fun SequenceScope<Token>.visit(header: Edit.ParagraphContent.Table.Header) {
            yield(Token.Header(header.id))
            header.colSpec.forEach { visit(it) }
        }

        override suspend fun SequenceScope<Token>.visit(colSpec: Edit.ParagraphContent.Table.ColumnSpec) {
            yield(Token.ColumnSpec(colSpec.id, colSpec.alignment, colSpec.span))
            visit(colSpec.headerContent)
        }

        override suspend fun SequenceScope<Token>.visit(row: Edit.ParagraphContent.Table.Row) {
            yield(Token.Row(row.id))
            row.cells.forEach { visit(it) }
        }

        override suspend fun SequenceScope<Token>.visit(cell: Edit.ParagraphContent.Table.Cell) {
            yield(Token.Cell(cell.id))
            cell.text.forEach { visit(it) }
        }

        private suspend fun SequenceScope<Token>.yieldWords(text: String) =
            text.split(' ').forEach { word -> yield(Token.Word(word)) }

    }.build(letter)

    override fun generateDiffSegments(editScript: EditScript<Token>): Pair<List<DiffSegment>, List<DiffSegment>> = Pair(
        generateDiffSegments(editScript.new, editScript.inserts),
        generateDiffSegments(editScript.old, editScript.deletes),
    )

    private fun generateDiffSegments(tokens: List<Token>, edits: List<EditOperation<Token>>): List<DiffSegment> = buildList {
        val cursor = TokenCursor(tokens, edits)
        var blockIndex = 0
        while (cursor.hasNext) {
            val (current) = cursor.consume()
            require(current is Token.Block) { "Found token that is not a Block at the top level: $current" }
            addAll(BlockParser(blockIndex++, cursor).parse())
        }
    }

    sealed class Token {

        // Equality is intentionally type-only: the diff algorithm treats two blocks
        // of the same type as structurally equivalent anchors, regardless of their id.
        data class Block(val id: Int?, val type: Edit.Block.Type) : Token() {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false
                return type == (other as Block).type
            }

            override fun hashCode(): Int = type.hashCode()
        }

        // Equality on fontType only: a font change is a meaningful structural difference.
        data class Literal(val id: Int?, val fontType: FontType) : Token() {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false
                return fontType == (other as Literal).fontType
            }

            override fun hashCode(): Int = fontType.hashCode()
        }

        data class Variable(val id: Int?, val fontType: FontType) : Token() {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false
                return fontType == (other as Variable).fontType
            }

            override fun hashCode(): Int = fontType.hashCode()
        }

        data class NewLine(val id: Int?) : Token() {
            override fun equals(other: Any?): Boolean = other is NewLine
            override fun hashCode(): Int = NewLine::class.hashCode()
        }

        data class Word(val word: String) : Token()

        // Equality on listType only: changing the list style is a meaningful structural difference.
        data class ItemList(val id: Int?, val listType: Listetype) : Token() {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false
                return listType == (other as ItemList).listType
            }

            override fun hashCode(): Int = listType.hashCode()
        }

        data class Item(val id: Int?) : Token() {
            override fun equals(other: Any?): Boolean = other is Item
            override fun hashCode(): Int = Item::class.hashCode()
        }

        data class Table(val id: Int?) : Token() {
            override fun equals(other: Any?): Boolean = other is Table
            override fun hashCode(): Int = Table::class.hashCode()
        }

        data class Header(val id: Int?) : Token() {
            override fun equals(other: Any?): Boolean = other is Header
            override fun hashCode(): Int = Header::class.hashCode()
        }

        // Equality on alignment and span: column structure changes are meaningful.
        data class ColumnSpec(
            val id: Int?,
            val alignment: Edit.ParagraphContent.Table.ColumnAlignment,
            val span: Int,
        ) : Token() {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false
                other as ColumnSpec
                return alignment == other.alignment && span == other.span
            }

            override fun hashCode(): Int = 31 * alignment.hashCode() + span.hashCode()
        }

        data class Row(val id: Int?) : Token() {
            override fun equals(other: Any?): Boolean = other is Row
            override fun hashCode(): Int = Row::class.hashCode()
        }

        data class Cell(val id: Int?) : Token() {
            override fun equals(other: Any?): Boolean = other is Cell
            override fun hashCode(): Int = Cell::class.hashCode()
        }
    }

    private class BlockParser(
        private val blockIndex: Int,
        private val cursor: TokenCursor<Token>,
    ) {
        fun parse(): List<DiffSegment> = buildList {
            var blockContentPosition = 0
            while (isBlockContent()) {
                val currentPosition = blockContentPosition++
                when {
                    cursor.peek() is Token.Literal || cursor.peek() is Token.Variable ->
                        addAll(consumeText(ContentIndex.BlockContentIndex(blockIndex, currentPosition)))
                    cursor.peek() is Token.NewLine -> cursor.consume()
                    cursor.peek() is Token.ItemList -> addAll(consumeItemList(currentPosition))
                    cursor.peek() is Token.Table -> addAll(consumeTable(currentPosition))
                    else -> error("Unexpected block-level token: ${cursor.peek()}")
                }
            }
        }

        private fun consumeText(contentIndex: ContentIndex): List<DiffSegment> {
            cursor.consume() // Consume Literal or Variable (edit on this token is not surfaced as a DiffSegment)
            var currentDiff: DiffSegment? = null
            var text = ""
            return buildList {
                while (cursor.peek() is Token.Word) {
                    val (current, edit) = cursor.consume()
                    require(current is Token.Word)
                    val toAppend = " ${current.word}"
                    if (edit != null) {
                        currentDiff = if (currentDiff == null) {
                            DiffSegment(index = contentIndex, startOffset = text.length, endOffset = text.length + toAppend.length)
                        } else if (currentDiff!!.endOffset == text.length) {
                            currentDiff!!.copy(endOffset = text.length + toAppend.length)
                        } else {
                            add(currentDiff!!)
                            DiffSegment(index = contentIndex, startOffset = text.length, endOffset = text.length + toAppend.length)
                        }
                    }
                    text += toAppend
                }
                if (currentDiff != null) add(currentDiff!!)
            }
        }

        private fun consumeItemList(listContentIndex: Int): List<DiffSegment> {
            cursor.consume() // ItemList token
            return buildList {
                var itemIndex = 0
                while (cursor.peek() is Token.Item) {
                    cursor.consume() // Item token
                    var itemContentPosition = 0
                    while (isTextContent()) {
                        val currentPosition = itemContentPosition++
                        when {
                            cursor.peek() is Token.Literal || cursor.peek() is Token.Variable ->
                                addAll(consumeText(ContentIndex.ItemContentIndex(blockIndex, listContentIndex, itemIndex, currentPosition)))
                            cursor.peek() is Token.NewLine -> cursor.consume()
                            else -> error("Unexpected text-level token: ${cursor.peek()}")
                        }
                    }
                    itemIndex++
                }
            }
        }

        private fun consumeTable(tableContentIndex: Int): List<DiffSegment> {
            cursor.consume() // Table token
            return buildList {
                if (cursor.peek() is Token.Header) {
                    cursor.consume()
                    var cellIndex = 0
                    while (cursor.peek() is Token.ColumnSpec) {
                        cursor.consume()
                        if (cursor.peek() is Token.Cell) {
                            cursor.consume()
                            var cellContentPosition = 0
                            while (isTextContent()) {
                                val currentPosition = cellContentPosition++
                                when {
                                    cursor.peek() is Token.Literal || cursor.peek() is Token.Variable ->
                                        addAll(consumeText(ContentIndex.TableCellContentIndex(blockIndex, tableContentIndex, -1, cellIndex, currentPosition)))
                                    cursor.peek() is Token.NewLine -> cursor.consume()
                                    else -> error("Unexpected text-level token: ${cursor.peek()}")
                                }
                            }
                        }
                        cellIndex++
                    }
                }
                var rowIndex = 0
                while (cursor.peek() is Token.Row) {
                    cursor.consume()
                    var cellIndex = 0
                    while (cursor.peek() is Token.Cell) {
                        cursor.consume()
                        var cellContentPosition = 0
                        while (isTextContent()) {
                            val currentPosition = cellContentPosition++
                            when {
                                cursor.peek() is Token.Literal || cursor.peek() is Token.Variable ->
                                    addAll(consumeText(ContentIndex.TableCellContentIndex(blockIndex, tableContentIndex, rowIndex, cellIndex, currentPosition)))
                                cursor.peek() is Token.NewLine -> cursor.consume()
                                else -> error("Unexpected text-level token: ${cursor.peek()}")
                            }
                        }
                        cellIndex++
                    }
                    rowIndex++
                }
            }
        }

        private fun isBlockContent() = cursor.peek().let {
            it is Token.Literal || it is Token.Variable || it is Token.NewLine ||
                it is Token.ItemList || it is Token.Table
        }

        // Items and table cells can only contain Text (Literal, Variable, NewLine).
        private fun isTextContent() = cursor.peek().let {
            it is Token.Literal || it is Token.Variable || it is Token.NewLine
        }
    }
}

private class TokenCursor<T : Any>(tokens: List<T>, edits: List<EditOperation<T>>) {
    init {
        require(edits.distinctBy { it.position }.size == edits.size) { "Expected edits to only have unique position references" }
    }
    private val tokens = tokens
    private val edits = edits.associateBy { it.position }
    private var currentIndex = 0
    val hasNext: Boolean get() = currentIndex < tokens.size

    fun peek(): T? = tokens.getOrNull(currentIndex)
    fun consume(): Pair<T, EditOperation<T>?> = Pair(tokens[currentIndex], edits[currentIndex++]).also {
        require(it.second == null || it.second?.value == it.first) { "Expected edit operation value to match tokens at position ${currentIndex - 1}, but was ${it.second?.value} and ${it.first}" }
    }
}
