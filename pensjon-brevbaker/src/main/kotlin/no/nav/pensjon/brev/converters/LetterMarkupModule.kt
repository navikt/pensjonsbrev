package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl


@OptIn(InterneDataklasser::class)
object LetterMarkupModule : SimpleModule() {
    private fun readResolve(): Any = LetterMarkupModule

    class DeserializationException(message: String): Exception(message)

    init {
        addDeserializer(LetterMarkup.Block::class.java, blockDeserializer())
        addDeserializer(LetterMarkup.ParagraphContent::class.java, paragraphContentDeserializer())
        addDeserializer(LetterMarkup.ParagraphContent.Text::class.java, textContentDeserializer())

        settOppDeserialiseringFraInterfaceTilImplementasjon()
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
                    LetterMarkup.ParagraphContent.Type.FORM_TEXT -> ParagraphContentImpl.Form.TextImpl::class.java
                    LetterMarkup.ParagraphContent.Type.FORM_CHOICE -> ParagraphContentImpl.Form.MultipleChoiceImpl::class.java
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

    private fun settOppDeserialiseringFraInterfaceTilImplementasjon() {
        addDeserializer(LetterMarkup.Sakspart::class.java, object :
            FellesDeserializer<LetterMarkup.Sakspart, LetterMarkupImpl.SakspartImpl>(LetterMarkupImpl.SakspartImpl::class.java) {}
        )
        addDeserializer(LetterMarkup.Signatur::class.java, object :
            FellesDeserializer<LetterMarkup.Signatur, LetterMarkupImpl.SignaturImpl>(LetterMarkupImpl.SignaturImpl::class.java) {}
        )

        addDeserializer(LetterMarkup.ParagraphContent.ItemList::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.ItemList, ParagraphContentImpl.ItemListImpl>(
                ParagraphContentImpl.ItemListImpl::class.java
            ) {}
        )

        addDeserializer(LetterMarkup.ParagraphContent.ItemList.Item::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.ItemList.Item, ParagraphContentImpl.ItemListImpl.ItemImpl>(
                ParagraphContentImpl.ItemListImpl.ItemImpl::class.java
            ) {}
        )

        addDeserializer(LetterMarkup.ParagraphContent.Text.Literal::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.Text.Literal, ParagraphContentImpl.TextImpl.LiteralImpl>(
                ParagraphContentImpl.TextImpl.LiteralImpl::class.java
            ) {}
        )

        addDeserializer(LetterMarkup.ParagraphContent.Text.Variable::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.Text.Variable, ParagraphContentImpl.TextImpl.VariableImpl>(
                ParagraphContentImpl.TextImpl.VariableImpl::class.java
            ) {}
        )

        addDeserializer(LetterMarkup.ParagraphContent.Text.NewLine::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.Text.NewLine, ParagraphContentImpl.TextImpl.NewLineImpl>(
                ParagraphContentImpl.TextImpl.NewLineImpl::class.java
            ) {}
        )

        addDeserializer(LetterMarkup.Attachment::class.java, object :
            FellesDeserializer<LetterMarkup.Attachment, LetterMarkupImpl.AttachmentImpl>(LetterMarkupImpl.AttachmentImpl::class.java) {}
        )

        addDeserializer(LetterMarkup.ParagraphContent.Table::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.Table, ParagraphContentImpl.TableImpl>(ParagraphContentImpl.TableImpl::class.java) {}
        )
        addDeserializer(LetterMarkup.ParagraphContent.Table.Row::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.Table.Row, ParagraphContentImpl.TableImpl.RowImpl>(
                ParagraphContentImpl.TableImpl.RowImpl::class.java
            ) {}
        )
        addDeserializer(LetterMarkup.ParagraphContent.Table.Cell::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.Table.Cell, ParagraphContentImpl.TableImpl.CellImpl>(
                ParagraphContentImpl.TableImpl.CellImpl::class.java
            ) {}
        )
        addDeserializer(LetterMarkup.ParagraphContent.Table.Header::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.Table.Header, ParagraphContentImpl.TableImpl.HeaderImpl>(
                ParagraphContentImpl.TableImpl.HeaderImpl::class.java
            ) {}
        )
        addDeserializer(LetterMarkup.ParagraphContent.Table.ColumnSpec::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.Table.ColumnSpec, ParagraphContentImpl.TableImpl.ColumnSpecImpl>(
                ParagraphContentImpl.TableImpl.ColumnSpecImpl::class.java
            ) {}
        )
        addDeserializer(LetterMarkup.ParagraphContent.Form.MultipleChoice.Choice::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.Form.MultipleChoice.Choice, ParagraphContentImpl.Form.MultipleChoiceImpl.ChoiceImpl>(ParagraphContentImpl.Form.MultipleChoiceImpl.ChoiceImpl::class.java) {}
        )

        addDeserializer(LetterMarkup::class.java, object :
            FellesDeserializer<LetterMarkup, LetterMarkupImpl>(LetterMarkupImpl::class.java) {}
        )
    }

    private abstract class FellesDeserializer<T, V : T>(private val v: Class<V>) : JsonDeserializer<T>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): T =
            parser.codec.treeToValue(parser.codec.readTree<JsonNode>(parser), v)
    }
}