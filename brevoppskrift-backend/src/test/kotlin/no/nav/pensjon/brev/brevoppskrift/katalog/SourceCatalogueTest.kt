package no.nav.pensjon.brev.brevoppskrift.katalog

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
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
}
