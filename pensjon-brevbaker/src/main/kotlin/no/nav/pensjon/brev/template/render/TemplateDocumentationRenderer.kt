package no.nav.pensjon.brev.template.render

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.BinaryOperation.Documentation
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.intValueSelector
import no.nav.pensjon.brev.template.render.TemplateDocumentation.Expression.*
import no.nav.pensjon.brev.template.render.TemplateDocumentation.Expression.Invoke.Operation
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification

private typealias AssignedReplacements = Map<Expression.FromScope.Assigned<*>, Expression<*>>

object TemplateDocumentationRenderer {

    fun render(template: LetterTemplate<*, *>, lang: Language, modelSpecification: TemplateModelSpecification): TemplateDocumentation =
        TemplateDocumentation(
            title = renderText(template.title, lang),
            outline = renderOutline(template.outline, lang),
            attachments = template.attachments.map { renderAttachment(it, lang) },
            templateModelSpecification = modelSpecification,
        )

    private fun renderAttachment(attachment: IncludeAttachment<*, *>, lang: Language): TemplateDocumentation.Attachment =
        TemplateDocumentation.Attachment(
            title = renderText(attachment.template.title, lang),
            outline = renderOutline(attachment.template.outline, lang),
            include = renderExpression(attachment.predicate, emptyMap()),
            attachmentData = renderExpression(attachment.data, emptyMap()),
        )

    private fun <T : Element<*>, R : TemplateDocumentation.Element> renderContentOrStructure(
        contentOrStructure: List<ContentOrControlStructure<*, T>>,
        mapper: (T) -> List<R>,
    ): List<TemplateDocumentation.ContentOrControlStructure<R>> =
        contentOrStructure.flatMap { el -> renderContentOrStructure(el) { mapper(it) } }

    private fun <T : Element<*>, R : TemplateDocumentation.Element> renderContentOrStructure(
        contentOrStructure: ContentOrControlStructure<*, T>,
        mapper: (T) -> List<R>,
    ): List<TemplateDocumentation.ContentOrControlStructure<R>> =
        when (contentOrStructure) {
            is ContentOrControlStructure.Content -> mapper(contentOrStructure.content).map { TemplateDocumentation.ContentOrControlStructure.Content(it) }

            is ContentOrControlStructure.Conditional -> {
                val elseIf = liftNestedIfElse(contentOrStructure.showElse, mapper)
                listOf(
                    TemplateDocumentation.ContentOrControlStructure.Conditional(
                        predicate = renderExpression(contentOrStructure.predicate, emptyMap()),
                        showIf = renderContentOrStructure(contentOrStructure.showIf, mapper),
                        elseIf = elseIf.first,
                        showElse = elseIf.second,
                    )
                )
            }

            is ContentOrControlStructure.ForEach<*, T, *> -> listOf(
                TemplateDocumentation.ContentOrControlStructure.ForEach(
                    // TODO: consider if Next-expression should be assigned in renderExpression
                    items = renderExpression(contentOrStructure.items, emptyMap()),
                    body = renderContentOrStructure(contentOrStructure.body.toList(), mapper),
                )
            )
        }

    private fun <T : Element<*>, R : TemplateDocumentation.Element> liftNestedIfElse(
        showElse: List<ContentOrControlStructure<*, T>>,
        mapper: (T) -> List<R>,
    ): Pair<List<TemplateDocumentation.ContentOrControlStructure.Conditional.ElseIf<R>>, List<TemplateDocumentation.ContentOrControlStructure<R>>> {
        val first = showElse.firstOrNull()
        return if (showElse.size == 1 && first is ContentOrControlStructure.Conditional) {
            liftNestedIfElse(first.showElse, mapper).let { (nestedIfElse, nestedElse) ->
                listOf(
                    TemplateDocumentation.ContentOrControlStructure.Conditional.ElseIf(
                        renderExpression(first.predicate, emptyMap()),
                        renderContentOrStructure(first.showIf, mapper)
                    )
                ).plus(nestedIfElse) to nestedElse
            }
        } else {
            emptyList<TemplateDocumentation.ContentOrControlStructure.Conditional.ElseIf<R>>() to renderContentOrStructure(showElse, mapper)
        }
    }

