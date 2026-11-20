package no.nav.pensjon.brev.brevoppskrift.model

import com.fasterxml.jackson.databind.JsonNode
import java.time.LocalDate
import java.time.Year

data class ValidationIssue(val path: String, val code: String, val message: String)

object DraftValidator {
    fun validate(draft: Draft): List<ValidationIssue> = Validation(draft).run()

    private class Validation(private val draft: Draft, private val documentDepth: Int = 0) {
        private val issues = mutableListOf<ValidationIssue>()
        private val ids = mutableSetOf<String>()
        private val fields = mutableMapOf<FieldId, ScopedField>()
        private val choices = mutableSetOf<ChoiceId>()
        private val namePattern = Regex("[A-Za-z][A-Za-z0-9_]*")
        private val kotlinKeywords = setOf(
            "as", "break", "class", "continue", "do", "else", "false", "for", "fun", "if", "in", "interface",
            "is", "null", "object", "package", "return", "super", "this", "throw", "true", "try", "typealias",
            "typeof", "val", "var", "when", "while",
        )

        private data class ScopedField(val field: DataField, val ancestors: List<DataField>)
        private data class Scope(val repeated: Set<FieldId> = emptySet(), val nonNull: Set<FieldId> = emptySet())

        fun run(): List<ValidationIssue> {
            if (draft.formatVersion != DRAFT_FORMAT_VERSION) issue("formatVersion", "UNSUPPORTED_VERSION", "Ukjent utkastversjon.")
            name(draft.metadata.name, "metadata.name")
            metadata()
            source()
            if (draft.metadata.languages.isEmpty()) issue("metadata.languages", "MISSING_LANGUAGE", "Velg minst ett språk.")
            translations(draft.title, "title", requireText = true)
            registerFields(draft.fields, emptyList(), "fields", 0)
            draft.choices.forEachIndexed { index, choice ->
                val path = "choices[$index]"
                id(choice.id.value, "$path.id")
                name(choice.name, "$path.name")
                translations(choice.prompt, "$path.prompt", requireText = true)
                choices.add(choice.id)
            }
            if (draft.choices.map { it.name }.distinct().size != draft.choices.size) issue("choices", "DUPLICATE_NAME", "Valgnavn må være unike.")
            if (draft.choices.isNotEmpty() && draft.metadata.type != DocumentType.REDIGERBAR) {
                issue("choices", "INVALID_DOCUMENT_TYPE", "Saksbehandlervalg krever et redigerbart brev.")
            }
            if (draft.outline.isEmpty()) issue("outline", "EMPTY_OUTLINE", "Brevet må ha innhold.")
            blocks(draft.outline, Scope(), "outline", 0)
            scenarios()
            review()
            draft.attachments.forEachIndexed { index, attachment ->
                val path = "attachments[$index]"
                id(attachment.id.value, "$path.id")
                if (attachment.revision.value < 1) issue("$path.revision", "INVALID_REVISION", "Vedleggskopien må ha en positiv revisjon.")
                if (attachment.draft.metadata.type != DocumentType.VEDLEGG) issue("$path.draft", "INVALID_ATTACHMENT", "Kopien må være et vedlegg.")
                if (attachment.draft.metadata.targetModule != draft.metadata.targetModule) issue("$path.draft", "MODULE_MISMATCH", "Vedlegg må tilhøre samme målmodul.")
                if (!attachment.draft.metadata.languages.containsAll(draft.metadata.languages)) issue("$path.draft", "LANGUAGE_MISMATCH", "Vedlegget må støtte brevets språk.")
                attachment.fieldId?.let {
                    val binding = field(it, Scope(), "$path.fieldId")
                    fun shape(children: List<DataField>): List<DataField> =
                        children.sortedBy { child -> child.name }.map { child -> child.copy(id = FieldId(""), children = shape(child.children)) }
                    if (binding != null && (binding.kind != FieldKind.OBJECT || shape(binding.children) != shape(attachment.draft.fields))) {
                        issue("$path.fieldId", "INVALID_ATTACHMENT_BINDING", "Objektbindingen må ha samme feltnavn, typer, nullbarhet og enumverdier som vedleggets datafelt.")
                    }
                }
                nested(attachment.draft, "$path.draft")
            }
            draft.baseline?.let {
                if (it.draft.baseline != null || it.draft.comments.isNotEmpty()) issue("baseline", "NESTED_BASELINE", "Grunnlinjen må være uten kommentarer og nøstet grunnlinje.")
                if (it.kind !in setOf("IMPORTED", "APPROVED")) issue("baseline.kind", "INVALID_BASELINE", "Ukjent grunnlinjetype.")
                nested(it.draft, "baseline.draft")
            }
            return issues
        }

