package no.nav.pensjon.brev.skribenten.common

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringHandlerTestBase
import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
import no.nav.pensjon.brev.skribenten.model.SaksbehandlervalgMap
import no.nav.pensjon.brev.skribenten.model.SaksbehandlervalgVerdi
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.vendors.ForUpdateOption
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.Instant
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class UpdateBrevredigeringJsonEncryptedValuesTest : BrevredigeringHandlerTestBase() {

    @ParameterizedTest
    @ValueSource(strings = ["unreserved", "expired", "active"])
    fun `backfill includes unreserved and expired letters but skips active reservations`(reservation: String): Unit = runBlocking {
        val valg = SaksbehandlervalgMap().apply { put("valg1", SaksbehandlervalgVerdi.Boolean(true)) }
        val brev = opprettBrev(saksbehandlerValg = valg).resultOrFail()
        val sistReservert = when (reservation) {
            "unreserved" -> null
            "expired" -> Instant.now().minusSeconds(16 * 60)
            "active" -> Instant.now()
            else -> error("Unknown reservation: $reservation")
        }
        transaction {
            BrevredigeringTable.update({ BrevredigeringTable.id eq brev.info.id }) {
                it[BrevredigeringTable.sistReservert] = sistReservert
                it[BrevredigeringTable.saksbehandlerValgKryptert] = null
            }
        }

        val job = JobConfig("test-backfill-reservation-${brev.info.id}")
        job.updateBrevredigeringJson()

        transaction {
            val rad = BrevredigeringTable.selectAll().where { BrevredigeringTable.id eq brev.info.id }.single()
            if (reservation == "active") {
                assertThat(rad[BrevredigeringTable.saksbehandlerValgKryptert]).isNull()
                assertThat(job.completed).isFalse()
            } else {
                assertThat(rad[BrevredigeringTable.saksbehandlerValgKryptert]).isEqualTo(valg)
            }
        }
    }

    @Test
    fun `backfill reads current values after a concurrent writer commits`(): Unit = runBlocking {
        val originalValg = SaksbehandlervalgMap().apply { put("valg1", SaksbehandlervalgVerdi.Boolean(false)) }
        val oppdaterteValg = SaksbehandlervalgMap().apply { put("valg1", SaksbehandlervalgVerdi.Boolean(true)) }
        val brev = opprettBrev(saksbehandlerValg = originalValg).resultOrFail()
        transaction {
            BrevredigeringTable.update({ BrevredigeringTable.id eq brev.info.id }) {
                it[BrevredigeringTable.sistReservert] = Instant.now().minusSeconds(16 * 60)
                it[BrevredigeringTable.saksbehandlerValgKryptert] = null
            }
        }

        val writerPid = CompletableFuture<Int>()
        val writerKanCommitte = CountDownLatch(1)
        val executor = Executors.newFixedThreadPool(2)
        try {
            val writer = executor.submit {
                transaction {
                    BrevredigeringTable.selectAll().where { BrevredigeringTable.id eq brev.info.id }
                        .forUpdate(ForUpdateOption.ForUpdate).single()
                    writerPid.complete(exec("SELECT pg_backend_pid()") { result ->
                        check(result.next())
                        result.getInt(1)
                    }!!)
                    check(writerKanCommitte.await(10, TimeUnit.SECONDS))
                    BrevredigeringTable.update({ BrevredigeringTable.id eq brev.info.id }) {
                        it[BrevredigeringTable.saksbehandlerValg] = oppdaterteValg
                        it[BrevredigeringTable.saksbehandlerValgKryptert] = oppdaterteValg
                    }
                }
            }
            val pid = writerPid.get(5, TimeUnit.SECONDS)
            val job = executor.submit {
                JobConfig("test-backfill-encrypted-${brev.info.id}").updateBrevredigeringJson()
            }

            val deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5)
            var jobVenterPaaLaas = false
            while (!jobVenterPaaLaas && System.nanoTime() < deadline) {
                jobVenterPaaLaas = transaction {
                    exec("SELECT EXISTS (SELECT 1 FROM pg_stat_activity WHERE $pid = ANY(pg_blocking_pids(pid)))") { result ->
                        check(result.next())
                        result.getBoolean(1)
                    }!!
                }
                if (!jobVenterPaaLaas) Thread.sleep(10)
            }
            assertThat(jobVenterPaaLaas).isTrue()
            writerKanCommitte.countDown()
            writer.get(5, TimeUnit.SECONDS)
            job.get(10, TimeUnit.SECONDS)
        } finally {
            writerKanCommitte.countDown()
            executor.shutdownNow()
        }

        transaction {
            val rad = BrevredigeringTable.selectAll().where { BrevredigeringTable.id eq brev.info.id }.single()
            assertThat(rad[BrevredigeringTable.saksbehandlerValgKryptert])
                .isEqualTo(oppdaterteValg)
            assertThat(rad[BrevredigeringTable.saksbehandlerValg]).isEqualTo(oppdaterteValg)
        }
    }
}
