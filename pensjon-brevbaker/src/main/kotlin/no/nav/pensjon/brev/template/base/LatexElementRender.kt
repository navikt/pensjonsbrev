package no.nav.pensjon.brev.template.base

import no.nav.pensjon.brev.latex.LatexPrintWriter
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Element.Text.FontType
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.LanguageSupport


fun renderElement(
    scope: ExpressionScope<*, *>,
    element: Element<*>,
    printWriter: LatexPrintWriter
): Unit =
    when (element) {
        is Element.Conditional<*> -> conditional(element, scope, printWriter)
        is Element.Form.MultipleChoice<*> -> formMultipleChoice(printWriter, element, scope)
        is Element.Form.Text<*> -> formText(printWriter, element, scope)
        is Element.IncludePhrase<*, *> -> includePhrase(element, scope, printWriter)
        is Element.ItemList<*> -> itemList(printWriter, element, scope)
        is Element.NewLine<*> -> printWriter.printCmd("newline")
        is Element.Paragraph<*> -> paragraph(printWriter, element, scope)
        is Element.Table<*> -> table(printWriter, element, scope)
        is Element.Text.Expression.ByLanguage<*> ->
            printText(printWriter, element, element.expr(scope.language).eval(scope))
        is Element.Text.Expression<*> -> printText(printWriter, element, element.expression.eval(scope))
        is Element.Text.Literal<*> -> printText(printWriter, element, element.text(scope.language))
        is Element.Title1<*> -> title1(printWriter, element, scope)
        is Element.ForEachView<*, *> -> element.render(scope) { s, e -> renderElement(s, e, printWriter) }
    }

private fun table(
    printWriter: LatexPrintWriter,
    element: Element.Table<*>,
    scope: ExpressionScope<*, *>
) {
    with(printWriter) {
        val rows = element.rows.filter { it.condition == null || it.condition.eval(scope) }
        if (rows.isEmpty()) return
        val columnSpec = element.header.colSpec
        printCmd("begin") {
            arg { print("letterTable") }
            arg { print(columnHeadersLatexString(columnSpec)) }
        }

        renderTableCells(columnSpec.map { it.headerContent }, scope, printWriter, columnSpec)
        rows.forEach { renderTableCells(it.cells, scope, printWriter, columnSpec) }

        printCmd("end") { arg { print("letterTable") } }
    }
}

private fun formMultipleChoice(
    printWriter: LatexPrintWriter,
    element: Element.Form.MultipleChoice<*>,
    scope: ExpressionScope<*, *>
) {
    with(printWriter) {
        if (element.vspace) {
            printCmd("formvspace")
        }

        printCmd("begin") {
            arg { it.print("formChoice") }
            arg { renderElement(scope, element.prompt, it) }
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
    element: Element.Form.Text<*>,
    scope: ExpressionScope<*, *>
) {
    with(printWriter) {
        if (element.vspace) {
            printCmd("formvspace")
        }

        printCmd("formText") {
            arg {
                renderElement(scope, element.prompt, it)
                it.print(" ${".".repeat(element.size)}")
            }
        }
    }
}

private fun paragraph(
    printWriter: LatexPrintWriter,
    element: Element.Paragraph<*>,
    scope: ExpressionScope<*, *>
) {
    printWriter.printCmd("templateparagraph") {
        arg { element.paragraph.forEach { child -> renderElement(scope, child, it) } }
    }
}

private fun itemList(
    printWriter: LatexPrintWriter,
    element: Element.ItemList<*>,
    scope: ExpressionScope<*, *>
) {
    with(printWriter) {
        val items = element.items.filter { it.condition == null || it.condition.eval(scope) }
        if (items.isEmpty()) return
        printCmd("begin") {
            arg { print("itemize") }
        }
        items.forEach { item ->
            print("""\item """, escape = false)
            item.elements.forEach {
                renderElement(scope, it, this)
            }
        }
        printCmd("end") {
            arg { print("itemize") }
        }
    }
}

private fun conditional(
    element: Element.Conditional<*>,
    scope: ExpressionScope<*, *>,
    printWriter: LatexPrintWriter
) {
    with(element) {
        val toRender = if (predicate.eval(scope)) showIf else showElse
        toRender.forEach { renderElement(scope, it, printWriter) }
    }
}

private fun printText(
    printWriter: LatexPrintWriter,
    element: Element.Text<*>,
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

private fun includePhrase(
    element: Element.IncludePhrase<*, *>,
    scope: ExpressionScope<*, *>,
    printWriter: LatexPrintWriter
) {
    val newScope = ExpressionScope(element.data.eval(scope), scope.felles, scope.language)
    element.phrase.elements.forEach { renderElement(newScope, it, printWriter) }
}

private fun title1(
    printWriter: LatexPrintWriter,
    element: Element.Title1<*>,
    scope: ExpressionScope<*, *>
) {
    with(printWriter) {
        printCmd("lettersectiontitle") {
            arg { element.title1.forEach { child -> renderElement(scope, child, it) } }
        }
    }
}

private fun columnHeadersLatexString(columnSpec: List<Element.Table.ColumnSpec<out LanguageSupport>>) =
    columnSpec.map {
        ("X" +
                when (it.alignment) {
                    Element.Table.ColumnAlignment.LEFT -> "[l]"
                    Element.Table.ColumnAlignment.RIGHT -> "[r]"
                }).repeat(it.columnSpan)
    }.joinToString("")

private fun renderTableCells(
    cells: List<Element.Table.Cell<out LanguageSupport>>,
    scope: ExpressionScope<*, *>,
    printWriter: LatexPrintWriter,
    colSpec: List<Element.Table.ColumnSpec<out LanguageSupport>>
) {
    with(printWriter) {
        cells.forEachIndexed { index, cell ->
            val columnSpan = colSpec[index].columnSpan
            if (columnSpan > 1) {
                print("\\SetCell[c=$columnSpan]{}", escape = false)
            }
            cell.elements.forEach { cellElement ->
                renderElement(scope, cellElement, printWriter)
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