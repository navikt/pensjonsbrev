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

internal object EditV2Markup {
    fun toEdit(markup: LetterMarkup): EditV2.Letter =
        EditV2.Letter(
            title = EditV2.Title(markup.title1.toEdit(null)),
            saksinformasjon = toEdit(markup.saksinformasjon),
            blocks = markup.blocks.toEdit(),
            signatur = toEdit(markup.signatur),
            deletedBlocks = emptyList(),
        )

    fun toEdit(markup: Attachment): EditV2.Attachment =
        EditV2.Attachment(
            title = EditV2.Title(markup.title1.toEdit(null)),
            blocks = markup.blocks.toEdit(),
            deletedBlocks = emptyList(),
            inkluderSaksinformasjon = markup.inkluderSaksinformasjon,
        )

    fun toEdit(saksinformasjon: Saksinformasjon): EditV2.Saksinformasjon =
        EditV2.Saksinformasjon(
            gjelderNavn = saksinformasjon.gjelderNavn,
            gjelderPersonidentifikator = saksinformasjon.gjelderPersonidentifikator.value,
            annenMottakerNavn = saksinformasjon.annenMottakerNavn,
            saksnummer = saksinformasjon.saksnummer.value,
            dokumentDato = saksinformasjon.dokumentDato,
        )

    fun toEdit(signatur: Signatur): EditV2.Signatur =
        EditV2.Signatur(
            saksbehandlerNavn = signatur.saksbehandlerSignatur?.saksbehandlerNavn,
            attesterendeSaksbehandlerNavn = signatur.saksbehandlerSignatur?.attesterendeSaksbehandlerNavn,
            navAvsenderEnhet = signatur.navAvsenderEnhet,
        )

    private fun List<Block>.toEdit(): List<EditV2.Block> = map { it.toEdit() }

    private fun Block.toEdit(): EditV2.Block =
        when (this) {
            is Block.Title2 -> EditV2.Block.Title2(id = id, content = content.toEdit(id))
            is Block.Title3 -> EditV2.Block.Title3(id = id, content = content.toEdit(id))
            is Block.Title4 -> EditV2.Block.Title4(id = id, content = content.toEdit(id))
            is Block.Paragraph -> EditV2.Block.Paragraph(id = id, content = content.toEdit(id))
            is Block.ItemList -> EditV2.Block.ItemList(id = id, items = items.map { it.toEdit(id) })
            is Block.NumberedList -> EditV2.Block.NumberedList(id = id, items = items.map { it.toEdit(id) })
            is Block.Table -> EditV2.Block.Table(
                id = id,
                header = EditV2.Block.Table.Header(
                    id = header.id,
                    colSpec = header.colSpec.map {
                        EditV2.Block.Table.ColumnSpec(
                            id = it.id,
                            content = it.content.toEdit(it.id),
                            alignment = it.alignment.toEdit(),
                            span = it.span,
                            parentId = header.id,
                        )
                    },
                    parentId = id,
                ),
                rows = rows.map { row ->
                    EditV2.Block.Table.Row(
                        id = row.id,
                        cells = row.cells.map { cell -> EditV2.Block.Table.Cell(id = cell.id, content = cell.content.toEdit(cell.id), parentId = row.id) },
                        parentId = id,
                    )
                },
            )

            is Block.FormText, is Block.FormChoice -> throw UnsupportedOperationException("Skribenten does not support element type: $type")
        }

    private fun Block.Item.toEdit(parentId: Int?): EditV2.Item =
        EditV2.Item(id = id, content = content.toEdit(id), parentId = parentId)

    private fun List<Text>.toEdit(parentId: Int?): List<EditV2.Text> = map { it.toEdit(parentId) }

    private fun Text.toEdit(parentId: Int?): EditV2.Text =
        when (this) {
            is Text.Literal -> EditV2.Text.Literal(id = id, text = text, fontType = fontType.toEdit(), editBehaviour = editBehaviour?.toEdit(), parentId = parentId)
            is Text.Variable -> EditV2.Text.Variable(id = id, text = text, fontType = fontType.toEdit(), editBehaviour = editBehaviour?.toEdit(), parentId = parentId)
            is Text.NewLine -> EditV2.Text.NewLine(id = id, parentId = parentId)
        }

    private fun Text.FontType.toEdit(): EditV2.FontType =
        when (this) {
            Text.FontType.PLAIN -> EditV2.FontType.PLAIN
            Text.FontType.BOLD -> EditV2.FontType.BOLD
            Text.FontType.ITALIC -> EditV2.FontType.ITALIC
        }

    private fun EditBehaviour.toEdit(): EditV2.EditBehaviour =
        when (this) {
            EditBehaviour.FRITEKST -> EditV2.EditBehaviour.FRITEKST
            EditBehaviour.REDIGERBAR_DATA -> EditV2.EditBehaviour.REDIGERBAR_DATA
        }

