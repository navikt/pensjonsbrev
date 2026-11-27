package no.nav.pensjon.brev.brevoppskrift.lagring

import no.nav.pensjon.brev.brevoppskrift.model.Draft
import no.nav.pensjon.brev.brevoppskrift.model.DraftId
import no.nav.pensjon.brev.brevoppskrift.model.DraftRevision
import no.nav.pensjon.brev.brevoppskrift.model.SourceId
import no.nav.pensjon.brev.brevoppskrift.auth.AuthorName
import no.nav.pensjon.brev.brevoppskrift.api.sourceIdentityIssues
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.core.vendors.ForUpdateOption
import kotlinx.coroutines.CancellationException
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.json.contains
import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import java.sql.SQLException
import java.time.Instant
import java.util.UUID

class RevisionConflict(val currentRevision: DraftRevision) : RuntimeException("REVISION_CONFLICT")
class SourceConflict : RuntimeException("SOURCE_CONFLICT")
class ArchivedDraft : RuntimeException("DRAFT_ARCHIVED")
class MissingDraft : RuntimeException("DRAFT_NOT_FOUND")
data class AttachmentUse(val id: DraftId, val name: String, val revision: DraftRevision)

class DraftRepository(private val transactions: Transactional) {
    suspend fun ready(): Boolean = try {
        transactions.transaction { DraftTable.select(DraftTable.id).limit(1).toList(); true }
    } catch (cancelled: CancellationException) {
        throw cancelled
    } catch (_: Exception) {
        false
    }

    suspend fun list(): List<DraftSummary> = transactions.transaction {
        DraftTable.select(DraftTable.id, DraftTable.revision, DraftTable.status, DraftTable.sourceKey, DraftTable.metadata, DraftTable.updatedAt, DraftTable.updatedBy)
            .where { DraftTable.archivedAt.isNull() }
            .orderBy(DraftTable.updatedAt, SortOrder.DESC)
            .map { row ->
                DraftSummary(row[DraftTable.id], row[DraftTable.revision], row[DraftTable.status], row[DraftTable.sourceKey], row[DraftTable.metadata], row[DraftTable.updatedAt], row[DraftTable.updatedBy])
            }
    }

    suspend fun get(id: DraftId): DraftRecord? = transactions.transaction { findDraft(id) }

    suspend fun attachmentUses(id: DraftId): List<AttachmentUse> = transactions.transaction {
        val reference = draftObjectMapper.writeValueAsString(mapOf("attachments" to listOf(mapOf("id" to id.value.toString()))))
        DraftTable.select(DraftTable.id, DraftTable.metadata, DraftTable.document)
            .where { DraftTable.archivedAt.isNull() and DraftTable.document.contains(reference) }
            .map { AttachmentUse(it[DraftTable.id], it[DraftTable.metadata].name, it[DraftTable.document].attachments.first { copy -> copy.id.value == id.value.toString() }.revision) }
    }

    suspend fun create(draft: Draft, sourceKey: SourceId?, author: AuthorName): DraftRecord {
        try {
            return transactions.transaction {
                val newId = DraftId(UUID.randomUUID())
                val now = Instant.now()
                DraftTable.insert {
                    it[id] = newId
                    it[revision] = DraftRevision(1)
                    it[status] = DraftStatus.WORK
                    it[DraftTable.sourceKey] = sourceKey
                    it[document] = draft
                    it[metadata] = draft.metadata
                    it[createdAt] = now
                    it[updatedAt] = now
                    it[createdBy] = author
                    it[updatedBy] = author
                }
                requireNotNull(findDraft(newId))
            }
        } catch (exception: SQLException) {
            if (generateSequence<Throwable>(exception) { it.cause }.filterIsInstance<SQLException>().any { it.sqlState == "23505" }) throw SourceConflict()
            throw exception
        }
    }

    suspend fun update(id: DraftId, expectedRevision: DraftRevision, draft: Draft, author: AuthorName): DraftRecord =
        mutate(id, expectedRevision) { current ->
            if (sourceIdentityIssues(draft, current.draft).isNotEmpty()) throw SourceConflict()
            DraftTable.update({ DraftTable.id eq id }) {
                it[document] = draft
                it[metadata] = draft.metadata
                it[revision] = DraftRevision(current.revision.value + 1)
                it[updatedAt] = Instant.now()
                it[updatedBy] = author
            }
        }

    suspend fun status(id: DraftId, expectedRevision: DraftRevision, status: DraftStatus, author: AuthorName): DraftRecord =
        mutate(id, expectedRevision) { current ->
            DraftTable.update({ DraftTable.id eq id }) {
                it[DraftTable.status] = status
                it[revision] = DraftRevision(current.revision.value + 1)
                it[updatedAt] = Instant.now()
                it[updatedBy] = author
            }
        }

    suspend fun archive(id: DraftId, expectedRevision: DraftRevision, reason: ArchiveReason, author: AuthorName): DraftRecord =
        mutate(id, expectedRevision) { current ->
            val now = Instant.now()
            DraftTable.update({ DraftTable.id eq id }) {
                it[archivedAt] = now
                it[archiveReason] = reason
                it[revision] = DraftRevision(current.revision.value + 1)
                it[updatedAt] = now
                it[updatedBy] = author
            }
        }

    private suspend fun mutate(id: DraftId, expectedRevision: DraftRevision, operation: (DraftRecord) -> Unit): DraftRecord = transactions.transaction {
        val current = findDraft(id, lock = true) ?: throw MissingDraft()
        if (current.archivedAt != null) throw ArchivedDraft()
        if (expectedRevision != current.revision) throw RevisionConflict(current.revision)
        operation(current)
        requireNotNull(findDraft(id))
    }
}

internal fun org.jetbrains.exposed.v1.jdbc.JdbcTransaction.findDraft(id: DraftId, lock: Boolean = false): DraftRecord? {
        val query = DraftTable.selectAll().where { DraftTable.id eq id }
        if (lock) query.forUpdate(ForUpdateOption.ForUpdate)
        return query.singleOrNull()?.let(::record)
}

private fun record(row: ResultRow): DraftRecord = DraftRecord(
        id = row[DraftTable.id],
        revision = row[DraftTable.revision],
        status = row[DraftTable.status],
        sourceKey = row[DraftTable.sourceKey],
        draft = row[DraftTable.document],
        createdAt = row[DraftTable.createdAt],
        updatedAt = row[DraftTable.updatedAt],
        updatedBy = row[DraftTable.updatedBy],
        archivedAt = row[DraftTable.archivedAt],
        archiveReason = row[DraftTable.archiveReason],
)
