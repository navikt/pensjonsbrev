package no.nav.pensjon.brev.skribenten.letter.v2

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import no.nav.brev.brevbaker.markup.Attachment as AttachmentMarkup
import no.nav.brev.brevbaker.markup.LetterMarkup
import java.time.LocalDate

object EditV2 {
    const val VERSION = 2

    data class Letter(
        val title: Title,
        val saksinformasjon: Saksinformasjon,
        val blocks: List<Block>,
        val signatur: Signatur,
        val deletedBlocks: List<Int>,
        val version: Int = VERSION,
    ) {
        init {
            require(version == VERSION) { "EditV2.Letter må ha version=$VERSION, men fikk $version" }
        }

        fun withSaksinformasjon(
            gjelderNavn: String = saksinformasjon.gjelderNavn,
            gjelderPersonidentifikator: String = saksinformasjon.gjelderPersonidentifikator,
            annenMottakerNavn: String? = saksinformasjon.annenMottakerNavn,
            saksnummer: String = saksinformasjon.saksnummer,
            dokumentDato: LocalDate = saksinformasjon.dokumentDato,
        ): Letter = copy(
            saksinformasjon = Saksinformasjon(
                gjelderNavn = gjelderNavn,
                gjelderPersonidentifikator = gjelderPersonidentifikator,
                annenMottakerNavn = annenMottakerNavn,
                saksnummer = saksnummer,
                dokumentDato = dokumentDato,
            )
        )

        fun withSignaturAttestant(attesterendeSaksbehandlerNavn: String?): Letter =
            copy(signatur = signatur.copy(attesterendeSaksbehandlerNavn = attesterendeSaksbehandlerNavn))

        fun withSignaturSaksbehandler(saksbehandlerNavn: String): Letter =
            copy(signatur = signatur.copy(saksbehandlerNavn = saksbehandlerNavn))

        fun merge(rendered: LetterMarkup): Letter = UpdateEditedLetterV2.merge(this, rendered)

        fun updateSaksinformasjonOgSignatur(rendered: LetterMarkup): Letter =
            UpdateEditedLetterV2.updateSaksinformasjonOgSignatur(this, rendered)

        fun toMarkup(): LetterMarkup = EditV2Markup.toMarkup(this)

        fun literals(): List<Text.Literal> =
            object : EditLetterVisitorV2<Text.Literal>(this) {
                override fun visit(text: Text.Literal) = emit(text)
            }.build()

        companion object {
            fun from(markup: LetterMarkup): Letter = EditV2Markup.toEdit(markup)
        }
    }

    data class Attachment(
        val title: Title,
        val blocks: List<Block>,
        val deletedBlocks: List<Int>,
        val inkluderSaksinformasjon: Boolean,
        val version: Int = VERSION,
    ) {
        init {
            require(version == VERSION) { "EditV2.Attachment må ha version=$VERSION, men fikk $version" }
        }

        fun merge(rendered: AttachmentMarkup): Attachment = UpdateEditedLetterV2.merge(this, rendered)

        fun toMarkup(): AttachmentMarkup = EditV2Markup.toMarkup(this)

        companion object {
            fun from(markup: AttachmentMarkup): Attachment = EditV2Markup.toEdit(markup)
        }
    }

    data class Saksinformasjon(
        val gjelderNavn: String,
        val gjelderPersonidentifikator: String,
        val annenMottakerNavn: String?,
        val saksnummer: String,
        val dokumentDato: LocalDate,
    )

    data class Signatur(
        val saksbehandlerNavn: String?,
        val attesterendeSaksbehandlerNavn: String?,
        val navAvsenderEnhet: String,
    )

    interface Identifiable {
        val id: Int?
        val parentId: Int?

        @JsonIgnore
        fun isNew(): Boolean = id == null

        @JsonIgnore
        fun isEdited(): Boolean
    }

    data class Title(
        val text: List<Text>,
        val deletedContent: List<Int> = emptyList(),
    )

    enum class EditBehaviour { FRITEKST, REDIGERBAR_DATA }

    enum class FontType { PLAIN, BOLD, ITALIC }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    @JsonSubTypes(
        JsonSubTypes.Type(value = Block.Title2::class, name = "TITLE2"),
        JsonSubTypes.Type(value = Block.Title3::class, name = "TITLE3"),
        JsonSubTypes.Type(value = Block.Title4::class, name = "TITLE4"),
        JsonSubTypes.Type(value = Block.Paragraph::class, name = "PARAGRAPH"),
        JsonSubTypes.Type(value = Block.ItemList::class, name = "ITEM_LIST"),
        JsonSubTypes.Type(value = Block.NumberedList::class, name = "NUMBERED_LIST"),
        JsonSubTypes.Type(value = Block.Table::class, name = "TABLE"),
    )
    sealed class Block(val type: Type) : Identifiable {
        enum class Type { TITLE2, TITLE3, TITLE4, PARAGRAPH, ITEM_LIST, NUMBERED_LIST, TABLE }

        abstract val originalType: Type?
        abstract val missingFromTemplate: Boolean

        @JsonIgnore
        fun isChangedType(): Boolean = type != (originalType ?: type)

        sealed class TextBlock(type: Type) : Block(type) {
            abstract val content: List<Text>
            abstract val deletedContent: List<Int>

            override fun isEdited(): Boolean =
                isNew() || isChangedType() || content.any { it.isEdited() || it.parentId != id } || deletedContent.isNotEmpty()
        }

        sealed class ListBlock(type: Type) : Block(type) {
            abstract val items: List<Item>
            abstract val deletedItems: List<Int>

            override fun isEdited(): Boolean =
                isNew() || isChangedType() || items.any { it.isEdited() || it.parentId != id } || deletedItems.isNotEmpty()
        }

