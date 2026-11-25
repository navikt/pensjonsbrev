package no.nav.pensjon.brev.brevoppskrift.kotlin

import com.fasterxml.jackson.databind.JsonNode
import no.nav.pensjon.brev.brevoppskrift.api.*
import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import no.nav.pensjon.brev.brevoppskrift.katalog.BuildSourceCatalogue
import no.nav.pensjon.brev.brevoppskrift.katalog.SourceTextSlot
import no.nav.pensjon.brev.brevoppskrift.model.*

class KotlinDraftGenerator(private val catalogue: BuildSourceCatalogue) : DraftGenerator {
    override suspend fun generate(draft: Draft): GenerationResult {
        val issues = DraftValidator.validate(draft) + catalogue.checkSource(draft)
        if (issues.isNotEmpty()) return GenerationResult(emptyList(), issues)
        return if (draft.source != null) imported(draft) else fresh(draft)
    }

    private suspend fun imported(draft: Draft): GenerationResult {
        val entry = requireNotNull(catalogue.trustedEntry(requireNotNull(draft.source)))
        val original = requireNotNull(entry.files[entry.templatePath])
        val replacements = mutableListOf<Pair<SourceTextSlot, String>>()
        for ((binding, slots) in entry.slots.groupBy { Triple(it.blockId, it.inlineIndex, it.language) }) {
            val (blockId, inlineIndex, language) = binding
            val translations = if (blockId == null) draft.title else {
                val block = draft.outline.find { it.id == blockId }
                val inline = when (block) {
                    is Block.Paragraph -> block.text[inlineIndex]
                    is Block.Heading -> block.text[inlineIndex]
                    else -> error("Ugyldig katalogbinding.")
                } as Inline.Text
                inline.text
            }
            val value = translations.getValue(language)
            val ordered = slots.sortedBy { it.start }
            val before = ordered.joinToString("") { it.originalText }
            if (value == before) continue
            val prefix = before.commonPrefixWith(value).length
            val suffix = before.substring(prefix).commonSuffixWith(value.substring(prefix)).length
            val inserted = value.substring(prefix, value.length - suffix)
            var offset = 0
            var pendingInsertion = true
            // Patch literal tokens, retaining concatenation operators, comments and surrounding layout.
            for (slot in ordered) {
                val end = offset + slot.originalText.length
                val insertion = if (pendingInsertion && prefix <= end) {
                    pendingInsertion = false
                    inserted
                } else ""
                val from = (prefix - offset).coerceIn(0, slot.originalText.length)
                val until = (before.length - suffix - offset).coerceIn(0, slot.originalText.length)
                val replacement = slot.originalText.take(from) + insertion + slot.originalText.substring(until)
                if (replacement != slot.originalText) replacements.add(slot to replacement)
                offset = end
            }
        }
        var source = original
        for ((slot, replacement) in replacements.sortedByDescending { it.first.start }) {
            require(slot.start in 0..slot.end && slot.end <= source.length) { "Ugyldig katalogposisjon." }
            source = source.replaceRange(slot.start, slot.end, kotlinString(replacement))
        }
        val files = mutableListOf(GeneratedFile(entry.templatePath, source))
        // Preserve the existing DTO, imports, selectors, metadata, code and phrase identity, byte for byte.
        // Historical snapshots without role metadata keep their previously supported DTO export.
        val dtoPaths = entry.dtoPaths ?: entry.files.keys.filter { it.value.contains("-api-model/") || it.value.contains("/api-model/") }.toSet()
        entry.files.filterKeys { it != entry.templatePath && it in entry.reference.files && it in dtoPaths && it.value.endsWith(".kt") }
            .toSortedMap(compareBy { it.value }).forEach { (path, content) -> files.add(GeneratedFile(path, content)) }
        val profile = moduleProfile(draft.metadata.targetModule)
        files.add(GeneratedFile(RepositoryPath("${profile.templateRoot}/src/test/kotlin/${entry.templatePackage?.replace('.', '/')}/${draft.metadata.name}AuthoringTest.kt"),
            importedTest(draft, requireNotNull(entry.templatePackage))))
        files.add(GeneratedFile(RepositoryPath("${profile.templateRoot}/src/test/resources/brevoppskrift/${draft.metadata.name}-sjekkliste.md"), checklist(draft, imported = true)))
        return GenerationResult(files.sortedBy { it.path.value }, emptyList())
    }

