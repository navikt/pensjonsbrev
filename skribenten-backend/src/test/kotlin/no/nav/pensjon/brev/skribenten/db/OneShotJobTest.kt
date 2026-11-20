package no.nav.pensjon.brev.skribenten.db

import io.ktor.client.engine.cio.*
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.skribenten.SharedPostgres
import no.nav.pensjon.brev.skribenten.common.TekniskVersjon
import no.nav.pensjon.brev.skribenten.common.oneShotJobs
import no.nav.pensjon.brev.skribenten.services.NaisLeaderService
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.less
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.junit.jupiter.api.*
import java.util.concurrent.atomic.AtomicInteger

class OneShotJobTest {
    private val jobnameCounter = AtomicInteger(0)

    @BeforeAll
    fun initDb() {
        SharedPostgres.subscribeAndEnsureDatabaseInitialized(this)
    }

    @AfterAll
    fun kansellerDbAvhengighet() {
        SharedPostgres.cancelSubscription(this)
        engine.close()
    }

    // Util function to avoid silly name collisions in tests
    private fun jobName(name: String): String {
        return "${jobnameCounter.incrementAndGet()}-$name"
    }

    private val engine = CIO.create()

    @Test
    fun `will execute the job and record it as completed`() {
        var isExecuted = false
        val name = jobName("test-job")

        runBlocking {
            oneShotJobs(NaisLeaderService(null, engine)) {
                job(name) { tekniskVersjon ->
                    isExecuted = true
                    transaction {
                        val job = OneShotJobTable.selectAll().where { OneShotJobTable.id eq name }.single()
                        assertThat(job[OneShotJobTable.tekniskVersjon]).isEqualTo(tekniskVersjon)
                        assertThat(tekniskVersjon.value).isGreaterThan(1)
                        assertThat(job[OneShotJobTable.completedAt]).isNull()
                    }
                }
            }
        }

        assertThat(isExecuted).isTrue()
        transaction {
            val job = OneShotJobTable.selectAll().where { OneShotJobTable.id eq name }.singleOrNull()
            assertThat(job).isNotNull
            assertThat(job?.get(OneShotJobTable.completedAt)).isNotNull
        }
    }

    @Test
    fun `will not execute the job if it has already been executed`() {
        var executionCount = 0

        val name = jobName("no-duplicate-execution")
        fun aJob() {
            runBlocking {
                oneShotJobs(NaisLeaderService(null, engine)) {
                    job(name) {
                        executionCount++
                    }
                }
            }
        }

        aJob()
        assertThat(executionCount).isEqualTo(1)
        aJob()
        assertThat(executionCount).isEqualTo(1)
    }

    @Test
    fun `a failing job will not be recorded as completed`() {
        var isExecuted = false
        val name = jobName("failing-job")

        fun failingJob() = runBlocking {
            oneShotJobs(NaisLeaderService(null, engine)) {
                job(name) {
                    isExecuted = true
                    throw RuntimeException("Simulated failure")
                }
            }
        }

        runCatching { failingJob() }

        assertThat(isExecuted).isTrue()
        transaction {
            val job = OneShotJobTable.selectAll().where { OneShotJobTable.id eq name }.single()
            assertThat(job[OneShotJobTable.completedAt]).isNull()
        }
    }

    @Test
    fun `will not execute job if not the leader`() {
        val name = jobName("not-leader-job")
        var isExecuted = false

        runBlocking {
            oneShotJobs(FakeLeaderService(true, false)) {
                job(name) {
                    isExecuted = true
                }
            }
        }

        assertThat(isExecuted).isFalse()
        transaction {
            val job = OneShotJobTable.selectAll().where { OneShotJobTable.id eq name }.singleOrNull()
            assertThat(job).isNull()
        }
    }

    @Test
    fun `will execute job if current instance is the leader`() {
        val name = jobName("leader-job")
        var isExecuted = false

        runBlocking {
            oneShotJobs(FakeLeaderService(true, true)) {
                job(name) {
                    isExecuted = true
                }
            }
        }

        assertThat(isExecuted).isTrue()
        transaction {
            val job = OneShotJobTable.selectAll().where { OneShotJobTable.id eq name }.singleOrNull()
            assertThat(job).isNotNull
            assertThat(job?.get(OneShotJobTable.completedAt)).isNotNull
        }
    }

    @Test
    fun `new jobs receive increasing technical versions`() {
        val versions = mutableListOf<TekniskVersjon>()

        runBlocking {
            oneShotJobs(NaisLeaderService(null, engine)) {
                job(jobName("first-version")) { versions.add(it) }
                job(jobName("second-version")) { versions.add(it) }
            }
        }

        assertThat(versions).hasSize(2)
        assertThat(versions[1]).isGreaterThan(versions[0])
        transaction {
            val olderJobs = OneShotJobTable.selectAll()
                .where { OneShotJobTable.tekniskVersjon less versions[1] }
                .map { it[OneShotJobTable.tekniskVersjon] }
            assertThat(olderJobs).contains(versions[0]).doesNotContain(versions[1])
        }
    }

    @Test
    fun `incomplete job keeps its technical version when retried after another job`() {
        val name = jobName("incomplete-job")
        val versions = mutableListOf<TekniskVersjon>()
        val newerVersions = mutableListOf<TekniskVersjon>()

        runBlocking {
            oneShotJobs(NaisLeaderService(null, engine)) {
                job(name) {
                    versions.add(it)
                    completed = false
                }
                job(jobName("newer-job")) { newerVersions.add(it) }
            }
        }
        transaction {
            val job = OneShotJobTable.selectAll().where { OneShotJobTable.id eq name }.single()
            assertThat(job[OneShotJobTable.completedAt]).isNull()
        }

        runBlocking {
            oneShotJobs(NaisLeaderService(null, engine)) {
                job(name) { versions.add(it) }
            }
        }

        assertThat(versions).hasSize(2)
        assertThat(versions[1]).isEqualTo(versions[0])
        assertThat(newerVersions.single()).isGreaterThan(versions[0])
        transaction {
            val job = OneShotJobTable.selectAll().where { OneShotJobTable.id eq name }.single()
            assertThat(job[OneShotJobTable.completedAt]).isNotNull()
        }
    }

    @Test
    fun `failed job keeps its technical version when retried`() {
        val name = jobName("retry-failed-job")
        val versions = mutableListOf<TekniskVersjon>()

        runBlocking {
            runCatching {
                oneShotJobs(NaisLeaderService(null, engine)) {
                    job(name) {
                        versions.add(it)
                        throw RuntimeException("Simulated failure")
                    }
                }
            }
            oneShotJobs(NaisLeaderService(null, engine)) {
                job(name) { versions.add(it) }
            }
        }

        assertThat(versions).hasSize(2)
        assertThat(versions[1]).isEqualTo(versions[0])
        transaction {
            val job = OneShotJobTable.selectAll().where { OneShotJobTable.id eq name }.single()
            assertThat(job[OneShotJobTable.completedAt]).isNotNull()
        }
    }
}