package no.nav.pensjon.brev.skribenten.serialize

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.pensjon.brev.skribenten.letter.Edit

object EditLetterJacksonModule : SimpleModule() {
    class DeserializationException(msg: String) : Exception(msg)

    // object JacksonModule krever denne.
    @Suppress("unused")
    private fun readResolve(): Any = EditLetterJacksonModule

    init {
        addDeserializer(Edit.Block::class.java, blockDeserializer())
        addDeserializer(Edit.ParagraphContent::class.java, paragraphContentDeserializer())
        addDeserializer(Edit.ParagraphContent.Text::class.java, textContentDeserializer())
    }


    private fun blockDeserializer() = object : StdDeserializer<Edit.Block>(Edit.Block::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Edit.Block {
            val node = p.codec.readTree<JsonNode>(p)
            val type = when (Edit.Block.Type.valueOf(node.get("type").textValue())) {
                Edit.Block.Type.TITLE1 -> Edit.Block.Title1::class.java
                Edit.Block.Type.TITLE2 -> Edit.Block.Title2::class.java
                Edit.Block.Type.PARAGRAPH -> Edit.Block.Paragraph::class.java
            }
            return p.codec.treeToValue(node, type)
        }
    }

    private fun paragraphContentDeserializer() = object : StdDeserializer<Edit.ParagraphContent>(Edit.ParagraphContent::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Edit.ParagraphContent {
            val node = p.codec.readTree<JsonNode>(p)
            val type = when (Edit.ParagraphContent.Type.valueOf(node.get("type").textValue())) {
                Edit.ParagraphContent.Type.ITEM_LIST -> Edit.ParagraphContent.ItemList::class.java
                Edit.ParagraphContent.Type.LITERAL -> Edit.ParagraphContent.Text.Literal::class.java
                Edit.ParagraphContent.Type.NEW_LINE -> Edit.ParagraphContent.Text.NewLine::class.java
                Edit.ParagraphContent.Type.VARIABLE -> Edit.ParagraphContent.Text.Variable::class.java
                Edit.ParagraphContent.Type.TABLE -> Edit.ParagraphContent.Table::class.java
            }
            return p.codec.treeToValue(node, type)
        }
    }

    private fun textContentDeserializer() = object : StdDeserializer<Edit.ParagraphContent.Text>(Edit.ParagraphContent.Text::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Edit.ParagraphContent.Text {
            val node = p.codec.readTree<JsonNode>(p)
            val type = when (Edit.ParagraphContent.Type.valueOf(node.get("type").textValue())) {
                Edit.ParagraphContent.Type.ITEM_LIST -> throw DeserializationException("ITEM_LIST is not allowed in a text-only block.")
                Edit.ParagraphContent.Type.LITERAL -> Edit.ParagraphContent.Text.Literal::class.java
                Edit.ParagraphContent.Type.NEW_LINE -> Edit.ParagraphContent.Text.NewLine::class.java
                Edit.ParagraphContent.Type.VARIABLE -> Edit.ParagraphContent.Text.Variable::class.java
                Edit.ParagraphContent.Type.TABLE -> throw DeserializationException("TABLE is not allowed in a text-only block.")
            }
            return p.codec.treeToValue(node, type)
        }
    }
}