    private fun fresh(draft: Draft, languages: Set<DraftLanguage> = draft.metadata.languages): GenerationResult {
        val unsupported = mutableListOf<ValidationIssue>()
        fun issue(path: String, message: String) { unsupported.add(ValidationIssue(path, "GENERATION_UNSUPPORTED", message)) }
        if (draft.metadata.type != DocumentType.VEDLEGG && draft.metadata.brevkode == null) issue("metadata.brevkode", "Velg en brevkode før Kotlin genereres.")
        if (draft.metadata.type == DocumentType.VEDLEGG && draft.attachments.isNotEmpty()) issue("attachments", "Vedleggs-DSL støtter ikke vedlegg inne i vedlegg.")
        if (draft.attachments.mapNotNull { it.fieldId }.distinct().size != draft.attachments.count { it.fieldId != null }) {
            issue("attachments", "Hver vedleggskopi må ha sin egen objektbinding; samme felt kan ikke binde ulike nominelle DTO-typer.")
        }
        draft.attachments.forEachIndexed { index, copy ->
            val binding = copy.fieldId?.let { findField(draft.fields, it) }
            if (copy.draft.source != null) issue("attachments[$index].draft.source", "Kildeimporterte vedlegg trenger en egen verifisert importprofil.")
            if (binding == null && copy.draft.fields.isNotEmpty()) issue("attachments[$index].fieldId", "Vedlegget har datafelt og krever en objektbinding.")
            if (binding != null && (binding.kind != FieldKind.OBJECT || fieldShape(binding.children) != fieldShape(copy.draft.fields))) {
                issue("attachments[$index].fieldId", "Objektbindingens feltnavn, typer, nullbarhet og enumverdier må samsvare med vedleggets DTO.")
            }
        }
        if (draft.metadata.type == DocumentType.REDIGERBAR) {
            if (draft.metadata.kategori == null || draft.metadata.brevkontekst !in setOf("ALLE", "SAK", "VEDTAK") || draft.metadata.sakstyper.isEmpty()) {
                issue("metadata", "Redigerbare brev krever kategori, brevkontekst og sakstyper.")
            }
            if (draft.metadata.kategori?.matches(Regex("[A-Za-z][A-Za-z0-9_]*")) == false ||
                draft.metadata.sakstyper.any { !it.matches(Regex("[A-Za-z][A-Za-z0-9_]*")) }) issue("metadata", "Kategori og sakstyper må være symbolske koder.")
            if (draft.choices.any { it.prompt.values.distinct().size > 1 }) issue("choices", "DSL-ens saksbehandlervalg har én visningstekst, ikke oversettelser. Ingen språk droppes automatisk.")
        }
        fun fields(values: List<DataField>) {
            values.forEach {
                if (it.typeReference != null) issue("fields.${it.id.value}", "Eksterne typer må genereres gjennom kildeimport.")
                fields(it.children)
            }
        }
        fields(draft.fields)
        fun inspectInlines(content: List<Inline>) {
            content.forEach {
                when (it) {
                    is Inline.Field -> if (it.formatter == Formatter.CURRENCY && findField(draft.fields, it.fieldId)?.kind == FieldKind.DOUBLE) {
                        issue("outline", "Valutaformat for Double har ingen verifisert DSL-formatter. Velg heltallsbeløp eller vanlig tallformat.")
                    }
                    is Inline.Conditional -> { inspectInlines(it.ifTrue); inspectInlines(it.ifFalse) }
                    else -> Unit
                }
            }
        }
        fun inspectBlocks(content: List<Block>) {
            content.forEach {
                when (it) {
                    is Block.Paragraph -> inspectInlines(it.text)
                    is Block.Heading -> inspectInlines(it.text)
                    is Block.ItemList -> it.items.forEach { item -> inspectInlines(item) }
                    is Block.Table -> it.rows.forEach { row -> row.forEach { cell -> inspectInlines(cell) } }
                    is Block.Conditional -> { inspectBlocks(it.ifTrue); inspectBlocks(it.ifFalse) }
                    is Block.Repeat -> inspectBlocks(it.body)
                    is Block.LockedSource -> issue("outline", "Låst innhold krever kildeimport.")
                }
            }
        }
        inspectBlocks(draft.outline)
        if (unsupported.isNotEmpty()) return GenerationResult(emptyList(), unsupported)
        val profile = moduleProfile(draft.metadata.targetModule)
        val pkg = "${profile.templatePackage}.verksted"
        val dtoPkg = "${profile.dtoPackage}.verksted"
        val copies = draft.attachments.map { copy ->
            val copied = copy.draft.copy(metadata = copy.draft.metadata.copy(name = attachmentName(copy, languages)))
            val result = fresh(copied, languages)
            Triple(copy, copied, result)
        }
        val childIssues = copies.flatMap { (copy, _, result) -> result.issues.map { it.copy(path = "attachments.${copy.id.value}.draft.${it.path}") } }
        if (childIssues.isNotEmpty()) return GenerationResult(emptyList(), childIssues)
        val writers = copies.map { (copy, child, _) -> copy to Writer(child, dtoPkg, languages) }
        val writer = Writer(draft, dtoPkg, languages, writers)
        val files = listOf(
            GeneratedFile(RepositoryPath("${profile.templateRoot}/src/main/kotlin/${pkg.replace('.', '/')}/${draft.metadata.name}.kt"), writer.template(pkg, dtoPkg)),
            GeneratedFile(RepositoryPath("${profile.dtoRoot}/src/main/kotlin/${dtoPkg.replace('.', '/')}/${draft.metadata.name}Dto.kt"), writer.dto(dtoPkg)),
            GeneratedFile(RepositoryPath("${profile.templateRoot}/src/test/kotlin/${pkg.replace('.', '/')}/${draft.metadata.name}AuthoringTest.kt"), writer.test(pkg, dtoPkg)),
            GeneratedFile(RepositoryPath("${profile.templateRoot}/src/test/resources/brevoppskrift/${draft.metadata.name}-sjekkliste.md"), checklist(draft, imported = false)),
        ) + copies.flatMap { it.third.files }
        val conflicts = files.groupBy { it.path }.filterValues { group -> group.map { it.content }.distinct().size > 1 }
        if (conflicts.isNotEmpty()) return GenerationResult(emptyList(), listOf(ValidationIssue("attachments", "GENERATION_PATH_CONFLICT", "Vedleggene genererer motstridende filer.")))
        return GenerationResult(files.distinctBy { it.path }.sortedBy { it.path.value }, emptyList())
    }