    private fun renderOutline(
        outline: List<OutlineElement<*>>,
        lang: Language,
    ): List<TemplateDocumentation.ContentOrControlStructure<TemplateDocumentation.Element.OutlineContent>> =
        renderContentOrStructure(outline) { listOf(renderOutline(it, lang)) }

    private fun renderOutline(element: Element.OutlineContent<*>, lang: Language): TemplateDocumentation.Element.OutlineContent =
        when (element) {
            is Element.OutlineContent.Title1 -> TemplateDocumentation.Element.OutlineContent.Title1(renderText(element.text, lang))
            is Element.OutlineContent.Title2 -> TemplateDocumentation.Element.OutlineContent.Title2(renderText(element.text, lang))
            is Element.OutlineContent.Paragraph -> TemplateDocumentation.Element.OutlineContent.Paragraph(
                renderContentOrStructure(element.paragraph) {
                    renderParagraphContent(it, lang)
                }
            )
        }

    private fun renderParagraphContent(
        element: Element.OutlineContent.ParagraphContent<*>,
        lang: Language,
    ): List<TemplateDocumentation.Element.ParagraphContent> =
        when (element) {
            is Element.OutlineContent.ParagraphContent.Form -> listOf(TemplateDocumentation.Element.ParagraphContent.Text.Literal("## missing documentation ##"))
            is Element.OutlineContent.ParagraphContent.ItemList -> listOf(
                TemplateDocumentation.Element.ParagraphContent.ItemList(
                    renderContentOrStructure(element.items) { listOf(renderItem(it, lang)) }
                )
            )

            is Element.OutlineContent.ParagraphContent.Table -> listOf(renderTable(element, lang))
            is Element.OutlineContent.ParagraphContent.Text -> renderText(element, lang)
        }

    private fun renderTable(table: Element.OutlineContent.ParagraphContent.Table<*>, lang: Language): TemplateDocumentation.Element.ParagraphContent {
        return TemplateDocumentation.Element.ParagraphContent.Table(
            header = renderRow(table.header.colSpec.map { it.headerContent }, lang),
            rows = renderContentOrStructure(table.rows) { listOf(renderRow(it.cells, lang)) }
        )
    }

    private fun renderRow(
        cells: List<Element.OutlineContent.ParagraphContent.Table.Cell<*>>,
        lang: Language,
    ): TemplateDocumentation.Element.ParagraphContent.Table.Row =
        TemplateDocumentation.Element.ParagraphContent.Table.Row(cells.map {
            TemplateDocumentation.Element.ParagraphContent.Table.Cell(
                renderText(
                    it.text,
                    lang
                )
            )
        })

    private fun renderText(
        text: List<TextElement<*>>,
        lang: Language,
    ): List<TemplateDocumentation.ContentOrControlStructure<TemplateDocumentation.Element.ParagraphContent.Text>> =
        renderContentOrStructure(text) { renderText(it, lang) }

    private fun renderText(
        element: Element.OutlineContent.ParagraphContent.Text<*>,
        lang: Language,
    ): List<TemplateDocumentation.Element.ParagraphContent.Text> =
        when (element) {
            is Element.OutlineContent.ParagraphContent.Text.Literal -> listOf(TemplateDocumentation.Element.ParagraphContent.Text.Literal(element.text(lang)))
            is Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage -> renderTextExpression(element.expr(lang))
            is Element.OutlineContent.ParagraphContent.Text.Expression -> renderTextExpression(element.expression)
            is Element.OutlineContent.ParagraphContent.Text.NewLine -> emptyList()
        }

