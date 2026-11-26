package no.nav.pensjon.brev.brevoppskrift.lagring

import com.zaxxer.hikari.HikariDataSource
import no.nav.pensjon.brev.brevoppskrift.model.exampleDraft
import no.nav.pensjon.brev.brevoppskrift.model.AttachmentCopy
import no.nav.pensjon.brev.brevoppskrift.model.DraftRevision
import no.nav.pensjon.brev.brevoppskrift.model.SourceId
import no.nav.pensjon.brev.brevoppskrift.model.AttachmentReference
import no.nav.pensjon.brev.brevoppskrift.auth.AuthorName
import no.nav.pensjon.brev.brevoppskrift.model.DraftId
import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import no.nav.pensjon.brev.brevoppskrift.TestPostgres
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.v1.jdbc.Database
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import java.util.UUID
import java.util.concurrent.Callable
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Execution(ExecutionMode.SAME_THREAD)
class DraftRepositoryTest {
    @Test
    fun `existing primitive PostgreSQL row and JSONB load through semantic columns`(): Unit = runBlocking {
        val json = requireNotNull(javaClass.getResourceAsStream("/semantic-draft-v1.json")).use { draftObjectMapper.readTree(it) }
        val document = draftObjectMapper.writeValueAsString(json["draft"]).replace("'", "''")
        val metadata = draftObjectMapper.writeValueAsString(json["draft"]["metadata"]).replace("'", "''")
        transactions.transaction {
            exec("""
                INSERT INTO draft (id, revision, status, source_key, document, metadata, created_at, updated_at, created_by, updated_by)
                VALUES ('00000000-0000-0000-0000-000000000001', 2, 'WORK', 'synthetic-source',
                        '$document'::jsonb, '$metadata'::jsonb, '2026-01-01T00:00:00Z', '2026-01-01T00:00:00Z', 'Synthetic author', 'Synthetic author')
            """.trimIndent())
        }
        val stored = requireNotNull(repository.get(DraftId(UUID.fromString("00000000-0000-0000-0000-000000000001"))))
        assertThat(draftObjectMapper.readTree(draftObjectMapper.writeValueAsBytes(stored))).isEqualTo(json)
    }

    @Test
    fun `JSONB round trips and stale saves do not overwrite stored edits`(): Unit = runBlocking {
        val created = repository.create(exampleDraft(), null, AuthorName("Synthetic author"))
        val changed = created.draft.copy(metadata = created.draft.metadata.copy(name = "UpdatedName"))
        val updated = repository.update(created.id, DraftRevision(1), changed, AuthorName("Synthetic editor"))
        assertThat(updated.revision.value).isEqualTo(2)
        assertThat(repository.get(created.id)?.draft).isEqualTo(changed)
        assertThat(runCatching { repository.update(created.id, DraftRevision(1), created.draft, AuthorName("Synthetic author")) }.exceptionOrNull()).isInstanceOf(RevisionConflict::class.java)
        assertThat(repository.get(created.id)?.draft).isEqualTo(changed)
        assertThat(repository.list().single { it.id == created.id }.metadata).isEqualTo(changed.metadata)
    }

    @Test
    fun `concurrent saves admit exactly one revision`(): Unit = runBlocking {
        val created = repository.create(exampleDraft(), null, AuthorName("Synthetic author"))
        val results = concurrently(
            { repository.update(created.id, DraftRevision(1), created.draft, AuthorName("Synthetic editor one")) },
            { repository.update(created.id, DraftRevision(1), created.draft, AuthorName("Synthetic editor two")) },
        )
        assertThat(results.count { it.isSuccess }).isEqualTo(1)
        assertThat(results.single { it.isFailure }.exceptionOrNull()).isInstanceOf(RevisionConflict::class.java)
        assertThat(repository.get(created.id)?.revision?.value).isEqualTo(2)
    }

    @Test
    fun `concurrent first imports are unique and archive frees the source`(): Unit = runBlocking {
        val source = SourceId(UUID.randomUUID().toString())
        val results = concurrently(
            { repository.create(exampleDraft(), source, AuthorName("Synthetic editor one")) },
            { repository.create(exampleDraft(), source, AuthorName("Synthetic editor two")) },
        )
        assertThat(results.count { it.isSuccess }).isEqualTo(1)
        assertThat(results.single { it.isFailure }.exceptionOrNull()).isInstanceOf(SourceConflict::class.java)
        val created = results.single { it.isSuccess }.getOrThrow()
        val archived = repository.archive(created.id, DraftRevision(1), ArchiveReason.RESET, AuthorName("Synthetic editor"))
        assertThat(archived.archivedAt).isNotNull()
        assertThat(repository.list().map { it.id }).doesNotContain(created.id)
        assertThat(runCatching { repository.update(created.id, DraftRevision(2), created.draft, AuthorName("Synthetic editor")) }.exceptionOrNull()).isInstanceOf(ArchivedDraft::class.java)
        assertThat(repository.create(exampleDraft(), source, AuthorName("Synthetic editor")).sourceKey).isEqualTo(source)
    }

