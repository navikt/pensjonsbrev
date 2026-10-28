package no.nav.pensjon.brev.template.render

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import no.nav.pensjon.brev.template.render.TemplateDocumentation.ContentOrControlStructure
import no.nav.pensjon.brev.template.render.TemplateDocumentation.Element
import no.nav.pensjon.brev.template.render.TemplateDocumentation.Expr

/**
 * A line is rendered (and indexed by the search frontend) as a sequence of
 * segments. Literal text is searchable; variables are shown as a placeholder but
 * are never searchable. The JSON shape mirrors the frontend `LineSegment`
 * discriminated union (`{type:"text",value}` / `{type:"var",label}`).
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(TextSegment.Text::class, name = "text"),
    JsonSubTypes.Type(TextSegment.Variable::class, name = "var"),
)
@JsonPropertyOrder("type")
sealed class TextSegment {
    data class Text(val value: String) : TextSegment()
    data class Variable(val label: String) : TextSegment()
}

data class TemplateTextBlock(val index: Int, val segments: List<TextSegment>)

object TemplateTextExtractor {

    private const val MAX_VAR_LABEL_LENGTH = 40
    private const val VAR_SENTINEL = "\u0001"
    private val WHITESPACE = Regex("\\s+")

    fun extract(documentation: TemplateDocumentation): List<TemplateTextBlock> =
        elementLines(documentation.outline) +
            documentation.attachments.flatMap { elementLines(it.outline) }

    private fun normalize(value: String): String = value.replace(WHITESPACE, " ").trim()

    private fun variableName(expression: Expr?): String =
        when (expression) {
            null -> ""
            is Expr.Literal -> expression.value
            is Expr.FieldPath ->
                expression.segments.lastOrNull()
                    ?: when (val source = expression.source) {
                        is TemplateDocumentation.DataSource.Scope -> source.name.takeUnless { it == "argument" }.orEmpty()
                        is TemplateDocumentation.DataSource.ForEachVar -> ""
                        is TemplateDocumentation.DataSource.Computed -> variableName(source.expr)
                    }
            is Expr.AssociativeOp -> expression.operands.firstNotNullOfOrNull { variableName(it).takeIf(String::isNotEmpty) }.orEmpty()
            is Expr.Comparison -> variableName(expression.left).ifEmpty { variableName(expression.right) }
            is Expr.Not -> variableName(expression.term)
            is Expr.FunctionCall -> expression.args.firstNotNullOfOrNull { variableName(it).takeIf(String::isNotEmpty) }.orEmpty()
            is Expr.Format -> variableName(expression.value)
            is Expr.Conditional -> variableName(expression.predicate).ifEmpty { variableName(expression.ifTrue) }
            is Expr.NullCoalesce -> variableName(expression.value).ifEmpty { variableName(expression.fallback) }
            is Expr.EditableField -> variableName(expression.value).ifEmpty { variableName(expression.fallback) }
        }

    private fun describeExpression(expression: Expr): String {
        val label = normalize(variableName(expression))
        if (label.isEmpty()) {
            return "variabel"
        }
        return if (label.length > MAX_VAR_LABEL_LENGTH) "${label.substring(0, MAX_VAR_LABEL_LENGTH)}…" else label
    }

    private fun <E : Element> flatten(items: List<ContentOrControlStructure<E>>): List<E> =
        items.flatMap { cocs ->
            when (cocs) {
                is ContentOrControlStructure.Content -> listOf(cocs.content)
                is ContentOrControlStructure.Conditional ->
                    flatten(cocs.showIf) + cocs.elseIf.flatMap { flatten(it.showIf) } + flatten(cocs.showElse)
                is ContentOrControlStructure.ForEach -> flatten(cocs.body)
            }
        }

    private data class LineParts(val raw: String, val labels: List<String>) {
        val isVisible: Boolean get() = normalize(raw).isNotEmpty()
    }

    private fun partsOf(texts: List<Element.ParagraphContent.Text>): LineParts =
        LineParts(
            raw = texts.joinToString("") { text ->
                when (text) {
                    is Element.ParagraphContent.Text.Literal -> text.text
                    is Element.ParagraphContent.Text.Expression -> VAR_SENTINEL
                }
            },
            labels = texts.filterIsInstance<Element.ParagraphContent.Text.Expression>().map { describeExpression(it.expression) },
        )

    private fun LineParts.toSegments(): List<TextSegment>? =
        normalize(raw).split(VAR_SENTINEL)
            .flatMapIndexed { i, part ->
                val text = part.trimEdges(start = i == 0, end = i == labels.size)
                listOfNotNull(
                    text.ifEmpty { null }?.let(TextSegment::Text),
                    labels.getOrNull(i)?.let(TextSegment::Variable),
                )
            }
            .ifEmpty { null }

    private fun lineFrom(texts: List<ContentOrControlStructure<Element.ParagraphContent.Text>>): List<TextSegment>? =
        partsOf(flatten(texts)).toSegments()

    private fun rowLine(row: Element.ParagraphContent.Table.Row): List<TextSegment>? {
        val cells = row.cells.map { partsOf(flatten(it.text)) }.filter { it.isVisible }
        return LineParts(cells.joinToString(" | ") { it.raw }, cells.flatMap { it.labels }).toSegments()
    }

    private fun listLines(list: Element.ParagraphContent.ItemList): List<List<TextSegment>> =
        flatten(list.items).mapNotNull { lineFrom(it.text) }

    private fun tableLines(table: Element.ParagraphContent.Table): List<List<TextSegment>> =
        (listOf(table.header) + flatten(table.rows)).mapNotNull(::rowLine)

    private fun paragraphLines(items: List<ContentOrControlStructure<Element.ParagraphContent>>): List<List<TextSegment>> {
        fun lineOf(run: List<Element.ParagraphContent.Text>) = listOfNotNull(partsOf(run).toSegments())

        val (lines, trailingRun) = flatten(items).fold(
            emptyList<List<TextSegment>>() to emptyList<Element.ParagraphContent.Text>(),
        ) { (lines, run), content ->
            when (content) {
                is Element.ParagraphContent.Text -> lines to (run + content)
                is Element.ParagraphContent.ItemList -> (lines + lineOf(run) + listLines(content)) to emptyList()
                is Element.ParagraphContent.Table -> (lines + lineOf(run) + tableLines(content)) to emptyList()
            }
        }
        return lines + lineOf(trailingRun)
    }

    private fun titleBlock(index: Int, text: List<ContentOrControlStructure<Element.ParagraphContent.Text>>): List<TemplateTextBlock> =
        listOfNotNull(lineFrom(text)?.let { TemplateTextBlock(index, it) })

    private fun elementLines(items: List<ContentOrControlStructure<out Element>>): List<TemplateTextBlock> {
        @Suppress("UNCHECKED_CAST")
        return flatten(items as List<ContentOrControlStructure<Element>>).flatMap { element ->
            when (element) {
                is Element.OutlineContent.Title1 -> titleBlock(element.index, element.text)
                is Element.OutlineContent.Title2 -> titleBlock(element.index, element.text)
                is Element.OutlineContent.Title3 -> titleBlock(element.index, element.text)
                is Element.OutlineContent.Paragraph ->
                    paragraphLines(element.paragraph).map { TemplateTextBlock(element.index, it) }
                else -> emptyList()
            }
        }
    }
}

private fun String.trimEdges(start: Boolean, end: Boolean): String =
    (if (start) trimStart() else this).let { if (end) it.trimEnd() else it }
