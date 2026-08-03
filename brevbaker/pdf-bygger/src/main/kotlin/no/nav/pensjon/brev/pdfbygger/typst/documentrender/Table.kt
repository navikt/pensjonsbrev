package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.TypstCodeScope
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Table


internal fun TypstCodeScope.renderTable(table: Table) {
    if (table.rows.isEmpty() && table.header.colSpec.isEmpty()) return

    val columnSpec = table.header.colSpec

    appendCodeFunction("letter-table") {
        args {
            rawArg("languageSettings")
            namedArgRaw("columns", columnSpecToTypst(columnSpec))
            namedArgRaw("column-align", columnAlignmentToTypst(columnSpec))

            columnSpec.forEach { spec ->
                contentArg { renderTextContent(spec.headerContent.text) }
            }

            table.rows.forEach { row ->
                row.cells.forEach { cell ->
                    contentArg { renderTextContent(cell.text) }
                }
            }
        }
    }
}

private fun columnSpecToTypst(columnSpec: List<Table.ColumnSpec>): String {
    val columns = columnSpec.map { spec ->
        "${spec.span}fr"
    }
    return "(${columns.joinToString(", ")},)"
}

private fun columnAlignmentToTypst(columnSpec: List<Table.ColumnSpec>): String {
    val alignments = columnSpec.map { spec ->
        when (spec.alignment) {
            Table.ColumnAlignment.LEFT -> "left"
            Table.ColumnAlignment.RIGHT -> "right"
        }
    }
    return "(${alignments.joinToString(", ")},)"
}
