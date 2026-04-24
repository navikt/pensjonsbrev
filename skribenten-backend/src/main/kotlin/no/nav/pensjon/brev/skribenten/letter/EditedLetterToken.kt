package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.letter.Edit.Block
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Variable
import no.nav.pensjon.brev.skribenten.letter.EditedLetterToken.*


sealed class EditedLetterToken {

    data class Block(val id: Int?, val type: Block.Type) : EditedLetterToken() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Block

            return type == other.type
        }

        override fun hashCode(): Int {
            return type.hashCode()
        }
    }

    data class Content(val id: Int?, val type: Edit.ParagraphContent.Type) : EditedLetterToken() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Content

            return type == other.type
        }

        override fun hashCode(): Int {
            return type.hashCode()
        }
    }

    data class ContentFont(val type: FontType) : EditedLetterToken() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ContentFont

            return type == other.type
        }

        override fun hashCode(): Int {
            return type.hashCode()
        }
    }

    data class ContentText(val char: Char) : EditedLetterToken() {
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

val Edit.Letter.editables: Sequence<EditedLetterToken>
    get() = object : EditLetterSequence<EditedLetterToken>() {
        override suspend fun SequenceScope<EditedLetterToken>.visit(block: Block) {
            yield(Block(block.id, block.type))
            block.content.forEach {
                visit(it)
            }
        }

        override suspend fun SequenceScope<EditedLetterToken>.visit(content: Variable) {
            yield(Content(id = content.id, type = content.type))
            yield(ContentFont(type = content.fontType))
            yieldTextEditables(content.text)
        }

        override suspend fun SequenceScope<EditedLetterToken>.visit(content: Edit.ParagraphContent.Text.Literal) {
            yield(Content(id = content.id, type = content.type))
            yield(ContentFont(content.fontType))
            yieldTextEditables(content.editedText ?: content.text)
        }

        private suspend fun SequenceScope<EditedLetterToken>.yieldTextEditables(text: String) =
            text.forEach { char -> yield(ContentText(char = char)) }

    }.build(this)

