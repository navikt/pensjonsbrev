package no.nav.pensjon.brev.brevoppskrift.kotlin

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import no.nav.pensjon.brev.brevoppskrift.katalog.BuildSourceCatalogue
import no.nav.pensjon.brev.brevoppskrift.katalog.CatalogueSnapshot
import no.nav.pensjon.brev.brevoppskrift.model.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream

class KotlinDraftGeneratorTest {
    private val catalogue = BuildSourceCatalogue.fromResource(ByteArrayInputStream(draftObjectMapper.writeValueAsBytes(CatalogueSnapshot(GitCommit("a".repeat(40)), emptyList()))))
    private val generator = KotlinDraftGenerator(catalogue)

    @Test
    fun `new templates generate deterministic module aware DSL DTO and real render tests`(): Unit = runBlocking {
        TargetModule.entries.forEach { module ->
            val draft = exampleDraft().copy(metadata = exampleDraft().metadata.copy(targetModule = module, brevkode = "EXAMPLE"))
            val result = generator.generate(draft)
            assertThat(result.issues).isEmpty()
            assertThat(result.files).hasSize(4)
            assertThat(generator.generate(draft)).isEqualTo(result)
            val profile = moduleProfile(module)
            assertThat(result.files.map { it.path }).anyMatch { it.value.startsWith(profile.dtoRoot + "/src/main/kotlin/") }
            assertThat(result.files.find { it.path.value.endsWith("/ExampleLetter.kt") }?.content).contains("languages(Bokmal, Nynorsk, English)", "AutobrevTemplate<ExampleLetterDto>")
            assertThat(result.files.find { it.path.value.endsWith("AuthoringTest.kt") }?.content).contains("LetterTestImpl", "renderTestHtml", "FellesFactory.fellesAuto")
        }
    }

    @Test
    fun `identical block wording uses stable distinct draft identities`(): Unit = runBlocking {
        val original = exampleDraft()
        val wording = listOf(Inline.Text(DraftLanguage.entries.associateWith { "Same wording" }))
        val draft = original.copy(
            metadata = original.metadata.copy(brevkode = "EXAMPLE"),
            outline = listOf(
                Block.Heading(BlockId("heading-one"), 1, wording),
                Block.Heading(BlockId("heading-two"), 2, wording),
                Block.Heading(BlockId("heading-three"), 3, wording),
                Block.Paragraph(BlockId("paragraph-one"), wording),
                Block.Paragraph(BlockId("paragraph-two"), wording),
            ),
        )
        val result = generator.generate(draft)
        assertThat(result.issues).isEmpty()
        val template = result.files.single { it.path.value.endsWith("/ExampleLetter.kt") }.content
        assertThat(template).contains(
            """title1(uniqueness = "heading-one")""",
            """title2(uniqueness = "heading-two")""",
            """title3(uniqueness = "heading-three")""",
            """paragraph(uniqueness = "paragraph-one")""",
            """paragraph(uniqueness = "paragraph-two")""",
        )
    }

    @Test
    fun `editable choice maps have a mutable default constructor and scenario construction uses the map constructor`(): Unit = runBlocking {
        TargetModule.entries.forEach { module ->
            val original = exampleDraft()
            val draft = original.copy(
                metadata = original.metadata.copy(
                    type = DocumentType.REDIGERBAR,
                    targetModule = module,
                    brevkode = "EXAMPLE",
                    kategori = "INFORMASJON",
                    brevkontekst = "SAK",
                    sakstyper = setOf("EXAMPLE"),
                ),
                choices = listOf(CaseworkerChoice(ChoiceId("include"), "include", DraftLanguage.entries.associateWith { "Include section" })),
                scenarios = listOf(
                    TestScenario(ScenarioId("enabled"), "Enabled", mapOf(FieldId("amount") to draftObjectMapper.readTree("100")), mapOf(ChoiceId("include") to true)),
                    TestScenario(ScenarioId("disabled"), "Disabled", mapOf(FieldId("amount") to draftObjectMapper.readTree("200")), mapOf(ChoiceId("include") to false)),
                ),
            )
            val result = generator.generate(draft)
            assertThat(result.issues).isEmpty()
            val dto = result.files.single { it.path.value.endsWith("/ExampleLetterDto.kt") }.content
            assertThat(dto).contains(
                "class Valg() : LinkedHashMap<String, Any?>(), SaksbehandlervalgIDSL",
                "constructor(switches: Map<String, Any?>) : this() { putAll(switches) }",
                "override val saksbehandlerValg: Valg = Valg()",
            )
            assertThat(dto).doesNotContain("private val values:", "by values", "by switches", "com.fasterxml.jackson", "@Json")
            val renderTest = result.files.single { it.path.value.endsWith("AuthoringTest.kt") }.content
            assertThat(renderTest).contains(
                "ExampleLetterDto.Valg(switches = mapOf(\"include\" to true))",
                "ExampleLetterDto.Valg(switches = mapOf(\"include\" to false))",
            )
        }
    }

