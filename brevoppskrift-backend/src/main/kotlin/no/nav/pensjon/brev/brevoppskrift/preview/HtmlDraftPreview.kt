package no.nav.pensjon.brev.brevoppskrift.preview

import com.fasterxml.jackson.databind.JsonNode
import no.nav.pensjon.brev.brevoppskrift.api.DraftPreview
import no.nav.pensjon.brev.brevoppskrift.api.PreviewRequest
import no.nav.pensjon.brev.brevoppskrift.api.PreviewResult
import no.nav.pensjon.brev.brevoppskrift.model.*
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

/** A deliberately small interpreter for the model, not a Kotlin evaluator or production letter renderer. */
class HtmlDraftPreview : DraftPreview {
    override fun render(request: PreviewRequest): PreviewResult {
        val issues = DraftValidator.validate(request.draft)
        if (issues.isNotEmpty()) return PreviewResult("", issues.map { "${it.path}: ${it.message}" })
        if (request.language !in request.draft.metadata.languages) return PreviewResult("", listOf("Språket finnes ikke i utkastet."))
        val scenario = request.scenarioId?.let { id -> request.draft.scenarios.find { it.id == id } }
        if (request.scenarioId != null && scenario == null) return PreviewResult("", listOf("Scenarioet finnes ikke."))
        val renderer = Renderer(request.draft, request.language, scenario)
        return renderer.render()
    }

    private class Renderer(private val draft: Draft, private val language: DraftLanguage, private val scenario: TestScenario?) {
        private val warnings = linkedSetOf<String>()
        private val fields = mutableMapOf<FieldId, DataField>()
        private val parents = mutableMapOf<FieldId, FieldId>()
        private val locale = when (language) {
            DraftLanguage.NB -> Locale.forLanguageTag("nb-NO")
            DraftLanguage.NN -> Locale.forLanguageTag("nn-NO")
            DraftLanguage.EN -> Locale.UK
        }
        private var remaining = 10_000

        init {
            if (draft.source != null) {
                warnings.add("Kildeimporten støtter bare tekstendringer med låst struktur. Forhåndsvisningen viser strukturerte tekstnoder, ikke full rendring av originalmalen.")
                warnings.add("Scenarioverdier evaluerer bare eksplisitte felt, valg og vilkår i utkastmodellen. Original Kotlin, frasekall, vedlegg og skjult logikk evalueres ikke.")
            }
            fun register(children: List<DataField>, parent: FieldId? = null) {
                children.forEach {
                    fields[it.id] = it
                    if (parent != null) parents[it.id] = parent
                    register(it.children, it.id)
                }
            }
            register(draft.fields)
        }

        fun render(): PreviewResult {
            val lang = when (language) { DraftLanguage.NB -> "nb"; DraftLanguage.NN -> "nn"; DraftLanguage.EN -> "en" }
            val content = blocks(draft.outline, scenario?.values.orEmpty())
            val attachments = draft.attachments.joinToString("") { attachment ->
                val copied = attachment.draft
                val binding = attachment.fieldId?.let { value(it, scenario?.values.orEmpty()) }
                if (attachment.fieldId != null && (binding == null || binding.isNull)) {
                    if (binding == null) warnings.add("Vedlegget ${attachment.id.value} mangler scenarioverdi for objektbindingen.")
                    return@joinToString ""
                }
                val values = if (binding?.isObject == true) copied.fields.mapNotNull { field -> binding.get(field.name)?.let { field.id to it } }.toMap() else emptyMap()
                val childScenario = if (binding != null) TestScenario(ScenarioId("preview"), "Vedlegg", values) else copied.scenarios.find { it.id == scenario?.id }
                val child = Renderer(copied, language, childScenario).render()
                warnings.addAll(child.warnings)
                "<section class=\"attachment\">${child.html}</section>"
            }
            return PreviewResult("<article lang=\"$lang\"><h1>${escape(draft.title[language].orEmpty())}</h1>$content$attachments</article>", warnings.toList())
        }

        private fun budget(): Boolean {
            remaining--
            if (remaining < 0) warnings.add("Forhåndsvisningen ble avkortet fordi scenarioet er for stort.")
            return remaining >= 0
        }

        private fun value(id: FieldId, environment: Map<FieldId, JsonNode>): JsonNode? {
            if (id in environment) return environment[id]
            val parent = parents[id] ?: return null
            return value(parent, environment)?.get(fields[id]?.name.orEmpty())
        }

        private fun blocks(content: List<Block>, environment: Map<FieldId, JsonNode>): String = buildString {
            for (block in content) {
                if (!budget()) break
                append(when (block) {
                    is Block.Heading -> "<h${block.level + 1}>${inline(block.text, environment)}</h${block.level + 1}>"
                    is Block.Paragraph -> "<p>${inline(block.text, environment)}</p>"
                    is Block.ItemList -> {
                        val tag = if (block.numbered) "ol" else "ul"
                        "<$tag>${block.items.joinToString("") { if (budget()) "<li>${inline(it, environment)}</li>" else "" }}</$tag>"
                    }
                    is Block.Table -> "<table><thead><tr>${block.header.joinToString("") { "<th>${escape(it[language].orEmpty())}</th>" }}</tr></thead><tbody>" +
                        block.rows.joinToString("") { row -> if (budget()) "<tr>${row.joinToString("") { "<td>${inline(it, environment)}</td>" }}</tr>" else "" } + "</tbody></table>"
                    is Block.Conditional -> when (test(block.predicate, environment)) {
                        true -> blocks(block.ifTrue, environment)
                        false -> blocks(block.ifFalse, environment)
                        null -> "<p>[Vilkår mangler scenarioverdi]</p>"
                    }
                    is Block.Repeat -> {
                        val list = value(block.fieldId, environment)
                        if (list?.isArray != true) {
                            warnings.add("Gjentakelsen ${block.id.value} mangler en listeverdi.")
                            "<p>[Gjentakelse uten scenario]</p>"
                        } else buildString {
                            for (item in list) {
                                if (!budget()) break
                                append(blocks(block.body, environment + (block.fieldId to item)))
                            }
                        }
                    }
                    is Block.LockedSource -> {
                        warnings.add("Låst kildeinnhold ${block.bindingId} er bevart i Kotlin, men evalueres ikke i forhåndsvisningen.")
                        "<p>[Låst kildeinnhold]</p>"
                    }
                })
            }
        }