    private fun importedTest(draft: Draft, pkg: String): String = """
        package $pkg

        import org.junit.jupiter.api.Test
        import org.assertj.core.api.Assertions.assertThat
        import no.nav.brev.brevbaker.FellesFactory
        import no.nav.brev.brevbaker.LetterTestImpl
        import no.nav.brev.brevbaker.renderTestHtml
        import ${fixturePackage(draft.metadata.targetModule)}.Fixtures
        import no.nav.pensjon.brev.template.Language.*

        class ${draft.metadata.name}AuthoringTest {
            @Test
            fun `source identity and all languages are preserved`() {
                assertThat(${draft.metadata.name}.kode.kode()).isEqualTo(${kotlinString(requireNotNull(draft.metadata.brevkode))})
                assertThat(${draft.metadata.name}.template.language.all()).hasSize(${draft.metadata.languages.size})
                val data = Fixtures.create(${draft.metadata.name}::class)
                ${draft.metadata.languages.sortedBy { it.ordinal }.joinToString("\n                ") {
                    "LetterTestImpl(${draft.metadata.name}.template, data, ${languageName(it)}, FellesFactory.${if (draft.metadata.type == DocumentType.REDIGERBAR) "felles" else "fellesAuto"}).renderTestHtml(\"${draft.metadata.name}_authoring_${it.name}\")"
                }}
            }
        }
    """.trimIndent() + "\n"

