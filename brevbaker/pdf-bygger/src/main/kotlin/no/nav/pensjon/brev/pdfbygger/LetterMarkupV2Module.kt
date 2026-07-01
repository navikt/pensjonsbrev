package no.nav.pensjon.brev.pdfbygger

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2Impl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2Impl.BlockImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2Impl.TextImpl


@OptIn(InterneDataklasser::class)
internal object LetterMarkupV2Module : SimpleModule() {
    private fun readResolve(): Any = LetterMarkupV2Module

    init {
        addDeserializer(LetterMarkupV2.Block::class.java, blockDeserializer())
        addDeserializer(LetterMarkupV2.Text::class.java, textDeserializer())

        addAbstractTypeMapping<LetterMarkupV2.Saksinformasjon, LetterMarkupV2Impl.SaksinformasjonImpl>()
        addAbstractTypeMapping<LetterMarkupV2.Signatur, LetterMarkupV2Impl.SignaturImpl>()
        addAbstractTypeMapping<LetterMarkupV2.SaksbehandlerSignatur, LetterMarkupV2Impl.SaksbehandlerSignaturImpl>()
        addAbstractTypeMapping<LetterMarkupV2.Block.ListContent.Item, BlockImpl.ItemListImpl.ItemImpl>()
        addAbstractTypeMapping<LetterMarkupV2.Text.Literal, TextImpl.LiteralImpl>()
        addAbstractTypeMapping<LetterMarkupV2.Text.Variable, TextImpl.VariableImpl>()
        addAbstractTypeMapping<LetterMarkupV2.Text.NewLine, TextImpl.NewLineImpl>()
        addAbstractTypeMapping<LetterMarkupV2.Attachment, LetterMarkupV2Impl.AttachmentImpl>()
        addAbstractTypeMapping<LetterMarkupV2.Block.Table, BlockImpl.TableImpl>()
        addAbstractTypeMapping<LetterMarkupV2.Block.Table.Row, BlockImpl.TableImpl.RowImpl>()
        addAbstractTypeMapping<LetterMarkupV2.Block.Table.Cell, BlockImpl.TableImpl.CellImpl>()
        addAbstractTypeMapping<LetterMarkupV2.Block.Table.Header, BlockImpl.TableImpl.HeaderImpl>()
        addAbstractTypeMapping<LetterMarkupV2.Block.Table.ColumnSpec, BlockImpl.TableImpl.ColumnSpecImpl>()
        addAbstractTypeMapping<LetterMarkupV2, LetterMarkupV2Impl>()
    }

    private fun blockDeserializer() =
        object : StdDeserializer<LetterMarkupV2.Block>(LetterMarkupV2.Block::class.java) {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LetterMarkupV2.Block {
                val node = p.codec.readTree<JsonNode>(p)
                val type = when (LetterMarkupV2.Block.Type.valueOf(node.get("type").textValue())) {
                    LetterMarkupV2.Block.Type.TITLE2 -> BlockImpl.Title2Impl::class.java
                    LetterMarkupV2.Block.Type.TITLE3 -> BlockImpl.Title3Impl::class.java
                    LetterMarkupV2.Block.Type.TITLE4 -> BlockImpl.Title4Impl::class.java
                    LetterMarkupV2.Block.Type.PARAGRAPH -> BlockImpl.ParagraphImpl::class.java
                    LetterMarkupV2.Block.Type.ITEM_LIST -> BlockImpl.ItemListImpl::class.java
                    LetterMarkupV2.Block.Type.NUMBERED_LIST -> BlockImpl.NumberedListImpl::class.java
                    LetterMarkupV2.Block.Type.TABLE -> BlockImpl.TableImpl::class.java
                }
                return p.codec.treeToValue(node, type)
            }
        }

    private fun textDeserializer() =
        object : StdDeserializer<LetterMarkupV2.Text>(LetterMarkupV2.Text::class.java) {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LetterMarkupV2.Text {
                val node = p.codec.readTree<JsonNode>(p)
                val type = when (LetterMarkupV2.Text.Type.valueOf(node.get("type").textValue())) {
                    LetterMarkupV2.Text.Type.LITERAL -> TextImpl.LiteralImpl::class.java
                    LetterMarkupV2.Text.Type.VARIABLE -> TextImpl.VariableImpl::class.java
                    LetterMarkupV2.Text.Type.NEW_LINE -> TextImpl.NewLineImpl::class.java
                }
                return p.codec.treeToValue(node, type)
            }
        }
}
