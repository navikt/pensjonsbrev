package no.nav.pensjon.brev.skribenten.db

import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.skribenten.oneShotJobs
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import org.testcontainers.postgresql.PostgreSQLContainer
import java.util.concurrent.atomic.AtomicInteger

class OneShotJobTest {
    private val postgres = PostgreSQLContainer("postgres:17-alpine")
    private val config = ConfigFactory.parseMap(mapOf("services.leader.url" to null))
    private val jobnameCounter = AtomicInteger(0)

    init {
        postgres.start()
        Database.connect(
            HikariDataSource(HikariConfig().apply {
                this.jdbcUrl = postgres.jdbcUrl
                this.username = postgres.username
                this.password = postgres.password
                this.initializationFailTimeout = 6000
                maximumPoolSize = 2
                validate()
            })
        )
        transaction {
            SchemaUtils.create(OneShotJobTable)
        }
    }

    // Util function to avoid silly name collisions in tests
    private fun jobName(name: String): String = "${jobnameCounter.incrementAndGet()}-$name"

    @Test
    fun `will execute the job and record it as completed`() {
        var isExecuted = false
        val name = jobName("test-job")

        runBlocking {
            oneShotJobs(config) {
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
    fun `will not execute the job if it has already been executed`() {
        var executionCount = 0

        val name = jobName("no-duplicate-execution")
        fun aJob() {
            runBlocking {
                oneShotJobs(config) {
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
            oneShotJobs(config) {
                job(name) {
                    isExecuted = true
                    throw RuntimeException("Simulated failure")
                }
            }
        }

        runCatching { failingJob() }

        assertThat(isExecuted).isTrue()
        // Check that the job was not recorded as completed
        transaction {
            val job = OneShotJobTable.selectAll().where { OneShotJobTable.id eq name }.singleOrNull()
            assertThat(job).isNull()
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
}