    private class Writer(
        private val draft: Draft,
        private val dtoPackage: String,
        private val languages: Set<DraftLanguage> = draft.metadata.languages,
        private val attachmentWriters: List<Pair<AttachmentCopy, Writer>> = emptyList(),
    ) {
        private val name = draft.metadata.name
        private val dto = "${name}Dto"
        private val editable = draft.metadata.type == DocumentType.REDIGERBAR
        private val attachment = draft.metadata.type == DocumentType.VEDLEGG
        private val fieldIndex = mutableMapOf<FieldId, DataField>()
        private val parents = mutableMapOf<FieldId, FieldId>()
        private val owners = mutableMapOf<FieldId, String>()
        private val boundTypes = mutableMapOf<FieldId, String>()
        private val boundEnums = mutableMapOf<FieldId, String>()
        private val externalFields = mutableSetOf<FieldId>()
        private fun text(value: Translations, font: FontType = FontType.PLAIN): String =
            no.nav.pensjon.brev.brevoppskrift.kotlin.text(value, font, languages.sortedBy { it.ordinal })

        init {
            fun register(fields: List<DataField>, owner: String, parent: FieldId? = null) {
                fields.forEach { field ->
                    fieldIndex[field.id] = field
                    owners[field.id] = owner
                    if (parent != null) parents[field.id] = parent
                    register(field.children, "$dto.Value${fieldIndex.size}", field.id)
                }
            }
            register(draft.fields, if (editable) "$dto.PesysData" else dto)
            attachmentWriters.forEach { (copy, child) ->
                copy.fieldId?.let { id ->
                    val binding = fieldIndex.getValue(id)
                    boundTypes[id] = "${child.dtoPackage}.${child.dto}"
                    externalFields.add(id)
                    fun bind(parent: List<DataField>, nested: List<DataField>) {
                        parent.forEach { field ->
                            val target = nested.single { it.name == field.name }
                            fun qualify(type: String) = type.replace(child.dto, "${child.dtoPackage}.${child.dto}")
                            owners[field.id] = qualify(child.owners.getValue(target.id))
                            if (field.kind in setOf(FieldKind.OBJECT, FieldKind.LIST)) boundTypes[field.id] = qualify(child.structuredType(target))
                            if (field.kind == FieldKind.ENUM) boundEnums[field.id] = qualify(child.enumType(target))
                            externalFields.add(field.id)
                            bind(field.children, target.children)
                        }
                    }
                    bind(binding.children, child.draft.fields)
                }
            }
        }

        private fun structuredType(field: DataField): String = boundTypes[field.id] ?: owners.getValue(field.children.first().id)
        private fun enumType(field: DataField): String = boundEnums[field.id] ?: "$dto.Enum${fieldIndex.keys.indexOf(field.id)}"
        private fun type(field: DataField): String = when (field.kind) {
            FieldKind.STRING -> "String"
            FieldKind.NUMBER, FieldKind.YEAR, FieldKind.MONTH, FieldKind.PERCENT -> "Int"
            FieldKind.DOUBLE -> "Double"
            FieldKind.BOOLEAN -> "Boolean"
            FieldKind.DATE -> "LocalDate"
            FieldKind.ENUM -> enumType(field)
            FieldKind.OBJECT -> structuredType(field)
            FieldKind.LIST -> "List<${structuredType(field)}>"
            else -> error("Uverifisert felttype.")
        } + if (field.nullable) "?" else ""

        private fun selector(field: DataField): String = "field${fieldIndex.keys.indexOf(field.id)}"
        private fun expression(id: FieldId, scope: Map<FieldId, String>): String {
            scope[id]?.let { return it }
            val parent = parents[id]
            val base = if (parent != null) expression(parent, scope) else if (editable) "argument.select(pesysSelector)" else "argument"
            return "$base.select(${selector(fieldIndex.getValue(id))})"
        }

        fun dto(pkg: String): String = buildString {
            append("package $pkg\n\nimport java.time.LocalDate\nimport no.nav.pensjon.brev.api.model.maler.*\n\n")
            if (editable) {
                append("data class $dto(\n    override val pesysData: PesysData,\n    override val saksbehandlerValg: Valg = Valg(),\n) : RedigerbarBrevdata<$dto.PesysData> {\n")
                append("    class Valg() : LinkedHashMap<String, Any?>(), SaksbehandlervalgIDSL {\n")
                append("        constructor(switches: Map<String, Any?>) : this() { putAll(switches) }\n")
                append("    }\n")
                append(dataClass("PesysData", draft.fields, "FagsystemBrevdata", "    "))
            } else {
                append(classStart(dto, draft.fields, if (attachment) "VedleggData" else "AutobrevData", ""))
            }
            fieldIndex.values.filter { it.id !in externalFields && it.kind in setOf(FieldKind.OBJECT, FieldKind.LIST) }.forEach {
                append(dataClass(structuredType(it).substringAfterLast('.'), it.children, null, "    "))
            }
            fieldIndex.values.filter { it.id !in externalFields && it.kind == FieldKind.ENUM }.forEach {
                append("    enum class ${enumType(it).substringAfterLast('.')} { ${it.enumValues.joinToString(", ")} }\n")
            }
            append("}\n")
        }

        private fun classStart(className: String, fields: List<DataField>, supertype: String?, indent: String): String {
            val inheritance = supertype?.let { " : $it" }.orEmpty()
            if (fields.isEmpty()) return if (attachment && supertype == "VedleggData") "${indent}class $className : LinkedHashMap<String, Any?>(), VedleggData {\n"
                else "${indent}class $className$inheritance {\n"
            return "${indent}data class $className(\n" + fields.joinToString("") { "${indent}    val ${it.name}: ${type(it)},\n" } + "$indent)$inheritance {\n"
        }

        private fun dataClass(className: String, fields: List<DataField>, supertype: String?, indent: String): String =
            classStart(className, fields, supertype, indent) + "$indent}\n"

        fun template(pkg: String, dtoPkg: String): String = buildString {
            append("package $pkg\n\n")
            append("import $dtoPkg.$dto\nimport java.time.LocalDate\n")
            append("import no.nav.pensjon.brev.template.*\nimport no.nav.pensjon.brev.template.Language.*\n")
            append("import no.nav.pensjon.brev.template.dsl.*\nimport no.nav.pensjon.brev.template.dsl.expression.*\n")
            append("import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers\n")
            append("import no.nav.pensjon.brev.api.model.maler.Brevkode\n")
            append("import no.nav.pensjon.brev.api.model.IBrevkategori\nimport no.nav.pensjon.brev.api.model.ISakstype\nimport no.nav.pensjon.brev.api.model.TemplateDescription\n")
            append("import no.nav.pensjon.brevbaker.api.model.LetterMetadata\n\n")
            if (attachment) {
                append("object $name {\n")
                fieldIndex.values.forEach { field ->
                    append("    private val ${selector(field)} = SimpleSelector<${owners.getValue(field.id)}, ${type(field)}>(${kotlinString(owners.getValue(field.id))}, ${kotlinString(field.name)}, ${kotlinString(type(field))}) { ${field.name} }\n")
                }
                append("    val template = createAttachment<${languageType(languages)}, $dto>(\n")
                append("        title = { ${text(draft.title)} },\n")
                append("        outline = {\n${blocks(draft.outline, emptyMap(), "            ")}        },\n    )\n}\n")
                return@buildString
            }
            append("@TemplateModelHelpers\nobject $name : ${if (editable) "RedigerbarTemplate" else "AutobrevTemplate"}<$dto> {\n")
            append("    private enum class Kode : Brevkode.${if (editable) "Redigerbart" else "Automatisk"} { ${draft.metadata.brevkode}; override fun kode() = name }\n")
            append("    override val kode: Brevkode.${if (editable) "Redigerbart" else "Automatisk"} = Kode.${draft.metadata.brevkode}\n")
            if (editable) {
                append("    private enum class Kategori : IBrevkategori { ${draft.metadata.kategori}; override val kode get() = name }\n")
                append("    private enum class Sakstype : ISakstype { ${draft.metadata.sakstyper.sorted().joinToString(", ")}; override val kode get() = name }\n")
                append("    override val kategori: IBrevkategori = Kategori.${draft.metadata.kategori}\n")
                append("    override val brevkontekst = TemplateDescription.Brevkontekst.${draft.metadata.brevkontekst}\n")
                append("    override val sakstyper: Set<ISakstype> = setOf(${draft.metadata.sakstyper.sorted().joinToString(", ") { "Sakstype.$it" }})\n")
                append("    private val pesysSelector = SimpleSelector<$dto, $dto.PesysData>(${kotlinString(dto)}, \"pesysData\", \"PesysData\") { pesysData }\n")
            }
            fieldIndex.values.forEach { field ->
                append("    private val ${selector(field)} = SimpleSelector<${owners.getValue(field.id)}, ${type(field)}>(${kotlinString(owners.getValue(field.id))}, ${kotlinString(field.name)}, ${kotlinString(type(field))}) { ${field.name} }\n")
            }
            append("    override val template = createTemplate(\n")
            append("        languages = languages(${languages.sortedBy { it.ordinal }.joinToString(", ") { languageName(it) }}),\n")
            append("        letterMetadata = LetterMetadata(displayTitle = ${kotlinString(draft.metadata.displayTitle ?: draft.title[DraftLanguage.NB] ?: draft.title.values.first())}, ")
            append("distribusjonstype = LetterMetadata.Distribusjonstype.${draft.metadata.distribusjonstype ?: "VIKTIG"}, brevtype = LetterMetadata.Brevtype.${draft.metadata.brevtype ?: "INFORMASJONSBREV"}),\n")
            append("    ) {\n")
            draft.choices.forEach { append("        val choice${draft.choices.indexOf(it)} = saksbehandlervalg(${kotlinString(it.id.value)}, ${kotlinString(it.prompt.values.first())}).bool()\n") }
            append("        title { ${text(draft.title)} }\n        outline {\n")
            append(blocks(draft.outline, emptyMap(), "            "))
            append("        }\n")
            attachmentWriters.forEach { (copy, child) ->
                val reference = "$pkg.${child.name}.template"
                if (copy.fieldId == null) {
                    append("        includeAttachment($reference, ${child.dtoPackage}.${child.dto}().expr())\n")
                } else {
                    val field = fieldIndex.getValue(copy.fieldId)
                    val nullable = field.nullable || ancestors(field.id).any { fieldIndex.getValue(it).nullable }
                    append("        ${if (nullable) "includeAttachmentIfNotNull" else "includeAttachment"}($reference, ${expression(field.id, emptyMap())})\n")
                }
            }
            append("    }\n}\n")
        }

        private fun blocks(content: List<Block>, scope: Map<FieldId, String>, indent: String): String = content.joinToString("") { block ->
            when (block) {
                is Block.Paragraph -> "${indent}paragraph(uniqueness = ${kotlinString(block.id.value)}) {\n${inlines(block.text, scope, "$indent    ")}$indent}\n"
                is Block.Heading -> "${indent}title${block.level}(uniqueness = ${kotlinString(block.id.value)}) {\n${inlines(block.text, scope, "$indent    ")}$indent}\n"
                is Block.ItemList -> "${indent}paragraph(uniqueness = ${kotlinString(block.id.value)}) {\n$indent    ${if (block.numbered) "numberedList" else "list"} {\n" +
                    block.items.joinToString("") { "$indent        item {\n${inlines(it, scope, "$indent            ")}$indent        }\n" } + "$indent    }\n$indent}\n"
                is Block.Table -> "${indent}paragraph(uniqueness = ${kotlinString(block.id.value)}) {\n$indent    table(header = {\n" +
                    block.header.joinToString("") { "$indent        column { ${text(it)} }\n" } + "$indent    }) {\n" +
                    block.rows.joinToString("") { row -> "$indent        row {\n" + row.joinToString("") { "$indent            cell {\n${inlines(it, scope, "$indent                ")}$indent            }\n" } + "$indent        }\n" } + "$indent    }\n$indent}\n"
                is Block.Conditional -> "${indent}showIf(${predicate(block.predicate, scope)}) {\n${blocks(block.ifTrue, scope, "$indent    ")}$indent}" +
                    if (block.ifFalse.isNotEmpty()) ".orShow {\n${blocks(block.ifFalse, scope, "$indent    ")}$indent}\n" else "\n"
                is Block.Repeat -> {
                    val local = "item${scope.size}"
                    "${indent}forEach(${expression(block.fieldId, scope)}${if (nullable(fieldIndex.getValue(block.fieldId), scope)) ".ifNull(emptyList())" else ""}) { $local ->\n" +
                        blocks(block.body, scope + (block.fieldId to local), "$indent    ") + "$indent}\n"
                }
                is Block.LockedSource -> error("Låst innhold krever importprofil.")
            }
        }

        private fun inlines(content: List<Inline>, scope: Map<FieldId, String>, indent: String): String = content.joinToString("") { node ->
            when (node) {
                is Inline.Text -> "$indent${text(node.text, node.fontType)}\n"
                Inline.NewLine -> "${indent}newline()\n"
                is Inline.Field -> "${indent}text(${languages.sortedBy { it.ordinal }.joinToString(", ") { language ->
                    "${languageFunction(language)} { +${formatted(node, language, scope)} }"
                }})\n"
                is Inline.FreeText -> "${indent}text(${languages.sortedBy { it.ordinal }.joinToString(", ") { language ->
                    "${languageFunction(language)} { +fritekst(${kotlinString(node.prompt.getValue(language))}) }"
                }})\n"
                is Inline.Conditional -> "${indent}showIf(${predicate(node.predicate, scope)}) {\n${inlines(node.ifTrue, scope, "$indent    ")}$indent}.orShow {\n${inlines(node.ifFalse, scope, "$indent    ")}$indent}\n"
            }
        }

        private fun formatted(node: Inline.Field, language: DraftLanguage, scope: Map<FieldId, String>): String {
            val field = fieldIndex.getValue(node.fieldId)
            val expr = expression(node.fieldId, scope)
            val nullable = nullable(field, scope)
            val value = if (nullable && field.kind in setOf(FieldKind.BOOLEAN, FieldKind.ENUM)) "this" else expr
            val formatted = when (node.formatter) {
                Formatter.CURRENCY -> "$expr.format(LocalizedFormatter.CurrencyFormat)"
                Formatter.MONTH_YEAR -> "$expr.format(LocalizedFormatter.MonthYearFormatter)"
                Formatter.DEFAULT -> when (field.kind) {
                    FieldKind.STRING -> expr
                    FieldKind.NUMBER, FieldKind.DOUBLE, FieldKind.DATE -> "$expr.format()"
                    FieldKind.YEAR -> "$expr.select(SimpleSelector<Int, String>(\"Int\", \"year\", \"String\") { toString() })"
                    FieldKind.MONTH -> if (nullable)
                        "$expr.safe { this.select(SimpleSelector<Int, java.time.Month>(\"Int\", \"month\", \"Month\") { java.time.Month.of(this) }).format() }"
                        else "$expr.select(SimpleSelector<Int, java.time.Month>(\"Int\", \"month\", \"Month\") { java.time.Month.of(this) }).format()"
                    FieldKind.PERCENT -> if (nullable)
                        "$expr.safe { this.format() + \" %\" }" else "($expr.format() + \" %\")"
                    FieldKind.BOOLEAN -> "ifElse($value, \"true\", \"false\")"
                    // Both branches must be Expression<String>. Mixing a String with the nested
                    // Expression selects the generic literal overload and widens the result to Any.
                    FieldKind.ENUM -> field.enumValues.foldRight("\"\".expr()") { enum, remainder ->
                        "ifElse<String>($value.equalTo(${enumType(field)}.$enum), ${kotlinString(enum)}.expr(), $remainder)"
                    }
                    else -> error("Uverifisert skalar.")
                }
            }
            // Safe selection makes a scalar nullable if any ancestor is nullable.
            val nullableFormatted = if (nullable && field.kind in setOf(FieldKind.BOOLEAN, FieldKind.ENUM)) "$expr.safe { $formatted }" else formatted
            return if (nullable) "$nullableFormatted.ifNull(${kotlinString(node.fallback?.get(language).orEmpty())})" else formatted
        }

        private fun ancestors(id: FieldId): List<FieldId> = parents[id]?.let { listOf(it) + ancestors(it) }.orEmpty()
        private fun nullable(field: DataField, scope: Map<FieldId, String>): Boolean =
            (listOf(field.id) + ancestors(field.id)).takeWhile { it !in scope }.any { fieldIndex.getValue(it).nullable }

        private fun predicate(value: Predicate, scope: Map<FieldId, String>): String = when (value) {
            is Predicate.IsNotNull -> "${expression(value.fieldId, scope)}.notNull()"
            is Predicate.Choice -> "choice${draft.choices.indexOfFirst { it.id == value.choiceId }}${if (value.value) "" else ".not()"}"
            is Predicate.Not -> "(${predicate(value.predicate, scope)}).not()"
            is Predicate.All -> value.predicates.joinToString(" and ", "(", ")") { "(${predicate(it, scope)})" }
            is Predicate.Any -> value.predicates.joinToString(" or ", "(", ")") { "(${predicate(it, scope)})" }
            is Predicate.Compare -> {
                val field = fieldIndex.getValue(value.fieldId)
                val literal = when (val lit = value.value) {
                    is Literal.Text -> if (field.kind == FieldKind.ENUM) "${enumType(field)}.${lit.value}" else kotlinString(lit.value)
                    is Literal.Number -> if (field.kind == FieldKind.DOUBLE) lit.value.toDouble().toString() else lit.value.intValueExact().toString()
                    is Literal.BooleanValue -> lit.value.toString()
                    is Literal.Date -> "LocalDate.parse(${kotlinString(lit.value)})"
                }
                val operator = when (value.operator) {
                    Comparison.EQUAL -> "equalTo"; Comparison.NOT_EQUAL -> "notEqualTo"
                    Comparison.GREATER_THAN -> "greaterThan"; Comparison.GREATER_THAN_OR_EQUAL -> "greaterThanOrEqual"
                    Comparison.LESS_THAN -> "lessThan"; Comparison.LESS_THAN_OR_EQUAL -> "lessThanOrEqual"
                }
                val expr = expression(value.fieldId, scope)
                if (field.nullable || ancestors(field.id).any { fieldIndex.getValue(it).nullable }) {
                    "$expr.safe { this.$operator($literal) }.ifNull(false)"
                } else {
                    "$expr.$operator($literal)"
                }
            }
        }

        fun test(pkg: String, dtoPkg: String): String = buildString {
            append("package $pkg\n\nimport $dtoPkg.$dto\nimport java.time.LocalDate\n")
            append("import no.nav.brev.brevbaker.*\nimport no.nav.pensjon.brev.template.Language.*\nimport org.junit.jupiter.api.Test\n\n")
            if (attachment) {
                append("import no.nav.pensjon.brev.template.dsl.languages\nimport no.nav.pensjon.brev.template.dsl.expression.expr\nimport no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata\n\n")
            }
            append("class ${name}AuthoringTest {\n    @Test\n    fun `renders synthetic data in every supported language`() {\n")
            val scenarios: List<TestScenario?> = if (draft.scenarios.isEmpty()) listOf(null) else draft.scenarios
            scenarios.forEachIndexed { index, scenario ->
                val data = if (editable) "$dto(${instance("$dto.PesysData", draft.fields, scenario?.values.orEmpty())}, $dto.Valg(switches = mapOf(${draft.choices.joinToString(", ") { "${kotlinString(it.id.value)} to ${scenario?.choices?.get(it.id) ?: false}" }})))"
                    else instance(dto, draft.fields, scenario?.values.orEmpty())
                append("        val data$index = $data\n")
                languages.sortedBy { it.ordinal }.forEach {
                    if (attachment) append("        LetterTestImpl(createVedleggTestTemplate($name.template, data$index.expr(), languages(${languageName(it)})), EmptyAutobrevdata, ${languageName(it)}, FellesFactory.fellesAuto).renderTestHtml(\"${name}_${index}_${it.name}\")\n")
                    else append("        LetterTestImpl($name.template, data$index, ${languageName(it)}, FellesFactory.${if (editable) "felles" else "fellesAuto"}).renderTestHtml(\"${name}_${index}_${it.name}\")\n")
                }
            }
            append("    }\n}\n")
        }

        private fun instance(type: String, fields: List<DataField>, values: Map<FieldId, JsonNode>): String =
            "$type(${fields.joinToString(", ") { "${it.name} = ${fixture(it, values[it.id])}" }})"

        private fun fixture(field: DataField, value: JsonNode?): String {
            if (value?.isNull == true || (value == null && field.nullable)) return "null"
            return when (field.kind) {
                FieldKind.STRING -> kotlinString(value?.textValue() ?: "Syntetisk testverdi")
                FieldKind.NUMBER -> value?.intValue()?.toString() ?: "1"
                FieldKind.YEAR -> value?.intValue()?.toString() ?: "2000"
                FieldKind.MONTH -> value?.intValue()?.toString() ?: "1"
                FieldKind.PERCENT -> value?.intValue()?.toString() ?: "0"
                FieldKind.DOUBLE -> value?.doubleValue()?.toString() ?: "1.0"
                FieldKind.BOOLEAN -> value?.booleanValue()?.toString() ?: "false"
                FieldKind.DATE -> "LocalDate.parse(${kotlinString(value?.textValue() ?: "2000-01-01")})"
                FieldKind.ENUM -> "${enumType(field)}.${value?.textValue() ?: field.enumValues.first()}"
                FieldKind.OBJECT -> objectFixture(field, value)
                FieldKind.LIST -> "listOf(${value?.map { objectFixture(field, it) }?.joinToString(", ").orEmpty()})"
                else -> error("Uverifisert testverdi.")
            }
        }

        private fun objectFixture(field: DataField, value: JsonNode?): String = "${structuredType(field)}(" +
            field.children.joinToString(", ") { "${it.name} = ${fixture(it, value?.get(it.name))}" } + ")"
    }
}

