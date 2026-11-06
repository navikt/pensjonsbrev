@file:OptIn(MarkupModelApi::class)

package no.nav.pensjon.brev.skribenten.letter.v2

import no.nav.brev.brevbaker.markup.Attachment
import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.brev.brevbaker.markup.MarkupModel
import no.nav.brev.brevbaker.markup.MarkupModelApi
import no.nav.brev.brevbaker.markup.Saksinformasjon
import no.nav.brev.brevbaker.markup.Signatur
import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.EditBehaviour
import no.nav.brev.brevbaker.markup.outline.Text
import java.time.LocalDate

object MarkupV2 {
    fun literal(id: Int, text: String, fontType: Text.FontType = Text.FontType.PLAIN, editBehaviour: EditBehaviour? = null): Text.Literal =
        MarkupModel.literal(id, text, fontType, editBehaviour)

    fun variable(id: Int, text: String, fontType: Text.FontType = Text.FontType.PLAIN, editBehaviour: EditBehaviour? = null): Text.Variable =
        MarkupModel.variable(id, text, fontType, editBehaviour)

    fun newLine(id: Int): Text.NewLine = MarkupModel.newLine(id)

    fun title2(id: Int, vararg content: Text): Block.Title2 = MarkupModel.title2(id, content.toList())
    fun title3(id: Int, vararg content: Text): Block.Title3 = MarkupModel.title3(id, content.toList())
    fun title4(id: Int, vararg content: Text): Block.Title4 = MarkupModel.title4(id, content.toList())
    fun paragraph(id: Int, vararg content: Text): Block.Paragraph = MarkupModel.paragraph(id, content.toList())
    fun itemList(id: Int, vararg items: Block.Item): Block.ItemList = MarkupModel.itemList(id, items.toList())
    fun numberedList(id: Int, vararg items: Block.Item): Block.NumberedList = MarkupModel.numberedList(id, items.toList())
    fun item(id: Int, vararg content: Text): Block.Item = MarkupModel.item(id, content.toList())

    fun table(id: Int, header: Block.Table.Header, vararg rows: Block.Table.Row): Block.Table = MarkupModel.table(id, rows.toList(), header)
    fun header(id: Int, vararg colSpec: Block.Table.ColumnSpec): Block.Table.Header = MarkupModel.header(id, colSpec.toList())
    fun columnSpec(id: Int, vararg content: Text, alignment: Block.Table.ColumnAlignment = Block.Table.ColumnAlignment.LEFT, span: Int = 1): Block.Table.ColumnSpec =
        MarkupModel.columnSpec(id, content.toList(), alignment, span)
    fun row(id: Int, vararg cells: Block.Table.Cell): Block.Table.Row = MarkupModel.row(id, cells.toList())
    fun cell(id: Int, vararg content: Text): Block.Table.Cell = MarkupModel.cell(id, content.toList())

    fun saksinformasjon(
        gjelderNavn: String = "Test Testeson",
        gjelderPersonidentifikator: String = "1234568910",
        annenMottakerNavn: String? = null,
        saksnummer: String = "1234",
        dokumentDato: LocalDate = LocalDate.now(),
    ): Saksinformasjon = MarkupModel.saksinformasjon(gjelderNavn, gjelderPersonidentifikator, annenMottakerNavn, saksnummer, dokumentDato)

    fun signatur(
        saksbehandlerNavn: String? = "Kjersti Saksbehandler",
        attesterendeSaksbehandlerNavn: String? = null,
        navAvsenderEnhet: String = "Nav Familie- og pensjonsytelser Porsgrunn",
    ): Signatur = MarkupModel.signatur(navAvsenderEnhet, saksbehandlerNavn?.let { MarkupModel.saksbehandlerSignatur(it, attesterendeSaksbehandlerNavn) })
}

fun letter(
    vararg blocks: Block,
    title: String = "En tittel",
    saksinformasjon: Saksinformasjon = MarkupV2.saksinformasjon(),
    signatur: Signatur = MarkupV2.signatur(),
): LetterMarkup =
    MarkupModel.letterMarkup(
        title1 = listOf(MarkupModel.literal(1, title)),
        saksinformasjon = saksinformasjon,
        blocks = blocks.toList(),
        signatur = signatur,
    )

