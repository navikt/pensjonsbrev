package no.nav.pensjon.brev.template.base

import no.nav.pensjon.brev.latex.LatexPrintWriter
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType

fun renderContent(
    scope: ExpressionScope<*, *>,
    contentOrControlStructure: AnyElement<*>,
    printWriter: LatexPrintWriter,
): Unit =
    when (contentOrControlStructure) {
        is ContentOrControlStructure.Content -> renderElement(scope, contentOrControlStructure.content, printWriter)
        is ContentOrControlStructure.Conditional -> conditional(contentOrControlStructure, scope, printWriter)
        is ContentOrControlStructure.ForEach<*, *, *> -> contentOrControlStructure.render(scope) { s, c -> renderContent(s, c, printWriter) }
    }

fun renderElement(
    scope: ExpressionScope<*, *>,
    element: Element<*>,
    printWriter: LatexPrintWriter
): Unit =
    when (element) {
        is Element.OutlineContent.ParagraphContent.Form.MultipleChoice<*> -> formMultipleChoice(printWriter, element, scope)
        is Element.OutlineContent.ParagraphContent.Form.Text<*> -> formText(printWriter, element, scope)
        is Element.OutlineContent.ParagraphContent.ItemList<*> -> list(printWriter, element, scope)
        is Element.OutlineContent.ParagraphContent.ItemList.Item<*> -> listItem(printWriter, element, scope)
        is Element.OutlineContent.ParagraphContent.Text.NewLine<*> -> printWriter.printCmd("newline")
        is Element.OutlineContent.Paragraph<*> -> paragraph(printWriter, element, scope)
        is Element.OutlineContent.ParagraphContent.Table<*> -> table(printWriter, element, scope)
        is Element.OutlineContent.ParagraphContent.Table.Row<*> -> renderTableCells(element.cells, scope, printWriter, element.colSpec)
        is Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage<*> ->
            printText(printWriter, element, element.expr(scope.language).eval(scope))
        is Element.OutlineContent.ParagraphContent.Text.Expression<*> -> printText(printWriter, element, element.expression.eval(scope))
        is Element.OutlineContent.ParagraphContent.Text.Literal<*> -> printText(printWriter, element, element.text(scope.language))
        is Element.OutlineContent.Title1<*> -> title1(printWriter, element, scope)
    }

private fun listItem(
    printWriter: LatexPrintWriter,
    element: Element.OutlineContent.ParagraphContent.ItemList.Item<*>,
    scope: ExpressionScope<*, *>
) {
    printWriter.print("""\item """, escape = false)
    element.text.forEach { renderContent(scope, it, printWriter) }
    printWriter.println("")

}

private fun table(
    printWriter: LatexPrintWriter,
    element: Element.OutlineContent.ParagraphContent.Table<*>,
    scope: ExpressionScope<*, *>
) {
    with(printWriter) {
        val tableElements = element.rows
        if (tableElements.isNotEmpty() && tableElements.any { willRenderRow(it, scope) }) {
            val columnSpec = element.header.colSpec

            printCmd("begin") {
                arg { print("letterTable") }
                arg { print(columnHeadersLatexString(columnSpec)) }
            }

            renderTableCells(columnSpec.map { it.headerContent }, scope, printWriter, columnSpec)
            tableElements.forEach { renderContent(scope, it, printWriter) }

            printCmd("end") { arg { print("letterTable") } }
        }
    }
}

private fun willRenderRow(element: TableRowElement<*>, scope: ExpressionScope<*, *>): Boolean {
    return when (element) {
        is ContentOrControlStructure.Conditional -> {
            val toRender = if (element.predicate.eval(scope)) element.showIf else element.showElse
            return toRender.any { willRenderRow(it, scope) }
        }
        is ContentOrControlStructure.ForEach<*, Element.OutlineContent.ParagraphContent.Table.Row<*>, *> -> {
            var hasRow = false
            element.render(scope) { s, e -> hasRow = willRenderRow(e, s) }
            hasRow
        }
        is ContentOrControlStructure.Content -> true
    }
}

private fun formMultipleChoice(
    printWriter: LatexPrintWriter,
    element: Element.OutlineContent.ParagraphContent.Form.MultipleChoice<*>,
    scope: ExpressionScope<*, *>
) {
    with(printWriter) {
        if (element.vspace) {
            printCmd("formvspace")
        }

        printCmd("begin") {
            arg { it.print("formChoice") }
            arg { renderContent(scope, element.prompt, it) }
        }

        element.choices.forEach {
            printCmd("item")
            renderElement(scope, it, printWriter)
        }

        printCmd("end", "formChoice")
    }
}

