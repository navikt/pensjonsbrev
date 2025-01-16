package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.*


fun letter(vararg blocks: Block) =
    LetterMarkup(
        title = "En tittel",
        sakspart = Sakspart("Test Testeson", "1234568910", "1234", "20.12.2022"),
        blocks = blocks.toList(),
        signatur = Signatur("Med vennlig hilsen", "Saksbehandler", "Kjersti Saksbehandler", null, "Nav Familie- og pensjonsytelser Porsgrunn")
    )

fun editedLetter(vararg blocks: Edit.Block, deleted: Set<Int> = emptySet(), fixParentIds: Boolean = true): Edit.Letter =
    Edit.Letter(
        "En tittel",
        Sakspart("Test Testeson", "1234568910", "1234", "20.12.2022"),
        blocks.toList().let {
            if (fixParentIds) {
                blocks.map { it.fixParentIds(null) }
            } else it
        },
        Signatur("Med vennlig hilsen", "Saksbehandler", "Kjersti Saksbehandler", null, "Nav Familie- og pensjonsytelser Porsgrunn"),
        deleted
    )

fun Edit.Letter.fixParentIds(): Edit.Letter =
    copy(blocks = blocks.map { it.fixParentIds(null) })

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

