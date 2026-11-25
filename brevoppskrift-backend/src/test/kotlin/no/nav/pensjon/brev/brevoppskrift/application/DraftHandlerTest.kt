package no.nav.pensjon.brev.brevoppskrift.application

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import no.nav.pensjon.brev.brevoppskrift.TestPostgres
import no.nav.pensjon.brev.brevoppskrift.IntegrationsConfig
import no.nav.pensjon.brev.brevoppskrift.MAX_DRAFT_BYTES
import no.nav.pensjon.brev.brevoppskrift.api.*
import no.nav.pensjon.brev.brevoppskrift.createMetrics
import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import no.nav.pensjon.brev.brevoppskrift.integrasjoner.*
import no.nav.pensjon.brev.brevoppskrift.katalog.*
import no.nav.pensjon.brev.brevoppskrift.kotlin.KotlinDraftGenerator
import no.nav.pensjon.brev.brevoppskrift.lagring.*
import no.nav.pensjon.brev.brevoppskrift.model.*
import no.nav.pensjon.brev.brevoppskrift.auth.AuthorName
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.v1.jdbc.Database
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class DraftHandlerTest {
    @Test
    fun `existing contextual catalogue check rejects locked import structure before saving`(): Unit = runBlocking {
        val source = SourceReference(SourceId("synthetic"), GitCommit("a".repeat(40)), mapOf(RepositoryPath("Synthetic.kt") to Sha256Digest("b".repeat(64))))
        val imported = exampleDraft().copy(
            source = source,
            outline = exampleDraft().outline + Block.LockedSource(BlockId("locked"), "phrase"),
        )
        val summary = TemplateSource(source.id, "Synthetic", "AUTOBREV", "UFOERE", listOf("NB", "NN", "EN"), source.commit, true, emptyList())
        val catalogue = BuildSourceCatalogue.fromResource(draftObjectMapper.writeValueAsBytes(
            CatalogueSnapshot(source.commit, listOf(CatalogueEntry(summary, source, imported, RepositoryPath("Synthetic.kt"), emptyMap()))),
        ).inputStream())
        withHandler(catalogue) { handler, repository ->
            val author = AuthorName("Synthetic author")
            val saved = handler.create(CreateDraftRequest(sourceId = source.id), author)
            for (altered in listOf(
                saved.draft.copy(outline = saved.draft.outline.dropLast(1)),
                saved.draft.copy(metadata = saved.draft.metadata.copy(name = "Renamed")),
            )) {
                assertThat(runCatching { handler.update(saved.id, UpdateDraftRequest(saved.revision, altered), author) }.exceptionOrNull())
                    .isInstanceOfSatisfying(InvalidDraft::class.java) { failure ->
                        assertThat(failure.issues).anyMatch { it.code == "LOCKED_SOURCE_CHANGED" }
                    }
                assertThat(repository.get(saved.id)).isEqualTo(saved)
            }
            val edited = saved.draft.copy(title = saved.draft.title.mapValues { "${it.value} [edited]" })
            assertThat(handler.update(saved.id, UpdateDraftRequest(saved.revision, edited), author).draft.title).isEqualTo(edited.title)
        }
    }

    @Test
    fun `ready drafts reject ungeneratable saves but work drafts can remain incomplete`(): Unit = runBlocking {
        withHandler { handler, repository ->
            val author = AuthorName("Synthetic author")
            val draft = exampleDraft().let { it.copy(metadata = it.metadata.copy(brevkode = "SYNTHETIC")) }
            val saved = handler.create(CreateDraftRequest(draft = draft), author)
            val ready = handler.status(saved.id, StatusRequest(saved.revision, DraftStatus.READY), author)
            val incomplete = ready.draft.copy(metadata = ready.draft.metadata.copy(brevkode = null))
            assertThat(runCatching { handler.update(ready.id, UpdateDraftRequest(ready.revision, incomplete), author) }.exceptionOrNull())
                .isInstanceOfSatisfying(InvalidDraft::class.java) { failure ->
                    assertThat(failure.issues).anyMatch { it.path == "metadata.brevkode" }
                }
            assertThat(repository.get(ready.id)).isEqualTo(ready)
            val edited = ready.draft.copy(title = ready.draft.title.mapValues { "${it.value} [edited]" })
            val updated = handler.update(ready.id, UpdateDraftRequest(ready.revision, edited), author)
            assertThat(updated.status).isEqualTo(DraftStatus.READY)
            val work = handler.status(updated.id, StatusRequest(updated.revision, DraftStatus.WORK), author)
            assertThat(handler.update(work.id, UpdateDraftRequest(work.revision, incomplete), author).status).isEqualTo(DraftStatus.WORK)
        }
    }

    @Test
    fun `approval rejects a baseline that makes the attachment depth invalid without saving`(): Unit = runBlocking {
        withHandler { handler, repository ->
            val author = AuthorName("Synthetic author")
            val attachment = exampleDraft().let { it.copy(metadata = it.metadata.copy(type = DocumentType.VEDLEGG)) }
            val nested = (1..4).fold(attachment) { child, index ->
                attachment.copy(attachments = listOf(AttachmentCopy(AttachmentReference("copy$index"), DraftRevision(1), child)))
            }
            val draft = exampleDraft().copy(attachments = listOf(AttachmentCopy(AttachmentReference("root-copy"), DraftRevision(1), nested)))
            assertThat(DraftValidator.validate(draft)).isEmpty()
            val saved = handler.create(CreateDraftRequest(draft = draft), author)
            assertThat(runCatching { handler.approve(saved.id, RevisionRequest(saved.revision), author) }.exceptionOrNull())
                .isInstanceOfSatisfying(InvalidDraft::class.java) { failure ->
                    assertThat(failure.issues).anyMatch { it.path.startsWith("baseline.draft") && it.code == "MAX_DEPTH" }
                }
            assertThat(repository.get(saved.id)).isEqualTo(saved)
        }
    }

    @Test
    fun `approval rejects a baseline exceeding the update payload limit and accepted approvals remain saveable`(): Unit = runBlocking {
        withHandler { handler, repository ->
            val author = AuthorName("Synthetic author")
            val draft = exampleDraft().copy(outline = listOf(Block.Paragraph(BlockId("large"), listOf(
                Inline.Text(translations + (DraftLanguage.EN to "x".repeat(3 * 1024 * 1024))),
            ))))
            assertThat(draftObjectMapper.writeValueAsBytes(CreateDraftRequest(draft = draft)).size).isLessThan(MAX_DRAFT_BYTES)
            val saved = handler.create(CreateDraftRequest(draft = draft), author)
            assertThat(runCatching { handler.approve(saved.id, RevisionRequest(saved.revision), author) }.exceptionOrNull())
                .isInstanceOfSatisfying(ApiFailure::class.java) { failure ->
                    assertThat(failure.status.value).isEqualTo(413)
                    assertThat(failure.code).isEqualTo("DRAFT_TOO_LARGE")
                }
            assertThat(repository.get(saved.id)).isEqualTo(saved)
            val small = handler.create(CreateDraftRequest(draft = exampleDraft()), author)
            val approved = handler.approve(small.id, RevisionRequest(small.revision), author)
            val request = UpdateDraftRequest(approved.revision, approved.draft)
            assertThat(DraftValidator.validate(approved.draft)).isEmpty()
            assertThat(draftObjectMapper.writeValueAsBytes(request).size).isLessThanOrEqualTo(MAX_DRAFT_BYTES)
            assertThat(handler.update(approved.id, request, author).draft.baseline).isEqualTo(approved.draft.baseline)
        }
    }

    private suspend fun withHandler(
        catalogue: BuildSourceCatalogue = BuildSourceCatalogue(),
        body: suspend (DraftHandler, DraftRepository) -> Unit,
    ) {
        openDatabase(postgres.database()).use { dataSource ->
            val repository = DraftRepository(Transactional(Database.connect(dataSource)))
            val metrics = createMetrics()
            try {
                DraftNotifications(null, IntegrationsConfig(WorkshopUrl("https://workshop.invalid"))).use { notifications ->
                    body(DraftHandler(repository, KotlinDraftGenerator(catalogue), catalogue, notifications, metrics), repository)
                }
            } finally {
                metrics.close()
            }
        }
    }

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
                DraftNotifications(null, IntegrationsConfig(WorkshopUrl("https://workshop.invalid"))).use { notifications ->
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
                DraftNotifications(notifications, IntegrationsConfig(WorkshopUrl("https://workshop.invalid"))).use { sender ->
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
