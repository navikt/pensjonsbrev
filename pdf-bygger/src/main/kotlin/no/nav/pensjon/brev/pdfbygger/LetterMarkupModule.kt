package no.nav.pensjon.brev.pdfbygger

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.SakspartImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.SignaturImpl


@OptIn(InterneDataklasser::class)
internal object LetterMarkupModule : SimpleModule() {
    private fun readResolve(): Any = LetterMarkupModule

    class DeserializationException(message: String) : Exception(message)

    init {
        addDeserializer(LetterMarkup.Block::class.java, blockDeserializer())
        addDeserializer(LetterMarkup.ParagraphContent::class.java, paragraphContentDeserializer())
        addDeserializer(LetterMarkup.ParagraphContent.Text::class.java, textContentDeserializer())

        addAbstractTypeMapping<LetterMarkup.Sakspart, SakspartImpl>()
        addAbstractTypeMapping<LetterMarkup.Signatur, SignaturImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.ItemList, ParagraphContentImpl.ItemListImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.ItemList.Item, ParagraphContentImpl.ItemListImpl.ItemImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Text.Literal, ParagraphContentImpl.TextImpl.LiteralImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Text.Variable, ParagraphContentImpl.TextImpl.VariableImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Text.NewLine, ParagraphContentImpl.TextImpl.NewLineImpl>()
        addAbstractTypeMapping<LetterMarkup.Attachment, LetterMarkupImpl.AttachmentImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Table, ParagraphContentImpl.TableImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Table.Row, ParagraphContentImpl.TableImpl.RowImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Table.Cell, ParagraphContentImpl.TableImpl.CellImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Table.Header, ParagraphContentImpl.TableImpl.HeaderImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Table.ColumnSpec, ParagraphContentImpl.TableImpl.ColumnSpecImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Form.MultipleChoice.Choice, ParagraphContentImpl.Form.MultipleChoiceImpl.ChoiceImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Form.MultipleChoice, ParagraphContentImpl.Form.MultipleChoiceImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Form.Text, ParagraphContentImpl.Form.TextImpl>()
        // TODO: Bytt tilbake til addAbstractTypeMapping når title som markup er tatt i bruk i brevbaker.
//        addAbstractTypeMapping<LetterMarkup, LetterMarkupImpl>()
        addDeserializer(LetterMarkup::class.java, TitleAsMarkupOrStringDeserializer)
    }

    object TitleAsMarkupOrStringDeserializer : JsonDeserializer<LetterMarkup>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LetterMarkup {
            val node = p.codec.readTree<JsonNode>(p)

            if (node.isObject && node is ObjectNode) {
                val titleNode = node.get("title")
                if (titleNode.isTextual) {
                    node.replace("title", ArrayNode(ctxt.nodeFactory).apply {
                        addObject().apply {
                            put("id", -1)
                            put("text", "")
                            put("editedText", titleNode.textValue())
                            put("type", LetterMarkup.ParagraphContent.Type.LITERAL.name)
                            put("fontType", LetterMarkup.ParagraphContent.Text.FontType.PLAIN.name)
                        }
                    })
                }
            }
            return p.codec.treeToValue(node, LetterMarkupImpl::class.java)
        }
    }

    private fun blockDeserializer() =
        object : StdDeserializer<LetterMarkup.Block>(LetterMarkup.Block::class.java) {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LetterMarkup.Block {
                val node = p.codec.readTree<JsonNode>(p)
                val type = when (LetterMarkup.Block.Type.valueOf(node.get("type").textValue())) {
                    LetterMarkup.Block.Type.TITLE1 -> LetterMarkupImpl.BlockImpl.Title1Impl::class.java
                    LetterMarkup.Block.Type.TITLE2 -> LetterMarkupImpl.BlockImpl.Title2Impl::class.java
                    LetterMarkup.Block.Type.PARAGRAPH -> LetterMarkupImpl.BlockImpl.ParagraphImpl::class.java
                }
                return p.codec.treeToValue(node, type)
            }
        }

    private fun paragraphContentDeserializer() =
        object : StdDeserializer<LetterMarkup.ParagraphContent>(LetterMarkup.ParagraphContent::class.java) {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LetterMarkup.ParagraphContent {
                val node = p.codec.readTree<JsonNode>(p)
                val type = when (LetterMarkup.ParagraphContent.Type.valueOf(node.get("type").textValue())) {
                    LetterMarkup.ParagraphContent.Type.ITEM_LIST -> LetterMarkup.ParagraphContent.ItemList::class.java
                    LetterMarkup.ParagraphContent.Type.LITERAL -> LetterMarkup.ParagraphContent.Text.Literal::class.java
                    LetterMarkup.ParagraphContent.Type.VARIABLE -> LetterMarkup.ParagraphContent.Text.Variable::class.java
                    LetterMarkup.ParagraphContent.Type.TABLE -> LetterMarkup.ParagraphContent.Table::class.java
                    LetterMarkup.ParagraphContent.Type.FORM_TEXT -> LetterMarkup.ParagraphContent.Form.Text::class.java
                    LetterMarkup.ParagraphContent.Type.FORM_CHOICE -> LetterMarkup.ParagraphContent.Form.MultipleChoice::class.java
                    LetterMarkup.ParagraphContent.Type.NEW_LINE -> LetterMarkup.ParagraphContent.Text.NewLine::class.java
                }
                return p.codec.treeToValue(node, type)
            }
        }

    private fun textContentDeserializer() =
        object : StdDeserializer<LetterMarkup.ParagraphContent.Text>(LetterMarkup.ParagraphContent.Text::class.java) {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LetterMarkup.ParagraphContent.Text {
                val node = p.codec.readTree<JsonNode>(p)
                val clazz = when (val contentType = LetterMarkup.ParagraphContent.Type.valueOf(node.get("type").textValue())) {
                    LetterMarkup.ParagraphContent.Type.LITERAL -> LetterMarkup.ParagraphContent.Text.Literal::class.java
                    LetterMarkup.ParagraphContent.Type.VARIABLE -> LetterMarkup.ParagraphContent.Text.Variable::class.java
                    LetterMarkup.ParagraphContent.Type.NEW_LINE -> LetterMarkup.ParagraphContent.Text.NewLine::class.java
                    LetterMarkup.ParagraphContent.Type.TABLE,
                    LetterMarkup.ParagraphContent.Type.FORM_TEXT,
                    LetterMarkup.ParagraphContent.Type.FORM_CHOICE,
                    LetterMarkup.ParagraphContent.Type.ITEM_LIST -> throw DeserializationException("$contentType is not allowed in a text-only block.")
                }
                return p.codec.treeToValue(node, clazz)
            }
        }
}

fun pdfByggerObjectMapper() = jacksonObjectMapper().apply { pdfByggerConfig() }

fun ObjectMapper.pdfByggerConfig() {
    registerModule(JavaTimeModule())
    registerModule(LetterMarkupModule)
    registerModule(PrimitiveModule)
    enable(SerializationFeature.INDENT_OUTPUT)
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
}