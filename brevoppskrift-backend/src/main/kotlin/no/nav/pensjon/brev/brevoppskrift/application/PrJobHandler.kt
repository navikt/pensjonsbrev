package no.nav.pensjon.brev.brevoppskrift.application

import io.ktor.http.HttpStatusCode
import no.nav.pensjon.brev.brevoppskrift.api.*
import no.nav.pensjon.brev.brevoppskrift.integrasjoner.PrJob
import no.nav.pensjon.brev.brevoppskrift.integrasjoner.PrJobRepository
import no.nav.pensjon.brev.brevoppskrift.integrasjoner.PrService
import no.nav.pensjon.brev.brevoppskrift.lagring.*
import no.nav.pensjon.brev.brevoppskrift.model.DraftId

class PrJobHandler(
    private val repository: DraftRepository,
    private val generator: DraftGenerator,
    private val jobs: PrJobRepository,
    private val service: PrService?,
) {
    val enabled: Boolean get() = service != null

    fun requireEnabled() {
        available(service)
    }

    suspend fun submit(id: DraftId, request: RevisionRequest): PrJob {
        requireEnabled()
        if (request.revision.value < 1) throw ApiFailure(HttpStatusCode.BadRequest, "INVALID_REVISION", "Revisjonen må være positiv.")
        val current = repository.get(id) ?: throw MissingDraft()
        if (current.archivedAt != null) throw ArchivedDraft()
        if (request.revision != current.revision) throw RevisionConflict(current.revision)
        if (current.status != DraftStatus.READY) throw ApiFailure(HttpStatusCode.Conflict, "DRAFT_NOT_READY", "Lagre utkastet og marker det som klart først.")
        val generated = generator.generate(current.draft)
        if (generated.issues.isNotEmpty()) throw InvalidDraft(generated.issues)
        return jobs.create(current.id, request.revision)
    }

    suspend fun latest(id: DraftId): PrJob =
        jobs.latest(id) ?: throw ApiFailure(HttpStatusCode.NotFound, "PR_NOT_FOUND", "Ingen PR er opprettet.")
}
