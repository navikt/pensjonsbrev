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
}