internal data class ModuleProfile(val templateRoot: String, val dtoRoot: String, val templatePackage: String, val dtoPackage: String)

private fun fieldShape(fields: List<DataField>): List<DataField> =
    fields.sortedBy { it.name }.map { it.copy(id = FieldId(""), children = fieldShape(it.children)) }

private fun attachmentName(copy: AttachmentCopy, languages: Set<DraftLanguage>): String {
    val content = copy.id.value + ":" + copy.revision.value + ":" + languages.sortedBy { it.ordinal }.joinToString() + ":" + draftObjectMapper.writeValueAsString(copy.draft.forBaseline())
    val hash = java.security.MessageDigest.getInstance("SHA-256").digest(content.toByteArray(Charsets.UTF_8))
        .joinToString("") { "%02x".format(it) }.take(12)
    return "${copy.draft.metadata.name.take(45)}R${copy.revision.value}$hash"
}

private fun languageType(languages: Set<DraftLanguage>): String {
    val types = languages.sortedBy { it.ordinal }.map { "Language.${languageName(it)}" }
    val kind = when (types.size) { 1 -> "Single"; 2 -> "Double"; 3 -> "Triple"; else -> error("Ugyldig språksett.") }
    return "LanguageSupport.$kind<${types.joinToString(", ")}>"
}