private fun formText(
    printWriter: LatexPrintWriter,
    element: Element.OutlineContent.ParagraphContent.Form.Text<*>,
    scope: ExpressionScope<*, *>
) {
    with(printWriter) {
        if (element.vspace) {
            printCmd("formvspace")
        }

        printCmd("formText") {
            arg {
                renderContent(scope, element.prompt, it)
                it.print(" ${".".repeat(element.size)}")
            }
        }
    }
}

private fun paragraph(
    printWriter: LatexPrintWriter,
    element: Element.OutlineContent.Paragraph<*>,
    scope: ExpressionScope<*, *>
) {
    printWriter.printCmd("templateparagraph") {
        arg { element.paragraph.forEach { child -> renderContent(scope, child, it) } }
    }
}

private fun list(
    printWriter: LatexPrintWriter,
    element: Element.OutlineContent.ParagraphContent.ItemList<*>,
    scope: ExpressionScope<*, *>
) {
    if (element.items.any { willRenderItem(it, scope) }) {
        with(printWriter) {
            printCmd("begin") { arg { print("itemize") } }
            element.items.forEach { renderContent(scope, it, printWriter) }
            printCmd("end") { arg { print("itemize") } }
        }
    }
}

private fun willRenderItem(element: ListItemElement<*>, scope: ExpressionScope<*, *>): Boolean {
    return when (element) {
        is ContentOrControlStructure.Conditional -> {
            val toRender = if (element.predicate.eval(scope)) element.showIf else element.showElse
            return toRender.any { willRenderItem(it, scope) }
        }
        is ContentOrControlStructure.ForEach<*, Element.OutlineContent.ParagraphContent.ItemList.Item<*>, *> -> {
            var hasItem = false
            element.render(scope) { s, e -> hasItem = willRenderItem(e, s) }
            hasItem
        }
        is ContentOrControlStructure.Content -> true
    }
}


private fun <C : Element<*>> conditional(
    element: ContentOrControlStructure.Conditional<*, C>,
    scope: ExpressionScope<*, *>,
    printWriter: LatexPrintWriter,
) {
    with(element) {
        val toRender = if (predicate.eval(scope)) showIf else showElse
        toRender.forEach { renderContent(scope, it, printWriter) }
    }
}

private fun printText(
    printWriter: LatexPrintWriter,
    element: Element.OutlineContent.ParagraphContent.Text<*>,
    textLiteral: String
) {
    with(printWriter) {
        when (element.fontType) {
            FontType.PLAIN -> print(textLiteral)
            FontType.BOLD -> printCmd("textbf") { arg { print(textLiteral) } }
            FontType.ITALIC -> printCmd("textit") { arg { print(textLiteral) } }
        }
    }
}

private fun title1(
    printWriter: LatexPrintWriter,
    element: Element.OutlineContent.Title1<*>,
    scope: ExpressionScope<*, *>
) {
    with(printWriter) {
        printCmd("lettersectiontitle") {
            arg { element.text.forEach { child -> renderContent(scope, child, it) } }
        }
    }
}

private fun columnHeadersLatexString(columnSpec: List<Element.OutlineContent.ParagraphContent.Table.ColumnSpec<LanguageSupport>>) =
    columnSpec.joinToString("") {
        ("X" +
                when (it.alignment) {
                    Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT -> "[l]"
                    Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT -> "[r]"
                }).repeat(it.columnSpan)
    }

private fun renderTableCells(
    cells: List<Element.OutlineContent.ParagraphContent.Table.Cell<LanguageSupport>>,
    scope: ExpressionScope<*, *>,
    printWriter: LatexPrintWriter,
    colSpec: List<Element.OutlineContent.ParagraphContent.Table.ColumnSpec<LanguageSupport>>
) {
    with(printWriter) {
        cells.forEachIndexed { index, cell ->
            val columnSpan = colSpec[index].columnSpan
            if (columnSpan > 1) {
                print("\\SetCell[c=$columnSpan]{}", escape = false)
            }
            cell.text.forEach { cellElement ->
                renderContent(scope, cellElement, printWriter)
            }
            if (columnSpan > 1) {
                print(" ${"& ".repeat(columnSpan - 1)}", escape = false)
            }
            if (index < cells.lastIndex) {
                print("&", escape = false)
            }
        }
        print("""\\""", escape = false)
    }
}