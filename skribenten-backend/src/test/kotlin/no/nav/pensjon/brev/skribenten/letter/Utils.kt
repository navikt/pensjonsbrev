@file:OptIn(InterneDataklasser::class)

package no.nav.pensjon.brev.skribenten.letter

import no.nav.brev.InterneDataklasser
import no.nav.brev.Listetype
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Table.ColumnAlignment.LEFT
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.FontType
import java.time.LocalDate


fun letter(vararg blocks: LetterMarkup.Block) =
    LetterMarkupImpl(
        title = listOf(ParagraphContentImpl.TextImpl.LiteralImpl(1, "En tittel")),
        sakspart = SakspartImpl(
            gjelderNavn = "Test Testeson",
            gjelderFoedselsnummer = Foedselsnummer("1234568910"),
            annenMottakerNavn = null,
            saksnummer = "1234",
            dokumentDato = LocalDate.now()
        ),
        blocks = blocks.toList(),
        signatur = SignaturImpl(
            hilsenTekst = "Med vennlig hilsen",
            saksbehandlerNavn = "Kjersti Saksbehandler",
            attesterendeSaksbehandlerNavn = null,
            navAvsenderEnhet = "Nav Familie- og pensjonsytelser Porsgrunn"
        )
    )

fun editedLetter(vararg blocks: Edit.Block, deleted: Set<Int> = emptySet(), fixParentIds: Boolean = true, dokumentDato: LocalDate = LocalDate.now()): Edit.Letter =
    Edit.Letter(
        title = Edit.Title(listOf(Edit.ParagraphContent.Text.Literal(1, "En tittel"))),
        sakspart = SakspartImpl(
            gjelderNavn = "Test Testeson",
            gjelderFoedselsnummer = Foedselsnummer("1234568910"),
            annenMottakerNavn = null,
            saksnummer = "1234",
            dokumentDato = dokumentDato
        ),
        blocks = if (fixParentIds) { blocks.map { it.fixParentIds(null) }.toList() } else blocks.toList(),
        signatur = SignaturImpl(
            hilsenTekst = "Med vennlig hilsen",
            saksbehandlerNavn = "Kjersti Saksbehandler",
            attesterendeSaksbehandlerNavn = null,
            navAvsenderEnhet = "Nav Familie- og pensjonsytelser Porsgrunn"
        ),
        deletedBlocks = deleted
    )

fun paragraph(vararg content: Edit.ParagraphContent) =
    Edit.Block.Paragraph(id = null, editable = true, content = content.toList())

fun literal(
    id: Int? = null,
    text: String,
    fontType: FontType = FontType.PLAIN,
    editedText: String? = null,
    editedFontType: FontType? = null,
) = Edit.ParagraphContent.Text.Literal(id, text, fontType, editedText, editedFontType)

fun variable(id: Int? = null, text: String, fontType: FontType = FontType.PLAIN) =
    Edit.ParagraphContent.Text.Variable(id, text, fontType)

fun newLine(id: Int? = null) = Edit.ParagraphContent.Text.NewLine(id)

fun itemList(vararg items: Edit.ParagraphContent.ItemList.Item) =
    Edit.ParagraphContent.ItemList(id = null, items = items.toList())

fun item(vararg content: Edit.ParagraphContent.Text) =
    Edit.ParagraphContent.ItemList.Item(id = null, content = content.toList())

fun table(header: Edit.ParagraphContent.Table.Header, vararg rows: Edit.ParagraphContent.Table.Row) =
    Edit.ParagraphContent.Table(id = null, rows = rows.toList(), header = header)

fun header(vararg colSpecs: Edit.ParagraphContent.Table.ColumnSpec) =
    Edit.ParagraphContent.Table.Header(id = null, colSpec = colSpecs.toList())

fun colSpec(headerContent: Edit.ParagraphContent.Table.Cell = cell()) =
    Edit.ParagraphContent.Table.ColumnSpec(id = null, headerContent = headerContent, alignment = LEFT, span = 1)

fun row(vararg cells: Edit.ParagraphContent.Table.Cell) =
    Edit.ParagraphContent.Table.Row(id = null, cells = cells.toList())

fun cell(vararg text: Edit.ParagraphContent.Text) =
    Edit.ParagraphContent.Table.Cell(id = null, text = text.toList())

private fun Edit.Block.fixParentIds(parentId: Int?): Edit.Block =
    when (this) {
        is Edit.Block.Paragraph -> copy(content = content.map { it.fixParentIds(id) }, parentId = this.parentId ?: parentId)
        is Edit.Block.Title1 -> copy(content = content.map { it.fixParentIds(id) }, parentId = this.parentId ?: parentId)
        is Edit.Block.Title2 -> copy(content = content.map { it.fixParentIds(id) }, parentId = this.parentId ?: parentId)
        is Edit.Block.Title3 -> copy(content = content.map { it.fixParentIds(id) }, parentId = this.parentId ?: parentId)
    }

private fun Edit.ParagraphContent.fixParentIds(parentId: Int?): Edit.ParagraphContent =
    when(this) {
        is Edit.ParagraphContent.ItemList -> copy(items = items.map { it.fixParentIds(id) }, parentId = this.parentId ?: parentId)
        is Edit.ParagraphContent.Table -> copy(rows = rows.map { it.fixParentIds(id) }, header = header.fixParentIds(id), parentId = this.parentId ?: parentId)
        is Edit.ParagraphContent.Text -> fixParentIds(parentId)
    }

private fun Edit.ParagraphContent.Text.fixParentIds(parentId: Int?): Edit.ParagraphContent.Text =
    when(this) {
        is Edit.ParagraphContent.Text.Literal -> copy(parentId = this.parentId ?: parentId)
        is Edit.ParagraphContent.Text.Variable -> copy(parentId = this.parentId ?: parentId)
        is Edit.ParagraphContent.Text.NewLine -> copy(parentId = this.parentId ?: parentId)
    }

private fun Edit.ParagraphContent.ItemList.Item.fixParentIds(parentId: Int?): Edit.ParagraphContent.ItemList.Item =
    copy(content = content.map { it.fixParentIds(id) }, parentId = this.parentId ?: parentId)

private fun Edit.ParagraphContent.Table.Row.fixParentIds(parentId: Int?): Edit.ParagraphContent.Table.Row =
    copy(cells = cells.map { it.fixParentIds(id) }, parentId = this.parentId ?: parentId)

private fun Edit.ParagraphContent.Table.Cell.fixParentIds(parentId: Int?): Edit.ParagraphContent.Table.Cell =
    copy(text = text.map { it.fixParentIds(id) }, parentId = this.parentId ?: parentId)

private fun Edit.ParagraphContent.Table.Header.fixParentIds(parentId: Int?): Edit.ParagraphContent.Table.Header =
    copy(colSpec = colSpec.map { it.fixParentIds(id) }, parentId = this.parentId ?: parentId)

private fun Edit.ParagraphContent.Table.ColumnSpec.fixParentIds(parentId: Int?): Edit.ParagraphContent.Table.ColumnSpec =
    copy(headerContent = headerContent.fixParentIds(id), parentId = this.parentId ?: parentId)

