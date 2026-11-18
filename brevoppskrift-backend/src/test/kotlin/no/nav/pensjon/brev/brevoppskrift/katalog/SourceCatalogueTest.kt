package no.nav.pensjon.brev.brevoppskrift.katalog

import kotlinx.coroutines.runBlocking
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.node.ObjectNode
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
    private val registry = "$pkg.UfoereTemplates"
    private val dtoPath = RepositoryPath("ufoere/ufoere-api-model/src/main/kotlin/no/nav/pensjon/brev/ufore/api/model/ExampleDto.kt")

    private fun sources(content: String = """paragraph { text(bokmal { +"Bokmål" }, nynorsk { +"Nynorsk" }, english { +"English" }) }"""): Map<RepositoryPath, String> {
        val registrations = mapOf("UfoereTemplates" to pkg, "ProductionTemplates" to "no.nav.pensjon.brev.maler",
            "AlderTemplates" to "no.nav.pensjon.brev.alder.maler", "PlanleggePensjonTemplates" to "no.nav.pensjon.brev.planleggepensjon")
        return registrations.map { (name, packageName) ->
            RepositoryPath("registers/$name.kt") to """
                package $packageName
                object $name {
                    override fun hentAutobrevmaler() = setOf(${if ("$packageName.$name" == registry) "Example" else ""})
                    override fun hentRedigerbareMaler() = setOf()
                }
            """.trimIndent()
        }.toMap() + mapOf(
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

    private fun catalogue(snapshot: CatalogueSnapshot): BuildSourceCatalogue =
        BuildSourceCatalogue.fromResource(ByteArrayInputStream(draftObjectMapper.writeValueAsBytes(snapshot)))

    @Test
    fun `legacy source references retain scalar identity commit paths and digests`() {
        val json = """{"id":"UFOERE:Example","commit":"${commit.value}","files":{"${dtoPath.value}":"${"b".repeat(64)}"}}"""
        val reference = draftObjectMapper.readValue(json, SourceReference::class.java)
        assertThat(reference.id).isEqualTo(SourceId("UFOERE:Example"))
        assertThat(reference.commit).isEqualTo(commit)
        assertThat(reference.files).containsExactlyEntriesOf(mapOf(dtoPath to Sha256Digest("b".repeat(64))))
        assertThat(draftObjectMapper.readTree(draftObjectMapper.writeValueAsBytes(reference))).isEqualTo(draftObjectMapper.readTree(json))
    }

    @Test
    fun `attachment provenance is checked recursively even when the parent origin is invalid`(): Unit = runBlocking {
        val catalogue = catalogue(KotlinSourceImporter().use { it.build(commit, sources()) })
        val imported = requireNotNull(catalogue.import(catalogue.list().single().id))
        val altered = imported.copy(source = requireNotNull(imported.source).copy(files = mapOf(path to Sha256Digest("b".repeat(64)))))
        val nested = imported.copy(attachments = listOf(AttachmentCopy(AttachmentReference("nested-copy"), DraftRevision(1), altered)))
        val root = imported.copy(source = requireNotNull(imported.source).copy(id = SourceId("unknown")),
            attachments = listOf(AttachmentCopy(AttachmentReference("copy"), DraftRevision(1), nested)))
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
        val catalogue = catalogue(KotlinSourceImporter().use { it.build(commit, input) })
        val imported = requireNotNull(catalogue.import(catalogue.list().single().id))
        val reordered = imported.copy(title = imported.title.entries.sortedBy { it.key.ordinal }.associate { it.key to it.value })
        val generated = KotlinDraftGenerator(catalogue).generate(reordered).files.single { it.path == path }.content
        assertThat(generated.substringAfter("title {").substringBefore("outline")).contains("""text(english { +"Title" }, nynorsk { +"Tittel" }, bokmal { +"Tittel" })""")
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
        val catalogue = catalogue(KotlinSourceImporter().use { it.build(commit, input) })
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
        val catalogue = catalogue(KotlinSourceImporter().use { it.build(commit, input) })
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
        val catalogue = catalogue(KotlinSourceImporter().use { it.build(commit, input) })
        val draft = requireNotNull(catalogue.import(catalogue.list().single().id))
        val changes = mapOf(
            "Bokmål med annen tekst" to listOf("Bokmål ", "med", " annen tekst"),
            "Bokmål ny med tekst" to listOf("Bokmål ny ", "med", " tekst"),
            "Annen tekst" to listOf("Annen", "", " tekst"),
            "Bokmål" to listOf("Bokmål", "", ""),
            "Bokmål med tekst!" to listOf("Bokmål ", "med", " tekst!"),
            "" to listOf("", "", ""),
        )
        for ((value, fragments) in changes) {
            val edited = editFirstParagraph(draft, value)
            val result = KotlinDraftGenerator(catalogue).generate(edited)
            assertThat(result.issues).isEmpty()
            val generated = result.files.single { it.path == path }.content
            val expected = input.getValue(path)
                .replace(""""Bokmål """", "\"${fragments[0]}\"")
                .replace(""""med"""", "\"${fragments[1]}\"")
                .replace("""" tekst"""", "\"${fragments[2]}\"")
            assertThat(generated).describedAs("Edit: $value").isEqualTo(expected)
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
    fun `production registry backing set getters do not fail or duplicate visible templates`() {
        val input = sources().toMutableMap()
        input[RepositoryPath("registers/ProductionTemplates.kt")] = """
            package no.nav.pensjon.brev.maler
            object ProductionTemplates {
                private val autobrev = setOf(ProductionExample)
                private val redigerbare = setOf()
                private val skjult = setOf(HiddenTemplate)
                override fun hentAutobrevmaler() = autobrev
                override fun hentRedigerbareMaler() = redigerbare
            }
        """.trimIndent()
        input[RepositoryPath("pensjon/ProductionExample.kt")] = input.getValue(path).replace(pkg, "no.nav.pensjon.brev.maler").replace("object Example", "object ProductionExample")
        val snapshot = KotlinSourceImporter().use { it.build(commit, input) }
        assertThat(snapshot.entries.filter { it.summary.targetModule == "PENSJON" }.map { it.summary.name }).containsExactly("ProductionExample")
    }

    @Test
    fun `only sets reached from entry points are visible including parenthesized block getters`() {
        val input = sources().toMutableMap()
        input[RepositoryPath("registers/ProductionTemplates.kt")] = """
            package no.nav.pensjon.brev.maler
            object ProductionTemplates {
                private val autobrev = setOf(HiddenTemplate)
                private val redigerbare = setOf(HiddenTemplate)
                private val visible = setOf(ProductionExample)
                private val alias = visible
                override fun hentAutobrevmaler() { return (this.alias) }
                override fun hentRedigerbareMaler() = setOf()
            }
        """.trimIndent()
        input[RepositoryPath("pensjon/ProductionExample.kt")] = input.getValue(path).replace(pkg, "no.nav.pensjon.brev.maler").replace("object Example", "object ProductionExample")
        input[RepositoryPath("pensjon/HiddenTemplate.kt")] = input[RepositoryPath("pensjon/ProductionExample.kt")]!!.replace("ProductionExample", "HiddenTemplate")
        val snapshot = KotlinSourceImporter().use { it.build(commit, input) }
        assertThat(snapshot.entries.filter { it.summary.targetModule == "PENSJON" }.map { it.summary.name }).containsExactly("ProductionExample")
        assertThat(snapshot.entries.flatMap { it.summary.diagnostics }.map { it.code }).doesNotContain("UNRESOLVED_TEMPLATE")
    }

    @Test
    fun `syntax import preserves all three languages DTO identity and commit hashes`(): Unit = runBlocking {
        val snapshot = KotlinSourceImporter().use { it.build(commit, sources()) }
        val catalogue = catalogue(snapshot)
        val entry = catalogue.list().single()
        assertThat(entry.editable).isTrue()
        assertThat(entry.languages).containsExactly("NB", "NN", "EN")
        val draft = requireNotNull(catalogue.import(entry.id))
        assertThat(draft.source?.files?.keys).contains(path, dtoPath)
        assertThat(draft.source?.files?.values).allMatch { it.value.matches(Regex("[a-f0-9]{64}")) }
        assertThat(draft.fields.single().kind).isEqualTo(FieldKind.NUMBER)
        val updated = draft.copy(title = draft.title + (DraftLanguage.EN to "Edited ${'$'}{doNotExecute()}"))
        val generated = KotlinDraftGenerator(catalogue).generate(updated)
        assertThat(generated.issues).isEmpty()
        assertThat(generated.files.find { it.path == path }?.content).contains("object Example : AutobrevTemplate<ExampleDto>", "Edited \\${'$'}{doNotExecute()}")
        assertThat(generated.files.find { it.path == dtoPath }?.content).isEqualTo(sources().getValue(dtoPath))
    }

    @Test
    fun `opaque unsupported control flow interpolation and complex DTOs disable editing`() {
        listOf(
            sources("showIf(predicate) { paragraph { text(bokmal { +\"x\" }) } }"),
            sources("""paragraph { text(bokmal { +"${'$'}secret" }, nynorsk { +"tekst" }, english { +"text" }) }"""),
            sources() + (dtoPath to "package no.nav.pensjon.brev.ufore.api.model\ndata class ExampleDto(val values: Map<String, List<Unknown>>) : AutobrevData"),
        ).forEach { source ->
            val snapshot = KotlinSourceImporter().use { it.build(commit, source) }
            val catalogue = catalogue(snapshot)
            assertThat(catalogue.list().single().editable).isFalse()
            assertThat(catalogue.list().single().diagnostics).isNotEmpty()
            assertThat(catalogue.import(catalogue.list().single().id)).isNull()
        }
    }

    @Test
    fun `locked phrases survive but are not flattened documentation`(): Unit = runBlocking {
        val source = sources("""includePhrase(Felles.RettTilInnsyn)
            paragraph { text(bokmal { +"BM" }, nynorsk { +"NN" }, english { +"EN" }) }""")
        val catalogue = catalogue(KotlinSourceImporter().use { it.build(commit, source) })
        val draft = requireNotNull(catalogue.import(catalogue.list().single().id))
        assertThat(draft.outline.first()).isInstanceOf(Block.LockedSource::class.java)
        assertThat(KotlinDraftGenerator(catalogue).generate(draft).files.find { it.path == path }?.content).contains("includePhrase(Felles.RettTilInnsyn)")
        assertThat(catalogue.checkSource(draft.copy(outline = draft.outline.drop(1))).map { it.code }).contains("LOCKED_SOURCE_CHANGED")
    }

    @Test
    fun `JSON cannot introduce Kotlin bodies and forged locked bindings never reach generated files`(): Unit = runBlocking {
        val source = sources("""includePhrase(Felles.RettTilInnsyn)
            paragraph { text(bokmal { +"BM" }, nynorsk { +"NN" }, english { +"EN" }) }""")
        val catalogue = catalogue(KotlinSourceImporter().use { it.build(commit, source) })
        val draft = requireNotNull(catalogue.import(catalogue.list().single().id))
        val generator = KotlinDraftGenerator(catalogue)
        val locked = draft.outline.first() as Block.LockedSource
        val forged = draft.copy(outline = listOf(locked.copy(bindingId = "arbitraryKotlin()")) + draft.outline.drop(1))
        // Exercise request JSON, not only a UI object or readonly property.
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

    @Test
    fun `forged references field edits and changed dependencies cannot generate`(): Unit = runBlocking {
        val snapshot = KotlinSourceImporter().use { it.build(commit, sources()) }
        val catalogue = catalogue(snapshot)
        val draft = requireNotNull(catalogue.import(catalogue.list().single().id))
        val source = requireNotNull(draft.source)
        assertThat(catalogue.checkSource(draft.copy(source = source.copy(commit = GitCommit("b".repeat(40))))).map { it.code }).contains("STALE_SOURCE")
        assertThat(catalogue.checkSource(draft.copy(source = source.copy(files = source.files - dtoPath))).map { it.code }).contains("STALE_SOURCE")
        assertThat(catalogue.checkSource(draft.copy(fields = emptyList())).map { it.code }).contains("LOCKED_SOURCE_CHANGED")
        val changed = catalogue(KotlinSourceImporter().use { it.build(commit, sources() + (RepositoryPath("dependency.kt") to "package no.nav.pensjon.brev.ufore.api.model\nobject Changed")) })
        assertThat(changed.checkSource(draft).map { it.code }).contains("STALE_SOURCE")
    }

    @Test
    fun `packaged catalogue contains exactly visible registries across target modules`(): Unit = runBlocking {
        val catalogue = BuildSourceCatalogue()
        assertThat(catalogue.list()).isNotEmpty()
        assertThat(catalogue.list().map { it.targetModule }.toSet()).isEqualTo(TargetModule.entries.map { it.name }.toSet())
        assertThat(catalogue.list().map { it.id }.distinct()).hasSameSizeAs(catalogue.list())
        catalogue.list().forEach { entry ->
            assertThat(entry.sourceCommit.value).matches("[a-f0-9]{40,64}")
            if (entry.editable) assertThat(catalogue.checkSource(requireNotNull(catalogue.import(entry.id)))).isEmpty()
            else assertThat(entry.diagnostics).isNotEmpty()
        }
    }
}
