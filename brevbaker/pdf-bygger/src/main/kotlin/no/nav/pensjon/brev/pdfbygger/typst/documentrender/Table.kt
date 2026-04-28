package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.TypstCodeScope
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Table

/**
 * Render a table using the letter-table function from Typst template.
 *
 * Output format:
 * #letter-table(
 *   columns: (1fr, 1fr, ...),
 *   [Header1], [Header2], ...
 *   [Cell1], [Cell2], ...
 * )
 *
 * The Typst template handles header styling (bold, background color, extra padding),
 * header repetition on subsequent pages, and continuation messages using languageSettings.
 *
 * @param table The table to render
 */
internal fun TypstCodeScope.renderTable(table: Table) {
    if (table.rows.isEmpty() && table.header.colSpec.isEmpty()) return

    val columnSpec = table.header.colSpec

    appendCodeFunction("letter-table") {
        args {
            rawArg("languageSettings")

            // Column specification - span defines relative width (e.g., 2fr is twice as wide as 1fr)
            namedArgRaw("columns", columnSpecToTypst(columnSpec))

            // Alignment specification - one alignment per column
            namedArgRaw("column-align", columnAlignmentToTypst(columnSpec))


            // Header row - one cell per column
            columnSpec.forEach { spec ->
                contentArg { renderTextContent(spec.headerContent.text) }
            }

            // Data rows - one cell per column
            table.rows.forEach { row ->
                row.cells.forEach { cell ->
                    contentArg { renderTextContent(cell.text) }
                }
            }
        }
    }
}


/**
 * Convert column specifications to Typst column format.
 *
 * For Typst tables, we use fractional units (Nfr) for flexible columns.
 * The span value determines the relative width - a column with span=2 is twice as wide as span=1.
 */
private fun columnSpecToTypst(columnSpec: List<Table.ColumnSpec>): String {
    val columns = columnSpec.map { spec ->
        "${spec.span}fr"
    }
    return "(${columns.joinToString(", ")},)"
}

/**
 * Convert column alignments to Typst alignment format.
 *
 * Returns a tuple of alignments matching the column order.
 * LEFT maps to Typst's 'left', RIGHT maps to 'right'.
 */
private fun columnAlignmentToTypst(columnSpec: List<Table.ColumnSpec>): String {
    val alignments = columnSpec.map { spec ->
        when (spec.alignment) {
            Table.ColumnAlignment.LEFT -> "left"
            Table.ColumnAlignment.RIGHT -> "right"
        }
    }
    return "(${alignments.joinToString(", ")},)"
}
