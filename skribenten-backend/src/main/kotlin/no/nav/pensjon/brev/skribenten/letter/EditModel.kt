@file:OptIn(InterneDataklasser::class)

package no.nav.pensjon.brev.skribenten.letter

import com.fasterxml.jackson.annotation.JsonIgnore
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brevbaker.api.model.ElementTags
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Block
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Sakspart
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Signatur
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl
import java.time.LocalDate

object Edit {
    data class Letter(
        val title: Title,
        val sakspart: Sakspart,
        val blocks: List<Block>,
        // TODO: Lag egen edit-modell for signatur slik at saksbehandlerNavn kan påkreves
        val signatur: Signatur,
        val deletedBlocks: Set<Int>
    ) {
        fun withSakspart(
            gjelderNavn: String = sakspart.gjelderNavn,
            gjelderFoedselsnummer: Foedselsnummer = sakspart.gjelderFoedselsnummer,
            annenMottakerNavn: String? = sakspart.annenMottakerNavn,
            saksnummer: String = sakspart.saksnummer,
            dokumentDato: LocalDate = sakspart.dokumentDato,
        ) = copy(
            sakspart = LetterMarkupImpl.SakspartImpl(
                gjelderNavn = gjelderNavn,
                gjelderFoedselsnummer = gjelderFoedselsnummer,
                annenMottakerNavn = annenMottakerNavn,
                saksnummer = saksnummer,
                dokumentDato = dokumentDato,
            )
        )

        fun withSignatur(
            saksbehandler: String = signatur.saksbehandlerNavn!!,
            attestant: String? = signatur.attesterendeSaksbehandlerNavn,
        ) = copy(
            signatur = LetterMarkupImpl.SignaturImpl(
                hilsenTekst = signatur.hilsenTekst,
                saksbehandlerNavn = saksbehandler,
                attesterendeSaksbehandlerNavn = attestant,
                navAvsenderEnhet = signatur.navAvsenderEnhet,
            )
        )
    }

    interface Identifiable {
        val id: Int?
        val parentId: Int?

        @JsonIgnore
        fun isNew(): Boolean = id == null

        @JsonIgnore
        fun isEdited(): Boolean
    }

    data class Title(
        val text: List<Edit.ParagraphContent.Text>,
        val deletedContent: Set<Int> = emptySet(),
    )

    sealed class Block(val type: Type) : Identifiable {
        enum class Type {
            TITLE1, TITLE2, TITLE3, PARAGRAPH,
        }

        abstract val editable: Boolean
        abstract val content: List<ParagraphContent>
        abstract val deletedContent: Set<Int>
        abstract val originalType: Type?

        @JsonIgnore
        fun isChangedType() = type != (originalType ?: type)

        override fun isEdited(): Boolean = isNew() || isChangedType() || content.any { it.isEdited() || it.parentId != id } || deletedContent.isNotEmpty()

        data class Title1(
            override val id: Int?,
            override val editable: Boolean,
            override val content: List<ParagraphContent.Text>,
            override val deletedContent: Set<Int> = emptySet(),
            override val originalType: Type? = null,
            override val parentId: Int? = null,
        ) : Block(Type.TITLE1)

        data class Title2(
            override val id: Int?,
            override val editable: Boolean,
            override val content: List<ParagraphContent.Text>,
            override val deletedContent: Set<Int> = emptySet(),
            override val originalType: Type? = null,
            override val parentId: Int? = null,
        ) : Block(Type.TITLE2)

        data class Title3(
            override val id: Int?,
            override val editable: Boolean,
            override val content: List<ParagraphContent.Text>,
            override val deletedContent: Set<Int> = emptySet(),
            override val originalType: Type? = null,
            override val parentId: Int? = null,
        ) : Block(Type.TITLE3)

        data class Paragraph(
            override val id: Int?,
            override val editable: Boolean,
            override val content: List<ParagraphContent>,
            override val deletedContent: Set<Int> = emptySet(),
            override val originalType: Type? = null,
            override val parentId: Int? = null,
        ) : Block(Type.PARAGRAPH)
    }

