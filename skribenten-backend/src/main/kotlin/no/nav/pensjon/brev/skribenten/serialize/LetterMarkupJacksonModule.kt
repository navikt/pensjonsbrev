package no.nav.pensjon.brev.skribenten.serialize

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.skribenten.services.BrevbakerServiceException
import no.nav.pensjon.brev.skribenten.services.addAbstractTypeMapping
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsageImpl

@OptIn(InterneDataklasser::class)
internal object LetterMarkupJacksonModule : SimpleModule() {
    @Suppress("unused")
    private fun readResolve(): Any = LetterMarkupJacksonModule

    init {
        addDeserializer(LetterMarkup.Block::class.java, blockDeserializer())
        addDeserializer(LetterMarkup.ParagraphContent::class.java, paragraphContentDeserializer())
        addDeserializer(LetterMarkup.ParagraphContent.Text::class.java, textContentDeserializer())

        addAbstractTypeMapping<LetterMarkup.Sakspart, LetterMarkupImpl.SakspartImpl>()
        addAbstractTypeMapping<LetterMarkup.Signatur, LetterMarkupImpl.SignaturImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.ItemList, LetterMarkupImpl.ParagraphContentImpl.ItemListImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.ItemList.Item, LetterMarkupImpl.ParagraphContentImpl.ItemListImpl.ItemImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Text.Literal, LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Text.Variable, LetterMarkupImpl.ParagraphContentImpl.TextImpl.VariableImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Text.NewLine, LetterMarkupImpl.ParagraphContentImpl.TextImpl.NewLineImpl>()
        addAbstractTypeMapping<LetterMarkup.Attachment, LetterMarkupImpl.AttachmentImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Table, LetterMarkupImpl.ParagraphContentImpl.TableImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Table.Row, LetterMarkupImpl.ParagraphContentImpl.TableImpl.RowImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Table.Cell, LetterMarkupImpl.ParagraphContentImpl.TableImpl.CellImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Table.Header, LetterMarkupImpl.ParagraphContentImpl.TableImpl.HeaderImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Table.ColumnSpec, LetterMarkupImpl.ParagraphContentImpl.TableImpl.ColumnSpecImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Form.MultipleChoice.Choice, LetterMarkupImpl.ParagraphContentImpl.Form.MultipleChoiceImpl.ChoiceImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Form.MultipleChoice, LetterMarkupImpl.ParagraphContentImpl.Form.MultipleChoiceImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Form.Text, LetterMarkupImpl.ParagraphContentImpl.Form.TextImpl>()
        addAbstractTypeMapping<LetterMarkup, LetterMarkupImpl>()
        addAbstractTypeMapping<LetterMarkupWithDataUsage, LetterMarkupWithDataUsageImpl>()
        addAbstractTypeMapping<LetterMarkupWithDataUsage.Property, LetterMarkupWithDataUsageImpl.PropertyImpl>()
    }

    private fun blockDeserializer() =
        object : StdDeserializer<LetterMarkup.Block>(LetterMarkup.Block::class.java) {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LetterMarkup.Block {
                val node = p.codec.readTree<JsonNode>(p)
                val type = when (LetterMarkup.Block.Type.valueOf(node.get("type").textValue())) {
                    LetterMarkup.Block.Type.TITLE1 -> LetterMarkupImpl.BlockImpl.Title1Impl::class.java
                    LetterMarkup.Block.Type.TITLE2 -> LetterMarkupImpl.BlockImpl.Title2Impl::class.java
                    LetterMarkup.Block.Type.TITLE3 -> LetterMarkupImpl.BlockImpl.Title3Impl::class.java
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
                    LetterMarkup.ParagraphContent.Type.FORM_TEXT -> LetterMarkupImpl.ParagraphContentImpl.Form.TextImpl::class.java
                    LetterMarkup.ParagraphContent.Type.FORM_CHOICE -> LetterMarkupImpl.ParagraphContentImpl.Form.MultipleChoiceImpl::class.java
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
                    LetterMarkup.ParagraphContent.Type.ITEM_LIST -> throw BrevbakerServiceException("$contentType is not allowed in a text-only block.")
                }
                return p.codec.treeToValue(node, clazz)
            }
        }
}