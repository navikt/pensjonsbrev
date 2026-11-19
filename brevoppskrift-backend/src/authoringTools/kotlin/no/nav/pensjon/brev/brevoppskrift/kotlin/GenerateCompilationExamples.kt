package no.nav.pensjon.brev.brevoppskrift.kotlin

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import no.nav.pensjon.brev.brevoppskrift.katalog.BuildSourceCatalogue
import no.nav.pensjon.brev.brevoppskrift.katalog.CatalogueSnapshot
import no.nav.pensjon.brev.brevoppskrift.model.*
import java.io.ByteArrayInputStream
import java.io.File
import java.math.BigDecimal

/** Files go to backend build output, never into another agent's target module sources. */
fun main(args: Array<String>): Unit = runBlocking {
    require(args.size == 1)
    val root = File(args[0])
    val catalogue = BuildSourceCatalogue.fromResource(ByteArrayInputStream(draftObjectMapper.writeValueAsBytes(CatalogueSnapshot(GitCommit("a".repeat(40)), emptyList()))))
    val generator = KotlinDraftGenerator(catalogue)
    val jsonFixtures = mutableListOf<GeneratedDtoJsonFixture>()
    TargetModule.entries.forEach { module ->
        val translations = mapOf(DraftLanguage.NB to "Syntetisk tekst", DraftLanguage.NN to "Syntetisk tekst", DraftLanguage.EN to "Synthetic wording")
        val fields = listOf(
            DataField(FieldId("value"), "value", FieldKind.NUMBER),
            DataField(FieldId("rows"), "rows", FieldKind.LIST, nullable = true, children = listOf(
                DataField(FieldId("rowText"), "rowText", FieldKind.STRING),
                DataField(FieldId("rowValue"), "rowValue", FieldKind.NUMBER),
                DataField(FieldId("tags"), "tags", FieldKind.LIST, children = listOf(DataField(FieldId("tagText"), "tagText", FieldKind.STRING))),
            )),
            DataField(FieldId("date"), "date", FieldKind.DATE, nullable = true),
            DataField(FieldId("label"), "label", FieldKind.STRING),
            DataField(FieldId("ratio"), "ratio", FieldKind.DOUBLE),
            DataField(FieldId("enabled"), "enabled", FieldKind.BOOLEAN),
            DataField(FieldId("mode"), "mode", FieldKind.ENUM, enumValues = listOf("FIRST", "SECOND")),
            DataField(FieldId("year"), "year", FieldKind.YEAR),
            DataField(FieldId("month"), "month", FieldKind.MONTH),
            DataField(FieldId("percent"), "percent", FieldKind.PERCENT),
            DataField(FieldId("details"), "details", FieldKind.OBJECT, nullable = true, children = listOf(
                DataField(FieldId("detailText"), "detailText", FieldKind.STRING),
                DataField(FieldId("detailAmount"), "detailAmount", FieldKind.NUMBER),
                DataField(FieldId("detailEnabled"), "detailEnabled", FieldKind.BOOLEAN),
                DataField(FieldId("nested"), "nested", FieldKind.OBJECT, children = listOf(DataField(FieldId("nestedText"), "nestedText", FieldKind.STRING))),
            )),
        )
        val outline = listOf(
            Block.Heading(BlockId("heading"), 1, listOf(Inline.Text(translations))),
            Block.Heading(BlockId("heading-two"), 2, listOf(Inline.Text(translations))),
            Block.Heading(BlockId("heading-three"), 3, listOf(Inline.Text(translations))),
            Block.Paragraph(BlockId("paragraph"), listOf(Inline.Text(translations, FontType.BOLD), Inline.Field(FieldId("value"), Formatter.CURRENCY), Inline.NewLine,
                Inline.Field(FieldId("date"), Formatter.MONTH_YEAR, translations))),
            Block.Paragraph(BlockId("scalars"), listOf(Inline.Text(translations, FontType.ITALIC), Inline.Field(FieldId("label")), Inline.Field(FieldId("value")),
                Inline.Field(FieldId("ratio")), Inline.Field(FieldId("enabled")), Inline.Field(FieldId("mode")), Inline.Field(FieldId("date"), fallback = translations),
                Inline.Field(FieldId("year")), Inline.Field(FieldId("month")), Inline.Field(FieldId("percent")))),
            Block.Conditional(BlockId("nullable-object-guard"), Predicate.IsNotNull(FieldId("details")), listOf(
                Block.Paragraph(BlockId("nullable-object"), listOf(Inline.Field(FieldId("detailText"), fallback = translations),
                    Inline.Field(FieldId("detailAmount"), Formatter.CURRENCY, translations), Inline.Field(FieldId("detailEnabled")),
                    Inline.Field(FieldId("nestedText"), fallback = translations))),
            )),
            Block.ItemList(BlockId("list"), false, listOf(listOf(Inline.Text(translations)))),
            Block.ItemList(BlockId("numbered-list"), true, listOf(listOf(Inline.Field(FieldId("label"))), listOf(Inline.Text(translations)))),
            Block.Table(BlockId("table"), listOf(translations, translations), listOf(
                listOf(listOf(Inline.Text(translations)), listOf(Inline.Field(FieldId("value")))),
                listOf(listOf(Inline.Field(FieldId("label"))), listOf(Inline.Field(FieldId("ratio")))),
            )),
            Block.Conditional(BlockId("condition"), Predicate.Compare(FieldId("value"), Comparison.GREATER_THAN, Literal.Number(BigDecimal.ZERO)),
                listOf(Block.Paragraph(BlockId("true"), listOf(Inline.Text(translations)))), listOf(Block.Paragraph(BlockId("false"), listOf(Inline.Text(translations))))),
            Block.Conditional(BlockId("rows-guard"), Predicate.IsNotNull(FieldId("rows")), listOf(
                Block.Repeat(BlockId("repeat"), FieldId("rows"), listOf(
                    Block.Paragraph(BlockId("row"), listOf(Inline.Field(FieldId("rowText")), Inline.Field(FieldId("rowValue")))),
                    Block.Repeat(BlockId("nested-repeat"), FieldId("tags"), listOf(Block.Paragraph(BlockId("tag"), listOf(Inline.Field(FieldId("tagText")))))),
                )),
            )),
            Block.Conditional(BlockId("guarded-date"), Predicate.All(listOf(Predicate.IsNotNull(FieldId("date")),
                Predicate.Compare(FieldId("date"), Comparison.GREATER_THAN, Literal.Date("1999-01-01")))),
                listOf(Block.Paragraph(BlockId("date-text"), listOf(Inline.Field(FieldId("date")))))),
            Block.Conditional(BlockId("guarded-object"), Predicate.All(listOf(Predicate.IsNotNull(FieldId("details")),
                Predicate.Compare(FieldId("detailAmount"), Comparison.GREATER_THAN_OR_EQUAL, Literal.Number(BigDecimal.ZERO)))),
                listOf(Block.Paragraph(BlockId("detail-number"), listOf(Inline.Field(FieldId("detailAmount")))))),
            Block.Paragraph(BlockId("inline-condition"), listOf(Inline.Conditional(
                Predicate.Any(listOf(Predicate.Not(Predicate.Compare(FieldId("enabled"), Comparison.EQUAL, Literal.BooleanValue(false))),
                    Predicate.Compare(FieldId("label"), Comparison.NOT_EQUAL, Literal.Text("Absent")),
                    Predicate.Compare(FieldId("mode"), Comparison.EQUAL, Literal.Text("SECOND")))),
                listOf(Inline.Text(translations)), listOf(Inline.Text(translations, FontType.BOLD)),
            ))),
        ) + Comparison.entries.map { comparison ->
            Block.Conditional(BlockId("compare-${comparison.name.lowercase()}"), Predicate.Compare(FieldId("value"), comparison, Literal.Number(BigDecimal.TEN)),
                listOf(Block.Paragraph(BlockId("compare-text-${comparison.name.lowercase()}"), listOf(Inline.Text(translations)))))
        }
        val scenario = TestScenario(ScenarioId("synthetic"), "Syntetisk", mapOf(
            FieldId("value") to draftObjectMapper.readTree("100"),
            FieldId("rows") to draftObjectMapper.readTree("""[{"rowText":"synthetic","rowValue":20,"tags":[{"tagText":"tag"}]}]"""),
            FieldId("date") to draftObjectMapper.readTree("\"2000-01-01\""),
            FieldId("label") to draftObjectMapper.readTree("\"Synthetic\""), FieldId("ratio") to draftObjectMapper.readTree("20.5"),
            FieldId("enabled") to draftObjectMapper.readTree("true"), FieldId("mode") to draftObjectMapper.readTree("\"SECOND\""),
            FieldId("year") to draftObjectMapper.readTree("2000"), FieldId("month") to draftObjectMapper.readTree("11"), FieldId("percent") to draftObjectMapper.readTree("50"),
            FieldId("details") to draftObjectMapper.readTree("""{"detailText":"Synthetic","detailAmount":100,"detailEnabled":true,"nested":{"nestedText":"Nested"}}"""),
        ))
        val attachmentFields = listOf(
            DataField(FieldId("note"), "note", FieldKind.STRING),
            DataField(FieldId("amount"), "amount", FieldKind.NUMBER),
            DataField(FieldId("category"), "category", FieldKind.ENUM, enumValues = listOf("FIRST", "SECOND")),
            DataField(FieldId("lines"), "lines", FieldKind.LIST, children = listOf(DataField(FieldId("line"), "line", FieldKind.STRING))),
        )
        val attachmentData = draftObjectMapper.readTree("""{"note":"Bound attachment","amount":100,"category":"SECOND","lines":[{"line":"Attachment row"}]}""")
        val attachmentDraft = Draft(DRAFT_FORMAT_VERSION,
            DraftMetadata("Authoring${module.name.replace("_", "")}Attachment", DocumentType.VEDLEGG, module, DraftLanguage.entries.toSet()),
            translations, fields = attachmentFields,
            outline = listOf(
                Block.Paragraph(BlockId("attachment-paragraph"), listOf(Inline.Field(FieldId("note")), Inline.Field(FieldId("amount"), Formatter.CURRENCY), Inline.Field(FieldId("category")))),
                Block.Repeat(BlockId("attachment-repeat"), FieldId("lines"), listOf(Block.Paragraph(BlockId("attachment-row"), listOf(Inline.Field(FieldId("line")))))),
            ),
            scenarios = listOf(TestScenario(ScenarioId("attachment-scenario"), "Attachment", attachmentFields.associate { it.id to attachmentData.get(it.name) })))
        val staticAttachment = attachmentDraft.copy(
            metadata = attachmentDraft.metadata.copy(name = "${attachmentDraft.metadata.name}Static"),
            fields = emptyList(), scenarios = emptyList(),
            outline = listOf(Block.Paragraph(BlockId("static-paragraph"), listOf(Inline.Text(translations)))),
        )
        fun renamedFields(values: List<DataField>, prefix: String): List<DataField> =
            values.map { it.copy(id = FieldId(prefix + it.id.value), children = renamedFields(it.children, prefix)) }
        val bindingFields = listOf(
            DataField(FieldId("boundAttachment"), "boundAttachment", FieldKind.OBJECT, children = renamedFields(attachmentFields, "bound")),
            DataField(FieldId("optionalAttachment"), "optionalAttachment", FieldKind.OBJECT, nullable = true, children = renamedFields(attachmentFields, "optional")),
        )
        val nullableScenario = scenario.copy(id = ScenarioId("nullable"), name = "Nullable", values = scenario.values + mapOf(
            FieldId("value") to draftObjectMapper.readTree("0"), FieldId("rows") to draftObjectMapper.readTree("null"),
            FieldId("date") to draftObjectMapper.readTree("null"), FieldId("details") to draftObjectMapper.readTree("null"),
            FieldId("enabled") to draftObjectMapper.readTree("false"), FieldId("mode") to draftObjectMapper.readTree("\"FIRST\""),
        ))
        listOf(DocumentType.AUTOBREV, DocumentType.REDIGERBAR).forEach { type ->
            val editable = type == DocumentType.REDIGERBAR
            val draft = Draft(DRAFT_FORMAT_VERSION,
                DraftMetadata("Authoring${module.name.replace("_", "")}${type.name}", type, module, DraftLanguage.entries.toSet(),
                    brevkode = "AUTHORING_SYNTHETIC",
                    kategori = if (type == DocumentType.REDIGERBAR) "INFORMASJON" else null,
                    brevkontekst = if (type == DocumentType.REDIGERBAR) "SAK" else null,
                    sakstyper = if (type == DocumentType.REDIGERBAR) setOf("AUTHORING_SYNTHETIC") else emptySet()),
                translations, fields = fields + bindingFields,
                choices = if (editable) listOf(CaseworkerChoice(ChoiceId("include"), "include", DraftLanguage.entries.associateWith { "Synthetic switch" })) else emptyList(),
                outline = if (editable) outline + listOf(
                    Block.Paragraph(BlockId("free-text"), listOf(Inline.FreeText(translations))),
                    Block.Conditional(BlockId("choice-false"), Predicate.Choice(ChoiceId("include"), false),
                        listOf(Block.Paragraph(BlockId("choice-false-text"), listOf(Inline.Text(translations))))),
                    Block.Conditional(BlockId("choice"), Predicate.Choice(ChoiceId("include"), true),
                        listOf(Block.Paragraph(BlockId("choice-text"), listOf(Inline.Text(translations))))),
                ) else outline,
                scenarios = (if (editable) listOf(scenario.copy(choices = mapOf(ChoiceId("include") to true)),
                    nullableScenario.copy(choices = mapOf(ChoiceId("include") to false))) else listOf(scenario, nullableScenario)).mapIndexed { index, value ->
                    value.copy(values = value.values + mapOf(FieldId("boundAttachment") to attachmentData,
                        FieldId("optionalAttachment") to if (index == 0) attachmentData else draftObjectMapper.readTree("null")))
                },
                attachments = listOf(AttachmentCopy(AttachmentReference("bound-copy"), DraftRevision(1), attachmentDraft, FieldId("boundAttachment")),
                    AttachmentCopy(AttachmentReference("optional-copy"), DraftRevision(2), attachmentDraft, FieldId("optionalAttachment")),
                    AttachmentCopy(AttachmentReference("static-copy"), DraftRevision(3), staticAttachment)))
            val result = generator.generate(draft)
            check(result.issues.isEmpty()) { result.issues }
            result.files.filter { it.path.value.endsWith(".kt") }.forEach { generated ->
                val segment = if ("/src/test/" in generated.path.value) "test" else "main"
                val output = File(root, "${module.name}/$segment/${generated.path.value.substringAfter("/kotlin/")}")
                output.parentFile.mkdirs()
                output.writeText(generated.content)
            }
            val dto = result.files.single { it.path.value.endsWith("/${draft.metadata.name}Dto.kt") }
            val template = result.files.single { it.path.value.endsWith("/${draft.metadata.name}.kt") }
            fun className(file: no.nav.pensjon.brev.brevoppskrift.api.GeneratedFile, name: String): String =
                file.content.lineSequence().first { it.startsWith("package ") }.removePrefix("package ").trim() + "." + name
            jsonFixtures.add(GeneratedDtoJsonFixture(
                className(dto, "${draft.metadata.name}Dto"),
                className(template, draft.metadata.name),
                editable,
                draft.scenarios.map { draftObjectMapper.valueToTree(it.values) },
                attachmentCount = 3,
            ))
        }
        val standalone = generator.generate(attachmentDraft)
        check(standalone.issues.isEmpty()) { standalone.issues }
        standalone.files.filter { it.path.value.endsWith(".kt") }.forEach { file ->
            val segment = if ("/src/test/" in file.path.value) "test" else "main"
            val output = File(root, "${module.name}/$segment/${file.path.value.substringAfter("/kotlin/")}")
            output.parentFile.mkdirs()
            output.writeText(file.content)
        }
        val standaloneDto = standalone.files.single { it.path.value.endsWith("/${attachmentDraft.metadata.name}Dto.kt") }
        val standaloneTemplate = standalone.files.single { it.path.value.endsWith("/${attachmentDraft.metadata.name}.kt") }
        jsonFixtures.add(GeneratedDtoJsonFixture(
            standaloneDto.content.lineSequence().first { it.startsWith("package ") }.removePrefix("package ").trim() + ".${attachmentDraft.metadata.name}Dto",
            standaloneTemplate.content.lineSequence().first { it.startsWith("package ") }.removePrefix("package ").trim() + ".${attachmentDraft.metadata.name}",
            false, listOf(attachmentData, attachmentData), attachment = true,
        ))
        verifyUnsupportedPaths(generator, module, translations)
    }
    draftObjectMapper.writerWithDefaultPrettyPrinter().writeValue(File(root, "dto-json-fixtures.json"), jsonFixtures)
    println("AUTHORING_COMPILE_CORPUS=8 letter profiles plus standalone/copied attachments; NB/NN/EN; typed required/nullable attachment bindings, YEAR/MONTH/PERCENT, heading1/2/3, lists/table, block/inline conditions, nested repeat, nullable object/list/date, all scalar kinds/comparisons/formatters; editable free text/choices; Double currency blocked")
}

