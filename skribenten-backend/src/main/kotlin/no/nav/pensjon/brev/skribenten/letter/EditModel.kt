package no.nav.pensjon.brev.skribenten.letter

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.pensjon.brev.skribenten.services.BrevbakerServiceException
import no.nav.pensjon.brevbaker.api.model.RenderedLetterMarkdown
import no.nav.pensjon.brevbaker.api.model.RenderedLetterMarkdown.Block
import no.nav.pensjon.brevbaker.api.model.RenderedLetterMarkdown.ParagraphContent

object Edit {
    data class Letter(val blocks: List<Block>, val deletedBlocks: Set<Int>)

    interface Identifiable {
        val id: Int?

        @JsonIgnore
        fun isNew(): Boolean = id == null

        @JsonIgnore
        fun isEdited(): Boolean
    }

    sealed class Block(val type: Type) : Identifiable {
        enum class Type {
            TITLE1, TITLE2, PARAGRAPH,
        }

        abstract val editable: Boolean
        abstract val content: List<ParagraphContent>
        abstract val deletedContent: Set<Int>

        override fun isEdited(): Boolean = isNew() || content.any { it.isEdited() } || deletedContent.isNotEmpty()

        data class Title1(override val id: Int?, override val editable: Boolean, override val content: List<ParagraphContent.Text>, override val deletedContent: Set<Int> = emptySet()) : Block(Type.TITLE1)
        data class Title2(override val id: Int?, override val editable: Boolean, override val content: List<ParagraphContent.Text>, override val deletedContent: Set<Int> = emptySet()) : Block(Type.TITLE2)
        data class Paragraph(override val id: Int?, override val editable: Boolean, override val content: List<ParagraphContent>, override val deletedContent: Set<Int> = emptySet()) : Block(Type.PARAGRAPH)
    }

    sealed class ParagraphContent(val type: Type) : Identifiable {
        enum class Type {
            ITEM_LIST, LITERAL, VARIABLE,
        }

        data class ItemList(override val id: Int?, val items: List<Item>, val deletedItems: Set<Int> = emptySet()) : ParagraphContent(Type.ITEM_LIST) {
            data class Item(override val id: Int?, val content: List<Text>) : Identifiable {
                override fun isEdited(): Boolean = isNew() || content.any { it.isEdited() }
            }

            override fun isEdited(): Boolean = isNew() || items.any { it.isEdited() } || deletedItems.isNotEmpty()
        }

        sealed class Text(type: Type) : ParagraphContent(type) {
            abstract val text: String

            data class Literal(override val id: Int?, override val text: String, val editedText: String? = null) : Text(Type.LITERAL) {
                override fun isEdited(): Boolean = isNew() || editedText != null
            }

            data class Variable(override val id: Int?, override val text: String) : Text(Type.VARIABLE) {
                override fun isEdited(): Boolean = false
            }
        }
    }

    object JacksonModule : SimpleModule() {
        private fun readResolve(): Any = JacksonModule

        init {
            addDeserializer(Block::class.java, blockDeserializer())
            addDeserializer(ParagraphContent::class.java, paragraphContentDeserializer())
            addDeserializer(ParagraphContent.Text::class.java, textContentDeserializer())
        }

        private fun blockDeserializer() = object : StdDeserializer<Block>(Block::class.java) {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Block {
                val node = p.codec.readTree<JsonNode>(p)
                val type = when (Block.Type.valueOf(node.get("type").textValue())) {
                    Block.Type.TITLE1 -> Block.Title1::class.java
                    Block.Type.TITLE2 -> Block.Title2::class.java
                    Block.Type.PARAGRAPH -> Block.Paragraph::class.java
                }
                return p.codec.treeToValue(node, type)
            }
        }

        private fun paragraphContentDeserializer() = object : StdDeserializer<ParagraphContent>(ParagraphContent::class.java) {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ParagraphContent {
                val node = p.codec.readTree<JsonNode>(p)
                val type = when (ParagraphContent.Type.valueOf(node.get("type").textValue())) {
                    ParagraphContent.Type.ITEM_LIST -> ParagraphContent.ItemList::class.java
                    ParagraphContent.Type.LITERAL -> ParagraphContent.Text.Literal::class.java
                    ParagraphContent.Type.VARIABLE -> ParagraphContent.Text.Variable::class.java
                }
                return p.codec.treeToValue(node, type)
            }
        }

        private fun textContentDeserializer() = object : StdDeserializer<ParagraphContent.Text>(ParagraphContent.Text::class.java) {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ParagraphContent.Text {
                val node = p.codec.readTree<JsonNode>(p)
                val type = when (ParagraphContent.Type.valueOf(node.get("type").textValue())) {
                    ParagraphContent.Type.LITERAL -> ParagraphContent.Text.Literal::class.java
                    ParagraphContent.Type.VARIABLE -> ParagraphContent.Text.Variable::class.java
                    ParagraphContent.Type.ITEM_LIST -> throw BrevbakerServiceException("ITEM_LIST is not allowed in a text-only block.")
                }
                return p.codec.treeToValue(node, type)
            }
        }
    }
}

fun RenderedLetterMarkdown.toEdit(): Edit.Letter =
    Edit.Letter(blocks.toEdit(), emptySet())

fun List<Block>.toEdit(): List<Edit.Block> =
    map { it.toEdit() }

fun Block.toEdit(): Edit.Block =
    when (this) {
        is Block.Paragraph -> Edit.Block.Paragraph(id, editable, content.map { it.toEdit() }, emptySet())
        is Block.Title1 -> Edit.Block.Title1(id, editable, content.map { it.toEdit() })
        is Block.Title2 -> Edit.Block.Title2(id, editable, content.map { it.toEdit() })
    }

fun ParagraphContent.toEdit(): Edit.ParagraphContent =
    when (this) {
        is ParagraphContent.ItemList -> Edit.ParagraphContent.ItemList(id, items.map { it.toEdit() })
        is ParagraphContent.Text -> toEdit()
    }

fun ParagraphContent.Text.toEdit(): Edit.ParagraphContent.Text =
    when (this) {
        is ParagraphContent.Text.Literal -> Edit.ParagraphContent.Text.Literal(id, text, null)
        is ParagraphContent.Text.Variable -> Edit.ParagraphContent.Text.Variable(id, text)
    }

fun ParagraphContent.ItemList.Item.toEdit(): Edit.ParagraphContent.ItemList.Item =
    Edit.ParagraphContent.ItemList.Item(id, content.map { it.toEdit() })