    private fun Block.Table.ColumnAlignment.toEdit(): EditV2.Block.Table.ColumnAlignment =
        when (this) {
            Block.Table.ColumnAlignment.LEFT -> EditV2.Block.Table.ColumnAlignment.LEFT
            Block.Table.ColumnAlignment.RIGHT -> EditV2.Block.Table.ColumnAlignment.RIGHT
        }

    fun toMarkup(letter: EditV2.Letter): LetterMarkup =
        MarkupModel.letterMarkup(
            title1 = letter.title.text.toMarkup(),
            saksinformasjon = letter.saksinformasjon.toMarkup(),
            blocks = letter.blocks.map { it.toMarkup() },
            signatur = letter.signatur.toMarkup(),
        )

    fun toMarkup(attachment: EditV2.Attachment): Attachment =
        MarkupModel.attachment(
            title1 = attachment.title.text.toMarkup(),
            blocks = attachment.blocks.map { it.toMarkup() },
            inkluderSaksinformasjon = attachment.inkluderSaksinformasjon,
        )

    private fun EditV2.Saksinformasjon.toMarkup(): Saksinformasjon =
        MarkupModel.saksinformasjon(
            gjelderNavn = gjelderNavn,
            gjelderPersonidentifikator = gjelderPersonidentifikator,
            annenMottakerNavn = annenMottakerNavn,
            saksnummer = saksnummer,
            dokumentDato = dokumentDato,
        )

    private fun EditV2.Signatur.toMarkup(): Signatur =
        MarkupModel.signatur(
            navAvsenderEnhet = navAvsenderEnhet,
            saksbehandlerSignatur = saksbehandlerNavn?.let { MarkupModel.saksbehandlerSignatur(it, attesterendeSaksbehandlerNavn) },
        )

    private fun EditV2.Block.toMarkup(): Block =
        when (this) {
            is EditV2.Block.Title2 -> MarkupModel.title2(id ?: 0, content.toMarkup())
            is EditV2.Block.Title3 -> MarkupModel.title3(id ?: 0, content.toMarkup())
            is EditV2.Block.Title4 -> MarkupModel.title4(id ?: 0, content.toMarkup())
            is EditV2.Block.Paragraph -> MarkupModel.paragraph(id ?: 0, content.toMarkup())
            is EditV2.Block.ItemList -> MarkupModel.itemList(id ?: 0, items.map { it.toMarkup() })
            is EditV2.Block.NumberedList -> MarkupModel.numberedList(id ?: 0, items.map { it.toMarkup() })
            is EditV2.Block.Table -> MarkupModel.table(
                id = id ?: 0,
                rows = rows.map { row -> MarkupModel.row(row.id ?: 0, row.cells.map { MarkupModel.cell(it.id ?: 0, it.content.toMarkup()) }) },
                header = MarkupModel.header(
                    header.id ?: 0,
                    header.colSpec.map { MarkupModel.columnSpec(it.id ?: 0, it.content.toMarkup(), it.alignment.toMarkup(), it.span) },
                ),
            )
        }

    private fun EditV2.Item.toMarkup(): Block.Item = MarkupModel.item(id ?: 0, content.toMarkup())

    private fun List<EditV2.Text>.toMarkup(): List<Text> = map { it.toMarkup() }

    private fun EditV2.Text.toMarkup(): Text =
        when (this) {
            is EditV2.Text.Literal -> MarkupModel.literal(
                id = id ?: 0,
                text = editedText ?: text,
                fontType = (editedFontType ?: fontType).toMarkup(),
                editBehaviour = editBehaviour?.toMarkup(),
            )

            is EditV2.Text.Variable -> MarkupModel.variable(id = id ?: 0, text = text, fontType = fontType.toMarkup(), editBehaviour = editBehaviour?.toMarkup())
            is EditV2.Text.NewLine -> MarkupModel.newLine(id ?: 0)
        }

    private fun EditV2.FontType.toMarkup(): Text.FontType =
        when (this) {
            EditV2.FontType.PLAIN -> Text.FontType.PLAIN
            EditV2.FontType.BOLD -> Text.FontType.BOLD
            EditV2.FontType.ITALIC -> Text.FontType.ITALIC
        }

    private fun EditV2.EditBehaviour.toMarkup(): EditBehaviour =
        when (this) {
            EditV2.EditBehaviour.FRITEKST -> EditBehaviour.FRITEKST
            EditV2.EditBehaviour.REDIGERBAR_DATA -> EditBehaviour.REDIGERBAR_DATA
        }

    private fun EditV2.Block.Table.ColumnAlignment.toMarkup(): Block.Table.ColumnAlignment =
        when (this) {
            EditV2.Block.Table.ColumnAlignment.LEFT -> Block.Table.ColumnAlignment.LEFT
            EditV2.Block.Table.ColumnAlignment.RIGHT -> Block.Table.ColumnAlignment.RIGHT
        }
}
