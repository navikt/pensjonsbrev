package no.nav.pensjon.brev.brevoppskrift.katalog

import no.nav.pensjon.brev.brevoppskrift.api.SourceCatalogue
import no.nav.pensjon.brev.brevoppskrift.api.TemplateSource
import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import no.nav.pensjon.brev.brevoppskrift.lagring.SourceCatalogueRepository
import no.nav.pensjon.brev.brevoppskrift.model.*
import java.io.InputStream

/** Only the build tool writes this envelope. Source text never crosses the authoring API. */
data class CatalogueSnapshot(val commit: GitCommit, val entries: List<CatalogueEntry>)
data class CatalogueEntry(
    val summary: TemplateSource,
    val reference: SourceReference,
    val draft: Draft?,
    val templatePath: RepositoryPath,
    val files: Map<RepositoryPath, String>,
    val slots: List<SourceTextSlot> = emptyList(),
    val capabilities: Set<String> = emptySet(),
    val dtoName: String? = null,
    val templatePackage: String? = null,
)
data class SourceTextSlot(
    val start: Int,
    val end: Int,
    val blockId: BlockId?,
    val inlineIndex: Int = 0,
    val language: DraftLanguage,
    val originalText: String,
)

class BuildSourceCatalogue private constructor(private val snapshot: CatalogueSnapshot, private val history: SourceCatalogueRepository? = null) : SourceCatalogue {
    constructor() : this(readSnapshot())

    private val entries = snapshot.entries.associateBy { it.summary.id }

    override fun list(): List<TemplateSource> = snapshot.entries.map { entry ->
        entry.summary.copy(languages = entry.summary.languages.toList(), diagnostics = entry.summary.diagnostics.toList())
    }

    override fun import(sourceId: SourceId): Draft? = entries[sourceId]?.takeIf { it.summary.editable }?.draft?.let {
        draftObjectMapper.readValue(draftObjectMapper.writeValueAsBytes(it), Draft::class.java)
    }

    override suspend fun checkSource(draft: Draft): List<ValidationIssue> {
        suspend fun check(current: Draft, path: String, depth: Int): List<ValidationIssue> {
            if (depth > 20) return listOf(ValidationIssue(path, "MAX_DEPTH", "Vedleggstreet er for dypt."))
            return checkOwnSource(current).map { it.copy(path = path + it.path) } +
                current.attachments.flatMapIndexed { index, copy -> check(copy.draft, "${path}attachments[$index].draft.", depth + 1) }
        }
        return check(draft, "", 0)
    }

    private suspend fun checkOwnSource(draft: Draft): List<ValidationIssue> {
        val source = draft.source ?: return if (containsBinding(draft.outline) || containsExternalType(draft.fields)) {
            listOf(ValidationIssue("source", "SOURCE_REQUIRED", "Kildebundet innhold krever en kilde fra katalogen."))
        } else emptyList()
        val entry = trustedEntry(source) ?: return if (source.id in entries) {
            listOf(ValidationIssue("source", "STALE_SOURCE", "Kildeversjonen eller filhash-ene finnes ikke i den betrodde kildehistorikken."))
        } else listOf(ValidationIssue("source", "UNKNOWN_SOURCE", "Kilden finnes ikke i den betrodde kildehistorikken."))
        if (!entry.summary.editable) return entry.summary.diagnostics.ifEmpty {
            listOf(ValidationIssue("source", "IMPORT_UNSUPPORTED", "Kilden støtter ikke redigering."))
        }
        val imported = entry.draft ?: return listOf(ValidationIssue("source", "IMPORT_UNSUPPORTED", "Importartefakten mangler."))
        // This profile permits wording edits only. Any widened capability must have its own round-trip tests.
        if (identity(draft) != identity(imported)) return listOf(
            ValidationIssue("source", "LOCKED_SOURCE_CHANGED", "Denne importprofilen tillater bare tekstendringer. Struktur, metadata, felt, fraser og kildeidentitet er låst."),
        )
        return emptyList()
    }

    internal suspend fun trustedEntry(reference: SourceReference): CatalogueEntry? =
        entries[reference.id]?.takeIf { it.reference == reference } ?: history?.find(reference)

    companion object {
        const val RESOURCE = "/authoring/source-catalogue.json"

        suspend fun persistent(history: SourceCatalogueRepository): BuildSourceCatalogue {
            val snapshot = readSnapshot()
            history.retain(snapshot.entries)
            return BuildSourceCatalogue(snapshot, history)
        }

        fun fromResource(stream: InputStream, history: SourceCatalogueRepository? = null): BuildSourceCatalogue = stream.use {
            BuildSourceCatalogue(draftObjectMapper.readValue(it, CatalogueSnapshot::class.java), history)
        }

        private fun readSnapshot(): CatalogueSnapshot =
            requireNotNull(BuildSourceCatalogue::class.java.getResourceAsStream(RESOURCE)) {
                "Kildekatalog mangler. Kjør :brevoppskrift-backend:generateSourceCatalogue."
            }.use { draftObjectMapper.readValue(it, CatalogueSnapshot::class.java) }
    }
}

private fun containsBinding(blocks: List<Block>): Boolean = blocks.any {
    when (it) {
        is Block.LockedSource -> true
        is Block.Conditional -> containsBinding(it.ifTrue) || containsBinding(it.ifFalse)
        is Block.Repeat -> containsBinding(it.body)
        else -> false
    }
}

private fun containsExternalType(fields: List<DataField>): Boolean =
    fields.any { it.typeReference != null || containsExternalType(it.children) }

private fun identity(draft: Draft): Draft {
    fun inlines(content: List<Inline>): List<Inline> = content.map {
        when (it) {
            is Inline.Text -> it.copy(text = it.text.mapValues { "" })
            is Inline.Conditional -> it.copy(ifTrue = inlines(it.ifTrue), ifFalse = inlines(it.ifFalse))
            else -> it
        }
    }
    fun blocks(content: List<Block>): List<Block> = content.map {
        when (it) {
            is Block.Paragraph -> it.copy(text = inlines(it.text))
            is Block.Heading -> it.copy(text = inlines(it.text))
            is Block.Conditional -> it.copy(ifTrue = blocks(it.ifTrue), ifFalse = blocks(it.ifFalse))
            is Block.Repeat -> it.copy(body = blocks(it.body))
            else -> it
        }
    }
    return draft.copy(
        title = draft.title.mapValues { "" },
        outline = blocks(draft.outline),
        baseline = null,
        comments = emptyList(),
        scenarios = emptyList(),
    )
}