        private fun nested(value: Draft, path: String) {
            if (documentDepth >= 5) issue(path, "MAX_DEPTH", "Dokumentkopier er for dypt nøstet.")
            else issues.addAll(Validation(value, documentDepth + 1).run().map { it.copy(path = "$path.${it.path}") })
        }

        private fun metadata() {
            val metadata = draft.metadata
            metadata.brevkode?.let { name(it, "metadata.brevkode") }
            if (draft.source == null) {
                metadata.kategori?.let { name(it, "metadata.kategori") }
                metadata.sakstyper.forEach { name(it, "metadata.sakstyper") }
            }
            if (metadata.distribusjonstype != null && metadata.distribusjonstype !in setOf("VEDTAK", "VIKTIG", "ANNET")) {
                issue("metadata.distribusjonstype", "INVALID_METADATA", "Ukjent distribusjonstype.")
            }
            if (metadata.brevtype != null && metadata.brevtype !in setOf("VEDTAKSBREV", "INFORMASJONSBREV")) {
                issue("metadata.brevtype", "INVALID_METADATA", "Ukjent brevtype.")
            }
            if (draft.source == null && metadata.featureToggle != null) issue("metadata.featureToggle", "UNTRUSTED_BINDING", "Feature toggle må komme fra kildekatalogen.")
            if (metadata.type != DocumentType.REDIGERBAR && (metadata.kategori != null || metadata.brevkontekst != null || metadata.sakstyper.isNotEmpty())) {
                issue("metadata", "INVALID_DOCUMENT_TYPE", "Kategori, brevkontekst og sakstyper gjelder redigerbare brev.")
            }
        }

        private fun source() {
            draft.source?.let { reference ->
                if (reference.id.value.isBlank()) issue("source.id", "INVALID_SOURCE", "Kildeidentitet mangler.")
                if (!Regex("[a-f0-9]{40,64}").matches(reference.commit.value)) issue("source.commit", "INVALID_SOURCE", "Kilden må ha en full commit-hash.")
                if (reference.files.isEmpty()) issue("source.files", "INVALID_SOURCE", "Kildefilhash-er mangler.")
                reference.files.forEach { (path, hash) ->
                    if (path.value.startsWith("/") || '\\' in path.value || path.value.split('/').any { it == ".." || it.isEmpty() } || !Regex("[a-f0-9]{64}").matches(hash.value)) {
                        issue("source.files", "INVALID_SOURCE", "Kildefil må være en relativ repository-sti med SHA-256.")
                    }
                }
            }
        }

        private fun scenarios() {
            draft.scenarios.forEachIndexed { index, scenario ->
                val path = "scenarios[$index]"
                id(scenario.id.value, "$path.id")
                if (scenario.name.isBlank()) issue("$path.name", "MISSING_NAME", "Scenarioet må ha et navn.")
                scenario.choices.keys.forEach { if (it !in choices) issue("$path.choices", "UNKNOWN_CHOICE", "Scenarioet viser til et ukjent valg.") }
                scenario.values.forEach { (key, value) ->
                    val field = draft.fields.find { it.id == key }
                    if (field == null) issue("$path.values.${key.value}", "UNKNOWN_FIELD", "Scenarioverdier må bindes til ID-en til et rotfelt.")
                    else scenarioValue(field, value, "$path.values.${key.value}", 0)
                }
                draft.fields.filter { !it.nullable && it.id !in scenario.values }.forEach {
                    issue("$path.values.${it.id.value}", "MISSING_VALUE", "Scenarioet mangler et obligatorisk felt.")
                }
            }
        }