    sealed class ParagraphContent(val type: Type) : Identifiable {
        enum class Type {
            ITEM_LIST, LITERAL, VARIABLE, TABLE, NEW_LINE,
        }

        data class ItemList(
            override val id: Int?,
            val items: List<Item>,
            val deletedItems: Set<Int> = emptySet(),
            override val parentId: Int? = null,
        ) : ParagraphContent(Type.ITEM_LIST) {
            data class Item(
                override val id: Int?,
                val content: List<Text>,
                val deletedContent: Set<Int> = emptySet(),
                override val parentId: Int? = null,
            ) : Identifiable {
                override fun isEdited(): Boolean = isNew() || content.any { it.isEdited() || it.parentId != id } || deletedContent.isNotEmpty()
            }

            override fun isEdited(): Boolean = isNew() || items.any { it.isEdited() || it.parentId != id } || deletedItems.isNotEmpty()
        }

        data class Table(
            override val id: Int?,
            val rows: List<Row>,
            val header: Header,
            val deletedRows: Set<Int> = emptySet(),
            override val parentId: Int? = null,
        ) : ParagraphContent(Type.TABLE) {
            data class Row(override val id: Int?, val cells: List<Cell>, override val parentId: Int? = null) : Identifiable {
                override fun isEdited(): Boolean = isNew() || cells.any { it.isEdited() || it.parentId != id }
            }

            data class Cell(override val id: Int?, val text: List<Text>, override val parentId: Int? = null) : Identifiable {
                override fun isEdited(): Boolean = isNew() || text.any { it.isEdited() || it.parentId != id }
            }

            data class Header(override val id: Int?, val colSpec: List<ColumnSpec>, override val parentId: Int? = null) : Identifiable {
                override fun isEdited(): Boolean = isNew() || colSpec.any { it.isEdited() || it.parentId != id }
            }

            data class ColumnSpec(
                override val id: Int?,
                val headerContent: Cell,
                val alignment: ColumnAlignment,
                val span: Int,
                override val parentId: Int? = null,
            ) : Identifiable {
                override fun isEdited(): Boolean = isNew() || headerContent.isEdited()
            }

            enum class ColumnAlignment { LEFT, RIGHT }

            override fun isEdited(): Boolean = isNew() || deletedRows.isNotEmpty() || rows.any { it.isEdited() || it.parentId != id } || header.isEdited()
        }

        sealed class Text(type: Type) : ParagraphContent(type) {
            abstract val text: String
            abstract val fontType: FontType

            enum class FontType { PLAIN, BOLD, ITALIC }

            data class Literal(
                override val id: Int?,
                override val text: String,
                override val fontType: FontType = FontType.PLAIN,
                val editedText: String? = null,
                val editedFontType: FontType? = null,
                val tags: Set<ElementTags> = emptySet(),
                override val parentId: Int? = null,
            ) : Text(Type.LITERAL) {
                override fun isEdited(): Boolean = isNew() || editedText != null || editedFontType != null
            }

