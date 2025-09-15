package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import java.time.LocalDate


fun letter(vararg blocks: LetterMarkup.Block) =
    LetterMarkupImpl(
        title = listOf(ParagraphContentImpl.TextImpl.LiteralImpl(1, "En tittel")),
        sakspart = SakspartImpl(
            gjelderNavn = "Test Testeson",
            gjelderFoedselsnummer = Foedselsnummer("1234568910"),
            vergeNavn = null,
            annenMottakerNavn = null,
            saksnummer = "1234",
            dokumentDato = LocalDate.now()
        ),
        blocks = blocks.toList(),
        signatur = SignaturImpl("Med vennlig hilsen", "Saksbehandler", "Kjersti Saksbehandler", null, "Nav Familie- og pensjonsytelser Porsgrunn")
    )

fun editedLetter(vararg blocks: Edit.Block, deleted: Set<Int> = emptySet(), fixParentIds: Boolean = true, dokumentDato: LocalDate = LocalDate.now()): Edit.Letter =
    Edit.Letter(
        title = Edit.Title(listOf(Edit.ParagraphContent.Text.Literal(1, "En tittel"))),
        sakspart = SakspartImpl(
            gjelderNavn = "Test Testeson",
            gjelderFoedselsnummer = Foedselsnummer("1234568910"),
            vergeNavn = null,
            annenMottakerNavn = null,
            saksnummer = "1234",
            dokumentDato = dokumentDato
        ),
        blocks = if (fixParentIds) { blocks.map { it.fixParentIds(null) }.toList() } else blocks.toList(),
        signatur = SignaturImpl(
            hilsenTekst = "Med vennlig hilsen",
            saksbehandlerRolleTekst = "Saksbehandler",
            saksbehandlerNavn = "Kjersti Saksbehandler",
            attesterendeSaksbehandlerNavn = null,
            navAvsenderEnhet = "Nav Familie- og pensjonsytelser Porsgrunn"
        ),
        deletedBlocks = deleted
    )

private fun Edit.Block.fixParentIds(parentId: Int?): Edit.Block =
    when (this) {
        is Edit.Block.Paragraph -> copy(content = content.map { it.fixParentIds(id) }, parentId = this.parentId ?: parentId)
        is Edit.Block.Title1 -> copy(content = content.map { it.fixParentIds(id) }, parentId = this.parentId ?: parentId)
        is Edit.Block.Title2 -> copy(content = content.map { it.fixParentIds(id) }, parentId = this.parentId ?: parentId)
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

