package no.nav.pensjon.brev.brevoppskrift.application

import io.ktor.http.HttpStatusCode
import io.micrometer.core.instrument.MeterRegistry
import no.nav.pensjon.brev.brevoppskrift.api.*
import no.nav.pensjon.brev.brevoppskrift.integrasjoner.DraftNotifications
import no.nav.pensjon.brev.brevoppskrift.lagring.*
import no.nav.pensjon.brev.brevoppskrift.model.*
import java.time.Instant
import no.nav.pensjon.brev.brevoppskrift.auth.AuthorName

class DraftHandler(
    private val repository: DraftRepository,
    private val generator: DraftGenerator,
    private val catalogue: SourceCatalogue,
    private val notifications: DraftNotifications,
    private val metrics: MeterRegistry,
) {
    suspend fun get(id: DraftId): DraftRecord {
        val current = repository.get(id) ?: throw MissingDraft()
        if (current.archivedAt != null) throw ArchivedDraft()
        return current
    }

    suspend fun attachmentUses(id: DraftId): List<AttachmentUse> {
        get(id)
        return repository.attachmentUses(id)
    }

    suspend fun create(request: CreateDraftRequest, author: AuthorName): DraftRecord {
        if ((request.draft == null) == (request.sourceId == null)) throw ApiFailure(HttpStatusCode.BadRequest, "INVALID_CREATE", "Oppgi enten et nytt utkast eller en kildemal.")
        val imported = request.sourceId?.let { catalogue.import(it) ?: throw ApiFailure(HttpStatusCode.NotFound, "SOURCE_NOT_FOUND", "Fant ikke malen.") }
        val draft = imported ?: requireNotNull(request.draft)
        if (imported == null && draft.source != null) throw ApiFailure(HttpStatusCode.BadRequest, "INVALID_SOURCE", "Kildeopphav må komme fra katalogen.")
        validate(draft)
        val record = repository.create(stamp(null, draft, author), request.sourceId, author)
        metrics.counter("brevoppskrift.draft.created").increment()
        notifications.notify("Nytt utkast", record)
        return record
    }

    suspend fun update(id: DraftId, request: UpdateDraftRequest, author: AuthorName): DraftRecord {
        val current = get(id)
        revision(request.revision)
        if (request.revision != current.revision) throw RevisionConflict(current.revision)
        if (request.draft.source != current.draft.source) throw ApiFailure(HttpStatusCode.BadRequest, "IMMUTABLE_SOURCE", "Kildeopphav kan ikke endres.")
        validate(request.draft)
        val sourceIssues = catalogue.checkSource(request.draft, current.draft)
        if (sourceIssues.isNotEmpty()) throw InvalidDraft(sourceIssues)
        val stamped = stamp(current.draft, request.draft, author)
        val record = repository.update(id, request.revision, stamped, author)
        metrics.counter("brevoppskrift.draft.saved").increment()
        val newComments = stamped.comments.sumOf { 1 + it.replies.size } - current.draft.comments.sumOf { 1 + it.replies.size }
        if (newComments > 0) notifications.notify("Nye kommentarer og svar", record, newComments)
        return record
    }

    suspend fun status(id: DraftId, request: StatusRequest, author: AuthorName): DraftRecord {
        revision(request.revision)
        val current = get(id)
        if (request.revision != current.revision) throw RevisionConflict(current.revision)
        if (request.status == DraftStatus.READY) {
            validate(current.draft)
            generate(current.draft)
        }
        val record = repository.status(id, request.revision, request.status, author)
        metrics.counter("brevoppskrift.draft.status", "status", request.status.name).increment()
        if (current.status != record.status) notifications.notify(if (record.status == DraftStatus.READY) "Til utvikler" else "Til arbeid", record)
        return record
    }

    suspend fun approve(id: DraftId, request: RevisionRequest, author: AuthorName): DraftRecord {
        revision(request.revision)
        val current = get(id)
        if (request.revision != current.revision) throw RevisionConflict(current.revision)
        val approved = current.draft.copy(baseline = DraftBaseline(current.draft.copy(baseline = null, comments = emptyList()), "APPROVED", Instant.now(), author))
        val record = repository.update(id, request.revision, approved, author)
        notifications.notify("Godkjent grunnlinje", record)
        return record
    }

    suspend fun archive(id: DraftId, request: ArchiveRequest, author: AuthorName) {
        revision(request.revision)
        val current = get(id)
        if ((request.reason == ArchiveReason.RESET && current.sourceKey == null) || (request.reason == ArchiveReason.DISCARDED && current.sourceKey != null)) {
            throw ApiFailure(HttpStatusCode.BadRequest, "INVALID_ARCHIVE", "Arkivgrunnen passer ikke til utkastets opphav.")
        }
        val record = repository.archive(id, request.revision, request.reason, author)
        metrics.counter("brevoppskrift.draft.archived", "reason", request.reason.name).increment()
        notifications.notify("Arkivert: ${request.reason}", record)
    }

    private suspend fun generate(draft: Draft) {
        val generated = generator.generate(draft)
        if (generated.issues.isNotEmpty()) throw InvalidDraft(generated.issues)
    }

    private fun stamp(previous: Draft?, next: Draft, author: AuthorName): Draft = try {
        ReviewStamper.stamp(previous, next, author, Instant.now())
    } catch (_: IllegalArgumentException) {
        throw ApiFailure(HttpStatusCode.UnprocessableEntity, "INVALID_REVIEW", "Tidligere kommentarer og gjennomgangshistorikk kan ikke omskrives.")
    }

    private fun revision(value: DraftRevision) {
        if (value.value < 1) throw ApiFailure(HttpStatusCode.BadRequest, "INVALID_REVISION", "Revisjonen må være positiv.")
    }
}