        data class Title2(
            override val id: Int?,
            override val content: List<Text>,
            override val deletedContent: List<Int> = emptyList(),
            override val originalType: Type? = null,
            override val parentId: Int? = null,
            override val missingFromTemplate: Boolean = false,
        ) : TextBlock(Type.TITLE2)

        data class Title3(
            override val id: Int?,
            override val content: List<Text>,
            override val deletedContent: List<Int> = emptyList(),
            override val originalType: Type? = null,
            override val parentId: Int? = null,
            override val missingFromTemplate: Boolean = false,
        ) : TextBlock(Type.TITLE3)

        data class Title4(
            override val id: Int?,
            override val content: List<Text>,
            override val deletedContent: List<Int> = emptyList(),
            override val originalType: Type? = null,
            override val parentId: Int? = null,
            override val missingFromTemplate: Boolean = false,
        ) : TextBlock(Type.TITLE4)

        data class Paragraph(
            override val id: Int?,
            override val content: List<Text>,
            override val deletedContent: List<Int> = emptyList(),
            override val originalType: Type? = null,
            override val parentId: Int? = null,
            override val missingFromTemplate: Boolean = false,
        ) : TextBlock(Type.PARAGRAPH)

        data class ItemList(
            override val id: Int?,
            override val items: List<Item>,
            override val deletedItems: List<Int> = emptyList(),
            override val originalType: Type? = null,
            override val parentId: Int? = null,
            override val missingFromTemplate: Boolean = false,
        ) : ListBlock(Type.ITEM_LIST)

        data class NumberedList(
            override val id: Int?,
            override val items: List<Item>,
            override val deletedItems: List<Int> = emptyList(),
            override val originalType: Type? = null,
            override val parentId: Int? = null,
            override val missingFromTemplate: Boolean = false,
        ) : ListBlock(Type.NUMBERED_LIST)

        data class Table(
            override val id: Int?,
            val header: Header,
            val rows: List<Row>,
            val deletedRows: List<Int> = emptyList(),
            override val originalType: Type? = null,
            override val parentId: Int? = null,
            override val missingFromTemplate: Boolean = false,
        ) : Block(Type.TABLE) {
            data class Row(
                override val id: Int?,
                val cells: List<Cell>,
                val deletedCells: List<Int> = emptyList(),
                override val parentId: Int? = null,
            ) : Identifiable {
                override fun isEdited(): Boolean = isNew() || cells.any { it.isEdited() || it.parentId != id } || deletedCells.isNotEmpty()
            }

            data class Cell(
                override val id: Int?,
                val content: List<Text>,
                val deletedContent: List<Int> = emptyList(),
                override val parentId: Int? = null,
            ) : Identifiable {
                override fun isEdited(): Boolean = isNew() || content.any { it.isEdited() || it.parentId != id } || deletedContent.isNotEmpty()
            }

            data class Header(
                override val id: Int?,
                val colSpec: List<ColumnSpec>,
                val deletedColSpecs: List<Int> = emptyList(),
                override val parentId: Int? = null,
            ) : Identifiable {
                override fun isEdited(): Boolean = isNew() || colSpec.any { it.isEdited() || it.parentId != id } || deletedColSpecs.isNotEmpty()
            }

            data class ColumnSpec(
                override val id: Int?,
                val content: List<Text>,
                val alignment: ColumnAlignment,
                val span: Int,
                val deletedContent: List<Int> = emptyList(),
                override val parentId: Int? = null,
            ) : Identifiable {
                override fun isEdited(): Boolean = isNew() || content.any { it.isEdited() || it.parentId != id } || deletedContent.isNotEmpty()
            }

            enum class ColumnAlignment { LEFT, RIGHT }

            override fun isEdited(): Boolean =
                isNew() || isChangedType() || deletedRows.isNotEmpty() || rows.any { it.isEdited() || it.parentId != id } || header.isEdited()
        }
    }

    data class Item(
        override val id: Int?,
        val content: List<Text>,
        val deletedContent: List<Int> = emptyList(),
        override val parentId: Int? = null,
    ) : Identifiable {
        override fun isEdited(): Boolean = isNew() || content.any { it.isEdited() || it.parentId != id } || deletedContent.isNotEmpty()
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    @JsonSubTypes(
        JsonSubTypes.Type(value = Text.Literal::class, name = "LITERAL"),
        JsonSubTypes.Type(value = Text.Variable::class, name = "VARIABLE"),
        JsonSubTypes.Type(value = Text.NewLine::class, name = "NEW_LINE"),
    )
    sealed class Text(val type: Type) : Identifiable {
        enum class Type { LITERAL, VARIABLE, NEW_LINE }

        abstract val text: String
        abstract val fontType: FontType

        data class Literal(
            override val id: Int?,
            override val text: String,
            override val fontType: FontType = FontType.PLAIN,
            val editedText: String? = null,
            val editedFontType: FontType? = null,
            val editBehaviour: EditBehaviour? = null,
            override val parentId: Int? = null,
        ) : Text(Type.LITERAL) {
            override fun isEdited(): Boolean = isNew() || editedText != null || editedFontType != null
        }

        data class Variable(
            override val id: Int?,
            override val text: String,
            override val fontType: FontType = FontType.PLAIN,
            val editBehaviour: EditBehaviour? = null,
            override val parentId: Int? = null,
        ) : Text(Type.VARIABLE) {
            override fun isEdited(): Boolean = false
        }

        data class NewLine(override val id: Int?, override val parentId: Int? = null) : Text(Type.NEW_LINE) {
            override val text: String = ""
            override val fontType: FontType = FontType.PLAIN
            override fun isEdited(): Boolean = isNew()
        }
    }
}
