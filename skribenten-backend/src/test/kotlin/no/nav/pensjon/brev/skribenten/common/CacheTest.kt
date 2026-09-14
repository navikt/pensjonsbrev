package no.nav.pensjon.brev.skribenten.common

import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.skribenten.Metrics
import no.nav.pensjon.brev.skribenten.ValkeyConfig
import no.nav.pensjon.brev.skribenten.db.databaseObjectMapper
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import kotlin.time.Duration.Companion.minutes


val valkeyContainer = GenericContainer("valkey/valkey:8.0.0")

private fun SimpleMeterRegistry.cacheRequestCount(omraade: Cacheomraade, result: String): Double =
    find("skribenten_cache_requests").tag("omraade", omraade.prefix).tag("result", result).counter()?.count() ?: 0.0

private fun SimpleMeterRegistry.cacheOperationCount(operation: String, outcome: String): Double =
    find(Metrics.cacheOperationMetricName).tag("operation", operation).tag("outcome", outcome).timer()?.count()?.toDouble() ?: 0.0

class CacheTest {

    val valkeyConfig: ValkeyConfig

    init {
        valkeyContainer.withExposedPorts(6379)
        valkeyContainer.waitingFor(Wait.forListeningPort())
        valkeyContainer.start()
        valkeyConfig = ValkeyConfig(
            host = valkeyContainer.host,
            port = valkeyContainer.getMappedPort(6379),
            username = "default",
            password = "",
            ssl = false,
            enabled = true,
        )
    }

    @Test
    fun `henter verdi foerste gang, gjenbruker seinere`() {
        val registry = SimpleMeterRegistry()
        val cache = Valkey(valkeyConfig, registry)
        var counter = 0
        runBlocking {
            (1..10).forEach { _ ->
                // Her må vi tilordne til variabel, elles fungerer det ikkje
                val i1 = cache.cached(Cacheomraade.NORG, "k") {
                    counter++
                    123
                }
            }
        }
        val key = Cacheomraade.NORG.prefix + "-" + databaseObjectMapper.writeValueAsString("k")
        assertEquals(1, counter)
        assertEquals(1.0, registry.cacheRequestCount(Cacheomraade.NORG, "miss"), "det første oppslaget skal telle som miss")
        assertEquals(9.0, registry.cacheRequestCount(Cacheomraade.NORG, "hit"), "de neste 9 oppslagene skal telle som hit")

        // Et ekstra, direkte read() teller også som et hit, siden metrikkene registreres i Valkey.read() selv
        runBlocking {
            assertEquals(123, cache.read(key)?.toInt())
        }
        assertEquals(10.0, registry.cacheRequestCount(Cacheomraade.NORG, "hit"), "det direkte oppslaget over skal også telle som hit")
    }

    @Test
    fun `verdi som ikke er i cachen gir null for get`() {
        runBlocking {
            assertNull(Valkey(valkeyConfig).read("mangler"))
        }
    }

    @Test
    fun `kan oppdatere verdi som fins i cachen`() {
        runBlocking {
        val cache = Valkey(valkeyConfig)
        val key = "k"
        cache.update(key, "verdi1", 10.minutes)
        val v1 = cache.read(key)
        assertEquals("verdi1", v1)
        cache.update(key, "verdi2", 10.minutes)
        assertEquals("verdi2", cache.read("k"))
            }
    }

    @Test
    fun `kan lese navenheter`() {
        runBlocking {
            val key = "A12345"
            val value = """[{"id":"1","navn":"Nav 1"},{"id":"2","navn":"Nav 2"},{"id":"3","navn":"Nav 3"}]"""
            val cache = Valkey(valkeyConfig)

            cache.update(key, value, 10.minutes)

            assertEquals(
                value,
                cache.read(key)
            )
        }
    }

    @Test
    fun `feil mot Valkey teller som operation-feil, ikke som miss`() {
        val registry = SimpleMeterRegistry()
        // Ugyldig port simulerer at Valkey er utilgjengelig
        val utilgjengeligConfig = valkeyConfig.copy(port = 1)
        val cache = Valkey(utilgjengeligConfig, registry)

        val key = "${Cacheomraade.NORG.prefix}-\"k\""
        val value = runBlocking { cache.read(key) }

        assertNull(value, "et leseforsøk som feiler mot Valkey skal returnere null, som et reelt miss")
        assertEquals(0.0, registry.cacheRequestCount(Cacheomraade.NORG, "miss"), "en backend-feil skal ikke telles som cache-miss")
        assertEquals(1.0, registry.cacheOperationCount("read", "error"), "backend-feilen skal telles i operations-metrikken")
    }

    companion object {
        @JvmStatic
        @AfterAll
        fun stopValkey() {
            valkeyContainer.stop()
        }
    }

}