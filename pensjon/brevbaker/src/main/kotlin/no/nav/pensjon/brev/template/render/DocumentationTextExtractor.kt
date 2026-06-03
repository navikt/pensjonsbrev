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
 * discriminated union (`{kind:"text",value}` / `{kind:"var",label}`), so the
 * brevoppskrift search index can consume the extracted lines directly without
 * having to flatten the documentation tree itself.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "kind", include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(SearchLineSegment.Text::class, name = "text"),
    JsonSubTypes.Type(SearchLineSegment.Variable::class, name = "var"),
)
@JsonPropertyOrder("kind")
sealed class SearchLineSegment {
    data class Text(val value: String) : SearchLineSegment()
    data class Variable(val label: String) : SearchLineSegment()
}

/** A single line: an ordered list of text/variable segments with a reference
 *  to the outline block it originates from. */
data class SearchLine(val blockId: String, val segments: List<SearchLineSegment>)

/**
 * Flattens a template's rendered [TemplateDocumentation] into an ordered list of
 * searchable lines, recursing through every control structure (all conditional
 * branches and for-each bodies) in document order. Variables are kept as `var`
 * segments (excluded from the searchable text); literal text is normalised
 * (collapsed whitespace).
 */
object DocumentationTextExtractor {

    private const val MAX_VAR_LABEL_LENGTH = 40

    // Sentinel marks where a variable sits while we normalise whitespace on the
    // raw string. It is not whitespace, so it survives collapsing, and is
    // extremely unlikely to occur in real letter text.
    private const val VAR_SENTINEL = "\u0001"

    private val WHITESPACE = Regex("\\s+")

    fun extract(documentation: TemplateDocumentation): List<SearchLine> {
        val lines = mutableListOf<SearchLine>()
        lines += linesFromDocument(documentation.title, documentation.outline)
        for (attachment in documentation.attachments) {
            lines += linesFromDocument(attachment.title, attachment.outline)
        }
        return lines
    }

    private fun normalize(value: String): String = value.replace(WHITESPACE, " ").trim()

    /** Best-effort short, human readable name for the variable in an expression so
     * the reader sees which field sits in the text. Field references are nested
     * POSTFIX accessors rooted at the `argument` scope; we surface the leaf field
     * name rather than the whole path. Only used for display. */
    private fun variableName(expression: Expression?): String =
        when (expression) {
            null -> ""
            is Expression.LetterData ->
                // The synthetic root scopes are not useful names.
                if (expression.scopeName == "argument" || expression.scopeName == "forEach_item") "" else expression.scopeName
            is Expression.Literal -> expression.value
            is Expression.Invoke -> {
                // A POSTFIX `.field` accessor is the property name; the outermost
                // one is the leaf field. Other operators (functions like format(),
                // infix, …) wrap the underlying field, so look inside their operands.
                val field =
                    if (expression.operator.syntax == Documentation.Notation.POSTFIX && expression.operator.text.startsWith(".")) {
                        expression.operator.text.substring(1).trim()
                    } else {
                        ""
                    }
                if (field.isNotEmpty()) field else variableName(expression.first).ifEmpty { variableName(expression.second) }
            }
        }

    private fun describeExpression(expression: Expression): String {
        val label = normalize(variableName(expression))
        if (label.isEmpty()) {
            return "variabel"
        }
        return if (label.length > MAX_VAR_LABEL_LENGTH) "${label.substring(0, MAX_VAR_LABEL_LENGTH)}…" else label
    }

    /** Iterates the CONTENT elements of a control-structure array, recursing
     * through conditionals (all branches) and for-each bodies, preserving
     * document order. */
    private fun <E : Element> eachContent(items: List<ContentOrControlStructure<E>>, fn: (E) -> Unit) {
        for (cocs in items) {
            when (cocs) {
                is ContentOrControlStructure.Conditional -> {
                    eachContent(cocs.showIf, fn)
                    for (elseIf in cocs.elseIf) {
                        eachContent(elseIf.showIf, fn)
                    }
                    eachContent(cocs.showElse, fn)
                }
                is ContentOrControlStructure.ForEach -> eachContent(cocs.body, fn)
                is ContentOrControlStructure.Content -> fn(cocs.content)
            }
        }
    }

    /** Accumulates raw text (with variable sentinels) plus the labels of those
     * variables, so a line can be assembled once whitespace has been normalised. */
    private class LineBuilder {
        val raw = StringBuilder()
        val labels = mutableListOf<String>()

        fun pushVariable(expression: Expression) {
            raw.append(VAR_SENTINEL)
            labels.add(describeExpression(expression))
        }
    }

