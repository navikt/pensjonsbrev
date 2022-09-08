package no.nav.pensjon.brev.template.render

import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import no.nav.pensjon.brev.template.*
import java.util.*

object TemplateDocumentationRenderer {
    private val languageSettings = pensjonHTMLSettings
    private val css = RenderedFile.PlainText("style.css", getResource("html-template-doc/style.css").toString(Charsets.UTF_8))
    private val navLogoImg = "data:image/png;base64,${Base64.getEncoder().encodeToString(getResource("html/nav-logo.png"))}"

    private fun getResource(fileName: String): ByteArray =
        this::class.java.getResourceAsStream("/$fileName")
            ?.use { it.readAllBytes() }
            ?: throw IllegalStateException("""Could not find resource /$fileName""")

    fun render(letter: Letter<*>): RenderedHtmlLetter =
        renderLetter(letter.toScope(), letter.template)

    private fun renderLetter(scope: ExpressionScope<*, *>, template: LetterTemplate<*, *>): RenderedHtmlLetter = RenderedHtmlLetter().apply {
        addFile(css)
        newFile("index.html") {
            appendLine("<!DOCTYPE html>").appendHTML(prettyPrint = false).html {
                lang = scope.language.locale().toLanguageTag()
                head {
                    meta(charset = Charsets.UTF_8.name())
                    meta(name = "viewport", content = "width=device-width")
                    title { text("Template dokumentasjon for ${template.name}") }
                    link(href = "https://fonts.googleapis.com/css?family=Source+Sans+Pro", rel = "stylesheet", type = "text/css")
                    link(href = "style.css", rel = "stylesheet", type = "text/css")
                }
                body {
                    div(classes("rot")) {
                        div(classes("brev")) {
                            img(classes = classes("logo"), src = navLogoImg) // TODO: Alt text?
//                            brevdato(scope)
                            h1(classes("tittel")) { renderText(scope, template.title) }
                            div(classes("brevhode")) {
//                                renderSakspart(scope)
//                                brevdato(scope)
                            }
                            div(classes("brevkropp")) {
                                renderOutline(scope, template.outline)
//                                renderClosing(scope)
                            }
                        }
//                        render(scope, template.attachments) { attachmentScope, _, attachment ->
//                            renderAttachment(attachmentScope, attachment)
//                        }
                    }
                }
            }
        }
    }

    private fun FlowContent.renderExpression(scope: ExpressionScope<*, *>, expr: Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage<*>) =
        renderExpression(expr.expr(scope.language), false)

    private fun FlowContent.renderExpression(expr: Expression<*>, useLiteralNotation: Boolean) {
        when (expr) {
            is Expression.BinaryInvoke<*, *, *> -> {
                renderExpression(expr.first, useLiteralNotation)
                text(expr.operation.symbol)
                renderExpression(expr.second, useLiteralNotation)
            }

            is Expression.FromScope<*, *> -> exprContainer(expr) { text(expr.__name) }
            is Expression.Literal -> renderLiteral(expr.value, useLiteralNotation)
            is Expression.UnaryInvoke<*, *> -> when (expr.operation) {
                UnaryOperation.FormatPhoneNumber -> {
                    renderExpression(expr.value, useLiteralNotation)
                    exprContainer(expr) { text(".format()") }
                }

                is UnaryOperation.IfNull<*> -> {
                    renderExpression(expr.value, useLiteralNotation)
                    text(" ?: ")
                    renderLiteral(expr.operation.then, true)
                }

                UnaryOperation.Not -> {
                    if (expr.value is Expression.BinaryInvoke<*, * , *> && expr.value.operation is BinaryOperation.Equal) {
                        renderExpression(expr.value.first, useLiteralNotation)
                        text(" != ")
                        renderExpression(expr.value.second, useLiteralNotation)
                    } else {
                        text("!")
                        renderExpression(expr.value, useLiteralNotation)
                    }
                }

                is UnaryOperation.SafeCall -> {
                    renderExpression(expr.value, useLiteralNotation)
                    exprContainer(expr) { text("?." + expr.operation.selector.propertyName) }
                }

                is UnaryOperation.Select -> {
                    renderExpression(expr.value, useLiteralNotation)
                    exprContainer(expr) { text("." + expr.operation.selector.propertyName) }
                }

                is UnaryOperation.ToString -> {
                    renderExpression(expr.value, useLiteralNotation)
                    text(".toString()")
                }
            }


            // TODO we know that the only remaining option is the private ForEach.NextExpression:
            // is ContentOrControlStructure.ForEach.NextExpression -> Unit
            else -> {
                exprContainer(expr) { text("\$item") }
            }
        }
    }

    private fun FlowContent.exprContainer(expr: Expression<*>, block: FlowContent.() -> Unit): Unit =
        div(classes("expression")) {
            id = "expr-${expr.hashCode()}"
            block()
        }