            data class Variable(
                override val id: Int?,
                override val text: String,
                override val fontType: FontType = FontType.PLAIN,
                override val parentId: Int? = null,
                val tags: Set<ElementTags> = emptySet(),
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

}

fun LetterMarkup.toEdit(): Edit.Letter =
    Edit.Letter(Edit.Title(title.toEdit(null)), sakspart, blocks.toEdit(), signatur, emptySet())

fun List<Block>.toEdit(): List<Edit.Block> =
    map { it.toEdit() }

fun Block.toEdit(): Edit.Block =
    when (this) {
        is Block.Paragraph -> Edit.Block.Paragraph(id = id, editable = editable, content = content.map { it.toEdit(id) }, parentId = null)
        is Block.Title1 -> Edit.Block.Title1(id = id, editable = editable, content = content.toEdit(id), parentId = null)
        is Block.Title2 -> Edit.Block.Title2(id = id, editable = editable, content = content.toEdit(id), parentId = null)
        is Block.Title3 -> Edit.Block.Title3(id = id, editable = editable, content = content.toEdit(id), parentId = null)
    }

fun List<ParagraphContent.Text>.toEdit(parentId: Int?): List<Edit.ParagraphContent.Text> =
    map { it.toEdit(parentId) }

fun ParagraphContent.toEdit(parentId: Int?): Edit.ParagraphContent =
    when (this) {
        is ParagraphContent.ItemList -> Edit.ParagraphContent.ItemList(id = id, items = items.map { it.toEdit(id) }, parentId = parentId)
        is ParagraphContent.Text -> toEdit(parentId)
        is ParagraphContent.Form -> throw UnsupportedOperationException("Skribenten does not support element type: $type")
        is ParagraphContent.Table -> toEdit(parentId)
    }

fun ParagraphContent.Text.toEdit(parentId: Int?): Edit.ParagraphContent.Text =
    when (this) {
        is ParagraphContent.Text.Literal -> Edit.ParagraphContent.Text.Literal(id = id, text = text, fontType = fontType.toEdit(), tags = tags, parentId = parentId)
        is ParagraphContent.Text.Variable -> Edit.ParagraphContent.Text.Variable(id = id, text = text, fontType = fontType.toEdit(), parentId = parentId, tags = tags)
        is ParagraphContent.Text.NewLine -> Edit.ParagraphContent.Text.NewLine(id = id, parentId = parentId)
    }

fun ParagraphContent.Text.FontType.toEdit(): Edit.ParagraphContent.Text.FontType =
    when (this) {
        ParagraphContent.Text.FontType.PLAIN -> Edit.ParagraphContent.Text.FontType.PLAIN
        ParagraphContent.Text.FontType.BOLD -> Edit.ParagraphContent.Text.FontType.BOLD
        ParagraphContent.Text.FontType.ITALIC -> Edit.ParagraphContent.Text.FontType.ITALIC
    }

fun ParagraphContent.ItemList.Item.toEdit(parentId: Int?): Edit.ParagraphContent.ItemList.Item =
    Edit.ParagraphContent.ItemList.Item(id = id, content = content.toEdit(id), parentId = parentId)

fun ParagraphContent.Table.toEdit(parentId: Int?): Edit.ParagraphContent.Table =
    Edit.ParagraphContent.Table(id = id, rows = rows.map { it.toEdit(id) }, header = header.toEdit(id), parentId = parentId)

fun ParagraphContent.Table.Row.toEdit(parentId: Int?): Edit.ParagraphContent.Table.Row =
    Edit.ParagraphContent.Table.Row(id = id, cells = cells.map { it.toEdit(id) }, parentId = parentId)

fun ParagraphContent.Table.Cell.toEdit(parentId: Int?): Edit.ParagraphContent.Table.Cell =
    Edit.ParagraphContent.Table.Cell(id = id, text = text.toEdit(id), parentId = parentId)

fun ParagraphContent.Table.Header.toEdit(parentId: Int?): Edit.ParagraphContent.Table.Header =
    Edit.ParagraphContent.Table.Header(id = id, colSpec = colSpec.map { it.toEdit(id) }, parentId = parentId)

fun ParagraphContent.Table.ColumnSpec.toEdit(parentId: Int?): Edit.ParagraphContent.Table.ColumnSpec =
    Edit.ParagraphContent.Table.ColumnSpec(id = id, headerContent = headerContent.toEdit(id), alignment = alignment.toEdit(), span = span, parentId = parentId)

fun ParagraphContent.Table.ColumnAlignment.toEdit(): Edit.ParagraphContent.Table.ColumnAlignment =
    when (this) {
        ParagraphContent.Table.ColumnAlignment.LEFT -> Edit.ParagraphContent.Table.ColumnAlignment.LEFT
        ParagraphContent.Table.ColumnAlignment.RIGHT -> Edit.ParagraphContent.Table.ColumnAlignment.RIGHT
    }

fun Edit.Letter.toMarkup(): LetterMarkup =
    LetterMarkupImpl(title = title.text.toMarkup(), sakspart = sakspart, blocks = blocks.map { it.toMarkup() }, signatur = signatur)

fun Edit.Block.toMarkup(): Block =
    when (this) {
        is Edit.Block.Paragraph -> BlockImpl.ParagraphImpl(id = id ?: 0, editable = editable, content = content.map { it.toMarkup() })
        is Edit.Block.Title1 -> BlockImpl.Title1Impl(id = id ?: 0, editable = editable, content = content.toMarkup())
        is Edit.Block.Title2 -> BlockImpl.Title2Impl(id = id ?: 0, editable = editable, content = content.toMarkup())
        is Edit.Block.Title3 -> BlockImpl.Title3Impl(id = id ?: 0, editable = editable, content = content.toMarkup())
    }

fun List<Edit.ParagraphContent.Text>.toMarkup() =
    map { it.toMarkup() }

fun Edit.ParagraphContent.toMarkup(): ParagraphContent =
    when (this) {
        is Edit.ParagraphContent.ItemList -> ParagraphContentImpl.ItemListImpl(id = id ?: 0, items = items.map { it.toMarkup() })
        is Edit.ParagraphContent.Table -> ParagraphContentImpl.TableImpl(id = id ?: 0, rows = rows.map { it.toMarkup() }, header = header.toMarkup())
        is Edit.ParagraphContent.Text -> toMarkup()
    }

fun Edit.ParagraphContent.Text.toMarkup(): ParagraphContent.Text =
    when (this) {
        is Edit.ParagraphContent.Text.Literal -> ParagraphContentImpl.TextImpl.LiteralImpl(
            id = id ?: 0,
            text = editedText ?: text,
            fontType = (editedFontType ?: fontType).toMarkup(),
            tags = ElementTags.forLiteral(tags),
        )

        is Edit.ParagraphContent.Text.Variable -> ParagraphContentImpl.TextImpl.VariableImpl(
            id = id ?: 0,
            text = text,
            fontType = fontType.toMarkup(),
            tags = ElementTags.forVariable(tags)
        )

        is Edit.ParagraphContent.Text.NewLine -> ParagraphContentImpl.TextImpl.NewLineImpl(id = id ?: 0)
    }

fun Edit.ParagraphContent.Text.FontType.toMarkup(): ParagraphContent.Text.FontType =
    when (this) {
        Edit.ParagraphContent.Text.FontType.PLAIN -> ParagraphContent.Text.FontType.PLAIN
        Edit.ParagraphContent.Text.FontType.BOLD -> ParagraphContent.Text.FontType.BOLD
        Edit.ParagraphContent.Text.FontType.ITALIC -> ParagraphContent.Text.FontType.ITALIC
    }

fun Edit.ParagraphContent.ItemList.Item.toMarkup(): ParagraphContent.ItemList.Item =
    ParagraphContentImpl.ItemListImpl.ItemImpl(id = id ?: 0, content = content.toMarkup())

fun Edit.ParagraphContent.Table.Header.toMarkup(): ParagraphContent.Table.Header =
    ParagraphContentImpl.TableImpl.HeaderImpl(id = id ?: 0, colSpec = colSpec.map { it.toMarkup() })

fun Edit.ParagraphContent.Table.ColumnSpec.toMarkup(): ParagraphContent.Table.ColumnSpec =
    ParagraphContentImpl.TableImpl.ColumnSpecImpl(id = id ?: 0, headerContent = headerContent.toMarkup(), alignment = alignment.toMarkup(), span = span)

fun Edit.ParagraphContent.Table.ColumnAlignment.toMarkup(): ParagraphContent.Table.ColumnAlignment =
    when (this) {
        Edit.ParagraphContent.Table.ColumnAlignment.LEFT -> ParagraphContent.Table.ColumnAlignment.LEFT
        Edit.ParagraphContent.Table.ColumnAlignment.RIGHT -> ParagraphContent.Table.ColumnAlignment.RIGHT
    }

fun Edit.ParagraphContent.Table.Row.toMarkup(): ParagraphContent.Table.Row =
    ParagraphContentImpl.TableImpl.RowImpl(id = id ?: 0, cells = cells.map { it.toMarkup() })

fun Edit.ParagraphContent.Table.Cell.toMarkup(): ParagraphContent.Table.Cell =
    ParagraphContentImpl.TableImpl.CellImpl(id = id ?: 0, text = text.toMarkup())




