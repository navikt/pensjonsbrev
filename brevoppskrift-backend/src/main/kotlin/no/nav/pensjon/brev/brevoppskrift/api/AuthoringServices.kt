package no.nav.pensjon.brev.brevoppskrift.api

import no.nav.pensjon.brev.brevoppskrift.model.Draft
import no.nav.pensjon.brev.brevoppskrift.model.ValidationIssue
import no.nav.pensjon.brev.brevoppskrift.model.SourceId
import no.nav.pensjon.brev.brevoppskrift.model.GitCommit
import no.nav.pensjon.brev.brevoppskrift.model.RepositoryPath

data class GeneratedFile(val path: RepositoryPath, val content: String)
data class GenerationResult(val files: List<GeneratedFile>, val issues: List<ValidationIssue>)

interface DraftGenerator {
    suspend fun generate(draft: Draft): GenerationResult
}

data class TemplateSource(
    val id: SourceId,
    val name: String,
    val templateType: String,
    val targetModule: String,
    val languages: List<String>,
    val sourceCommit: GitCommit,
    val editable: Boolean,
    val diagnostics: List<ValidationIssue>,
)

interface SourceCatalogue {
    fun list(): List<TemplateSource>
    fun import(sourceId: SourceId): Draft?
    suspend fun checkSource(draft: Draft): List<ValidationIssue>

    /** expected must come from storage or a trusted catalogue import, not the incoming request. */
    suspend fun checkSource(draft: Draft, expected: Draft): List<ValidationIssue> =
        sourceIdentityIssues(draft, expected) + checkSource(draft)
}

internal fun sourceIdentityIssues(draft: Draft, expected: Draft, path: String = "", depth: Int = 0): List<ValidationIssue> {
    if (depth > 20) return listOf(ValidationIssue(path, "MAX_DEPTH", "Vedleggstreet er for dypt."))
    val own = if (draft.source != expected.source) {
        listOf(ValidationIssue("${path}source", "IMMUTABLE_SOURCE", "Kildeopphav kan ikke endres."))
    } else emptyList()
    val attachments = expected.attachments.associateBy { it.id }
    return own + draft.attachments.flatMapIndexed { index, attachment ->
        attachments[attachment.id]?.let {
            sourceIdentityIssues(attachment.draft, it.draft, "${path}attachments[$index].draft.", depth + 1)
        }.orEmpty()
    }
}