        private fun scenarioValue(field: DataField, value: JsonNode, path: String, depth: Int) {
            if (depth > 20) {
                issue(path, "MAX_DEPTH", "Scenarioverdien er for dypt nøstet.")
                return
            }
            if (value.isNull) {
                if (!field.nullable) issue(path, "NULL_VALUE", "Feltet tillater ikke null.")
                return
            }
            val valid = when (field.kind) {
                FieldKind.STRING -> value.isTextual
                FieldKind.NUMBER -> value.isIntegralNumber && value.canConvertToInt()
                FieldKind.DOUBLE -> value.isNumber && value.doubleValue().isFinite()
                FieldKind.BOOLEAN -> value.isBoolean
                FieldKind.DATE -> value.isTextual && runCatching { LocalDate.parse(value.textValue()) }.isSuccess
                FieldKind.YEAR -> value.isIntegralNumber && value.canConvertToInt() && runCatching { Year.of(value.intValue()) }.isSuccess
                FieldKind.MONTH -> value.isIntegralNumber && value.intValue() in 1..12
                FieldKind.PERCENT -> value.isIntegralNumber && value.intValue() in 0..100
                FieldKind.ENUM -> value.isTextual && value.textValue() in field.enumValues
                FieldKind.OBJECT -> value.isObject
                FieldKind.LIST -> value.isArray
            }
            if (!valid) {
                issue(path, "TYPE_MISMATCH", "Scenarioverdien samsvarer ikke med felttypen.")
                return
            }
            if (field.kind == FieldKind.OBJECT) {
                val names = field.children.map { it.name }.toSet()
                value.fieldNames().forEachRemaining { if (it !in names) issue("$path.$it", "UNKNOWN_FIELD", "Objektet har et ukjent felt.") }
                field.children.forEach { child ->
                    val childValue = value.get(child.name)
                    if (childValue == null && !child.nullable) issue("$path.${child.name}", "MISSING_VALUE", "Objektet mangler et obligatorisk felt.")
                    else if (childValue != null) scenarioValue(child, childValue, "$path.${child.name}", depth + 1)
                }
            } else if (field.kind == FieldKind.LIST) {
                value.forEachIndexed { index, child -> scenarioValue(field.copy(kind = FieldKind.OBJECT, nullable = false), child, "$path[$index]", depth + 1) }
            }
        }

        private fun review() {
            draft.comments.forEachIndexed { index, comment ->
                val path = "comments[$index]"
                id(comment.id.value, "$path.id")
                if (comment.text.isBlank()) issue("$path.text", "MISSING_TEXT", "Kommentaren må ha tekst.")
                if (comment.anchor.type.uppercase() !in setOf("DRAFT", "DOCUMENT", "BLOCK", "TEXT", "FIELD", "CHOICE")) {
                    issue("$path.anchor.type", "INVALID_ANCHOR", "Ukjent kommentaranker.")
                }
                val anchor = comment.anchor
                if ((anchor.type.uppercase() in setOf("BLOCK", "TEXT") && anchor.blockId?.value.isNullOrBlank()) ||
                    (anchor.type.uppercase() == "FIELD" && anchor.fieldId?.value.isNullOrBlank()) ||
                    (anchor.type.uppercase() == "CHOICE" && anchor.choiceId?.value.isNullOrBlank())) {
                    issue("$path.anchor", "INVALID_ANCHOR", "Kommentarankeret mangler referanse-ID.")
                }
                // Anchors are historical references: deleting a block must not destroy its comments.
                comment.anchor.language?.let { if (it !in draft.metadata.languages) issue("$path.anchor.language", "LANGUAGE_MISMATCH", "Kommentarspråket finnes ikke i brevet.") }
                comment.scenarioId?.let { if (it !in draft.scenarios.map { scenario -> scenario.id }) issue("$path.scenarioId", "UNKNOWN_SCENARIO", "Kommentarens scenario finnes ikke.") }
                comment.replies.forEachIndexed { replyIndex, reply ->
                    id(reply.id.value, "$path.replies[$replyIndex].id")
                    if (reply.text.isBlank()) issue("$path.replies[$replyIndex].text", "MISSING_TEXT", "Svaret må ha tekst.")
                }
                comment.states.forEachIndexed { stateIndex, state -> id(state.id.value, "$path.states[$stateIndex].id") }
            }
        }