    @Test
    fun `attachments generate revision scoped files typed binding and nullable inclusion in every target module`(): Unit = runBlocking {
        TargetModule.entries.forEach { module ->
            val child = exampleDraft().copy(
                metadata = exampleDraft().metadata.copy(name = "Notice", type = DocumentType.VEDLEGG, targetModule = module),
                fields = listOf(DataField(FieldId("note"), "note", FieldKind.STRING)),
                outline = listOf(Block.Paragraph(BlockId("child-text"), listOf(Inline.Field(FieldId("note"))))),
            )
            val root = exampleDraft().copy(
                metadata = exampleDraft().metadata.copy(targetModule = module, brevkode = "EXAMPLE"),
                fields = listOf(DataField(FieldId("binding"), "binding", FieldKind.OBJECT, nullable = true,
                    children = listOf(DataField(FieldId("bound-note"), "note", FieldKind.STRING)))),
                attachments = listOf(AttachmentCopy(AttachmentReference("notice-copy"), DraftRevision(42), child, FieldId("binding"))),
            )
            val result = generator.generate(root)
            assertThat(result.issues).isEmpty()
            assertThat(generator.generate(root)).isEqualTo(result)
            val childDto = result.files.single { it.path.value.endsWith("Dto.kt") && it.content.contains(" : VedleggData") }
            assertThat(childDto.path.value).contains("/NoticeR42")
            assertThat(childDto.content).doesNotContain("com.fasterxml.jackson", "@Json")
            assertThat(result.files.single { it.path.value.endsWith("/ExampleLetter.kt") }.content).contains("includeAttachmentIfNotNull(")
            assertThat(result.files.single { it.path.value.endsWith("/ExampleLetterDto.kt") }.content)
                .contains(childDto.path.value.substringAfterLast("/").removeSuffix(".kt"))
            val next = generator.generate(root.copy(attachments = listOf(root.attachments.single().copy(revision = DraftRevision(43)))))
            assertThat(next.issues).isEmpty()
            assertThat(next.files.map { it.path }).isNotEqualTo(result.files.map { it.path })
            val bad = root.copy(fields = listOf(root.fields.single().copy(children = listOf(DataField(FieldId("bound-note"), "note", FieldKind.NUMBER)))))
            assertThat(generator.generate(bad).issues.map { it.code }).contains("INVALID_ATTACHMENT_BINDING")
            assertThat(generator.generate(bad).files).isEmpty()
            assertThat(generator.generate(child).issues).isEmpty()
        }
    }

    @Test
    fun `year month percent keep integer JSON DTO fields and use typed DSL presentation`(): Unit = runBlocking {
        val original = exampleDraft()
        val draft = original.copy(
            metadata = original.metadata.copy(brevkode = "EXAMPLE"),
            fields = listOf(DataField(FieldId("year"), "year", FieldKind.YEAR), DataField(FieldId("month"), "month", FieldKind.MONTH), DataField(FieldId("percent"), "percent", FieldKind.PERCENT)),
            outline = listOf(Block.Paragraph(BlockId("paragraph"), listOf(Inline.Field(FieldId("year")), Inline.Field(FieldId("month")), Inline.Field(FieldId("percent"))))),
        )
        val result = generator.generate(draft)
        assertThat(result.issues).isEmpty()
        val dto = result.files.single { it.path.value.endsWith("/ExampleLetterDto.kt") }.content
        assertThat(dto).contains("val year: Int", "val month: Int", "val percent: Int")
        assertThat(result.files.single { it.path.value.endsWith("/ExampleLetter.kt") }.content).contains("java.time.Month.of(this)", "\" %\"")
    }

    @Test
    fun `multi value enum formatters keep every nested ifElse branch a String expression in all profiles`(): Unit = runBlocking {
        TargetModule.entries.forEach { module ->
            listOf(DocumentType.AUTOBREV, DocumentType.REDIGERBAR).forEach { type ->
                val original = exampleDraft()
                val editable = type == DocumentType.REDIGERBAR
                val draft = original.copy(
                    metadata = original.metadata.copy(
                        type = type,
                        targetModule = module,
                        brevkode = "EXAMPLE",
                        kategori = if (editable) "INFORMASJON" else null,
                        brevkontekst = if (editable) "SAK" else null,
                        sakstyper = if (editable) setOf("EXAMPLE") else emptySet(),
                    ),
                    fields = listOf(DataField(FieldId("mode"), "mode", FieldKind.ENUM, enumValues = listOf("FIRST", "SECOND", "THIRD"))),
                    outline = listOf(Block.Paragraph(BlockId("paragraph"), listOf(Inline.Field(FieldId("mode"))))),
                )
                val result = generator.generate(draft)
                assertThat(result.issues).isEmpty()
                val template = result.files.single { it.path.value.endsWith("/ExampleLetter.kt") }.content
                assertThat(template).contains(
                    "ifElse<String>(",
                    "\"FIRST\".expr(), ifElse<String>(",
                    "\"SECOND\".expr(), ifElse<String>(",
                    "\"THIRD\".expr(), \"\".expr())",
                    "text(bokmal { +ifElse<String>(",
                    "nynorsk { +ifElse<String>(",
                    "english { +ifElse<String>(",
                )
                assertThat(template).doesNotContain("\"FIRST\", ifElse(", "\"SECOND\", ifElse(")
            }
        }
    }

