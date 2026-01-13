package no.nav.pensjon.brev.pdfbygger.latex.documentRender

import no.nav.pensjon.brev.pdfbygger.latex.LatexAppendable
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Table

//TODO depricate table/itemlist/form inside paragraph and make them available outside.
// there should not be a different space between elements if within/outside paragraphs.
internal fun LatexAppendable.renderTable(table: Table, previous: LetterMarkup.Block?) {
    if (table.rows.isNotEmpty()) {
        val columnSpec = table.header.colSpec
        appendCmd(
            "begin", "letterTable", columnHeadersLatexString(columnSpec),
            titleTextOrNull(previous)?.let { "\\tabletitle $it" } ?: ""
            , escape = false // allerede escapet over.
        )
        renderTableCells(columnSpec.map { it.headerContent }, columnSpec)

        table.rows.forEach {
            renderTableCells(it.cells, table.header.colSpec)
        }

        appendCmd("end", "letterTable")
    }
}

internal fun titleTextOrNull(previous: LetterMarkup.Block?): String? =
    when (previous) {
        is LetterMarkup.Block.Title1 -> previous.content.renderToString()
        is LetterMarkup.Block.Title2 -> previous.content.renderToString()
        is LetterMarkup.Block.Title3 -> previous.content.renderToString()
        else -> null
    }?.takeIf { it.isNotBlank() }

private fun columnHeadersLatexString(columnSpec: List<Table.ColumnSpec>): String =
    columnSpec.joinToString("") {
        ("X" +
                when (it.alignment) {
                    Table.ColumnAlignment.LEFT -> "[l]"
                    Table.ColumnAlignment.RIGHT -> "[r]"
                }).repeat(it.span)
    }

private fun LatexAppendable.renderTableCells(cells: List<Table.Cell>, colSpec: List<Table.ColumnSpec>) {
    cells.forEachIndexed { index, cell ->
        val columnSpan = colSpec[index].span
        if (columnSpan > 1) {
            append("\\SetCell[c=$columnSpan]{}", escape = false)
        }
        renderTextContent(cell.text)
        if (columnSpan > 1) {
            append(" ${"& ".repeat(columnSpan - 1)}", escape = false)
        }
        if (index < cells.lastIndex) {
            append("&", escape = false)
        }
    }
    append("""\\""", escape = false)
}
