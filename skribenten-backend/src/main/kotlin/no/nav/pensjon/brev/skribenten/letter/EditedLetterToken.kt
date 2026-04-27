package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Variable
import no.nav.pensjon.brev.skribenten.letter.TextOnlyWordTokenizer.Token
import no.nav.pensjon.brev.skribenten.letter.TextOnlyWordTokenizer.Token.Block
import no.nav.pensjon.brev.skribenten.letter.TextOnlyWordTokenizer.Token.Content
import no.nav.pensjon.brev.skribenten.letter.TextOnlyWordTokenizer.Token.ContentFont
import no.nav.pensjon.brev.skribenten.letter.TextOnlyWordTokenizer.Token.ContentText

interface EditLetterTokenizer<Token> {
    fun tokenize(letter: Edit.Letter): Sequence<Token>
}

class TextOnlyWordTokenizer : EditLetterTokenizer<Token> {

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
            yield(ContentFont(content.fontType))
            yieldTextEditables(content.editedText ?: content.text)
        }

        private suspend fun SequenceScope<Token>.yieldTextEditables(text: String) =
            text.split(' ').forEach { char -> yield(ContentText(char = char)) }

    }.build(letter)

    sealed class Token {

        data class Block(val id: Int?, val type: Edit.Block.Type) : Token() {
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

        data class Content(val id: Int?, val type: Edit.ParagraphContent.Type) : Token() {
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

        data class ContentFont(val type: FontType) : Token()
        data class ContentText(val char: String) : Token()
    }
}