    private fun renderItem(
        item: Element.OutlineContent.ParagraphContent.ItemList.Item<*>,
        lang: Language,
    ): TemplateDocumentation.Element.ParagraphContent.ItemList.Item =
        TemplateDocumentation.Element.ParagraphContent.ItemList.Item(renderText(item.text, lang))

    private fun renderTextExpression(
        expr: Expression<String>,
    ): List<TemplateDocumentation.Element.ParagraphContent.Text> =
        expr.flattenLiteralConcat().mergeLiterals().map {
            if (it is Expression.Literal<String>) {
                TemplateDocumentation.Element.ParagraphContent.Text.Literal(it.value)
            } else {
                TemplateDocumentation.Element.ParagraphContent.Text.Expression(renderExpression(it, emptyMap()))
            }
        }

    private fun renderExpression(
        expr: Expression<*>,
        assignments: AssignedReplacements,
    ): TemplateDocumentation.Expression =
        when (expr) {
            is Expression.BinaryInvoke<*, *, *> -> renderBinaryInvoke(expr, assignments)
            is Expression.FromScope.Language -> LetterData("language")
            is Expression.FromScope.Felles -> LetterData("felles")
            is Expression.FromScope.Argument -> LetterData("argument")
            is Expression.FromScope.Assigned ->
                assignments[expr]
                    ?.let { renderExpression(it, assignments) }
                    ?: LetterData("forEach_item")

            is Expression.Literal -> Literal(expr.value.toString())
            is Expression.UnaryInvoke<*, *> -> renderUnaryInvoke(expr, assignments)
            is Expression.NullSafeApplication<*, *> -> renderExpression(
                expr = expr.application,
                assignments = assignments + (expr.assigned to expr.input)
            )
        }

    private fun renderBinaryInvoke(expr: Expression.BinaryInvoke<*, *, *>, assignments: AssignedReplacements) =
        when (expr.operation) {
            is LocalizedFormatter<*> -> renderExpression(expr.first, assignments)
            is BinaryOperation.IfNull<*> ->
                if (expr.second is Expression.Literal && (expr.second as Expression.Literal<Any?>).value == false) {
                    renderExpression(expr.first, assignments)
                } else {
                    renderAnyBinaryInvoke(expr, assignments)
                }

            else -> renderAnyBinaryInvoke(expr, assignments)
        }

    private fun renderAnyBinaryInvoke(
        expr: Expression.BinaryInvoke<*, *, *>,
        assignments: AssignedReplacements,
    ) =
        Invoke(
            renderOperation(expr.operation),
            renderExpression(expr.first, assignments),
            renderExpression(expr.second, assignments),
            "TODO"
        )

    private fun renderUnaryInvoke(
        expr: Expression.UnaryInvoke<*, *>,
        assignments: AssignedReplacements,
    ): TemplateDocumentation.Expression {

        // Representation of expression corresponding to `!(a == b) ` as `a != b`.
        if (expr.operation is UnaryOperation.Not
            && expr.value is Expression.BinaryInvoke<*, *, *>
            && (expr.value as Expression.BinaryInvoke<*, *, *>).operation is BinaryOperation.Equal<*>
        ) {
            return Invoke(
                operator = Operation("!=", Documentation.Notation.INFIX),
                first = renderExpression(
                    (expr.value as Expression.BinaryInvoke<*, *, *>).first,
                    assignments
                ),
                second = renderExpression(
                    (expr.value as Expression.BinaryInvoke<*, *, *>).second,
                    assignments
                ),
            )
        }
        // Representation of expression corresponding to `a.value` as `a` when they are IntValue.
        if (expr.operation is UnaryOperation.Select
            && (expr.operation as UnaryOperation.Select<*, *>).selector != intValueSelector
        ) {
            renderExpression(expr.value, assignments)
        }

        return Invoke(
            operator = renderOperation(expr.operation),
            first = renderExpression(expr.value, assignments),
        )
    }