    /** Turns an accumulated builder into a line, or null when there is nothing to
     * show. Variable sentinels are replaced by `var` segments paired with their
     * labels in order. */
    private fun finishLine(builder: LineBuilder): List<SearchLineSegment>? {
        val normalized = normalize(builder.raw.toString())
        if (normalized.isEmpty()) {
            return null
        }
        val parts = normalized.split(VAR_SENTINEL)
        val segments = mutableListOf<SearchLineSegment>()
        for (i in parts.indices) {
            var value = parts[i]
            if (i == 0) {
                value = value.trimStart()
            }
            if (i == parts.size - 1) {
                value = value.trimEnd()
            }
            if (value.isNotEmpty()) {
                segments.add(SearchLineSegment.Text(value))
            }
            if (i < builder.labels.size) {
                segments.add(SearchLineSegment.Variable(builder.labels[i]))
            }
        }
        return segments.ifEmpty { null }
    }

    private fun appendTextArray(builder: LineBuilder, items: List<ContentOrControlStructure<Element.ParagraphContent.Text>>) {
        eachContent(items) { text ->
            when (text) {
                is Element.ParagraphContent.Text.Literal -> builder.raw.append(text.text)
                is Element.ParagraphContent.Text.Expression -> builder.pushVariable(text.expression)
            }
        }
    }

    private fun segmentsFromTextArray(items: List<ContentOrControlStructure<Element.ParagraphContent.Text>>): List<SearchLineSegment>? {
        val builder = LineBuilder()
        appendTextArray(builder, items)
        return finishLine(builder)
    }

    private fun rowToSegments(row: Element.ParagraphContent.Table.Row): List<SearchLineSegment>? {
        val builder = LineBuilder()
        var first = true
        for (cell in row.cells) {
            val cellBuilder = LineBuilder()
            appendTextArray(cellBuilder, cell.text)
            if (normalize(cellBuilder.raw.toString()).isEmpty()) {
                continue
            }
            if (!first) {
                builder.raw.append(" | ")
            }
            builder.raw.append(cellBuilder.raw)
            builder.labels.addAll(cellBuilder.labels)
            first = false
        }
        return finishLine(builder)
    }

    private fun segmentsFromParagraph(items: List<ContentOrControlStructure<Element.ParagraphContent>>): List<List<SearchLineSegment>> {
        val lines = mutableListOf<List<SearchLineSegment>>()
        var buffer = LineBuilder()
        fun flush() {
            finishLine(buffer)?.let { lines.add(it) }
            buffer = LineBuilder()
        }
        fun pushLine(segments: List<SearchLineSegment>?) {
            if (segments != null) {
                lines.add(segments)
            }
        }

        eachContent(items) { content ->
            when (content) {
                is Element.ParagraphContent.Text.Literal -> buffer.raw.append(content.text)
                is Element.ParagraphContent.Text.Expression -> buffer.pushVariable(content.expression)
                is Element.ParagraphContent.ItemList -> {
                    flush()
                    eachContent(content.items) { item -> pushLine(segmentsFromTextArray(item.text)) }
                }
                is Element.ParagraphContent.Table -> {
                    flush()
                    pushLine(rowToSegments(content.header))
                    eachContent(content.rows) { row -> pushLine(rowToSegments(row)) }
                }
            }
        }
        flush()
        return lines
    }

    /** Handles outline-level elements (titles and paragraphs). Non-outline
     * elements (e.g. the bare `Text` elements of a document `title`) are ignored,
     * mirroring the frontend behaviour exactly. Each produced [SearchLine]
     * carries the `id` of its source outline element so the search frontend
     * can deep-link directly to the rendered block. */
    private fun linesFromElements(items: List<ContentOrControlStructure<out Element>>): List<SearchLine> {
        val lines = mutableListOf<SearchLine>()
        @Suppress("UNCHECKED_CAST")
        eachContent(items as List<ContentOrControlStructure<Element>>) { element ->
            when (element) {
                is Element.OutlineContent.Title1 -> segmentsFromTextArray(element.text)?.let { lines.add(SearchLine(element.id, it)) }
                is Element.OutlineContent.Title2 -> segmentsFromTextArray(element.text)?.let { lines.add(SearchLine(element.id, it)) }
                is Element.OutlineContent.Title3 -> segmentsFromTextArray(element.text)?.let { lines.add(SearchLine(element.id, it)) }
                is Element.OutlineContent.Paragraph -> segmentsFromParagraph(element.paragraph).forEach { lines.add(SearchLine(element.id, it)) }
                else -> Unit
            }
        }
        return lines
    }

    private fun linesFromDocument(
        title: List<ContentOrControlStructure<Element.ParagraphContent.Text>>,
        outline: List<ContentOrControlStructure<Element.OutlineContent>>,
    ): List<SearchLine> {
        val lines = mutableListOf<SearchLine>()
        // `title` carries Text elements, which `linesFromElements` ignores (only
        // Title/Paragraph outline elements produce lines) — matching the frontend.
        lines += linesFromElements(title)
        lines += linesFromElements(outline)
        return lines
    }
}