internal fun moduleProfile(module: TargetModule): ModuleProfile = when (module) {
    TargetModule.UFOERE -> ModuleProfile("ufoere/maler", "ufoere/ufoere-api-model", "no.nav.pensjon.brev.ufore.maler", "no.nav.pensjon.brev.ufore.api.model.maler")
    TargetModule.PENSJON -> ModuleProfile("pensjon/maler", "pensjon/pensjon-api-model", "no.nav.pensjon.brev.maler", "no.nav.pensjon.brev.api.model.maler")
    TargetModule.ALDER -> ModuleProfile("alder/maler", "alder/alder-api-model", "no.nav.pensjon.brev.alder.maler", "no.nav.pensjon.brev.alder.model")
    TargetModule.PLANLEGGE_PENSJON -> ModuleProfile("planlegge-pensjon-maler", "planlegge-pensjon-maler", "no.nav.pensjon.brev.planleggepensjon", "no.nav.pensjon.brev.planleggepensjon")
}

internal fun languageName(language: DraftLanguage): String = when (language) { DraftLanguage.NB -> "Bokmal"; DraftLanguage.NN -> "Nynorsk"; DraftLanguage.EN -> "English" }
private fun fixturePackage(module: TargetModule): String = when (module) {
    TargetModule.UFOERE -> "no.nav.pensjon.brev.ufore"
    TargetModule.PENSJON -> "no.nav.pensjon.brev"
    TargetModule.ALDER -> "no.nav.pensjon.brev.alder"
    TargetModule.PLANLEGGE_PENSJON -> "no.nav.pensjon.brev.planleggepensjon"
}
internal fun languageFunction(language: DraftLanguage): String = when (language) { DraftLanguage.NB -> "bokmal"; DraftLanguage.NN -> "nynorsk"; DraftLanguage.EN -> "english" }
internal fun text(translations: Translations, font: FontType = FontType.PLAIN, order: List<DraftLanguage> = translations.keys.sortedBy { it.ordinal }): String =
    "text(${order.joinToString(", ") { "${languageFunction(it)} { +${kotlinString(translations.getValue(it))} }" }}" +
        if (font != FontType.PLAIN) ", fontType = no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.${font.name})" else ")"