    @Test
    fun `required list under a guarded nullable object generates a nonnullable collection`(): Unit = runBlocking {
        val original = exampleDraft()
        val draft = original.copy(
            metadata = original.metadata.copy(brevkode = "EXAMPLE"),
            fields = listOf(DataField(FieldId("details"), "details", FieldKind.OBJECT, nullable = true, children = listOf(
                DataField(FieldId("rows"), "rows", FieldKind.LIST, children = listOf(DataField(FieldId("value"), "value", FieldKind.STRING))),
            ))),
            outline = listOf(Block.Conditional(BlockId("guard"), Predicate.IsNotNull(FieldId("details")), listOf(
                Block.Repeat(BlockId("repeat"), FieldId("rows"), listOf(Block.Paragraph(BlockId("paragraph"), listOf(Inline.Field(FieldId("value"))))))),
            )),
        )
        val result = generator.generate(draft)
        assertThat(result.issues).isEmpty()
        val template = result.files.single { it.path.value.endsWith("/ExampleLetter.kt") }.content
        assertThat(template).contains("forEach(argument.select(field0).select(field1).ifNull(emptyList()))")
    }

    @Test
    fun `nullable boolean and enum fields retain each language fallback`(): Unit = runBlocking {
        for (kind in listOf(FieldKind.BOOLEAN, FieldKind.ENUM)) {
            val original = exampleDraft()
            val draft = original.copy(
                metadata = original.metadata.copy(brevkode = "EXAMPLE"),
                fields = listOf(DataField(FieldId("value"), "value", kind, nullable = true, enumValues = if (kind == FieldKind.ENUM) listOf("FIRST", "SECOND") else emptyList())),
                outline = listOf(Block.Paragraph(BlockId("paragraph"), listOf(Inline.Field(FieldId("value"), fallback = DraftLanguage.entries.associateWith { "Missing$it" })))),
            )
            val result = generator.generate(draft)
            assertThat(result.issues).isEmpty()
            val template = result.files.single { it.path.value.endsWith("/ExampleLetter.kt") }.content
            for (language in DraftLanguage.entries) assertThat(template).describedAs("$kind/$language").contains(".ifNull(\"Missing$language\")")
            assertThat(template).contains(".safe { ").doesNotContain(".ifNull(false)")
        }
    }

    @Test
    fun `nullable ordered predicates unwrap safely rather than compare fabricated defaults`(): Unit = runBlocking {
        val original = exampleDraft()
        val draft = original.copy(
            metadata = original.metadata.copy(brevkode = "EXAMPLE"),
            fields = listOf(DataField(FieldId("date"), "date", FieldKind.DATE, nullable = true)),
            outline = listOf(Block.Conditional(BlockId("condition"), Predicate.All(listOf(
                Predicate.IsNotNull(FieldId("date")),
                Predicate.Compare(FieldId("date"), Comparison.GREATER_THAN, Literal.Date("1999-01-01")),
            )), listOf(Block.Paragraph(BlockId("paragraph"), listOf(Inline.Text(translations)))))),
        )
        val result = generator.generate(draft)
        assertThat(result.issues).isEmpty()
        val template = result.files.single { it.path.value.endsWith("/ExampleLetter.kt") }.content
        assertThat(template).contains(".safe { this.greaterThan(LocalDate.parse(\"1999-01-01\")) }.ifNull(false)")
        assertThat(template).doesNotContain(".ifNull(LocalDate.of")
    }

    @Test
    fun `Kotlin escapes interpolation control characters quotes and paths never derive from text`(): Unit = runBlocking {
        assertThat(kotlinString("\"\n${'$'}{Runtime.getRuntime()}\\")).isEqualTo("\"\\\"\\n\\${'$'}{Runtime.getRuntime()}\\\\\"")
        val draft = exampleDraft().copy(
            metadata = exampleDraft().metadata.copy(brevkode = "EXAMPLE"),
            title = DraftLanguage.entries.associateWith { "\"\n${'$'}{evil()}" },
        )
        val source = generator.generate(draft).files.first { it.path.value.endsWith("/ExampleLetter.kt") }.content
        assertThat(source).contains("\\${'$'}{evil()}")
        val invalid = draft.copy(metadata = draft.metadata.copy(name = "../../Evil"))
        assertThat(generator.generate(invalid).files).isEmpty()
    }

    @Test
    fun `unsupported external types currency and attachments block rather than lose content`(): Unit = runBlocking {
        val draft = exampleDraft().copy(
            metadata = exampleDraft().metadata.copy(brevkode = "EXAMPLE"),
            fields = listOf(DataField(FieldId("amount"), "amount", FieldKind.DOUBLE)),
            outline = listOf(Block.Paragraph(BlockId("paragraph"), listOf(Inline.Field(FieldId("amount"), Formatter.CURRENCY)))),
        )
        assertThat(generator.generate(draft).issues.map { it.code }).contains("GENERATION_UNSUPPORTED")
        assertThat(generator.generate(draft).files).isEmpty()
    }

}