fun attachment(vararg blocks: Block, inkluderSaksinformasjon: Boolean = false, title: String = "En vedleggstittel"): Attachment =
    MarkupModel.attachment(
        title1 = listOf(MarkupModel.literal(1, title)),
        blocks = blocks.toList(),
        inkluderSaksinformasjon = inkluderSaksinformasjon,
    )

@DslMarker
annotation class EditV2Dsl

@EditV2Dsl
class TextContentBuilderV2 {
    val texts = mutableListOf<EditV2.Text>()

    fun literal(
        id: Int? = null,
        text: String,
        fontType: EditV2.FontType = EditV2.FontType.PLAIN,
        editedText: String? = null,
        editedFontType: EditV2.FontType? = null,
        editBehaviour: EditV2.EditBehaviour? = null,
        parentId: Int? = null,
    ) {
        texts += EditV2.Text.Literal(id, text, fontType, editedText, editedFontType, editBehaviour, parentId)
    }

    fun variable(
        id: Int? = null,
        text: String,
        fontType: EditV2.FontType = EditV2.FontType.PLAIN,
        editBehaviour: EditV2.EditBehaviour? = null,
        parentId: Int? = null,
    ) {
        texts += EditV2.Text.Variable(id, text, fontType, editBehaviour, parentId)
    }

    fun newLine(id: Int? = null, parentId: Int? = null) {
        texts += EditV2.Text.NewLine(id, parentId)
    }
}

@EditV2Dsl
class ItemListBuilderV2 {
    val items = mutableListOf<EditV2.Item>()

    fun item(id: Int? = null, deletedContent: Collection<Int> = emptyList(), parentId: Int? = null, builder: TextContentBuilderV2.() -> Unit = {}) {
        items += EditV2.Item(id = id, content = TextContentBuilderV2().apply(builder).texts, deletedContent = deletedContent.toList(), parentId = parentId)
    }
}

@EditV2Dsl
class TableBuilderV2 {
    var header: EditV2.Block.Table.Header? = null
    val rows = mutableListOf<EditV2.Block.Table.Row>()

    fun header(id: Int? = null, deletedColSpecs: Collection<Int> = emptyList(), builder: HeaderBuilderV2.() -> Unit) {
        check(header == null) { "header already set" }
        header = EditV2.Block.Table.Header(id = id, colSpec = HeaderBuilderV2().apply(builder).colSpecs, deletedColSpecs = deletedColSpecs.toList())
    }

    fun row(id: Int? = null, deletedCells: Collection<Int> = emptyList(), parentId: Int? = null, builder: RowBuilderV2.() -> Unit) {
        rows += EditV2.Block.Table.Row(id = id, cells = RowBuilderV2().apply(builder).cells, deletedCells = deletedCells.toList(), parentId = parentId)
    }
}

@EditV2Dsl
class HeaderBuilderV2 {
    val colSpecs = mutableListOf<EditV2.Block.Table.ColumnSpec>()

    fun colSpec(
        id: Int? = null,
        alignment: EditV2.Block.Table.ColumnAlignment = EditV2.Block.Table.ColumnAlignment.LEFT,
        span: Int = 1,
        deletedContent: Collection<Int> = emptyList(),
        builder: TextContentBuilderV2.() -> Unit = {},
    ) {
        colSpecs += EditV2.Block.Table.ColumnSpec(
            id = id,
            content = TextContentBuilderV2().apply(builder).texts,
            alignment = alignment,
            span = span,
            deletedContent = deletedContent.toList(),
        )
    }
}

@EditV2Dsl
class RowBuilderV2 {
    val cells = mutableListOf<EditV2.Block.Table.Cell>()

    fun cell(id: Int? = null, deletedContent: Collection<Int> = emptyList(), parentId: Int? = null, builder: TextContentBuilderV2.() -> Unit = {}) {
        cells += EditV2.Block.Table.Cell(id = id, content = TextContentBuilderV2().apply(builder).texts, deletedContent = deletedContent.toList(), parentId = parentId)
    }
}

