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
