@file:OptIn(InterneDataklasser::class)

package no.nav.pensjon.brev.skribenten.letter

import no.nav.brev.InterneDataklasser
import no.nav.brev.Listetype
import no.nav.pensjon.brev.skribenten.common.EqualityBy
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.BlockContentIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.ItemContentIndex
import no.nav.pensjon.brev.skribenten.letter.ContentIndex.TableCellContentIndex
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.FontType

interface EditLetterDiff<Token : Any> {
    fun tokenize(letter: Edit.Letter): List<Token>
    fun generateDiffSegments(editScript: EditScript<Token>): Pair<List<DiffSegment>, List<DiffSegment>>

    fun diff(old: Edit.Letter, new: Edit.Letter): Pair<List<DiffSegment>, List<DiffSegment>> =
        generateDiffSegments(
            shortestEditScript(
                old = tokenize(old),
                new = tokenize(new),
            )
        )
}

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

    private fun generateDiffSegments(tokens: List<Token>, edits: List<EditOperation<Token>>): List<DiffSegment> = buildList {
        val cursor = TokenCursor(tokens, edits)
        var blockIndex = 0
        while (cursor.hasNext) {
            val (current) = cursor.consume()
            require(current is Token.Block) { "Found token that is not a Block at the top level: $current" }
            addAll(BlockParser(blockIndex++, cursor).parse())
        }
    }

    // Equality is intentionally excludes id so that it doesn't affect the diff algorithm
    sealed interface Token {
        class Block(val id: Int?, val type: Edit.Block.Type) : Token, EqualityBy<Block>(Block::type)

        class ItemList(val id: Int?, val listType: Listetype) : Token, EqualityBy<ItemList>(ItemList::listType)
        class Item(val id: Int?) : Token, EqualityBy<Item>()

        class Table(val id: Int?) : Token, EqualityBy<Table>()
        class TableHeader(val id: Int?) : Token, EqualityBy<TableHeader>()
        class ColumnSpec(val id: Int?, val alignment: Edit.ParagraphContent.Table.ColumnAlignment, val span: Int) : Token, EqualityBy<ColumnSpec>(ColumnSpec::alignment, ColumnSpec::span)
        class Row(val id: Int?) : Token, EqualityBy<Row>()
        class Cell(val id: Int?) : Token, EqualityBy<Cell>()

        sealed class Text : Token, EqualityBy<Text>(Text::fontType) {
            abstract val id: Int?
            abstract val fontType: FontType

            class Literal(override val id: Int?, override val fontType: FontType) : Text()
            class Variable(override val id: Int?, override val fontType: FontType) : Text()
        }

        class NewLine(val id: Int?) : Token, EqualityBy<NewLine>()
        data class Word(val word: String) : Token
    }

    // This is a general block parser which will not fail for Title-blocks that contain invalid content types.
    private class BlockParser(
        private val blockIndex: Int,
        private val cursor: TokenCursor<Token>,
    ) {
        fun parse(): List<DiffSegment> = buildList {
            var blockContentPosition = 0
            while (isBlockContent()) {
                val currentPosition = blockContentPosition++
                when (cursor.peek()) {
                    is Token.Text -> addAll(consumeText(BlockContentIndex(blockIndex, currentPosition)))
                    is Token.NewLine -> cursor.consume()
                    is Token.ItemList -> addAll(consumeItemList(currentPosition))
                    is Token.Table -> addAll(consumeTable(currentPosition))
                    else -> error("Unexpected block-level token: ${cursor.peek()}")
                }
            }
        }

        private fun consumeText(contentIndex: ContentIndex): List<DiffSegment> {
            cursor.requireAndConsume<Token.Text>()
            return buildList {
                var currentDiff: DiffSegment? = null
                var text = ""

                cursor.forEach<Token.Word> { current, edit ->
                    val toAppend = " ${current.word}"
                    if (edit != null) {
                        currentDiff = when {
                            currentDiff == null -> DiffSegment(index = contentIndex, startOffset = text.length, endOffset = text.length + toAppend.length)
                            currentDiff.endOffset == text.length -> currentDiff.copy(endOffset = text.length + toAppend.length)
                            else -> {
                                add(currentDiff)
                                DiffSegment(index = contentIndex, startOffset = text.length, endOffset = text.length + toAppend.length)
                            }
                        }
                    }
                    text += toAppend
                }
                // Add any dangling last diff segment
                if (currentDiff != null) {
                    add(currentDiff)
                }
            }
        }

        private fun consumeItemList(listContentIndex: Int): List<DiffSegment> {
            cursor.requireAndConsume<Token.ItemList>()

            return buildList {
                var itemIndex = 0
                cursor.forEach<Token.Item> { _, _ ->
                    addAll(consumeTextOnlyContent { ItemContentIndex(blockIndex, listContentIndex, itemIndex++, it) })
                }
            }
        }

        private fun consumeTable(tableContentIndex: Int): List<DiffSegment> {
            cursor.requireAndConsume<Token.Table>()

            return buildList {
                addAll(consumeTableHeader(tableContentIndex))

                var rowIndex = 0
                cursor.forEach<Token.Row> { _, _ ->
                    addAll(consumeRow(tableContentIndex, rowIndex++))
                }
            }
        }

        private fun consumeTableHeader(tableContentIndex: Int): List<DiffSegment> = buildList {
            cursor.requireAndConsume<Token.TableHeader>()
            var cellIndex = 0
            cursor.forEach<Token.ColumnSpec> { _, _ ->
                cursor.requireAndConsume<Token.Cell>()
                addAll(consumeCell(tableContentIndex, -1, cellIndex++))
            }
        }

        private fun consumeRow(tableContentIndex: Int, rowIndex: Int): List<DiffSegment> = buildList {
            var cellIndex = 0
            cursor.forEach<Token.Cell> { _, _ ->
                addAll(consumeCell(tableContentIndex, rowIndex, cellIndex++))
            }
        }

        private fun consumeCell(tableContentIndex: Int, rowIndex: Int, cellIndex: Int): List<DiffSegment> =
            consumeTextOnlyContent { TableCellContentIndex(blockIndex, tableContentIndex, rowIndex, cellIndex, it) }

        private fun consumeTextOnlyContent(makeIndex: (Int) -> ContentIndex): List<DiffSegment> = buildList {
            var contentPosition = 0
            while (isTextContent()) {
                val currentPosition = contentPosition++
                when (cursor.peek()) {
                    is Token.Text -> addAll(consumeText(makeIndex(currentPosition)))
                    is Token.NewLine -> cursor.consume()
                    else -> error("Unexpected text-level token: ${cursor.peek()}")
                }
            }
        }

        private fun isBlockContent() = cursor.peek().let {
            it is Token.Text || it is Token.NewLine || it is Token.ItemList || it is Token.Table
        }

        // Items and table cells can only contain Text (Literal, Variable, NewLine).
        private fun isTextContent() = cursor.peek().let {
            it is Token.Text || it is Token.NewLine
        }
    }
}