@EditV2Dsl
class EditLetterBuilderV2 {
    val blocks = mutableListOf<EditV2.Block>()

    fun title2(
        id: Int? = null,
        missingFromTemplate: Boolean = false,
        deletedContent: Collection<Int> = emptyList(),
        originalType: EditV2.Block.Type? = null,
        parentId: Int? = null,
        builder: TextContentBuilderV2.() -> Unit,
    ) {
        blocks += EditV2.Block.Title2(id, TextContentBuilderV2().apply(builder).texts, deletedContent.toList(), originalType, parentId, missingFromTemplate)
    }

    fun title3(
        id: Int? = null,
        missingFromTemplate: Boolean = false,
        deletedContent: Collection<Int> = emptyList(),
        originalType: EditV2.Block.Type? = null,
        parentId: Int? = null,
        builder: TextContentBuilderV2.() -> Unit,
    ) {
        blocks += EditV2.Block.Title3(id, TextContentBuilderV2().apply(builder).texts, deletedContent.toList(), originalType, parentId, missingFromTemplate)
    }

    fun title4(
        id: Int? = null,
        missingFromTemplate: Boolean = false,
        deletedContent: Collection<Int> = emptyList(),
        originalType: EditV2.Block.Type? = null,
        parentId: Int? = null,
        builder: TextContentBuilderV2.() -> Unit,
    ) {
        blocks += EditV2.Block.Title4(id, TextContentBuilderV2().apply(builder).texts, deletedContent.toList(), originalType, parentId, missingFromTemplate)
    }

    fun paragraph(
        id: Int? = null,
        missingFromTemplate: Boolean = false,
        deletedContent: Collection<Int> = emptyList(),
        originalType: EditV2.Block.Type? = null,
        parentId: Int? = null,
        builder: TextContentBuilderV2.() -> Unit,
    ) {
        blocks += EditV2.Block.Paragraph(id, TextContentBuilderV2().apply(builder).texts, deletedContent.toList(), originalType, parentId, missingFromTemplate)
    }

    fun itemList(
        id: Int? = null,
        missingFromTemplate: Boolean = false,
        deletedItems: Collection<Int> = emptyList(),
        originalType: EditV2.Block.Type? = null,
        parentId: Int? = null,
        builder: ItemListBuilderV2.() -> Unit,
    ) {
        blocks += EditV2.Block.ItemList(id, ItemListBuilderV2().apply(builder).items, deletedItems.toList(), originalType, parentId, missingFromTemplate)
    }

    fun numberedList(
        id: Int? = null,
        missingFromTemplate: Boolean = false,
        deletedItems: Collection<Int> = emptyList(),
        originalType: EditV2.Block.Type? = null,
        parentId: Int? = null,
        builder: ItemListBuilderV2.() -> Unit,
    ) {
        blocks += EditV2.Block.NumberedList(id, ItemListBuilderV2().apply(builder).items, deletedItems.toList(), originalType, parentId, missingFromTemplate)
    }

    fun table(
        id: Int? = null,
        missingFromTemplate: Boolean = false,
        deletedRows: Collection<Int> = emptyList(),
        originalType: EditV2.Block.Type? = null,
        parentId: Int? = null,
        builder: TableBuilderV2.() -> Unit,
    ) {
        val tb = TableBuilderV2().apply(builder)
        blocks += EditV2.Block.Table(
            id = id,
            header = requireNotNull(tb.header) { "table must have a header" },
            rows = tb.rows,
            deletedRows = deletedRows.toList(),
            originalType = originalType,
            parentId = parentId,
            missingFromTemplate = missingFromTemplate,
        )
    }
}

fun editLetterBlocksV2(builder: EditLetterBuilderV2.() -> Unit): List<EditV2.Block> =
    EditLetterBuilderV2().apply(builder).blocks.map { it.fixParentIds() }