        private fun registerFields(children: List<DataField>, ancestors: List<DataField>, path: String, depth: Int) {
            if (depth > 20) {
                issue(path, "MAX_DEPTH", "Datamodellen er for dypt nøstet.")
                return
            }
            if (children.map { it.name }.distinct().size != children.size) issue(path, "DUPLICATE_NAME", "Feltnavn må være unike blant søsken.")
            children.forEachIndexed { index, field ->
                val fieldPath = "$path[$index]"
                id(field.id.value, "$fieldPath.id")
                name(field.name, "$fieldPath.name")
                if (field.typeReference != null && draft.source == null) issue("$fieldPath.typeReference", "UNTRUSTED_BINDING", "Eksterne typer må komme fra kildekatalogen.")
                if (ancestors.isEmpty() && field.name in setOf("felles", "argument", "pesysData", "saksbehandlervalg")) {
                    issue("$fieldPath.name", "RESERVED_NAME", "Feltnavnet er reservert av DSL-en.")
                }
                fields.putIfAbsent(field.id, ScopedField(field, ancestors))
                val structured = field.kind in setOf(FieldKind.OBJECT, FieldKind.LIST)
                if (structured && field.children.isEmpty()) issue("$fieldPath.children", "MISSING_FIELDS", "Objekter og objektlister må ha felt.")
                if (!structured && field.children.isNotEmpty()) issue("$fieldPath.children", "INVALID_FIELDS", "Denne typen kan ikke ha delfelt.")
                if (field.kind == FieldKind.ENUM) {
                    if (field.enumValues.isEmpty()) issue("$fieldPath.enumValues", "MISSING_ENUM_VALUES", "Enum må ha verdier.")
                    if (field.enumValues.distinct().size != field.enumValues.size) issue("$fieldPath.enumValues", "DUPLICATE_NAME", "Enumverdier må være unike.")
                    field.enumValues.forEachIndexed { valueIndex, value -> name(value, "$fieldPath.enumValues[$valueIndex]") }
                } else if (field.enumValues.isNotEmpty()) {
                    issue("$fieldPath.enumValues", "INVALID_ENUM_VALUES", "Bare enumfelt kan ha enumverdier.")
                }
                registerFields(field.children, ancestors + field, "$fieldPath.children", depth + 1)
            }
        }

        private fun blocks(blocks: List<Block>, scope: Scope, path: String, depth: Int) {
            if (depth > 20) {
                issue(path, "MAX_DEPTH", "Innholdet er for dypt nøstet.")
                return
            }
            blocks.forEachIndexed { index, block ->
                val blockPath = "$path[$index]"
                id(block.id.value, "$blockPath.id")
                when (block) {
                    is Block.Heading -> {
                        if (block.level !in 1..3) issue("$blockPath.level", "INVALID_HEADING", "Overskriftsnivå må være 1, 2 eller 3.")
                        inline(block.text, scope, "$blockPath.text", depth + 1, plain = true)
                    }
                    is Block.Paragraph -> inline(block.text, scope, "$blockPath.text", depth + 1)
                    is Block.ItemList -> {
                        if (block.items.isEmpty()) issue("$blockPath.items", "EMPTY_LIST", "Listen må ha punkter.")
                        block.items.forEachIndexed { itemIndex, item -> inline(item, scope, "$blockPath.items[$itemIndex]", depth + 1) }
                    }
                    is Block.Table -> {
                        if (block.header.isEmpty()) issue("$blockPath.header", "EMPTY_TABLE", "Tabellen må ha kolonner.")
                        block.header.forEachIndexed { column, text -> translations(text, "$blockPath.header[$column]", requireText = true) }
                        block.rows.forEachIndexed { rowIndex, row ->
                            if (row.size != block.header.size) issue("$blockPath.rows[$rowIndex]", "INVALID_ROW", "Raden må ha én celle per kolonne.")
                            row.forEachIndexed { column, cell -> inline(cell, scope, "$blockPath.rows[$rowIndex][$column]", depth + 1) }
                        }
                    }
                    is Block.Conditional -> {
                        predicate(block.predicate, scope, "$blockPath.predicate", depth + 1)
                        blocks(block.ifTrue, scope.copy(nonNull = scope.nonNull + nonNullFields(block.predicate)), "$blockPath.ifTrue", depth + 1)
                        blocks(block.ifFalse, scope, "$blockPath.ifFalse", depth + 1)
                    }
                    is Block.Repeat -> {
                        val field = field(block.fieldId, scope, "$blockPath.fieldId")
                        if (field != null && field.kind != FieldKind.LIST) issue("$blockPath.fieldId", "NOT_A_LIST", "Gjentakelse krever et listefelt.")
                        if (field?.nullable == true && field.id !in scope.nonNull) issue("$blockPath.fieldId", "NULLABLE_LIST", "Listefeltet må være ikke-null i dette scopet.")
                        if (block.body.isEmpty()) issue("$blockPath.body", "EMPTY_REPEAT", "Gjentakelsen må ha innhold.")
                        blocks(block.body, scope.copy(repeated = scope.repeated + block.fieldId), "$blockPath.body", depth + 1)
                    }
                    is Block.LockedSource -> {
                        if (draft.source == null || block.bindingId.isBlank()) issue(blockPath, "UNTRUSTED_BINDING", "Låst innhold må bindes til kildekatalogen.")
                    }
                }
            }
        }