    @Test
    fun `status changes increment revisions and reject stale or archived writes`(): Unit = runBlocking {
        val created = repository.create(exampleDraft(), null, AuthorName("Synthetic author"))
        val ready = repository.status(created.id, created.revision, DraftStatus.READY, AuthorName("Synthetic editor"))
        assertThat(ready.status).isEqualTo(DraftStatus.READY)
        assertThat(ready.revision).isEqualTo(DraftRevision(2))
        assertThat(ready.updatedBy).isEqualTo(AuthorName("Synthetic editor"))
        assertThat(ready.draft).isEqualTo(created.draft)
        val stale = runCatching { repository.status(created.id, created.revision, DraftStatus.WORK, AuthorName("Synthetic author")) }.exceptionOrNull()
        assertThat(stale).isInstanceOf(RevisionConflict::class.java)
        assertThat((stale as RevisionConflict).currentRevision).isEqualTo(ready.revision)
        assertThat(repository.get(created.id)?.status).isEqualTo(DraftStatus.READY)
        repository.archive(ready.id, ready.revision, ArchiveReason.DONE, AuthorName("Synthetic editor"))
        assertThat(runCatching { repository.status(ready.id, DraftRevision(3), DraftStatus.WORK, AuthorName("Synthetic author")) }.exceptionOrNull()).isInstanceOf(ArchivedDraft::class.java)
    }

    @Test
    fun `attachment usage is queried from JSONB and excludes archived letters`(): Unit = runBlocking {
        val initial = repository.create(exampleDraft(), null, AuthorName("Synthetic author"))
        val attachment = repository.update(initial.id, DraftRevision(1), initial.draft, AuthorName("Synthetic author"))
        val letter = repository.create(exampleDraft().copy(attachments = listOf(AttachmentCopy(AttachmentReference(attachment.id.value.toString()), attachment.revision, attachment.draft))), null, AuthorName("Synthetic author"))
        assertThat(repository.attachmentUses(attachment.id).map { it.id }).containsExactly(letter.id)
        assertThat(repository.attachmentUses(attachment.id).single().revision).isEqualTo(attachment.revision)
        repository.archive(letter.id, DraftRevision(1), ArchiveReason.DONE, AuthorName("Synthetic author"))
        assertThat(repository.attachmentUses(attachment.id)).isEmpty()
    }

    @Test
    fun `transaction failures roll back and execute a write only once`(): Unit = runBlocking {
        val created = repository.create(exampleDraft(), null, AuthorName("Synthetic author"))
        var attempts = 0
        val failure = runCatching {
            transactions.transaction {
                attempts++
                exec("UPDATE draft SET revision = 99 WHERE id = '${created.id}'")
                throw java.sql.SQLException("synthetic rollback", "40001")
            }
        }.exceptionOrNull()
        assertThat(failure).isInstanceOf(java.sql.SQLException::class.java)
        assertThat(attempts).isEqualTo(1)
        assertThat(repository.get(created.id)?.revision?.value).isEqualTo(1)
    }

    private fun <T> concurrently(first: suspend () -> T, second: suspend () -> T): List<Result<T>> {
        val latch = CountDownLatch(1)
        return Executors.newFixedThreadPool(2).use { executor ->
            val results = listOf(first, second).map { operation -> executor.submit(Callable { latch.await(); runCatching { runBlocking { operation() } } }) }
            latch.countDown()
            results.map { it.get(20, TimeUnit.SECONDS) }
        }
    }

    companion object {
        private val postgres = TestPostgres.standalone()
        private lateinit var source: HikariDataSource
        private lateinit var repository: DraftRepository
        private lateinit var transactions: Transactional

        @JvmStatic
        @BeforeAll
        fun start() {
            postgres.start()
            source = openDatabase(DatabaseConfig(postgres.jdbcUrl, postgres.username, postgres.password))
            transactions = Transactional(Database.connect(source))
            repository = DraftRepository(transactions)
        }

        @JvmStatic
        @AfterAll
        fun stop() {
            if (::source.isInitialized) source.close()
            postgres.stop()
        }
    }
}
