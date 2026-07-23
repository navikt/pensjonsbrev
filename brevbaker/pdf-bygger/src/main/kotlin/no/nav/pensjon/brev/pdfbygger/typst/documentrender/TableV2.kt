package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.TypstCodeScope
import no.nav.brev.brevbaker.markup.outline.Block.Table


internal fun TypstCodeScope.renderTableV2(table: Table) {
    if (table.rows.isEmpty() && table.header.colSpec.isEmpty()) return

    val columnSpec = table.header.colSpec

    appendCodeFunction("letter-table") {
        args {
            rawArg("languageSettings")

            namedArgRaw("columns", columnSpecToTypstV2(columnSpec))
            namedArgRaw("column-align", columnAlignmentToTypstV2(columnSpec))

            columnSpec.forEach { spec ->
                contentArg { renderTextContentV2(spec.content) }
            }

            table.rows.forEach { row ->
                row.cells.forEach { cell ->
                    contentArg { renderTextContentV2(cell.content) }
                }
            }
        }
    }
}

private fun columnSpecToTypstV2(columnSpec: List<Table.ColumnSpec>): String {
    val columns = columnSpec.map { spec ->
        "${spec.span}fr"
    }
    return "(${columns.joinToString(", ")},)"
}

private fun columnAlignmentToTypstV2(columnSpec: List<Table.ColumnSpec>): String {
    val alignments = columnSpec.map { spec ->
        when (spec.alignment) {
            Table.ColumnAlignment.LEFT -> "left"
            Table.ColumnAlignment.RIGHT -> "right"
        }
    }
    return "(${alignments.joinToString(", ")},)"
}
