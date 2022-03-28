package no.nav.pensjon.brev.template.base

import no.nav.pensjon.brev.latex.LatexPrintWriter
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Element.Text.FontType
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.InvalidTableDeclarationException
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
        is Element.ItemList.Item<*> -> listItem(printWriter, element, scope)
        is Element.NewLine<*> -> printWriter.printCmd("newline")
        is Element.Paragraph<*> -> paragraph(printWriter, element, scope)
        is Element.Table<*> -> table(printWriter, element, scope)
        is Element.Text.Expression.ByLanguage<*> ->
            printText(printWriter, element, element.expr(scope.language).eval(scope))
        is Element.Text.Expression<*> -> printText(printWriter, element, element.expression.eval(scope))
        is Element.Text.Literal<*> -> printText(printWriter, element, element.text(scope.language))
        is Element.Title1<*> -> title1(printWriter, element, scope)
        is Element.ForEachView<*, *> -> element.render(scope) { s, e -> renderElement(s, e, printWriter) }
        is Element.Table.Row<*> -> renderTableCells(element.cells, scope, printWriter, element.colSpec)
    }

private fun listItem(
    printWriter: LatexPrintWriter,
    element: Element.ItemList.Item<*>,
    scope: ExpressionScope<*, *>
) {
    printWriter.print("""\item """, escape = false)
    element.elements.forEach { renderElement(scope, it, printWriter) }
}

private fun table(
    printWriter: LatexPrintWriter,
    element: Element.Table<*>,
    scope: ExpressionScope<*, *>
) {
    with(printWriter) {
        val tableElements = element.children
        if (tableElements.isEmpty()) return
        val atLeastOneRow = element.children.any { willRenderRow(it, scope) }
        if(!atLeastOneRow) return
        val columnSpec = element.header.colSpec

        printCmd("begin") {
            arg { print("letterTable") }
            arg { print(columnHeadersLatexString(columnSpec)) }
        }

        renderTableCells(columnSpec.map { it.headerContent }, scope, printWriter, columnSpec)
        tableElements.forEach { renderElement(scope, it, printWriter) }

        printCmd("end") { arg { print("letterTable") } }
    }
}

private fun willRenderRow(element: Element<*>, scope: ExpressionScope<*, *>): Boolean {
    return when(element) {
        is Element.Conditional<*> -> {
            val toRender = if (element.predicate.eval(scope)) element.showIf else element.showElse
            return toRender.any{ willRenderRow(it, scope)}
        }
        is Element.ForEachView<*, *> -> {
            var hasRow = false
            element.render(scope) { s, e -> hasRow = willRenderRow(e, s)}
            hasRow
        }
        is Element.Table.Row<*> -> true
        else -> throw InvalidTableDeclarationException("Unhandled element type within table " + element.javaClass.toString())
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
    val atLeastOneItem = element.items.any { willRenderItem(it, scope) }
    if (!atLeastOneItem) return
    with(printWriter) {
        printCmd("begin") { arg { print("itemize") } }
        element.items.forEach { renderElement(scope, it, printWriter) }
        printCmd("end") { arg { print("itemize") } }
    }
}

private fun willRenderItem(element: Element<*>, scope: ExpressionScope<*, *>): Boolean {
    return when(element) {
        is Element.Conditional<*> -> {
            val toRender = if (element.predicate.eval(scope)) element.showIf else element.showElse
            return toRender.any{ willRenderItem(it, scope)}
        }
        is Element.ForEachView<*, *> -> {
            var hasItem = false
            element.render(scope) { s, e -> hasItem = willRenderItem(e, s)}
            hasItem
        }
        is Element.ItemList.Item<*> -> true
        else -> throw InvalidTableDeclarationException("Unhandled element type within item list " + element.javaClass.toString())
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