    private fun renderOperation(operation: UnaryOperation<*, *>): Operation =
        when (operation) {
            is UnaryOperation.AbsoluteValue -> Operation("abs", Documentation.Notation.FUNCTION)

            is UnaryOperation.AbsoluteValueKroner -> Operation("abs", Documentation.Notation.FUNCTION)

            is UnaryOperation.MapCollection<*, *> -> TODO()
            is UnaryOperation.Not -> Operation("!", Documentation.Notation.PREFIX)

            // TODO: We currently skip over representing `?` of safeCalls.
            is UnaryOperation.SafeCall -> renderOperation(operation.operation)

            is UnaryOperation.Select -> Operation(
                ".${operation.selector.propertyName}",
                Documentation.Notation.POSTFIX
            )

            is UnaryOperation.SizeOf -> Operation("size", Documentation.Notation.POSTFIX)

            is UnaryOperation.ToString -> Operation("str", Documentation.Notation.FUNCTION)

            is UnaryOperation.IsEmpty -> Operation(text = "isEmpty", Documentation.Notation.FUNCTION)

            is UnaryOperation.FunksjonsbryterEnabled -> Operation(text = "enabled", Documentation.Notation.FUNCTION)

            is UnaryOperation.BrukerFulltNavn -> Operation("fulltNavn", Documentation.Notation.FUNCTION)

            is UnaryOperation.MapValue<*, *> -> Operation(operation.mapper.name, Documentation.Notation.FUNCTION)

            is UnaryOperation.QuotationEnd -> Operation(text = "\"", Documentation.Notation.POSTFIX)

            is UnaryOperation.QuotationStart -> Operation(text = "\"", Documentation.Notation.PREFIX)
        }

    private fun renderOperation(operation: BinaryOperation<*, *, *>): Operation =
        operation.doc?.let {
            Operation(
                text = it.name,
                syntax = it.syntax,
            )
        } ?: Operation(
            text = operation::class.simpleName ?: "undocumentedOperation",
            syntax = Documentation.Notation.FUNCTION
        )

    fun StringExpression.flattenLiteralConcat(): List<StringExpression> =
        if (this is Expression.Literal) {
            listOf(this)
        } else if (this is Expression.BinaryInvoke<*, *, *> && operation is BinaryOperation.Concat) {
            @Suppress("UNCHECKED_CAST")
            (first as StringExpression).flattenLiteralConcat() + (second as StringExpression).flattenLiteralConcat()
        } else {
            listOf(this)
        }

    fun List<StringExpression>.mergeLiterals(): List<StringExpression> =
        fold(emptyList()) { acc, current ->
            val previous = acc.lastOrNull()
            if (acc.isEmpty()) {
                listOf(current)
            } else if (previous is Expression.Literal<String> && current is Expression.Literal<String>) {
                acc.subList(0, acc.size - 1) + (previous.value + current.value).expr()
            } else {
                acc + current
            }
        }

}

