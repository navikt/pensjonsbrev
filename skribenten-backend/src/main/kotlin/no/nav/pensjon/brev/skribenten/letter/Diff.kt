package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.letter.Edit.Block
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Variable
import no.nav.pensjon.brev.skribenten.letter.Editable.*


sealed class Editable {
    data class BlockId(val index: Int, val id: Int?) : Editable() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as BlockId

            return id == other.id
        }

        override fun hashCode(): Int {
            return id ?: 0
        }
    }

    data class BlockType(val type: Block.Type) : Editable()
    data class ContentId(val blockIndex: Int, val contentIndex: Int, val id: Int?) : Editable() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ContentId

            return id == other.id
        }

        override fun hashCode(): Int {
            return id ?: 0
        }
    }

    data class ContentFont(val fontType: FontType) : Editable()
    data class ContentText(val blockIndex: Int, val contentIndex: Int, val char: Char) : Editable() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ContentText

            return char == other.char
        }

        override fun hashCode(): Int {
            return char.hashCode()
        }
    }
}

val Edit.Letter.editables: Sequence<Editable>
    get() = object : EditLetterSequence<Editable>() {
        var blockIndex = 0
        var contentIndex = 0

        override suspend fun SequenceScope<Editable>.visit(block: Block) {
            yield(BlockId(index = blockIndex, id = block.id))
            yield(BlockType(block.type))
            block.content.forEach {
                visit(it)
                contentIndex++
            }
            contentIndex = 0
            blockIndex++
        }

        override suspend fun SequenceScope<Editable>.visit(content: Variable) {
            yield(ContentId(blockIndex = blockIndex, contentIndex = contentIndex, id = content.id))
            yield(ContentFont(content.fontType))
            content.text.forEach { yield(ContentText(blockIndex = blockIndex, contentIndex = contentIndex, char = it)) }
        }

        override suspend fun SequenceScope<Editable>.visit(content: Edit.ParagraphContent.Text.Literal) {
            yield(ContentId(blockIndex = blockIndex, contentIndex = contentIndex, id = content.id))
            yield(ContentFont(content.fontType))
            content.text.forEach { yield(ContentText(blockIndex = blockIndex, contentIndex = contentIndex, char = it)) }
        }
    }.build(this)