fun editedLetter(
    vararg blocks: EditV2.Block,
    deleted: Collection<Int> = emptyList(),
    fixParentIds: Boolean = true,
    title: String = "En tittel",
    saksinformasjon: EditV2.Saksinformasjon = MarkupV2.saksinformasjon().toEdit(),
    signatur: EditV2.Signatur = MarkupV2.signatur().toEdit(),
): EditV2.Letter =
    EditV2.Letter(
        title = EditV2.Title(listOf(EditV2.Text.Literal(1, title))),
        saksinformasjon = saksinformasjon,
        blocks = if (fixParentIds) blocks.map { it.fixParentIds() } else blocks.toList(),
        signatur = signatur,
        deletedBlocks = deleted.toList(),
    )

fun editedLetter(
    deleted: Collection<Int> = emptyList(),
    fixParentIds: Boolean = true,
    title: String = "En tittel",
    saksinformasjon: EditV2.Saksinformasjon = MarkupV2.saksinformasjon().toEdit(),
    signatur: EditV2.Signatur = MarkupV2.signatur().toEdit(),
    builder: EditLetterBuilderV2.() -> Unit,
): EditV2.Letter =
    editedLetter(
        *EditLetterBuilderV2().apply(builder).blocks.toTypedArray(),
        deleted = deleted,
        fixParentIds = fixParentIds,
        title = title,
        saksinformasjon = saksinformasjon,
        signatur = signatur,
    )

fun editedAttachment(
    vararg blocks: EditV2.Block,
    deleted: Collection<Int> = emptyList(),
    inkluderSaksinformasjon: Boolean = false,
    fixParentIds: Boolean = true,
    title: String = "En vedleggstittel",
): EditV2.Attachment =
    EditV2.Attachment(
        title = EditV2.Title(listOf(EditV2.Text.Literal(1, title))),
        blocks = if (fixParentIds) blocks.map { it.fixParentIds() } else blocks.toList(),
        deletedBlocks = deleted.toList(),
        inkluderSaksinformasjon = inkluderSaksinformasjon,
    )

fun editedAttachment(
    deleted: Collection<Int> = emptyList(),
    inkluderSaksinformasjon: Boolean = false,
    fixParentIds: Boolean = true,
    title: String = "En vedleggstittel",
    builder: EditLetterBuilderV2.() -> Unit,
): EditV2.Attachment =
    editedAttachment(
        *EditLetterBuilderV2().apply(builder).blocks.toTypedArray(),
        deleted = deleted,
        inkluderSaksinformasjon = inkluderSaksinformasjon,
        fixParentIds = fixParentIds,
        title = title,
    )

private fun EditV2.Block.fixParentIds(): EditV2.Block =
    when (this) {
        is EditV2.Block.Title2 -> copy(content = content.fixParentIds(id))
        is EditV2.Block.Title3 -> copy(content = content.fixParentIds(id))
        is EditV2.Block.Title4 -> copy(content = content.fixParentIds(id))
        is EditV2.Block.Paragraph -> copy(content = content.fixParentIds(id))
        is EditV2.Block.ItemList -> copy(items = items.map { it.fixParentIds(id) })
        is EditV2.Block.NumberedList -> copy(items = items.map { it.fixParentIds(id) })
        is EditV2.Block.Table -> copy(
            header = header.copy(
                colSpec = header.colSpec.map { it.copy(content = it.content.fixParentIds(it.id), parentId = it.parentId ?: header.id) },
                parentId = header.parentId ?: id,
            ),
            rows = rows.map { row ->
                row.copy(
                    cells = row.cells.map { it.copy(content = it.content.fixParentIds(it.id), parentId = it.parentId ?: row.id) },
                    parentId = row.parentId ?: id,
                )
            },
        )
    }

private fun EditV2.Item.fixParentIds(parentId: Int?): EditV2.Item =
    copy(content = content.fixParentIds(id), parentId = this.parentId ?: parentId)

private fun List<EditV2.Text>.fixParentIds(parentId: Int?): List<EditV2.Text> =
    map {
        when (it) {
            is EditV2.Text.Literal -> it.copy(parentId = it.parentId ?: parentId)
            is EditV2.Text.Variable -> it.copy(parentId = it.parentId ?: parentId)
            is EditV2.Text.NewLine -> it.copy(parentId = it.parentId ?: parentId)
        }
    }