private class TokenCursor<T : Any>(private val tokens: List<T>, edits: List<EditOperation<T>>) {
    init {
        require(edits.distinctBy { it.position }.size == edits.size) { "Expected edits to only have unique position references" }
    }
    private val edits = edits.associateBy { it.position }
    private var currentIndex = 0
    val hasNext: Boolean get() = currentIndex < tokens.size

    fun peek(): T? = tokens.getOrNull(currentIndex)

    fun consume(): Pair<T, EditOperation<T>?> = Pair(tokens[currentIndex], edits[currentIndex++]).also {
        require(it.second == null || it.second?.value == it.first) {
            "Expected edit operation value to match tokens at position ${currentIndex - 1}, but was ${it.second?.value} and ${it.first}"
        }
    }

    inline fun <reified E : T> requireAndConsume(): E {
        val (token) = consume()
        require(token is E) { "Expected to consume ${E::class.simpleName}-token but found: $token" }
        return token
    }

    inline fun <reified E : T> consumeIf(): Pair<E, EditOperation<E>?>? {
        if (peek() !is E) return null
        val (token, edit) = consume()
        @Suppress("UNCHECKED_CAST")
        return Pair(token as E, edit as EditOperation<E>?)
    }

    inline fun <reified E : T> forEach(action: (E, EditOperation<E>?) -> Unit) {
        while (true) {
            val (token, edit) = consumeIf<E>() ?: break
            action(token, edit)
        }
    }
}