private fun findField(fields: List<DataField>, id: FieldId): DataField? =
    fields.firstOrNull { it.id == id } ?: fields.firstNotNullOfOrNull { findField(it.children, id) }

private fun checklist(draft: Draft, imported: Boolean): String = """
    # ${draft.metadata.name} – utviklersjekkliste
    - Målmodul: ${draft.metadata.targetModule}
    - Kildecommit: ${draft.source?.commit?.value ?: "ny mal"}
    - Importprofil: ${if (imported) "tekstendringer med låst Kotlin-struktur; ikke dokumentasjonsimport" else "ny strukturert mal"}
    - [ ] Kontroller brevkode, modulregistrering, kategori og sakstyper. Lokal kodeenum må flyttes til modulens register ved innføring av ny mal.
    - [ ] DTO-endring i publisert api-model: publiser lokalt, oppdater versjon og bygg målmodulen med KSP.
    - [ ] Kjør målmodulens tester og kontroller faktisk rendring på alle språk.
    - [ ] Kontroller syntetiske scenarier, vilkår, nullable felt, fraser og vedlegg.
    - [ ] Sammenlign med grunnlinjen og kontroller upstream før PR.
    - [ ] Rull tilbake ved å reversere PR-en. Ingen automatisk deploy eller merge.
""".trimIndent() + "\n"
