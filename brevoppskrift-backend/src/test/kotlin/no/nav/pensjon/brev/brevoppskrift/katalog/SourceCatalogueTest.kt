package no.nav.pensjon.brev.brevoppskrift.katalog

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.node.ObjectNode
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import no.nav.pensjon.brev.brevoppskrift.kotlin.KotlinDraftGenerator
import no.nav.pensjon.brev.brevoppskrift.model.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream

class SourceCatalogueTest {
    private val commit = GitCommit("a".repeat(40))
    private val pkg = "no.nav.pensjon.brev.ufore.maler"
    private val path = RepositoryPath("ufoere/maler/src/main/kotlin/${pkg.replace('.', '/')}/Example.kt")
    private val dtoPath = RepositoryPath("ufoere/ufoere-api-model/src/main/kotlin/no/nav/pensjon/brev/ufore/api/model/ExampleDto.kt")

    private fun sources(content: String = """paragraph { text(bokmal { +"Bokmål" }, nynorsk { +"Nynorsk" }, english { +"English" }) }"""): Map<RepositoryPath, String> {
        val registrations = mapOf(
            "UfoereTemplates" to pkg,
            "ProductionTemplates" to "no.nav.pensjon.brev.maler",
            "AlderTemplates" to "no.nav.pensjon.brev.alder.maler",
            "PlanleggePensjonTemplates" to "no.nav.pensjon.brev.planleggepensjon",
        )
        return registrations.entries.associate { entry ->
            RepositoryPath("registers/${entry.key}.kt") to """
                package ${entry.value}
                object ${entry.key} {
                    override fun hentAutobrevmaler() = setOf(${if (entry.key == "UfoereTemplates") "Example" else ""})
                    override fun hentRedigerbareMaler() = setOf()
                }
            """.trimIndent()
        } + mapOf(
            path to """
                package $pkg
                import no.nav.pensjon.brev.ufore.api.model.ExampleDto
                object Example : AutobrevTemplate<ExampleDto> {
                    override val kode = Codes.Auto.EXAMPLE
                    override val template = createTemplate(
                        languages = languages(Bokmal, Nynorsk, English),
                        letterMetadata = LetterMetadata(displayTitle = "Example", distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG, brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV),
                    ) {
                        title { text(bokmal { +"Tittel" }, nynorsk { +"Tittel" }, english { +"Title" }) }
                        outline { $content }
                    }
                }
            """.trimIndent(),
            dtoPath to """
                package no.nav.pensjon.brev.ufore.api.model
                data class ExampleDto(val value: Int) : AutobrevData
            """.trimIndent(),
        )
    }

    private fun catalogue(input: Map<RepositoryPath, String> = sources()): BuildSourceCatalogue {
        val snapshot = KotlinSourceImporter().use { it.build(commit, input) }
        return BuildSourceCatalogue.fromResource(ByteArrayInputStream(draftObjectMapper.writeValueAsBytes(snapshot)))
    }

    @Test
    fun `import preserves all languages DTO fields and exact source identity`(): Unit = runBlocking {
        val catalogue = catalogue()
        val entry = catalogue.list().single()
        assertThat(entry.editable).isTrue()
        assertThat(entry.languages).containsExactly("NB", "NN", "EN")
        val draft = requireNotNull(catalogue.import(entry.id))
        assertThat(draft.fields.single().kind).isEqualTo(FieldKind.NUMBER)
        assertThat(draft.source?.commit).isEqualTo(commit)
        assertThat(draft.source?.files?.keys).contains(path, dtoPath)
        assertThat(draft.source?.files?.values).allMatch { it.value.matches(Regex("[a-f0-9]{64}")) }
        assertThat(catalogue.checkSource(draft.copy(title = draft.title + (DraftLanguage.EN to "Edited")))).isEmpty()
        val json = draftObjectMapper.writeValueAsBytes(requireNotNull(draft.source))
        assertThat(draftObjectMapper.readValue(json, SourceReference::class.java)).isEqualTo(draft.source)
        val updated = draft.copy(title = draft.title + (DraftLanguage.EN to "Edited ${'$'}{doNotExecute()}"))
        val generated = KotlinDraftGenerator(catalogue).generate(updated)
        assertThat(generated.issues).isEmpty()
        assertThat(generated.files.find { it.path == path }?.content).contains("object Example : AutobrevTemplate<ExampleDto>", "Edited \\${'$'}{doNotExecute()}")
        assertThat(generated.files.find { it.path == dtoPath }?.content).isEqualTo(sources().getValue(dtoPath))
    }

