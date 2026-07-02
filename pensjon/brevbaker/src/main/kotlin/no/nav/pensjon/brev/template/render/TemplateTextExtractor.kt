package no.nav.pensjon.brev.template.render

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import no.nav.pensjon.brev.template.BinaryOperation.Documentation
import no.nav.pensjon.brev.template.render.TemplateDocumentation.ContentOrControlStructure
import no.nav.pensjon.brev.template.render.TemplateDocumentation.Element
import no.nav.pensjon.brev.template.render.TemplateDocumentation.Expression

/**
 * A line is rendered (and indexed by the search frontend) as a sequence of
 * segments. Literal text is searchable; variables are shown as a placeholder but
 * are never searchable. The JSON shape mirrors the frontend `LineSegment`
 * discriminated union (`{type:"text",value}` / `{type:"var",label}`), so the
 * brevoppskrift search index can consume the extracted lines directly without
 * having to flatten the documentation tree itself.
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

    // Sentinel marks where a variable sits while we normalise whitespace on the
    // raw string. It is not whitespace, so it survives collapsing, and is
    // extremely unlikely to occur in real letter text.
    private const val VAR_SENTINEL = "\u0001"

    private val WHITESPACE = Regex("\\s+")

    fun extract(documentation: TemplateDocumentation): List<TemplateTextBlock> =
        documentLines(documentation.title, documentation.outline) + documentation.attachments.flatMap { documentLines(it.title, it.outline) }

    private fun normalize(value: String): String = value.replace(WHITESPACE, " ").trim()

    private fun variableName(expression: Expression?): String =
        when (expression) {
            null -> ""
            is Expression.LetterData ->
                if (expression.scopeName == "argument" || expression.scopeName == "forEach_item") "" else expression.scopeName
            is Expression.Literal -> expression.value
            is Expression.Invoke -> {
                val field =
                    if (expression.operator.syntax == Documentation.Notation.POSTFIX && expression.operator.text.startsWith(".")) {
                        expression.operator.text.substring(1).trim()
                    } else {
                        ""
                    }
                field.ifEmpty { variableName(expression.first).ifEmpty { variableName(expression.second) } }
            }
        }

    private fun describeExpression(expression: Expression): String {
        val label = normalize(variableName(expression))
        if (label.isEmpty()) {
            return "variabel"
        }
        return if (label.length > MAX_VAR_LABEL_LENGTH) "${label.substring(0, MAX_VAR_LABEL_LENGTH)}…" else label
    }

    /** Recursively flattens a control-structure array to its CONTENT elements,
     * descending into conditionals (all branches) and for-each bodies while
     * preserving document order. */
    private fun <E : Element> flatten(items: List<ContentOrControlStructure<E>>): List<E> =
        items.flatMap { cocs ->
            when (cocs) {
                is ContentOrControlStructure.Content -> listOf(cocs.content)
                is ContentOrControlStructure.Conditional ->
                    flatten(cocs.showIf) + cocs.elseIf.flatMap { flatten(it.showIf) } + flatten(cocs.showElse)
                is ContentOrControlStructure.ForEach -> flatten(cocs.body)
            }
        }

    /** The raw text of a line (with a [VAR_SENTINEL] placeholder per variable) and
     * the variable labels in the order they appear. */
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

    /** Inline text (literals/expressions) accumulates into a single line; lists and
     * tables break that run and contribute their own lines, all in document order. */
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
                is Element.OutlineContent.Paragraph -> paragraphLines(element.paragraph).map { TemplateTextBlock(element.index, it) }
                else -> emptyList()
            }
        }
    }

    private fun documentLines(
        // `title` carries Text elements, which `elementLines` ignores (only Title/
        // Paragraph outline elements produce lines) — matching the frontend.
        title: List<ContentOrControlStructure<Element.ParagraphContent.Text>>,
        outline: List<ContentOrControlStructure<Element.OutlineContent>>,
    ): List<TemplateTextBlock> =
        elementLines(title) + elementLines(outline)
}

/** Trims leading/trailing whitespace only at the requested edges of the part. */
private fun String.trimEdges(start: Boolean, end: Boolean): String =
    (if (start) trimStart() else this).let { if (end) it.trimEnd() else it }