data class TemplateDocumentation(
    val title: List<ContentOrControlStructure<Element.ParagraphContent.Text>>,
    val outline: List<ContentOrControlStructure<Element.OutlineContent>>,
    val attachments: List<Attachment>,
    val templateModelSpecification: TemplateModelSpecification,
) {
    data class Attachment(
        val title: List<ContentOrControlStructure<Element.ParagraphContent.Text>>,
        val outline: List<ContentOrControlStructure<Element.OutlineContent>>,
        val include: Expression,
        val attachmentData: Expression,
    )

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "controlStructureType", include = JsonTypeInfo.As.PROPERTY)
    @JsonSubTypes(
        JsonSubTypes.Type(ContentOrControlStructure.Conditional::class, name = "CONDITIONAL"),
        JsonSubTypes.Type(ContentOrControlStructure.Content::class, name = "CONTENT"),
        JsonSubTypes.Type(ContentOrControlStructure.ForEach::class, name = "FOR_EACH"),
    )
    @JsonPropertyOrder("controlStructureType")
    sealed class ContentOrControlStructure<E : Element> {
        data class Content<E : Element>(val content: E) : ContentOrControlStructure<E>()
        data class Conditional<E : Element>(
            val predicate: Expression,
            val showIf: List<ContentOrControlStructure<E>>,
            val elseIf: List<ElseIf<E>>,
            val showElse: List<ContentOrControlStructure<E>>,
        ) : ContentOrControlStructure<E>() {
            data class ElseIf<E : Element>(val predicate: Expression, val showIf: List<ContentOrControlStructure<E>>)
        }

        data class ForEach<E : Element>(
            val items: Expression,
            val body: List<ContentOrControlStructure<E>>,
        ) : ContentOrControlStructure<E>()
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "elementType", include = JsonTypeInfo.As.PROPERTY)
    @JsonSubTypes(
        JsonSubTypes.Type(Element.OutlineContent.Title1::class, name = "TITLE1"),
        JsonSubTypes.Type(Element.OutlineContent.Title2::class, name = "TITLE2"),
        JsonSubTypes.Type(Element.OutlineContent.Paragraph::class, name = "PARAGRAPH"),
        JsonSubTypes.Type(Element.ParagraphContent.Text.Literal::class, name = "PARAGRAPH_TEXT_LITERAL"),
        JsonSubTypes.Type(Element.ParagraphContent.Text.Expression::class, name = "PARAGRAPH_TEXT_EXPRESSION"),
        JsonSubTypes.Type(Element.ParagraphContent.ItemList::class, name = "PARAGRAPH_ITEMLIST"),
        JsonSubTypes.Type(Element.ParagraphContent.ItemList.Item::class, name = "PARAGRAPH_ITEMLIST_ITEM"),
        JsonSubTypes.Type(Element.ParagraphContent.Table::class, name = "PARAGRAPH_TABLE"),
        JsonSubTypes.Type(Element.ParagraphContent.Table.Row::class, name = "PARAGRAPH_TABLE_ROW"),
    )
    @JsonPropertyOrder("elementType")
    sealed class Element {
        sealed class OutlineContent : Element() {
            data class Title1(val text: List<ContentOrControlStructure<ParagraphContent.Text>>) : OutlineContent()
            data class Title2(val text: List<ContentOrControlStructure<ParagraphContent.Text>>) : OutlineContent()
            data class Paragraph(val paragraph: List<ContentOrControlStructure<ParagraphContent>>) : OutlineContent()
        }

        sealed class ParagraphContent : Element() {
            sealed class Text : ParagraphContent() {
                data class Literal(val text: String) : Text()
                data class Expression(val expression: TemplateDocumentation.Expression) : Text()
            }

            data class ItemList(val items: List<ContentOrControlStructure<Item>>) : ParagraphContent() {
                data class Item(val text: List<ContentOrControlStructure<Text>>) : Element()
            }

            data class Table(val header: Row, val rows: List<ContentOrControlStructure<Row>>) : ParagraphContent() {
                data class Row(val cells: List<Cell>) : Element()
                data class Cell(val text: List<ContentOrControlStructure<Text>>)
            }
        }
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
    @JsonSubTypes(
        JsonSubTypes.Type(Literal::class, name = "LITERAL"),
        JsonSubTypes.Type(LetterData::class, name = "LETTER_DATA"),
        JsonSubTypes.Type(Invoke::class, name = "INVOKE"),
    )
    sealed class Expression {
        data class Literal(val value: String) : Expression()
        data class LetterData(val scopeName: String) : Expression()

        @JsonInclude(JsonInclude.Include.NON_NULL)
        data class Invoke(val operator: Operation, val first: Expression, val second: Expression? = null, val type: String? = null) : Expression() {
            data class Operation(val text: String, val syntax: Documentation.Notation)
        }
    }
}