        private fun inline(content: List<Inline>, scope: Scope, path: String, depth: Int, plain: Boolean = false) {
            if (depth > 20) {
                issue(path, "MAX_DEPTH", "Teksten er for dypt nøstet.")
                return
            }
            if (content.isEmpty()) issue(path, "EMPTY_TEXT", "Tekstinnhold kan ikke være tomt.")
            content.forEachIndexed { index, value ->
                val inlinePath = "$path[$index]"
                when (value) {
                    is Inline.Text -> {
                        translations(value.text, "$inlinePath.text")
                        if (plain && value.fontType != FontType.PLAIN) issue(inlinePath, "PLAIN_TEXT_ONLY", "Overskrifter må ha ren tekst.")
                    }
                    is Inline.Field -> {
                        val field = field(value.fieldId, scope, "$inlinePath.fieldId")
                        if (field?.kind in setOf(FieldKind.OBJECT, FieldKind.LIST)) issue(inlinePath, "NOT_SCALAR", "Tekst krever en skalarverdi.")
                        if (value.formatter == Formatter.CURRENCY && field?.kind !in setOf(FieldKind.NUMBER, FieldKind.DOUBLE)) {
                            issue("$inlinePath.formatter", "INVALID_FORMATTER", "Beløpsformat krever et tallfelt.")
                        }
                        if (value.formatter == Formatter.MONTH_YEAR && field?.kind != FieldKind.DATE) issue("$inlinePath.formatter", "INVALID_FORMATTER", "Måned/år-format krever en dato.")
                        value.fallback?.let { translations(it, "$inlinePath.fallback") }
                        if (field?.nullable == true && field.id !in scope.nonNull && value.fallback == null) {
                            issue(inlinePath, "MISSING_FALLBACK", "Nullable felt må ha tekst for manglende verdi.")
                        }
                    }
                    Inline.NewLine -> if (plain) issue(inlinePath, "PLAIN_TEXT_ONLY", "Overskrifter kan ikke ha linjeskift.")
                    is Inline.Conditional -> {
                        predicate(value.predicate, scope, "$inlinePath.predicate", depth + 1)
                        inline(value.ifTrue, scope.copy(nonNull = scope.nonNull + nonNullFields(value.predicate)), "$inlinePath.ifTrue", depth + 1, plain)
                        inline(value.ifFalse, scope, "$inlinePath.ifFalse", depth + 1, plain)
                    }
                    is Inline.FreeText -> {
                        translations(value.prompt, "$inlinePath.prompt", requireText = true)
                        if (plain || draft.metadata.type != DocumentType.REDIGERBAR) issue(inlinePath, "INVALID_FREE_TEXT", "Fritekst krever vanlig tekst i et redigerbart brev.")
                    }
                }
            }
        }