        private fun inline(content: List<Inline>, environment: Map<FieldId, JsonNode>): String = buildString {
            for (node in content) {
                if (!budget()) break
                append(when (node) {
                    is Inline.Text -> {
                        val text = escape(node.text[language].orEmpty())
                        when (node.fontType) { FontType.PLAIN -> text; FontType.BOLD -> "<strong>$text</strong>"; FontType.ITALIC -> "<em>$text</em>" }
                    }
                    Inline.NewLine -> "<br>"
                    is Inline.Field -> {
                        val fieldValue = value(node.fieldId, environment)
                        if (fieldValue == null) {
                            warnings.add("Feltet ${fields[node.fieldId]?.name ?: node.fieldId.value} mangler scenarioverdi.")
                            escape("[${fields[node.fieldId]?.name ?: node.fieldId.value}]")
                        } else if (fieldValue.isNull) escape(node.fallback?.get(language).orEmpty())
                        else escape(when (node.formatter) {
                            Formatter.DEFAULT -> when (fields[node.fieldId]?.kind) {
                                FieldKind.DOUBLE -> String.format(locale, "%.2f", fieldValue.doubleValue())
                                FieldKind.DATE -> LocalDate.parse(fieldValue.asText()).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(locale)).replace(' ', '\u00a0')
                                FieldKind.MONTH -> java.time.Month.of(fieldValue.intValue()).getDisplayName(java.time.format.TextStyle.FULL, locale)
                                FieldKind.PERCENT -> fieldValue.asText() + " %"
                                else -> fieldValue.asText()
                            }
                            Formatter.CURRENCY -> NumberFormat.getNumberInstance(locale).apply { maximumFractionDigits = 0 }.format(fieldValue.decimalValue()).replace(' ', '\u00a0')
                            Formatter.MONTH_YEAR -> LocalDate.parse(fieldValue.asText()).format(DateTimeFormatter.ofPattern("MMMM yyyy", locale))
                        })
                    }
                    is Inline.FreeText -> {
                        warnings.add("Fritekst fylles ut av saksbehandler og er ikke evaluert.")
                        escape("[${node.prompt[language].orEmpty()}]")
                    }
                    is Inline.Conditional -> when (test(node.predicate, environment)) {
                        true -> inline(node.ifTrue, environment)
                        false -> inline(node.ifFalse, environment)
                        null -> "[Vilkår mangler scenarioverdi]"
                    }
                })
            }
        }

        private fun test(predicate: Predicate, environment: Map<FieldId, JsonNode>): Boolean? {
            if (!budget()) return null
            return when (predicate) {
                is Predicate.IsNotNull -> value(predicate.fieldId, environment)?.let { !it.isNull } ?: unknown()
                is Predicate.Choice -> scenario?.choices?.get(predicate.choiceId)?.let { it == predicate.value } ?: unknown()
                is Predicate.Not -> test(predicate.predicate, environment)?.not()
                is Predicate.All -> {
                    val evaluated = predicate.predicates.map { test(it, environment) }
                    if (false in evaluated) false else if (null in evaluated) null else true
                }
                is Predicate.Any -> {
                    val evaluated = predicate.predicates.map { test(it, environment) }
                    if (true in evaluated) true else if (null in evaluated) null else false
                }
                is Predicate.Compare -> {
                    val actual = value(predicate.fieldId, environment)
                    if (actual == null || actual.isNull) unknown() else {
                        val order = when (val expected = predicate.value) {
                            is Literal.Text -> actual.asText().compareTo(expected.value)
                            is Literal.Number -> actual.decimalValue().compareTo(expected.value)
                            is Literal.BooleanValue -> actual.booleanValue().compareTo(expected.value)
                            is Literal.Date -> LocalDate.parse(actual.asText()).compareTo(LocalDate.parse(expected.value))
                        }
                        when (predicate.operator) {
                            Comparison.EQUAL -> order == 0
                            Comparison.NOT_EQUAL -> order != 0
                            Comparison.GREATER_THAN -> order > 0
                            Comparison.GREATER_THAN_OR_EQUAL -> order >= 0
                            Comparison.LESS_THAN -> order < 0
                            Comparison.LESS_THAN_OR_EQUAL -> order <= 0
                        }
                    }
                }
            }
        }

        private fun unknown(): Boolean? {
            warnings.add("Et vilkår mangler scenarioverdi og ble ikke evaluert.")
            return null
        }
    }
}

internal fun escape(value: String): String = value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
    .replace("\"", "&quot;").replace("'", "&#39;")
