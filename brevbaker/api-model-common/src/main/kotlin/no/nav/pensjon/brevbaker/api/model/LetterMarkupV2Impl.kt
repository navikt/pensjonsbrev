package no.nav.pensjon.brevbaker.api.model

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Block
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Block.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Block.ListContent.ItemList
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Block.ListContent.NumberedList
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Block.Table
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Text
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Text.FontType
import java.time.LocalDate

@Suppress("unused")
@InterneDataklasser
data class LetterMarkupWithDataUsageV2Impl(
    override val markup: LetterMarkupV2,
    override val letterDataUsage: Set<LetterMarkupWithDataUsageV2.Property>,
    override val brevtype: LetterMetadata.Brevtype,
) : LetterMarkupWithDataUsageV2 {

    @InterneDataklasser
    data class PropertyImpl(override val typeName: String, override val propertyName: String) : LetterMarkupWithDataUsageV2.Property
}

@Suppress("unused")
@InterneDataklasser
data class LetterMarkupV2Impl(
    override val title1: List<Text>,
    override val saksinformasjon: LetterMarkupV2.Saksinformasjon,
    override val blocks: List<Block>,
    override val signatur: LetterMarkupV2.Signatur,
) : LetterMarkupV2 {

    @InterneDataklasser
    data class AttachmentImpl(
        override val title1: List<Text>,
        override val blocks: List<Block>,
        override val inkluderSaksinformasjon: Boolean,
    ) : LetterMarkupV2.Attachment

    @InterneDataklasser
    data class SaksinformasjonImpl(
        override val gjelderNavn: String,
        override val gjelderFoedselsnummer: Foedselsnummer,
        override val annenMottakerNavn: String?,
        override val saksnummer: LetterMarkupV2.Saksnummer,
        override val dokumentDato: LocalDate,
    ) : LetterMarkupV2.Saksinformasjon

    @InterneDataklasser
    data class SignaturImpl(
        override val hilsenTekst: String,
        override val saksbehandlerSignatur: LetterMarkupV2.SaksbehandlerSignatur?,
        override val navAvsenderEnhet: String,
    ) : LetterMarkupV2.Signatur

    @InterneDataklasser
    data class SaksbehandlerSignaturImpl(
        override val saksbehandlerNavn: String,
        override val attesterendeSaksbehandlerNavn: String?,
    ) : LetterMarkupV2.SaksbehandlerSignatur

    object BlockImpl {

        @InterneDataklasser
        data class Title2Impl(override val id: Int, override val content: List<Text>) : Title.Title2 {
            override val type = Type.TITLE2
        }

        @InterneDataklasser
        data class Title3Impl(override val id: Int, override val content: List<Text>) : Title.Title3 {
            override val type = Type.TITLE3
        }

        @InterneDataklasser
        data class Title4Impl(override val id: Int, override val content: List<Text>) : Title.Title4 {
            override val type = Type.TITLE4
        }

        @InterneDataklasser
        data class ParagraphImpl(override val id: Int, override val content: List<Text>) : Paragraph {
            override val type = Type.PARAGRAPH
        }

        @InterneDataklasser
        data class ItemListImpl(override val id: Int, override val items: List<ListContent.Item>) : ItemList {
            override val type = Type.ITEM_LIST

            @InterneDataklasser
            data class ItemImpl(override val id: Int, override val content: List<Text>) : ListContent.Item
        }

        @InterneDataklasser
        data class NumberedListImpl(override val id: Int, override val items: List<ListContent.Item>) : NumberedList {
            override val type = Type.NUMBERED_LIST
        }

        @InterneDataklasser
        data class TableImpl(override val id: Int, override val rows: List<Table.Row>, override val header: Table.Header) : Table {
            override val type = Type.TABLE

            @InterneDataklasser
            data class RowImpl(override val id: Int, override val cells: List<Table.Cell>) : Table.Row
            @InterneDataklasser
            data class CellImpl(override val id: Int, override val text: List<Text>) : Table.Cell
            @InterneDataklasser
            data class HeaderImpl(override val id: Int, override val colSpec: List<Table.ColumnSpec>) : Table.Header
            @InterneDataklasser
            data class ColumnSpecImpl(
                override val id: Int,
                override val headerContent: Table.Cell,
                override val alignment: Table.ColumnAlignment,
                override val span: Int,
            ) : Table.ColumnSpec
        }
    }

    object TextImpl {
        @InterneDataklasser
        data class LiteralImpl(
            override val id: Int,
            override val text: String,
            override val fontType: FontType = FontType.PLAIN,
            override val tags: Set<ElementTags> = emptySet(),
        ) : Text.Literal {
            override val type: Text.Type = Text.Type.LITERAL
        }

        @InterneDataklasser
        data class VariableImpl(
            override val id: Int,
            override val text: String,
            override val fontType: FontType = FontType.PLAIN,
            override val tags: Set<ElementTags> = emptySet(),
        ) : Text.Variable {
            override val type = Text.Type.VARIABLE
        }

        @InterneDataklasser
        data class NewLineImpl(override val id: Int) : Text.NewLine {
            override val fontType = FontType.PLAIN
            override val text: String = ""
            override val type = Text.Type.NEW_LINE
        }
    }
}