    private fun Tag.renderLiteral(value: Any?, useLiteralNotation: Boolean): Unit =
        if (useLiteralNotation && value is String) {
            text("\"$value\"")
        } else if (value is Pair<*, *>) {
            renderLiteral(value.first, true)
            text(" : ")
            renderLiteral(value.second, true)
        } else {
            text(value.toString())
        }

    private fun <C : Element<*>> FlowContent.render(scope: ExpressionScope<*, *>, elements: List<ContentOrControlStructure<*, C>>, renderBlock: (scope: ExpressionScope<*, *>, element: C) -> Unit) {
        elements.forEach {
            controlStructure(scope, it) { controlStructureScope, content ->
                renderBlock(controlStructureScope, content)
            }
        }
    }

    private fun <C : Element<*>> FlowContent.controlStructure(scope: ExpressionScope<*, *>, element: ContentOrControlStructure<*, C>, block: (s: ExpressionScope<*, *>, e: C) -> Unit) {
        when (element) {
            is ContentOrControlStructure.Content -> block(scope, element.content)
            is ContentOrControlStructure.Conditional -> {
                div(classes("conditional")) {
                    div(classes("conditional-line", "first"))
                    div { text("Hvis") }
                    div(classes("conditional-body")) {
                        div(classes("predicate")) {
                            div { renderExpression(element.predicate, true) }
                        }
                        div {
                            render(scope, element.showIf, block)
                        }
                    }
                    renderShowElse(scope, element, block)
                }
            }

            is ContentOrControlStructure.ForEach<*, C, *> -> {
                div(classes("foreach")) {
                    div(classes("foreach-expr")) {
                        text("For hver ")
                        renderExpression(element.nextExpr, true)
                        text(" i listen: ")
                        renderExpression(element.items, true)
                    }
                    div(classes("foreach-body")) {
                        render(scope, element.body) { bodyScope, forEachElement ->
                            renderAnyContent(bodyScope, forEachElement)
                        }
                    }
                }
            }
        }
    }

    private fun <C : Element<*>> FlowContent.renderShowElse(scope: ExpressionScope<*, *>, element: ContentOrControlStructure.Conditional<*, C>, block: (s: ExpressionScope<*, *>, e: C) -> Unit) {
        val elseIf = element.showElse.filterIsInstance<ContentOrControlStructure.Conditional<*, C>>().firstOrNull()
        if (elseIf != null && element.showElse.size == 1) {
            div(classes("conditional-line")) {
                hr()
            }
            div { text("Eller hvis") }
            div(classes("conditional-body")) {
                div(classes("predicate")) {
                    div { renderExpression(elseIf.predicate, true) }
                }
                div {
                    render(scope, elseIf.showIf, block)
                }
            }
            renderShowElse(scope, elseIf, block)
        } else if (element.showElse.isNotEmpty()) {
            div(classes("conditional-line", "last"))
            div { text("Ellers") }
            div(classes("conditional-body")) {
                render(scope, element.showElse, block)
            }
        }
    }

    // render Element

    private fun FlowContent.renderAnyContent(scope: ExpressionScope<*, *>, element: Element<*>) {
        when (element) {
            is Element.OutlineContent<*> -> renderOutlineContent(scope, element)
            is Element.OutlineContent.ParagraphContent.ItemList.Item<*> -> {
                LI(emptyMap(), consumer).visit { renderText(scope, element.text) }
            }
            is Element.OutlineContent.ParagraphContent.Table.Row -> {
                TR(emptyMap(), consumer).visit { renderCells(scope, element) }
            }
        }
    }

    private fun FlowContent.renderOutline(scope: ExpressionScope<*, *>, outline: List<OutlineElement<*>>) =
        render(scope, outline) { outlineScope, element ->
            renderOutlineContent(outlineScope, element)
        }

    private fun FlowContent.renderOutlineContent(scope: ExpressionScope<*, *>, element: Element.OutlineContent<*>): Unit =
        when (element) {
            is Element.OutlineContent.ParagraphContent -> renderParagraphContent(scope, element)
            is Element.OutlineContent.Paragraph -> renderParagraph(scope, element)
            is Element.OutlineContent.Title1 -> h2(classes("title1")) { renderText(scope, element.text) }
        }

    private fun FlowContent.renderParagraph(scope: ExpressionScope<*, *>, paragraph: Element.OutlineContent.Paragraph<*>) {
        div(classes("paragraph")) {
            render(scope, paragraph.paragraph) { pScope, element ->
                renderParagraphContent(pScope, element)
            }
        }
    }

    private fun FlowContent.renderParagraphContent(scope: ExpressionScope<*, *>, element: Element.OutlineContent.ParagraphContent<*>) {
        when (element) {
            is Element.OutlineContent.ParagraphContent.Form -> renderForm(scope, element)
            is Element.OutlineContent.ParagraphContent.Text -> renderTextContent(scope, element)
            is Element.OutlineContent.ParagraphContent.ItemList -> renderList(scope, element)
            is Element.OutlineContent.ParagraphContent.Table -> renderTable(scope, element)
        }
    }

