package no.nav.pensjon.brev.brevoppskrift.integrasjoner

import kotlinx.coroutines.*
import no.nav.pensjon.brev.brevoppskrift.TestPostgres
import no.nav.pensjon.brev.brevoppskrift.IntegrationsConfig
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
import java.time.Clock
import java.time.Duration
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
            val notificationStarted = CompletableDeferred<Notification>()
            val notificationStopped = CompletableDeferred<Unit>()
            val notifications = DraftNotifications(object : NotificationService {
                override suspend fun send(notification: Notification) {
                    notificationStarted.complete(notification)
                    try {
                        awaitCancellation()
                    } finally {
                        notificationStopped.complete(Unit)
                    }
                }
            }, IntegrationsConfig(WorkshopUrl("https://workshop.invalid")))
            val metrics = createMetrics()
            val worker = PrJobWorker(service, jobs, metrics)
            try {
                notifications.notify("Nytt utkast", ready)
                worker.start()
                worker.start()
                val ticket = withTimeout(5000) { entered.await() }
                assertThat(withTimeout(5000) { notificationStarted.await() }.url).isEqualTo("https://workshop.invalid/arbeidsutkast/${ready.id.value}")
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
            } finally {
                notifications.close()
                worker.close()
                metrics.close()
            }
            assertThat(worker.isActive).isFalse()
            assertThat(notifications.isActive).isFalse()
        }
    }

    @Test
    fun `shutdown cancels transport without failing its job and restart recovers the snapshot`(): Unit = runBlocking {
        openDatabase(postgres.database()).use { dataSource ->
            val transactions = Transactional(Database.connect(dataSource))
            val drafts = DraftRepository(transactions)
            val clock = Clock.systemUTC()
            val jobs = PrJobRepository(transactions, clock)
            val saved = drafts.create(exampleDraft(), null, AuthorName("Synthetic author"))
            val ready = drafts.status(saved.id, saved.revision, DraftStatus.READY, AuthorName("Synthetic author"))
            val queued = jobs.create(ready.id, ready.revision)
            val entered = CompletableDeferred<PrTicket>()
            val cancelled = CompletableDeferred<Unit>()
            val blocked = object : PrService {
                override suspend fun create(ticket: PrTicket): PrUrl {
                    entered.complete(ticket)
                    try {
                        awaitCancellation()
                    } finally {
                        cancelled.complete(Unit)
                    }
                }
            }
            val metrics = createMetrics()
            try {
                PrJobWorker(blocked, jobs, metrics).use { worker ->
                    worker.start()
                    withTimeout(5000) { entered.await() }
                }
                withTimeout(5000) { cancelled.await() }
                assertThat(jobs.latest(ready.id)?.status).isEqualTo(PrStatus.RUNNING)
                val resumed = CompletableDeferred<PrTicket>()
                val service = object : PrService {
                    override suspend fun create(ticket: PrTicket): PrUrl {
                        resumed.complete(ticket)
                        return PrUrl("https://github.com/navikt/pensjonsbrev/pull/1")
                    }
                }
                val recovered = PrJobRepository(transactions, Clock.offset(clock, Duration.ofMinutes(4)))
                PrJobWorker(service, recovered, metrics).use { worker ->
                    worker.start()
                    val ticket = withTimeout(5000) { resumed.await() }
                    assertThat(ticket.id).isEqualTo(queued.id)
                    assertThat(ticket.draft).isEqualTo(ready.draft)
                    assertThat(ticket.attempts).isEqualTo(2)
                    withTimeout(5000) { while (jobs.latest(ready.id)?.status != PrStatus.SUCCEEDED) delay(10) }
                }
            } finally {
                metrics.close()
            }
        }
    }

    @Test
    fun `unexpected transport errors are stored as safe codes without their details`(): Unit = runBlocking {
        openDatabase(postgres.database()).use { dataSource ->
            val transactions = Transactional(Database.connect(dataSource))
            val drafts = DraftRepository(transactions)
            val jobs = PrJobRepository(transactions)
            val saved = drafts.create(exampleDraft(), null, AuthorName("Synthetic author"))
            val ready = drafts.status(saved.id, saved.revision, DraftStatus.READY, AuthorName("Synthetic author"))
            jobs.create(ready.id, ready.revision)
            val service = object : PrService {
                override suspend fun create(ticket: PrTicket): PrUrl = throw IllegalStateException("Synthetic private transport details")
            }
            val metrics = createMetrics()
            try {
                PrJobWorker(service, jobs, metrics).use { worker ->
                    worker.start()
                    withTimeout(5000) { while (jobs.latest(ready.id)?.status != PrStatus.FAILED) delay(10) }
                    assertThat(jobs.latest(ready.id)?.error).isEqualTo("PR_FAILED")
                }
            } finally {
                metrics.close()
            }
        }
    }

    @Test
    fun `an update during generation cannot enqueue the unvalidated revision`(): Unit = runBlocking {
        openDatabase(postgres.database()).use { dataSource ->
            val transactions = Transactional(Database.connect(dataSource))
            val drafts = DraftRepository(transactions)
            val jobs = PrJobRepository(transactions)
            val author = AuthorName("Synthetic author")
            val saved = drafts.create(exampleDraft(), null, author)
            val ready = drafts.status(saved.id, saved.revision, DraftStatus.READY, author)
            val generator = object : DraftGenerator {
                override suspend fun generate(draft: Draft): GenerationResult {
                    drafts.update(ready.id, ready.revision, draft.copy(title = translations.mapValues { "${it.value} [edited]" }), author)
                    return GenerationResult(emptyList(), emptyList())
                }
            }
            val service = object : PrService {
                override suspend fun create(ticket: PrTicket): PrUrl = error("Submission does not invoke transport")
            }
            val handler = PrJobHandler(drafts, generator, jobs, service)
            assertThat(runCatching { handler.submit(ready.id, RevisionRequest(ready.revision)) }.exceptionOrNull()).isInstanceOf(RevisionConflict::class.java)
            assertThat(jobs.latest(ready.id)).isNull()
        }
    }

    companion object {
        @JvmField
        @RegisterExtension
        val postgres = TestPostgres()
    }
}