private suspend fun verifyUnsupportedPaths(generator: KotlinDraftGenerator, module: TargetModule, translations: Translations) {
    val base = Draft(DRAFT_FORMAT_VERSION, DraftMetadata("UnsupportedProbe", DocumentType.AUTOBREV, module,
        DraftLanguage.entries.toSet(), brevkode = "AUTHORING_SYNTHETIC"), translations,
        outline = listOf(Block.Paragraph(BlockId("paragraph"), listOf(Inline.Text(translations)))))
    val probes = listOf(
        base.copy(fields = listOf(DataField(FieldId("amount"), "amount", FieldKind.DOUBLE)),
            outline = listOf(Block.Paragraph(BlockId("paragraph"), listOf(Inline.Field(FieldId("amount"), Formatter.CURRENCY))))),
    ) + base.copy(
        metadata = base.metadata.copy(type = DocumentType.REDIGERBAR, kategori = "INFORMASJON", brevkontekst = "SAK", sakstyper = setOf("AUTHORING_SYNTHETIC")),
        choices = listOf(CaseworkerChoice(ChoiceId("include"), "include", DraftLanguage.entries.associateWith { it.name })),
    )
    probes.forEach { probe ->
        val result = generator.generate(probe)
        check(result.files.isEmpty() && result.issues.any { it.code == "GENERATION_UNSUPPORTED" }) {
            "Unsupported generation path must be blocked, not emitted: ${probe.metadata.type}, ${probe.fields.map { it.kind }}"
        }
    }
    val external = generator.generate(base.copy(fields = listOf(DataField(FieldId("externalField"), "externalField", FieldKind.STRING, typeReference = "untrusted.External"))))
    check(external.files.isEmpty() && external.issues.any { it.code == "UNTRUSTED_BINDING" }) { "External type forgery must be blocked." }
}