    private fun FlowContent.renderText(scope: ExpressionScope<*, *>, elements: List<TextElement<*>>) {
        render(scope, elements) { inner, text -> renderTextContent(inner, text) }
    }

    private fun FlowContent.renderTextContent(scope: ExpressionScope<*, *>, element: Element.OutlineContent.ParagraphContent.Text<*>) {
        when (element.fontType) {
            Element.OutlineContent.ParagraphContent.Text.FontType.PLAIN -> renderTextContentWithoutStyle(scope, element)
            Element.OutlineContent.ParagraphContent.Text.FontType.BOLD -> span(classes("text-bold")) { renderTextContentWithoutStyle(scope, element) }
            Element.OutlineContent.ParagraphContent.Text.FontType.ITALIC -> span(classes("text-italic")) { renderTextContentWithoutStyle(scope, element) }
        }
    }

    private fun FlowContent.renderTextContentWithoutStyle(scope: ExpressionScope<*, *>, element: Element.OutlineContent.ParagraphContent.Text<*>): Unit =
        when (element) {
            is Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage -> renderExpression(scope, element)
            is Element.OutlineContent.ParagraphContent.Text.Expression -> renderExpression(element.expression, false)
            is Element.OutlineContent.ParagraphContent.Text.Literal -> text(element.text(scope.language))
            is Element.OutlineContent.ParagraphContent.Text.NewLine -> br
        }

    private fun FlowContent.renderForm(scope: ExpressionScope<*, *>, element: Element.OutlineContent.ParagraphContent.Form<*>): Unit =
        when (element) {
            // TODO: Dette er hacky
            is Element.OutlineContent.ParagraphContent.Form.MultipleChoice -> {
                div(classes("form-choice")) {
                    renderText(scope, listOf(element.prompt))
                    element.choices.forEach {
                        input(InputType.radio)
                        renderTextContent(scope, it)
                    }
                }
            }

            is Element.OutlineContent.ParagraphContent.Form.Text -> {
                div(classes("form-text")) {
                    renderText(scope, listOf(element.prompt))
                    input(type = InputType.text)
                }
            }
        }

    private fun FlowContent.renderList(scope: ExpressionScope<*, *>, element: Element.OutlineContent.ParagraphContent.ItemList<*>) {
        ul {
            render(scope, element.items) { listScope, content ->
                li { renderText(listScope, content.text) }
            }
        }
    }

    private fun FlowContent.renderTable(scope: ExpressionScope<*, *>, element: Element.OutlineContent.ParagraphContent.Table<*>) {
        table(classes("table")) {
            thead {
                tr {
                    element.header.colSpec.forEach {
                        th(classes = classes(alignmentClass(it.alignment))) {
                            colSpan = it.columnSpan.toString()
                            DIV(emptyMap, consumer).visit {
                                renderText(scope, it.headerContent.text)
                            }
                        }
                    }
                }
            }
            tbody {
//                render(scope, element.rows) { rowScope, row ->
//                    tr {
//                        row.cells.forEachIndexed { index, cell ->
//                            val spec = element.header.colSpec[index]
//                            td(classes = classes(alignmentClass(spec.alignment))) {
//                                colSpan = spec.columnSpan.toString()
//                                renderText(rowScope, cell.text)
//                            }
//                        }
//                    }
//                }
            }
        }
    }

    // TODO: Problem fordi vi i renderAnyContent ikke har tilgang til colSpec
    private fun TR.renderCells(scope: ExpressionScope<*, *>, row: Element.OutlineContent.ParagraphContent.Table.Row<*>) {
        row.cells.forEach {
            td { renderText(scope, it.text) }
        }
    }

    private fun classes(vararg classes: String?): String =
        classes.filterNotNull().joinToString(" ") { "pensjonsbrev-$it" }

    private fun alignmentClass(alignment: Element.OutlineContent.ParagraphContent.Table.ColumnAlignment): String =
        when (alignment) {
            Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT -> "text-left"
            Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT -> "text-right"
        }

    private fun FlowContent.renderSakspart(scope: ExpressionScope<*, *>) =
        div(classes("sakspart")) {
            with(scope.felles.bruker) {
                listOf(
                    LanguageSetting.Sakspart.navn to "$fornavn $mellomnavn $etternavn",
                    LanguageSetting.Sakspart.foedselsnummer to foedselsnummer.value,
                    LanguageSetting.Sakspart.saksnummer to scope.felles.saksnummer,
                )
            }.forEach {
                div(classes("sakspart-tittel")) { renderText(scope, languageSettings.settings[it.first]!!) }
                div(classes("sakspart-verdi")) { text(it.second) }
            }
        }
}