package no.nav.pensjon.brev.brevoppskrift.model

import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class DraftValidatorTest {
    @Test
    fun `source commit hashes require exactly 40 or 64 hexadecimal characters`() {
        val original = exampleDraft().copy(source = SourceReference(
            SourceId("source"),
            GitCommit("a".repeat(40)),
            mapOf(RepositoryPath("module/Template.kt") to Sha256Digest("b".repeat(64))),
        ))
        listOf(40, 64).forEach { length ->
            val draft = original.copy(source = original.source!!.copy(commit = GitCommit("a".repeat(length))))
            assertThat(DraftValidator.validate(draft)).describedAs("hash length %s", length).isEmpty()
        }
        (39..65).filter { it != 40 && it != 64 }.forEach { length ->
            val draft = original.copy(source = original.source!!.copy(commit = GitCommit("a".repeat(length))))
            assertThat(DraftValidator.validate(draft).filter { it.path == "source.commit" }.map { it.code })
                .describedAs("hash length %s", length)
                .containsExactly("INVALID_SOURCE")
        }
    }

    @Test
    fun `all three languages survive a polymorphic JSON round trip`() {
        val original = exampleDraft().copy(
            outline = listOf(
                Block.Paragraph(BlockId("paragraph"), listOf(Inline.Text(translations), Inline.NewLine, Inline.Field(FieldId("amount")))),
                Block.Conditional(BlockId("conditional"), Predicate.Compare(FieldId("amount"), Comparison.GREATER_THAN, Literal.Number(BigDecimal.TEN)), listOf(Block.Paragraph(BlockId("conditionalText"), listOf(Inline.Text(translations))))),
            ),
        )
        val restored = draftObjectMapper.readValue(draftObjectMapper.writeValueAsBytes(original), Draft::class.java)

        assertThat(restored).isEqualTo(original)
        assertThat(DraftValidator.validate(restored)).isEmpty()
    }

    @Test
    fun `missing English and unknown format versions are reported`() {
        val draft = exampleDraft().copy(formatVersion = 99, title = translations - DraftLanguage.EN)
        assertThat(DraftValidator.validate(draft).map { it.code }).contains("UNSUPPORTED_VERSION", "LANGUAGE_MISMATCH")
    }

    @Test
    fun `IDs are unique across blocks fields and choices`() {
        val original = exampleDraft()
        val draft = original.copy(
            metadata = original.metadata.copy(type = DocumentType.REDIGERBAR),
            choices = listOf(CaseworkerChoice(ChoiceId("amount"), "include", translations)),
            outline = listOf(Block.Paragraph(BlockId("amount"), listOf(Inline.Text(translations)))),
            scenarios = listOf(TestScenario(ScenarioId("amount"), "Syntetisk", mapOf(FieldId("amount") to draftObjectMapper.readTree("12")))),
            comments = listOf(DraftComment(ReviewEventId("amount"), CommentAnchor("DRAFT"), "Kontroller")),
            attachments = listOf(AttachmentCopy(
                AttachmentReference("amount"),
                DraftRevision(1),
                original.copy(metadata = original.metadata.copy(type = DocumentType.VEDLEGG)),
            )),
        )
        assertThat(DraftValidator.validate(draft).filter { it.code == "DUPLICATE_ID" }.map { it.path }).containsExactly(
            "choices[0].id", "outline[0].id", "scenarios[0].id", "comments[0].id", "attachments[0].id",
        )
    }

    @Test
    fun `legacy scenario map keys are typed without changing nested property names`() {
        val json = """{"id":"scenario","name":"Syntetisk","values":{"root-id":{"propertyName":"tekst"}},"choices":{"choice-id":true}}"""
        val scenario = draftObjectMapper.readValue(json, TestScenario::class.java)
        assertThat(scenario.values.keys).containsExactly(FieldId("root-id"))
        assertThat(scenario.values.getValue(FieldId("root-id")).get("propertyName").textValue()).isEqualTo("tekst")
        assertThat(scenario.choices).containsEntry(ChoiceId("choice-id"), true)
        assertThat(draftObjectMapper.readTree(draftObjectMapper.writeValueAsBytes(scenario))).isEqualTo(draftObjectMapper.readTree(json))
        val original = exampleDraft()
        val draft = original.copy(
            metadata = original.metadata.copy(type = DocumentType.REDIGERBAR),
            fields = listOf(DataField(FieldId("root-id"), "root", FieldKind.OBJECT, children = listOf(
                DataField(FieldId("child-id"), "propertyName", FieldKind.STRING),
            ))),
            choices = listOf(CaseworkerChoice(ChoiceId("choice-id"), "include", translations)),
            scenarios = listOf(scenario),
        )
        assertThat(DraftValidator.validate(draft)).isEmpty()
    }

    @Test
    fun `list fields require a repetition scope`() {
        val field = DataField(FieldId("rows"), "rows", FieldKind.LIST, children = listOf(DataField(FieldId("value"), "value", FieldKind.NUMBER)))
        val paragraph = Block.Paragraph(BlockId("paragraph"), listOf(Inline.Field(FieldId("value"))))
        val draft = exampleDraft().copy(fields = listOf(field), outline = listOf(paragraph))
        assertThat(DraftValidator.validate(draft).map { it.code }).contains("UNBOUND_LIST")

        val repeated = draft.copy(outline = listOf(Block.Repeat(BlockId("repeat"), FieldId("rows"), listOf(paragraph))))
        assertThat(DraftValidator.validate(repeated)).isEmpty()
    }

    @Test
    fun `nullable objects are bound only in the true branch`() {
        val field = DataField(FieldId("parent"), "parent", FieldKind.OBJECT, nullable = true, children = listOf(DataField(FieldId("value"), "value", FieldKind.NUMBER)))
        val paragraph = Block.Paragraph(BlockId("paragraph"), listOf(Inline.Field(FieldId("value"))))
        val draft = exampleDraft().copy(fields = listOf(field), outline = listOf(Block.Conditional(BlockId("condition"), Predicate.IsNotNull(FieldId("parent")), listOf(paragraph))))
        assertThat(DraftValidator.validate(draft)).isEmpty()

        val elseBranch = draft.copy(outline = listOf(Block.Conditional(BlockId("condition"), Predicate.IsNotNull(FieldId("parent")), emptyList(), listOf(paragraph))))
        assertThat(DraftValidator.validate(elseBranch).map { it.code }).contains("NULLABLE_PARENT")
    }

    @Test
    fun `OR does not grant a non-null binding present in only one branch`() {
        val field = DataField(FieldId("parent"), "parent", FieldKind.OBJECT, nullable = true, children = listOf(DataField(FieldId("value"), "value", FieldKind.NUMBER)))
        val predicate = Predicate.Any(listOf(Predicate.IsNotNull(FieldId("parent")), Predicate.Compare(FieldId("amount"), Comparison.EQUAL, Literal.Number(BigDecimal.ZERO))))
        val draft = exampleDraft().copy(
            fields = exampleDraft().fields + field,
            outline = listOf(Block.Conditional(BlockId("condition"), predicate, listOf(Block.Paragraph(BlockId("paragraph"), listOf(Inline.Field(FieldId("value"))))))),
        )
        assertThat(DraftValidator.validate(draft).map { it.code }).contains("NULLABLE_PARENT")
    }

    @Test
    fun `AND allows a checked nullable object in following terms`() {
        val field = DataField(FieldId("parent"), "parent", FieldKind.OBJECT, nullable = true, children = listOf(DataField(FieldId("value"), "value", FieldKind.NUMBER)))
        val predicate = Predicate.All(listOf(Predicate.IsNotNull(FieldId("parent")), Predicate.Compare(FieldId("value"), Comparison.EQUAL, Literal.Number(BigDecimal.ZERO))))
        val draft = exampleDraft().copy(fields = listOf(field), outline = listOf(Block.Conditional(BlockId("condition"), predicate, listOf(Block.Paragraph(BlockId("paragraph"), listOf(Inline.Field(FieldId("value"))))))))
        assertThat(DraftValidator.validate(draft)).isEmpty()
    }

    @Test
    fun `nullable scalar text requires a translated fallback`() {
        val draft = exampleDraft().copy(fields = listOf(DataField(FieldId("amount"), "amount", FieldKind.NUMBER, nullable = true)), outline = listOf(Block.Paragraph(BlockId("paragraph"), listOf(Inline.Field(FieldId("amount"))))))
        assertThat(DraftValidator.validate(draft).map { it.code }).contains("MISSING_FALLBACK")
        assertThat(DraftValidator.validate(draft.copy(outline = listOf(Block.Paragraph(BlockId("paragraph"), listOf(Inline.Field(FieldId("amount"), fallback = translations.mapValues { "" }))))))).isEmpty()
    }

    @Test
    fun `Kotlin names table widths and field formatters are validated`() {
        val draft = exampleDraft().copy(
            metadata = exampleDraft().metadata.copy(name = "class"),
            fields = listOf(DataField(FieldId("amount"), "amount", FieldKind.STRING)),
            outline = listOf(Block.Table(BlockId("table"), listOf(translations, translations), listOf(listOf(listOf(Inline.Field(FieldId("amount"), Formatter.CURRENCY)))))),
        )
        assertThat(DraftValidator.validate(draft).map { it.code }).contains("INVALID_NAME", "INVALID_ROW", "INVALID_FORMATTER")
    }

    @Test
    fun `choices and free text are restricted to editable letters`() {
        val draft = exampleDraft().copy(
            choices = listOf(CaseworkerChoice(ChoiceId("choice"), "choice", translations)),
            outline = listOf(Block.Paragraph(BlockId("paragraph"), listOf(Inline.FreeText(translations)))),
        )
        assertThat(DraftValidator.validate(draft).map { it.code }).contains("INVALID_DOCUMENT_TYPE", "INVALID_FREE_TEXT")
        assertThat(DraftValidator.validate(draft.copy(metadata = draft.metadata.copy(type = DocumentType.REDIGERBAR)))).isEmpty()
    }

    @Test
    fun `predicate types and enum values are validated`() {
        val predicate = Predicate.Compare(FieldId("enum"), Comparison.GREATER_THAN, Literal.Text("UNKNOWN"))
        val draft = exampleDraft().copy(
            fields = listOf(DataField(FieldId("enum"), "status", FieldKind.ENUM, enumValues = listOf("OPEN", "CLOSED"))),
            outline = listOf(Block.Conditional(BlockId("condition"), predicate, listOf(Block.Paragraph(BlockId("paragraph"), listOf(Inline.Text(translations)))))),
        )
        assertThat(DraftValidator.validate(draft).map { it.code }).contains("INVALID_ENUM_VALUE", "INVALID_COMPARISON")
    }

    @Test
    fun `excessively nested document structure is rejected`() {
        var block: Block = Block.Paragraph(BlockId("paragraph"), listOf(Inline.Text(translations)))
        repeat(25) { index -> block = Block.Conditional(BlockId("conditional$index"), Predicate.IsNotNull(FieldId("amount")), listOf(block)) }
        assertThat(DraftValidator.validate(exampleDraft().copy(outline = listOf(block))).map { it.code }).contains("MAX_DEPTH")
    }
}

val translations = mapOf(DraftLanguage.NB to "Du får et brev.", DraftLanguage.NN to "Du får eit brev.", DraftLanguage.EN to "You receive a letter.")

fun exampleDraft(): Draft = Draft(
    formatVersion = DRAFT_FORMAT_VERSION,
    metadata = DraftMetadata("ExampleLetter", DocumentType.AUTOBREV, TargetModule.UFOERE, DraftLanguage.entries.toSet()),
    title = translations,
    fields = listOf(DataField(FieldId("amount"), "amount", FieldKind.NUMBER)),
    outline = listOf(Block.Paragraph(BlockId("paragraph"), listOf(Inline.Text(translations)))),
)
