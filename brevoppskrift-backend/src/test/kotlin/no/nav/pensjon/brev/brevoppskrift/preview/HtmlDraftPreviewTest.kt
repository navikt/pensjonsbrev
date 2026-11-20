package no.nav.pensjon.brev.brevoppskrift.preview

import no.nav.pensjon.brev.brevoppskrift.api.PreviewRequest
import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import no.nav.pensjon.brev.brevoppskrift.model.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class HtmlDraftPreviewTest {
    private val preview = HtmlDraftPreview()

    @Test
    fun `all languages and scenario strings are HTML escaped never evaluated`() {
        val draft = exampleDraft().copy(
            fields = listOf(DataField(FieldId("value"), "value", FieldKind.STRING)),
            title = DraftLanguage.entries.associateWith { "<script>alert(1)</script>" },
            outline = listOf(Block.Paragraph(BlockId("paragraph"), listOf(Inline.Field(FieldId("value")), Inline.NewLine, Inline.Text(translations, FontType.BOLD)))),
            scenarios = listOf(TestScenario(ScenarioId("synthetic"), "Test", mapOf(FieldId("value") to draftObjectMapper.readTree("\"<img src=x onerror=alert(1)>\"")))),
        )
        DraftLanguage.entries.forEach { language ->
            val result = preview.render(PreviewRequest(draft, language, ScenarioId("synthetic")))
            assertThat(result.html).contains("&lt;script&gt;", "&lt;img", "<strong>", translations.getValue(language))
            assertThat(result.html).doesNotContain("<script", "<img")
            assertThat(result.warnings).isEmpty()
        }
    }

    @Test
    fun `conditions and repetition use selected scenario`() {
        val row = DataField(FieldId("rows"), "rows", FieldKind.LIST, children = listOf(DataField(FieldId("value"), "value", FieldKind.NUMBER)))
        val draft = exampleDraft().copy(
            fields = listOf(row),
            outline = listOf(Block.Repeat(BlockId("repeat"), FieldId("rows"), listOf(Block.Conditional(
                BlockId("condition"), Predicate.Compare(FieldId("value"), Comparison.GREATER_THAN, Literal.Number(BigDecimal.TEN)),
                listOf(Block.Paragraph(BlockId("true"), listOf(Inline.Field(FieldId("value"))))),
                listOf(Block.Paragraph(BlockId("false"), listOf(Inline.Text(translations)))),
            )))),
            scenarios = listOf(TestScenario(ScenarioId("synthetic"), "Test", mapOf(FieldId("rows") to draftObjectMapper.readTree("[{\"value\":2},{\"value\":20}]")))),
        )
        val result = preview.render(PreviewRequest(draft, DraftLanguage.EN, ScenarioId("synthetic")))
        assertThat(result.html).contains("<p>You receive a letter.</p>", "<p>20</p>")
        assertThat(result.warnings).isEmpty()
    }

    @Test
    fun `missing scenarios opaque logic and invalid requests are explicit`() {
        val source = SourceReference(SourceId("source"), GitCommit("a".repeat(40)), mapOf(RepositoryPath("X.kt") to Sha256Digest("b".repeat(64))))
        val draft = exampleDraft().copy(source = source, outline = listOf(Block.LockedSource(BlockId("locked"), "phrase")))
        assertThat(preview.render(PreviewRequest(draft, DraftLanguage.EN)).warnings).anyMatch { it.contains("evalueres ikke") }
        assertThat(preview.render(PreviewRequest(draft, DraftLanguage.EN, ScenarioId("missing"))).html).isEmpty()
        val invalid = draft.copy(source = null)
        assertThat(preview.render(PreviewRequest(invalid, DraftLanguage.EN)).html).isEmpty()
    }

    @Test
    fun `source text profiles explicitly warn that original semantics and scenario logic are not evaluated`() {
        val source = SourceReference(SourceId("source"), GitCommit("a".repeat(40)), mapOf(RepositoryPath("X.kt") to Sha256Digest("b".repeat(64))))
        val draft = exampleDraft().copy(
            source = source,
            scenarios = listOf(TestScenario(ScenarioId("synthetic"), "Test", mapOf(FieldId("amount") to draftObjectMapper.readTree("100")))),
        )
        val result = preview.render(PreviewRequest(draft, DraftLanguage.EN, ScenarioId("synthetic")))
        assertThat(result.html).contains(translations.getValue(DraftLanguage.EN))
        assertThat(result.warnings).anyMatch { it.contains("bare tekstendringer") }
        assertThat(result.warnings).anyMatch { it.contains("Original Kotlin") && it.contains("evalueres ikke") }
        val changedScenario = draft.copy(
            scenarios = listOf(TestScenario(ScenarioId("synthetic"), "Test", mapOf(FieldId("amount") to draftObjectMapper.readTree("200")))),
        )
        assertThat(preview.render(PreviewRequest(changedScenario, DraftLanguage.EN, ScenarioId("synthetic"))).html).isEqualTo(result.html)
    }

    @Test
    fun `nullable attachment bindings are omitted and typed integer presentations match generation`() {
        val child = exampleDraft().copy(
            metadata = exampleDraft().metadata.copy(type = DocumentType.VEDLEGG),
            fields = listOf(DataField(FieldId("note"), "note", FieldKind.STRING)),
            outline = listOf(Block.Paragraph(BlockId("child-text"), listOf(Inline.Field(FieldId("note"))))),
        )
        val draft = exampleDraft().copy(
            fields = listOf(
                DataField(FieldId("binding"), "binding", FieldKind.OBJECT, nullable = true, children = listOf(DataField(FieldId("bound-note"), "note", FieldKind.STRING))),
                DataField(FieldId("year"), "year", FieldKind.YEAR), DataField(FieldId("month"), "month", FieldKind.MONTH), DataField(FieldId("percent"), "percent", FieldKind.PERCENT),
            ),
            outline = listOf(Block.Paragraph(BlockId("paragraph"), listOf(Inline.Field(FieldId("year")), Inline.Field(FieldId("month")), Inline.Field(FieldId("percent"))))),
            attachments = listOf(AttachmentCopy(AttachmentReference("copy"), DraftRevision(1), child, FieldId("binding"))),
            scenarios = listOf(TestScenario(ScenarioId("synthetic"), "Test", mapOf(
                FieldId("binding") to draftObjectMapper.readTree("null"),
                FieldId("year") to draftObjectMapper.readTree("2000"), FieldId("month") to draftObjectMapper.readTree("11"), FieldId("percent") to draftObjectMapper.readTree("50"),
            ))),
        )
        val result = preview.render(PreviewRequest(draft, DraftLanguage.EN, ScenarioId("synthetic")))
        assertThat(result.html).contains("2000", "November", "50 %").doesNotContain("class=\"attachment\"")
        assertThat(result.warnings).isEmpty()
        val populated = draft.copy(scenarios = listOf(draft.scenarios.single().copy(
            values = draft.scenarios.single().values + (FieldId("binding") to draftObjectMapper.readTree("""{"note":"Bound attachment"}""")),
        )))
        assertThat(preview.render(PreviewRequest(populated, DraftLanguage.EN, ScenarioId("synthetic"))).html).contains("class=\"attachment\"", "Bound attachment")
    }

    @Test
    fun `scenario values are validated against full object type`() {
        val draft = exampleDraft().copy(
            fields = listOf(DataField(FieldId("parent"), "parent", FieldKind.OBJECT, children = listOf(DataField(FieldId("day"), "day", FieldKind.DATE)))),
            outline = listOf(Block.Paragraph(BlockId("paragraph"), listOf(Inline.Field(FieldId("day"))))),
            scenarios = listOf(TestScenario(ScenarioId("synthetic"), "Test", mapOf(FieldId("parent") to draftObjectMapper.readTree("{\"day\":\"not-a-date\",\"unexpected\":42}")))),
        )
        assertThat(DraftValidator.validate(draft).map { it.code }).contains("TYPE_MISMATCH", "UNKNOWN_FIELD")
        assertThat(preview.render(PreviewRequest(draft, DraftLanguage.NN, ScenarioId("synthetic"))).html).isEmpty()
    }

    @Test
    fun `formatters headings lists and tables render structured scenario values`() {
        val draft = exampleDraft().copy(
            fields = listOf(
                DataField(FieldId("amount"), "amount", FieldKind.NUMBER),
                DataField(FieldId("ratio"), "ratio", FieldKind.DOUBLE),
                DataField(FieldId("day"), "day", FieldKind.DATE),
            ),
            outline = listOf(
                Block.Heading(BlockId("heading"), 1, listOf(Inline.Text(translations))),
                Block.ItemList(BlockId("list"), true, listOf(listOf(Inline.Field(FieldId("amount"), Formatter.CURRENCY)))),
                Block.Table(BlockId("table"), listOf(translations), listOf(listOf(listOf(Inline.Field(FieldId("ratio")))))),
                Block.Paragraph(BlockId("date"), listOf(Inline.Field(FieldId("day")), Inline.NewLine, Inline.Field(FieldId("day"), Formatter.MONTH_YEAR))),
            ),
            scenarios = listOf(TestScenario(ScenarioId("synthetic"), "Test", mapOf(
                FieldId("amount") to draftObjectMapper.readTree("1234"),
                FieldId("ratio") to draftObjectMapper.readTree("20.5"),
                FieldId("day") to draftObjectMapper.readTree("\"2000-01-01\""),
            ))),
        )
        val result = preview.render(PreviewRequest(draft, DraftLanguage.EN, ScenarioId("synthetic")))
        assertThat(result.warnings).isEmpty()
        assertThat(result.html).contains(
            "<h2>You receive a letter.</h2>",
            "<ol><li>1,234</li></ol>",
            "<table><thead><tr><th>You receive a letter.</th>",
            "<td>20.50</td>",
            "1\u00a0January\u00a02000<br>January 2000",
        )
    }

    @Test
    fun `absent values differ from explicit null and unsupported languages never fall back`() {
        val draft = exampleDraft().copy(
            fields = listOf(DataField(FieldId("value"), "value", FieldKind.STRING, nullable = true)),
            outline = listOf(Block.Paragraph(BlockId("paragraph"), listOf(Inline.Field(FieldId("value"), fallback = translations)))),
            scenarios = listOf(TestScenario(ScenarioId("null"), "Null", mapOf(FieldId("value") to draftObjectMapper.readTree("null")))),
        )
        val absent = preview.render(PreviewRequest(draft, DraftLanguage.EN))
        assertThat(absent.html).contains("[value]")
        assertThat(absent.warnings).anyMatch { it.contains("mangler scenarioverdi") }
        val explicitNull = preview.render(PreviewRequest(draft, DraftLanguage.EN, ScenarioId("null")))
        assertThat(explicitNull.html).contains("<p>You receive a letter.</p>")
        assertThat(explicitNull.warnings).isEmpty()

        val singleLanguage = exampleDraft().copy(
            metadata = exampleDraft().metadata.copy(languages = setOf(DraftLanguage.NB)),
            title = mapOf(DraftLanguage.NB to "Tittel"),
            fields = emptyList(),
            outline = listOf(Block.Paragraph(BlockId("paragraph"), listOf(Inline.Text(mapOf(DraftLanguage.NB to "Tekst"))))),
        )
        val unsupported = preview.render(PreviewRequest(singleLanguage, DraftLanguage.EN))
        assertThat(unsupported.html).isEmpty()
        assertThat(unsupported.warnings).containsExactly("Språket finnes ikke i utkastet.")
    }
}
