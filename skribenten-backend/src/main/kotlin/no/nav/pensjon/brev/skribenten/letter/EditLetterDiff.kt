package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Variable
import no.nav.pensjon.brev.skribenten.letter.TextOnlyWordDiff.Token
import no.nav.pensjon.brev.skribenten.letter.TextOnlyWordDiff.Token.Block
import no.nav.pensjon.brev.skribenten.letter.TextOnlyWordDiff.Token.Content
import no.nav.pensjon.brev.skribenten.letter.TextOnlyWordDiff.Token.ContentFont
import no.nav.pensjon.brev.skribenten.letter.TextOnlyWordDiff.Token.ContentText

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

class TextOnlyWordDiff : EditLetterDiff<Token> {

    override fun tokenize(letter: Edit.Letter): Sequence<Token> = object : EditLetterSequence<Token>() {
        override suspend fun SequenceScope<Token>.visit(block: Edit.Block) {
            yield(Block(block.id, block.type))
            block.content.forEach {
                visit(it)
            }
        }

        override suspend fun SequenceScope<Token>.visit(content: Variable) {
            yield(Content(id = content.id, type = content.type))
            yield(ContentFont(type = content.fontType))
            yieldTextEditables(content.text)
        }

        override suspend fun SequenceScope<Token>.visit(content: Edit.ParagraphContent.Text.Literal) {
            yield(Content(id = content.id, type = content.type))
            yield(ContentFont(content.editedFontType ?: content.fontType))
            yieldTextEditables(content.editedText ?: content.text)
        }

        private suspend fun SequenceScope<Token>.yieldTextEditables(text: String) =
            text.split(' ').forEach { char -> yield(ContentText(char = char)) }

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
            require(current is Block) { "Found editable that is not a Block at the top level: $current" }
            addAll(consumeBlock(blockIndex++, cursor))
        }
    }

    sealed class Token {

        // Equality is intentionally type-only: the diff algorithm treats two blocks/contents
        // of the same type as structurally equivalent anchors, regardless of their id.
        data class Block(val id: Int?, val type: Edit.Block.Type) : Token() {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false
                other as Block
                return type == other.type
            }

            override fun hashCode(): Int = type.hashCode()
        }

        data class Content(val id: Int?, val type: Edit.ParagraphContent.Type) : Token() {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false
                other as Content
                return type == other.type
            }

            override fun hashCode(): Int = type.hashCode()
        }

        data class ContentFont(val type: FontType) : Token()
        data class ContentText(val char: String) : Token()
    }
}

private fun consumeBlock(
    blockIndex: Int,
    cursor: TokenCursor<Token>,
): List<DiffSegment> = buildList {
    var contentCount = 0
    while (cursor.peek() is Content) {
        val contentIndex = ContentIndex(blockIndex = blockIndex, contentIndex = contentCount++)

        val (content) = cursor.consume()
        require(content is Content) { "Found editable that is not a Content: $content" }
        require(cursor.consume().first is ContentFont) { "Found editable that is not a ContentFont" }

        var currentDiff: DiffSegment? = null
        var text = ""
        while (cursor.peek() is ContentText) {
            val (current, textEdit) = cursor.consume()
            require(current is ContentText)

            val toAppend = " ${current.char}"
            if (textEdit != null) {
                if (currentDiff == null) {
                    currentDiff = DiffSegment(index = contentIndex, startOffset = text.length, endOffset = text.length + toAppend.length)
                } else if (currentDiff.endOffset == text.length) {
                    currentDiff = currentDiff.copy(endOffset = text.length + toAppend.length)
                } else {
                    add(currentDiff)
                    currentDiff = DiffSegment(index = contentIndex, startOffset = text.length, endOffset = text.length + toAppend.length)
                }
            }
            text += toAppend
        }
        if (currentDiff != null) {
            add(currentDiff)
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
