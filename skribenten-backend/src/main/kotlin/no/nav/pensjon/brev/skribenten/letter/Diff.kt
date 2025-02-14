package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.letter.Edit.Block
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Variable
import no.nav.pensjon.brev.skribenten.letter.Editable.*


sealed class Editable {
    abstract val index: BlockIndex

    interface BlockIndex { val block: Int }
    interface ContentIndex : BlockIndex { val content: Int }
    interface CharIndex : ContentIndex { val offset: Int }

    object Index {
        data class Block(override val block: Int) : BlockIndex
        data class Content(override val block: Int, override val content: Int) : ContentIndex
        data class Char(override val block: Int, override val content: Int, override val offset: Int) : CharIndex
    }

    data class Block(override val index: BlockIndex, val id: Int?, val type: Block.Type) : Editable() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Block

            if (id != other.id) return false
            if (type != other.type) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id ?: 0
            result = 31 * result + type.hashCode()
            return result
        }
    }

//    data class BlockStart(override val index: BlockIndex) : Editable() {
//        override fun equals(other: Any?): Boolean = javaClass == other?.javaClass
//        override fun hashCode(): Int = javaClass.hashCode()
//    }
    @ConsistentCopyVisibility
    data class BlockId private constructor(val id: Int?) : Editable() {
        override lateinit var index: BlockIndex
            private set

        constructor(index: BlockIndex, id: Int?) : this(id) {
            this.index = index
        }
    }

    data class BlockType(override val index: BlockIndex, val type: Block.Type) : Editable() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as BlockType

            return type == other.type
        }

        override fun hashCode(): Int {
            return type.hashCode()
        }
    }

    data class ContentId(override val index: ContentIndex, val id: Int?) : Editable() {
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

    data class ContentFont(override val index: ContentIndex, val fontType: FontType) : Editable() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ContentFont

            return fontType == other.fontType
        }

        override fun hashCode(): Int {
            return fontType.hashCode()
        }
    }

    data class ContentText(override val index: CharIndex, val char: Char) : Editable() {
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
//            yield(BlockStart(Index.Block(blockIndex)))
//            yield(BlockId(index = Index.Block(blockIndex), id = block.id))
//            yield(BlockType(Index.Block(blockIndex), block.type))
            yield(Block(Index.Block(blockIndex), block.id, block.type))
            block.content.forEach {
                visit(it)
                contentIndex++
            }
            contentIndex = 0
            blockIndex++
        }

        override suspend fun SequenceScope<Editable>.visit(content: Variable) {
            yield(ContentId(Index.Content(block = blockIndex, content = contentIndex), id = content.id))
            yield(ContentFont(Index.Content(block = blockIndex, content = contentIndex), content.fontType))
            yieldTextEditables(blockIndex, contentIndex, content.text)
        }

        override suspend fun SequenceScope<Editable>.visit(content: Edit.ParagraphContent.Text.Literal) {
            yield(ContentId(Index.Content(block = blockIndex, content = contentIndex), id = content.id))
            yield(ContentFont(Index.Content(block = blockIndex, content = contentIndex), content.fontType))
            yieldTextEditables(blockIndex, contentIndex, content.text)
        }

        private suspend fun SequenceScope<Editable>.yieldTextEditables(blockIndex: Int, contentIndex: Int, text: String) =
            text.forEachIndexed {  offset, char -> yield(ContentText(Index.Char(block = blockIndex, content = contentIndex, offset = offset), char = char)) }

    }.build(this)

