package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.TypstAppendable
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Table

/**
 * Render a table using the next-page-table function from Typst template.
 *
 * Output format:
 * #next-page-table(
 *   columns: (1fr, 1fr, ...),
 *   [Title (optional)], [Header1], [Header2], ...
 *   [Cell1], [Cell2], ...
 * )
 *
 * @param table The table to render
 * @param previous The previous block, used to extract table title from preceding Title block
 */
internal fun TypstAppendable.renderTable(table: Table, previous: LetterMarkup.Block?) {
    if (table.rows.isEmpty() && table.header.colSpec.isEmpty()) return

    val columnSpec = table.header.colSpec
    val title = titleTextOrNull(previous)

    appendCodeFunction("next-page-table") {
        args {
            // Column specification - span defines relative width (e.g., 2fr is twice as wide as 1fr)
            namedArgRaw("columns", columnSpecToTypst(columnSpec))

            // Optional title as first content arg
            if (title != null) {
                contentArg { append(title, escape = false) } // Already escaped from renderToString
            }

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
 * Extract title text from a preceding Title block if present.
 */
internal fun titleTextOrNull(previous: LetterMarkup.Block?): String? =
    when (previous) {
        is LetterMarkup.Block.Title1 -> previous.content.renderToString()
        is LetterMarkup.Block.Title2 -> previous.content.renderToString()
        is LetterMarkup.Block.Title3 -> previous.content.renderToString()
        else -> null
    }?.takeIf { it.isNotBlank() }

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
    return "(${columns.joinToString(", ")})"
}

