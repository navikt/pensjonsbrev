package no.nav.pensjon.brev.brevoppskrift.integrasjoner

import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import no.nav.pensjon.brev.brevoppskrift.lagring.*
import no.nav.pensjon.brev.brevoppskrift.model.Draft
import no.nav.pensjon.brev.brevoppskrift.model.DraftId
import no.nav.pensjon.brev.brevoppskrift.model.DraftRevision
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.core.java.javaUUID
import org.jetbrains.exposed.v1.core.vendors.ForUpdateOption
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.javatime.timestamp
import org.jetbrains.exposed.v1.json.jsonb
import java.time.Instant
import java.time.Clock
import java.util.UUID

enum class PrStatus { PENDING, RUNNING, SUCCEEDED, FAILED }
data class PrJob(val id: PrJobId, val status: PrStatus, val prUrl: PrUrl? = null, val error: String? = null)
data class PrTicket(val id: PrJobId, val draftId: DraftId, val revision: DraftRevision, val draft: Draft, val lease: PrLeaseToken, val attempts: Int)

private object PrJobTable : Table("pr_job") {
    val id = javaUUID("id").transform(::PrJobId, PrJobId::value)
    val draftId = javaUUID("draft_id").transform(::DraftId, DraftId::value).references(DraftTable.id)
    val revision = long("revision").transform(::DraftRevision, DraftRevision::value)
    val document = jsonb<Draft>("document", serialize = draftObjectMapper::writeValueAsString, deserialize = { draftObjectMapper.readValue(it, Draft::class.java) })
    val status = enumerationByName<PrStatus>("status", 16)
    val attempts = integer("attempts")
    val leaseToken = javaUUID("lease_token").transform(::PrLeaseToken, PrLeaseToken::value).nullable()
    val leaseUntil = timestamp("lease_until").nullable()
    val prUrl = text("pr_url").transform(::PrUrl, PrUrl::value).nullable()
    val errorCode = text("error_code").nullable()
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
    override val primaryKey = PrimaryKey(id)
}

class PrJobRepository(private val transactions: Transactional, private val clock: Clock = Clock.systemUTC()) {
    suspend fun latest(draftId: DraftId): PrJob? = transactions.transaction {
        PrJobTable.selectAll().where { PrJobTable.draftId eq draftId }.orderBy(PrJobTable.createdAt, SortOrder.DESC).limit(1).singleOrNull()?.let(::result)
    }

    suspend fun create(draftId: DraftId, revision: DraftRevision): PrJob = transactions.transaction {
        val current = findDraft(draftId, lock = true) ?: throw MissingDraft()
        if (current.archivedAt != null) throw ArchivedDraft()
        if (current.revision != revision) throw RevisionConflict(current.revision)
        require(current.status == DraftStatus.READY) { "DRAFT_NOT_READY" }
        val existing = PrJobTable.selectAll().where { (PrJobTable.draftId eq draftId) and (PrJobTable.revision eq revision) }.singleOrNull()
        if (existing != null) return@transaction result(existing)
        val id = PrJobId(UUID.randomUUID())
        val now = Instant.now(clock)
        PrJobTable.insert {
            it[PrJobTable.id] = id
            it[PrJobTable.draftId] = draftId
            it[PrJobTable.revision] = revision
            it[document] = current.draft
            it[status] = PrStatus.PENDING
            it[attempts] = 0
            it[createdAt] = now
            it[updatedAt] = now
        }
        PrJob(id, PrStatus.PENDING)
    }

    suspend fun claim(): PrTicket? = transactions.transaction {
        val now = Instant.now(clock)
        PrJobTable.update({ (PrJobTable.status eq PrStatus.RUNNING) and (PrJobTable.leaseUntil less now) and (PrJobTable.attempts greaterEq 3) }) {
            it[status] = PrStatus.FAILED
            it[errorCode] = "ATTEMPTS_EXHAUSTED"
        }
        val row = PrJobTable.selectAll()
            .where { (PrJobTable.attempts less 3) and (((PrJobTable.status eq PrStatus.PENDING) and (PrJobTable.leaseUntil.isNull() or (PrJobTable.leaseUntil less now))) or ((PrJobTable.status eq PrStatus.RUNNING) and (PrJobTable.leaseUntil less now))) }
            .orderBy(PrJobTable.createdAt).limit(1).forUpdate(ForUpdateOption.ForUpdate).singleOrNull() ?: return@transaction null
        val lease = PrLeaseToken(UUID.randomUUID())
        PrJobTable.update({ PrJobTable.id eq row[PrJobTable.id] }) {
            it[status] = PrStatus.RUNNING
            it[leaseToken] = lease
            it[leaseUntil] = now.plusSeconds(180)
            it[attempts] = row[PrJobTable.attempts] + 1
            it[updatedAt] = now
        }
        PrTicket(row[PrJobTable.id], row[PrJobTable.draftId], row[PrJobTable.revision], row[PrJobTable.document], lease, row[PrJobTable.attempts] + 1)
    }

    suspend fun finish(ticket: PrTicket, url: PrUrl?, error: String?) = transactions.transaction {
        val retry = url == null && ticket.attempts < 3 && (error in setOf("GITHUB_429", "GITHUB_TIMEOUT", "GITHUB_UNAVAILABLE") || error?.startsWith("GITHUB_5") == true)
        PrJobTable.update({ (PrJobTable.id eq ticket.id) and (PrJobTable.leaseToken eq ticket.lease) and (PrJobTable.status eq PrStatus.RUNNING) }) {
            it[status] = if (url != null) PrStatus.SUCCEEDED else if (retry) PrStatus.PENDING else PrStatus.FAILED
            it[prUrl] = url
            it[errorCode] = error
            it[leaseToken] = null
            it[leaseUntil] = if (retry) Instant.now(clock).plusSeconds(30) else null
            it[updatedAt] = Instant.now(clock)
        }
    }

    private fun result(row: ResultRow) = PrJob(row[PrJobTable.id], row[PrJobTable.status], row[PrJobTable.prUrl], row[PrJobTable.errorCode])
}