        private fun predicate(predicate: Predicate, scope: Scope, path: String, depth: Int) {
            if (depth > 20) {
                issue(path, "MAX_DEPTH", "Vilkåret er for dypt nøstet.")
                return
            }
            when (predicate) {
                is Predicate.IsNotNull -> field(predicate.fieldId, scope, "$path.fieldId")
                is Predicate.Choice -> if (predicate.choiceId !in choices) issue("$path.choiceId", "UNKNOWN_CHOICE", "Saksbehandlervalget finnes ikke.")
                is Predicate.Compare -> {
                    val field = field(predicate.fieldId, scope, "$path.fieldId")
                    if (field?.nullable == true && field.id !in scope.nonNull) issue(path, "NULLABLE_COMPARISON", "Sammenligning krever et ikke-null felt.")
                    if (field != null) {
                        val compatible = when (field.kind) {
                            FieldKind.STRING, FieldKind.ENUM -> predicate.value is Literal.Text
                            FieldKind.NUMBER, FieldKind.DOUBLE, FieldKind.YEAR, FieldKind.MONTH, FieldKind.PERCENT -> predicate.value is Literal.Number
                            FieldKind.BOOLEAN -> predicate.value is Literal.BooleanValue
                            FieldKind.DATE -> predicate.value is Literal.Date
                            FieldKind.OBJECT, FieldKind.LIST -> false
                        }
                        if (!compatible) issue("$path.value", "TYPE_MISMATCH", "Verdien må ha samme type som feltet.")
                        if (field.kind == FieldKind.ENUM && predicate.value is Literal.Text && predicate.value.value !in field.enumValues) {
                            issue("$path.value", "INVALID_ENUM_VALUE", "Verdien finnes ikke i enumfeltet.")
                        }
                        if (field.kind in setOf(FieldKind.STRING, FieldKind.ENUM, FieldKind.BOOLEAN) && predicate.operator !in setOf(Comparison.EQUAL, Comparison.NOT_EQUAL)) {
                            issue("$path.operator", "INVALID_COMPARISON", "Denne typen støtter bare lik/ulik.")
                        }
                        if (predicate.value is Literal.Date && runCatching { LocalDate.parse(predicate.value.value) }.isFailure) {
                            issue("$path.value", "INVALID_DATE", "Dato må ha format ÅÅÅÅ-MM-DD.")
                        }
                        if (predicate.value is Literal.Number) {
                            if (field.kind in setOf(FieldKind.NUMBER, FieldKind.YEAR, FieldKind.MONTH, FieldKind.PERCENT) &&
                                runCatching { predicate.value.value.intValueExact() }.isFailure) {
                                issue("$path.value", "TYPE_MISMATCH", "Heltallsfelt krever et heltall innenfor Int-området.")
                            }
                            if (field.kind == FieldKind.DOUBLE && !predicate.value.value.toDouble().isFinite()) issue("$path.value", "TYPE_MISMATCH", "Double må være endelig.")
                        }
                    }
                }
                is Predicate.All -> {
                    if (predicate.predicates.isEmpty()) issue(path, "EMPTY_PREDICATE", "Og-vilkår må ha ledd.")
                    var current = scope
                    predicate.predicates.forEachIndexed { index, child ->
                        predicate(child, current, "$path.predicates[$index]", depth + 1)
                        current = current.copy(nonNull = current.nonNull + nonNullFields(child))
                    }
                }
                is Predicate.Any -> {
                    if (predicate.predicates.isEmpty()) issue(path, "EMPTY_PREDICATE", "Eller-vilkår må ha ledd.")
                    predicate.predicates.forEachIndexed { index, child -> predicate(child, scope, "$path.predicates[$index]", depth + 1) }
                }
                is Predicate.Not -> predicate(predicate.predicate, scope, "$path.predicate", depth + 1)
            }
        }

        private fun nonNullFields(predicate: Predicate): Set<FieldId> = when (predicate) {
            is Predicate.IsNotNull -> setOf(predicate.fieldId)
            is Predicate.All -> predicate.predicates.flatMap { nonNullFields(it) }.toSet()
            is Predicate.Any -> predicate.predicates.map { nonNullFields(it) }.reduceOrNull { left, right -> left intersect right } ?: emptySet()
            else -> emptySet()
        }

        private fun field(id: FieldId, scope: Scope, path: String): DataField? {
            val scoped = fields[id]
            if (scoped == null) {
                issue(path, "UNKNOWN_FIELD", "Feltet finnes ikke.")
                return null
            }
            if (scoped.ancestors.any { it.kind == FieldKind.LIST && it.id !in scope.repeated }) issue(path, "UNBOUND_LIST", "Listefelt må leses inne i en gjentakelse.")
            if (scoped.ancestors.any { it.nullable && it.id !in scope.nonNull }) issue(path, "NULLABLE_PARENT", "Nullable forelder må være ikke-null i dette scopet.")
            return scoped.field
        }

        private fun translations(text: Translations, path: String, requireText: Boolean = false) {
            if (text.keys != draft.metadata.languages) issue(path, "LANGUAGE_MISMATCH", "Tekst må finnes på alle valgte språk, og bare disse.")
            if (requireText && text.values.any { it.isBlank() }) issue(path, "MISSING_TEXT", "Tekst kan ikke være tom.")
        }

        private fun id(id: String, path: String) {
            if (id.isBlank() || id.length > 100) issue(path, "INVALID_ID", "ID må være mellom 1 og 100 tegn.")
            if (!ids.add(id)) issue(path, "DUPLICATE_ID", "ID-er må være unike i hele utkastet.")
            if (ids.size == 10_001) issue(path, "MAX_NODES", "Utkastet har for mange noder.")
        }

        private fun name(name: String, path: String) {
            if (!namePattern.matches(name) || name in kotlinKeywords || name.length > 100) issue(path, "INVALID_NAME", "Navnet må være et gyldig Kotlin-navn.")
        }

        private fun issue(path: String, code: String, message: String) {
            issues.add(ValidationIssue(path, code, message))
        }
    }
}
