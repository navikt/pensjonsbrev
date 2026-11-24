package no.nav.pensjon.brev.brevoppskrift.application

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import no.nav.pensjon.brev.brevoppskrift.TestPostgres
import no.nav.pensjon.brev.brevoppskrift.api.*
import no.nav.pensjon.brev.brevoppskrift.createMetrics
import no.nav.pensjon.brev.brevoppskrift.integrasjoner.*
import no.nav.pensjon.brev.brevoppskrift.lagring.*
import no.nav.pensjon.brev.brevoppskrift.model.*
import no.nav.pensjon.brev.brevoppskrift.auth.AuthorName
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.v1.jdbc.Database
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class DraftHandlerTest {
    @Test
    fun `attachment source changes are validation issues and leave stored revision unchanged`(): Unit = runBlocking {
        openDatabase(postgres.database()).use { dataSource ->
            val repository = DraftRepository(Transactional(Database.connect(dataSource)))
            val catalogue = object : SourceCatalogue {
                override fun list() = emptyList<TemplateSource>()
                override fun import(sourceId: SourceId): Draft? = null
                override suspend fun checkSource(draft: Draft) = emptyList<ValidationIssue>()
            }
            val imported = exampleDraft().let { draft ->
                draft.copy(
                    metadata = draft.metadata.copy(type = DocumentType.VEDLEGG),
                    source = SourceReference(SourceId("attachment"), GitCommit("a".repeat(40)), mapOf(RepositoryPath("ufoere/maler/Synthetic.kt") to Sha256Digest("b".repeat(64)))),
                )
            }
            val attachment = AttachmentCopy(AttachmentReference("source-copy"), DraftRevision(1), imported)
            val author = AuthorName("Synthetic author")
            val saved = repository.create(exampleDraft().copy(attachments = listOf(attachment)), null, author)
            val generator = object : DraftGenerator {
                override suspend fun generate(draft: Draft) = GenerationResult(emptyList(), emptyList())
            }
            val metrics = createMetrics()
            try {
                DraftNotifications(null, WorkshopUrl("https://workshop.invalid")).use { notifications ->
                    val handler = DraftHandler(repository, generator, catalogue, notifications, metrics)
                    for (changed in listOf(
                        imported.copy(source = null),
                        imported.copy(source = requireNotNull(imported.source).copy(commit = GitCommit("c".repeat(40)))),
                    )) {
                        val altered = saved.draft.copy(attachments = listOf(attachment.copy(draft = changed)))
                        assertThat(runCatching { handler.update(saved.id, UpdateDraftRequest(saved.revision, altered), author) }.exceptionOrNull())
                            .isInstanceOfSatisfying(InvalidDraft::class.java) { failure ->
                                assertThat(failure.issues).anyMatch { it.path == "attachments[0].draft.source" && it.code == "IMMUTABLE_SOURCE" }
                            }
                        assertThat(repository.get(saved.id)).isEqualTo(saved)
                    }
                    val edited = attachment.copy(draft = imported.copy(title = imported.title.mapValues { "${it.value} [edited]" }))
                    val updated = handler.update(saved.id, UpdateDraftRequest(saved.revision, saved.draft.copy(attachments = listOf(edited))), author)
                    assertThat(updated.revision.value).isEqualTo(saved.revision.value + 1)
                    assertThat(updated.draft.attachments.single().draft.title).isEqualTo(edited.draft.title)
                }
            } finally {
                metrics.close()
            }
        }
    }

    @Test
    fun `notification observes committed draft and its failure cannot undo the save`(): Unit = runBlocking {
        openDatabase(postgres.database()).use { dataSource ->
            val repository = DraftRepository(Transactional(Database.connect(dataSource)))
            val observed = CompletableDeferred<Int>()
            val notifications = object : NotificationService {
                override suspend fun send(notification: Notification) {
                    observed.complete(repository.list().size)
                    throw IllegalStateException("synthetic notification failure")
                }
            }
            val generator = object : DraftGenerator {
                override suspend fun generate(draft: Draft) = GenerationResult(emptyList(), emptyList())
            }
            val catalogue = object : SourceCatalogue {
                override fun list() = emptyList<TemplateSource>()
                override fun import(sourceId: SourceId): Draft? = null
                override suspend fun checkSource(draft: Draft) = emptyList<ValidationIssue>()
            }
            val metrics = createMetrics()
            try {
                DraftNotifications(notifications, WorkshopUrl("https://workshop.invalid")).use { sender ->
                    val handler = DraftHandler(repository, generator, catalogue, sender, metrics)
                    val record = handler.create(CreateDraftRequest(draft = exampleDraft()), AuthorName("Synthetic author"))
                    assertThat(withTimeout(5000) { observed.await() }).isEqualTo(1)
                    assertThat(repository.get(record.id)?.revision?.value).isEqualTo(1)
                    assertThat(metrics.get("brevoppskrift.draft.created").counter().count()).isEqualTo(1.0)
                }
            } finally {
                metrics.close()
            }
        }
    }

    companion object {
        @JvmField
        @RegisterExtension
        val postgres = TestPostgres()
    }
}
