package no.nav.pensjon.brev.brevoppskrift.integrasjoner

import kotlinx.coroutines.*
import no.nav.pensjon.brev.brevoppskrift.TestPostgres
import no.nav.pensjon.brev.brevoppskrift.api.*
import no.nav.pensjon.brev.brevoppskrift.application.PrJobHandler
import no.nav.pensjon.brev.brevoppskrift.createMetrics
import no.nav.pensjon.brev.brevoppskrift.lagring.*
import no.nav.pensjon.brev.brevoppskrift.model.*
import no.nav.pensjon.brev.brevoppskrift.auth.AuthorName
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.v1.jdbc.Database
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.util.concurrent.atomic.AtomicInteger

class PrJobWorkerTest {
    @Test
    fun `notification shutdown is independent of PR processing and snapshot submission`(): Unit = runBlocking {
        openDatabase(postgres.database()).use { dataSource ->
            val transactions = Transactional(Database.connect(dataSource))
            val drafts = DraftRepository(transactions)
            val jobs = PrJobRepository(transactions)
            val created = drafts.create(exampleDraft(), null, AuthorName("Synthetic author"))
            val entered = CompletableDeferred<PrTicket>()
            val release = CompletableDeferred<Unit>()
            val requests = AtomicInteger()
            val service = object : PrService {
                override suspend fun create(ticket: PrTicket): PrUrl {
                    requests.incrementAndGet()
                    entered.complete(ticket)
                    release.await()
                    return PrUrl("https://github.com/navikt/pensjonsbrev/pull/1")
                }
            }
            val generator = object : DraftGenerator {
                override suspend fun generate(draft: Draft) = GenerationResult(emptyList(), emptyList())
            }
            val handler = PrJobHandler(drafts, generator, jobs, service)
            assertThat(runCatching { handler.submit(created.id, RevisionRequest(DraftRevision(1))) }.exceptionOrNull()).isInstanceOf(ApiFailure::class.java)
            val ready = drafts.status(created.id, DraftRevision(1), DraftStatus.READY, AuthorName("Synthetic author"))
            val queued = handler.submit(ready.id, RevisionRequest(ready.revision))
            assertThat(handler.submit(ready.id, RevisionRequest(ready.revision))).isEqualTo(queued)
            val notificationStarted = CompletableDeferred<Unit>()
            val notificationStopped = CompletableDeferred<Unit>()
            val notifications = DraftNotifications(object : NotificationService {
                override suspend fun send(notification: Notification) {
                    notificationStarted.complete(Unit)
                    try {
                        awaitCancellation()
                    } finally {
                        notificationStopped.complete(Unit)
                    }
                }
            }, WorkshopUrl("https://workshop.invalid"))
            val metrics = createMetrics()
            val worker = PrJobWorker(service, jobs, metrics)
            try {
                notifications.notify("Nytt utkast", ready)
                worker.start()
                worker.start()
                val ticket = withTimeout(5000) { entered.await() }
                withTimeout(5000) { notificationStarted.await() }
                notifications.close()
                withTimeout(5000) { notificationStopped.await() }
                assertThat(worker.isActive).isTrue()
                assertThat(ticket.draft).isEqualTo(ready.draft)
                release.complete(Unit)
                withTimeout(10_000) {
                    while (handler.latest(ready.id).status != PrStatus.SUCCEEDED ||
                        metrics.find("brevoppskrift.pr.jobs").tag("status", "succeeded").counter()?.count() != 1.0
                    ) {
                        delay(10)
                    }
                }
                assertThat(requests.get()).isEqualTo(1)
                assertThat(metrics.get("brevoppskrift.pr.jobs").tag("status", "succeeded").counter().count()).isEqualTo(1.0)
            } finally {
                notifications.close()
                worker.close()
                metrics.close()
            }
            assertThat(worker.isActive).isFalse()
            assertThat(notifications.isActive).isFalse()
        }
    }

    companion object {
        @JvmField
        @RegisterExtension
        val postgres = TestPostgres()
    }
}