    @Test
    fun `only registry sets reachable from entry points are visible`() {
        val input = sources().toMutableMap()
        input[RepositoryPath("registers/UfoereTemplates.kt")] = """
            package $pkg
            object UfoereTemplates {
                private val hidden = setOf(HiddenTemplate)
                private val visible = setOf(Example)
                private val alias = visible
                override fun hentAutobrevmaler() { return (this.alias) }
                override fun hentRedigerbareMaler() = setOf()
            }
        """.trimIndent()
        input[RepositoryPath("ufoere/HiddenTemplate.kt")] = input.getValue(path).replace("object Example", "object HiddenTemplate")
        assertThat(catalogue(input).list().map { it.name }).containsExactly("Example")
        input[RepositoryPath("registers/UfoereTemplates.kt")] = input.getValue(RepositoryPath("registers/UfoereTemplates.kt"))
            .replace("return (this.alias)", "return visible.filter { true }")
        assertThatThrownBy { catalogue(input) }.isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `unsupported control flow interpolation and complex DTOs disable editing`() {
        listOf(
            sources("showIf(predicate) { paragraph { text(bokmal { +\"x\" }) } }"),
            sources("""paragraph { text(bokmal { +"${'$'}secret" }, nynorsk { +"tekst" }, english { +"text" }) }"""),
            sources() + (dtoPath to "package no.nav.pensjon.brev.ufore.api.model\ndata class ExampleDto(val values: Map<String, List<Unknown>>) : AutobrevData"),
        ).forEach { input ->
            val catalogue = catalogue(input)
            assertThat(catalogue.list().single().editable).isFalse()
            assertThat(catalogue.list().single().diagnostics).isNotEmpty()
            assertThat(catalogue.import(catalogue.list().single().id)).isNull()
        }
    }

    @Test
    fun `computed DTO getters disable editing instead of dropping properties`() {
        listOf("get() = value + 1", "get() { return value + 1 }").forEach { getter ->
            val input = sources() + (dtoPath to """
                package no.nav.pensjon.brev.ufore.api.model
                data class ExampleDto(val value: Int) : AutobrevData {
                    val derived: Int $getter
                }
            """.trimIndent())
            val catalogue = catalogue(input)
            val entry = catalogue.list().single()
            assertThat(entry.editable).describedAs(getter).isFalse()
            assertThat(entry.diagnostics.map { it.code }).contains("UNSUPPORTED_TYPE")
            assertThat(catalogue.import(entry.id)).isNull()
        }
    }

    @Test
    fun `stored provenance cannot be removed or replaced from text only imports`(): Unit = runBlocking {
        val catalogue = catalogue()
        val imported = requireNotNull(catalogue.import(catalogue.list().single().id))
        val source = requireNotNull(imported.source)
        val stripped = imported.copy(source = null, fields = emptyList())
        assertThat(catalogue.checkSource(stripped, imported).map { it.code }).contains("IMMUTABLE_SOURCE")
        val replaced = imported.copy(source = source.copy(commit = GitCommit("b".repeat(40))))
        assertThat(catalogue.checkSource(replaced, imported).map { it.code }).contains("IMMUTABLE_SOURCE")
        assertThat(catalogue.checkSource(imported.copy(title = translations), imported)).isEmpty()
        val fresh = exampleDraft()
        assertThat(catalogue.checkSource(fresh, fresh)).isEmpty()
        assertThat(catalogue.checkSource(imported, fresh).map { it.code }).contains("IMMUTABLE_SOURCE")

        val attachment = AttachmentCopy(AttachmentReference("copy"), DraftRevision(1), imported)
        val parent = fresh.copy(attachments = listOf(attachment))
        val altered = parent.copy(attachments = listOf(attachment.copy(draft = stripped)))
        assertThat(catalogue.checkSource(altered, parent))
            .anyMatch { it.path == "attachments[0].draft.source" && it.code == "IMMUTABLE_SOURCE" }
    }

    @Test
    fun `forged source identity fields and bindings are rejected`(): Unit = runBlocking {
        val input = sources("""includePhrase(Felles.RettTilInnsyn)
            paragraph { text(bokmal { +"BM" }, nynorsk { +"NN" }, english { +"EN" }) }""")
        val catalogue = catalogue(input)
        val draft = requireNotNull(catalogue.import(catalogue.list().single().id))
        val source = requireNotNull(draft.source)
        val locked = draft.outline.first() as Block.LockedSource
        assertThat(catalogue.checkSource(draft.copy(source = null)).map { it.code }).contains("SOURCE_REQUIRED")
        assertThat(catalogue.checkSource(draft.copy(source = source.copy(commit = GitCommit("b".repeat(40))))).map { it.code }).contains("STALE_SOURCE")
        assertThat(catalogue.checkSource(draft.copy(source = source.copy(files = source.files - dtoPath))).map { it.code }).contains("STALE_SOURCE")
        assertThat(catalogue.checkSource(draft.copy(fields = emptyList())).map { it.code }).contains("LOCKED_SOURCE_CHANGED")
        val forged = draft.copy(outline = listOf(locked.copy(bindingId = "arbitraryKotlin()")) + draft.outline.drop(1))
        assertThat(catalogue.checkSource(forged).map { it.code }).contains("LOCKED_SOURCE_CHANGED")
        val changed = catalogue(input + (RepositoryPath("dependency.kt") to "package no.nav.pensjon.brev.ufore.api.model\nobject Changed"))
        assertThat(changed.checkSource(draft).map { it.code }).contains("STALE_SOURCE")
    }

    @Test
    fun `nested attachment provenance is checked even when parent origin is invalid`(): Unit = runBlocking {
        val catalogue = catalogue()
        val imported = requireNotNull(catalogue.import(catalogue.list().single().id))
        val altered = imported.copy(source = requireNotNull(imported.source).copy(files = mapOf(path to Sha256Digest("b".repeat(64)))))
        val nested = imported.copy(attachments = listOf(AttachmentCopy(AttachmentReference("nested-copy"), DraftRevision(1), altered)))
        val root = imported.copy(
            source = requireNotNull(imported.source).copy(id = SourceId("unknown")),
            attachments = listOf(AttachmentCopy(AttachmentReference("copy"), DraftRevision(1), nested)),
        )
        val issues = catalogue.checkSource(root)
        assertThat(issues).anyMatch { it.path == "source" && it.code == "UNKNOWN_SOURCE" }
        assertThat(issues).anyMatch { it.path == "attachments[0].draft.attachments[0].draft.source" && it.code == "STALE_SOURCE" }
    }

    @Test
    fun `text calls retain original language type parameter order even when client reorders map keys`(): Unit = runBlocking {
        val input = sources("""paragraph { text(english { +"English" }, nynorsk { +"Nynorsk" }, bokmal { +"Bokmål" }) }""").toMutableMap()
        input[path] = input.getValue(path)
            .replace("languages(Bokmal, Nynorsk, English)", "languages(English, Nynorsk, Bokmal)")
            .replace("""text(bokmal { +"Tittel" }, nynorsk { +"Tittel" }, english { +"Title" })""",
                """text(english { +"Title" }, nynorsk { +"Tittel" }, bokmal { +"Tittel" })""")
        val catalogue = catalogue(input)
        val imported = requireNotNull(catalogue.import(catalogue.list().single().id))
        val reordered = imported.copy(title = imported.title.entries.sortedBy { it.key.ordinal }.associate { it.key to it.value })
        val result = KotlinDraftGenerator(catalogue).generate(reordered)
        assertThat(result.issues).isEmpty()
        assertThat(result.files.single { it.path == path }.content).isEqualTo(input.getValue(path))
    }

    @Test
    fun `unchanged imports preserve formatting comments raw strings and concatenation byte for byte`(): Unit = runBlocking {
        val quotes = "\"\"\""
        val input = sources("""
            paragraph {
                text(
                    english { +${quotes}English
            second line$quotes },
                    // Keep the original argument order and layout.
                    bokmal { +"Bokmål " + /* keep this comment */ "med tekst" },
                    nynorsk { +"Nynorsk med \"sitat\"\nog ny linje" },
                    fontType = FontType.BOLD,
                )
                newline()
                text(bokmal { +"Neste" }, nynorsk { +"Neste" }, english { +"Next" })
            }
            title2 { text(bokmal { +"Overskrift" }, nynorsk { +"Overskrift" }, english { +"Heading" }) }
        """.trimIndent())
        val catalogue = catalogue(input)
        val draft = requireNotNull(catalogue.import(catalogue.list().single().id))
        val result = KotlinDraftGenerator(catalogue).generate(draft)
        assertThat(result.issues).isEmpty()
        assertThat(result.files.single { it.path == path }.content).isEqualTo(input.getValue(path))
    }

    @Test
    fun `edits replace only selected language literals preserving every other source byte`(): Unit = runBlocking {
        val input = sources("""
            paragraph {
                text(
                    english { +"English" }, // Keep this comment.
                    bokmal { +"Bokmål" },
                    nynorsk { +"Nynorsk" },
                    fontType = FontType.ITALIC,
                )
            }
            paragraph { text(bokmal { +"Urørt" }, nynorsk { +"Urørt" }, english { +"Untouched" }) }
        """.trimIndent())
        val catalogue = catalogue(input)
        val draft = requireNotNull(catalogue.import(catalogue.list().single().id))
        val edited = editFirstParagraph(draft, "Endret \"tekst\" med ${'$'}{innhold}\nny linje")
            .copy(title = draft.title + (DraftLanguage.NB to "Endret tittel"))
        val result = KotlinDraftGenerator(catalogue).generate(edited)
        assertThat(result.issues).isEmpty()
        val generated = result.files.single { it.path == path }.content
        val expected = input.getValue(path)
            .replace("""bokmal { +"Tittel" }""", """bokmal { +"Endret tittel" }""")
            .replace("""bokmal { +"Bokmål" }""", """bokmal { +"Endret \"tekst\" med \${'$'}{innhold}\nny linje" }""")
        assertThat(generated).isEqualTo(expected)
        val reimported = KotlinSourceImporter().use { it.build(commit, input + (path to generated)) }.entries.single()
        assertThat(reimported.summary.editable).isTrue()
        assertThat(requireNotNull(reimported.draft).copy(source = null)).isEqualTo(edited.copy(source = null))
    }

    @Test
    fun `edits inside concatenated text preserve operators comments and unaffected fragments`(): Unit = runBlocking {
        val input = sources("""
            paragraph {
                text(
                    bokmal { +"Bokmål " + /* keep */ "med" +
                        // Keep this line break too.
                        " tekst" },
                    nynorsk { +"Nynorsk" },
                    english { +"English" },
                )
            }
        """.trimIndent())
        val catalogue = catalogue(input)
        val draft = requireNotNull(catalogue.import(catalogue.list().single().id))
        val changes = mapOf(
            "Bokmål med annen tekst" to listOf("Bokmål ", "med", " annen tekst"),
            "Bokmål ny med tekst" to listOf("Bokmål ny ", "med", " tekst"),
            "Annen tekst" to listOf("Annen", "", " tekst"),
            "Bokmål" to listOf("Bokmål", "", ""),
            "Bokmål med tekst!" to listOf("Bokmål ", "med", " tekst!"),
            "" to listOf("", "", ""),
        )
        for (change in changes.entries) {
            val edited = editFirstParagraph(draft, change.key)
            val result = KotlinDraftGenerator(catalogue).generate(edited)
            assertThat(result.issues).isEmpty()
            val generated = result.files.single { it.path == path }.content
            val fragments = change.value
            val expected = input.getValue(path)
                .replace(""""Bokmål """", "\"${fragments[0]}\"")
                .replace(""""med"""", "\"${fragments[1]}\"")
                .replace("""" tekst"""", "\"${fragments[2]}\"")
            assertThat(generated).describedAs("Edit: ${change.key}").isEqualTo(expected)
            val reimported = KotlinSourceImporter().use { it.build(commit, input + (path to generated)) }.entries.single()
            assertThat(reimported.summary.editable).isTrue()
            assertThat(requireNotNull(reimported.draft).copy(source = null)).isEqualTo(edited.copy(source = null))
        }
    }

    private fun editFirstParagraph(draft: Draft, value: String): Draft {
        val paragraph = draft.outline.first() as Block.Paragraph
        val inline = paragraph.text.first() as Inline.Text
        val edited = paragraph.copy(text = listOf(inline.copy(text = inline.text + (DraftLanguage.NB to value))) + paragraph.text.drop(1))
        return draft.copy(outline = listOf(edited) + draft.outline.drop(1))
    }

    @Test
    fun `locked phrases survive but are not flattened documentation`(): Unit = runBlocking {
        val input = sources("""includePhrase(Felles.RettTilInnsyn)
            paragraph { text(bokmal { +"BM" }, nynorsk { +"NN" }, english { +"EN" }) }""")
        val catalogue = catalogue(input)
        val draft = requireNotNull(catalogue.import(catalogue.list().single().id))
        assertThat(draft.outline.first()).isInstanceOf(Block.LockedSource::class.java)
        assertThat(KotlinDraftGenerator(catalogue).generate(draft).files.find { it.path == path }?.content).contains("includePhrase(Felles.RettTilInnsyn)")
        assertThat(catalogue.checkSource(draft.copy(outline = draft.outline.drop(1))).map { it.code }).contains("LOCKED_SOURCE_CHANGED")
    }

    @Test
    fun `JSON cannot introduce Kotlin bodies and forged locked bindings never reach generated files`(): Unit = runBlocking {
        val input = sources("""includePhrase(Felles.RettTilInnsyn)
            paragraph { text(bokmal { +"BM" }, nynorsk { +"NN" }, english { +"EN" }) }""")
        val catalogue = catalogue(input)
        val draft = requireNotNull(catalogue.import(catalogue.list().single().id))
        val generator = KotlinDraftGenerator(catalogue)
        val locked = draft.outline.first() as Block.LockedSource
        val forged = draft.copy(outline = listOf(locked.copy(bindingId = "arbitraryKotlin()")) + draft.outline.drop(1))
        val decoded = draftObjectMapper.readValue(draftObjectMapper.writeValueAsBytes(forged), Draft::class.java)
        assertThat(generator.generate(decoded).files).isEmpty()
        assertThat(generator.generate(decoded).issues.map { it.code }).contains("LOCKED_SOURCE_CHANGED")
        assertThat(generator.generate(forged.copy(source = null)).files).isEmpty()

        val withKotlin = draftObjectMapper.valueToTree<ObjectNode>(draft)
        (withKotlin.get("outline").get(0) as ObjectNode).put("kotlin", "arbitraryKotlin()")
        assertThatThrownBy { draftObjectMapper.treeToValue(withKotlin, Draft::class.java) }.isInstanceOf(JsonMappingException::class.java)

        val unknownType = draftObjectMapper.valueToTree<ObjectNode>(draft)
        (unknownType.get("outline").get(0) as ObjectNode).put("type", "KOTLIN")
        assertThatThrownBy { draftObjectMapper.treeToValue(unknownType, Draft::class.java) }.isInstanceOf(JsonMappingException::class.java)

        val reference = requireNotNull(draft.source)
        val alteredHash = draft.copy(source = reference.copy(files = reference.files + (path to Sha256Digest("f".repeat(64)))))
        assertThat(generator.generate(alteredHash).files).isEmpty()
        assertThat(generator.generate(alteredHash).issues.map { it.code }).contains("STALE_SOURCE")

        val forgedType = draft.copy(fields = draft.fields.map { it.copy(typeReference = "arbitraryKotlin()") })
        assertThat(generator.generate(forgedType).files).isEmpty()
        assertThat(generator.generate(forgedType).issues.map { it.code }).contains("LOCKED_SOURCE_CHANGED")
    }
}
