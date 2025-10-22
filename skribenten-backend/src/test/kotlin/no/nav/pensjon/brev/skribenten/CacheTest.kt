package no.nav.pensjon.brev.skribenten

import com.fasterxml.jackson.databind.ObjectMapper
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.skribenten.db.databaseObjectMapper
import org.junit.AfterClass
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.minutes


val valkeyContainer = GenericContainer("valkey/valkey:8.0.0")

class CacheTest {

    val valkeyConfig: Config

    init {
        valkeyContainer.withExposedPorts(6379)
        valkeyContainer.waitingFor(Wait.forListeningPort())
        valkeyContainer.start()
        valkeyConfig = ConfigFactory.parseMap(
            mapOf(
                "host" to valkeyContainer.host,
                "port" to valkeyContainer.getMappedPort(6379).toString(),
                "username" to "default",
                "password" to "",
                "ssl" to "false",
            )
        )
    }

    @Test
    fun `henter verdi foerste gang, gjenbruker seinere`() {
        val cache = Valkey(valkeyConfig)
        var counter = 0
        runBlocking {
            (1..10).forEach { _ ->
                cache.cached(Cacheomraade.NORG, "k") {
                    counter++
                    123
                }
            }
        }
        val key = databaseObjectMapper.writeValueAsString(Cacheomraade.NORG.prefix + "-" + databaseObjectMapper.writeValueAsString("k"))
        assertEquals(1, counter)
        assertEquals(123, cache.read(key)?.toInt())
    }

    @Test
    fun `verdi som ikke er i cachen gir null for get`() {
        assertNull(Valkey(valkeyConfig).read("mangler"))
    }

    @Test
    fun `kan oppdatere verdi som fins i cachen`() {
        val cache = Valkey(valkeyConfig)
        val key = "k"
        cache.update(key, "verdi1", 10.minutes)
        val v1 = cache.read(key)
        assertEquals("verdi1", v1)
        cache.update(key, "verdi2", 10.minutes)
        assertEquals("verdi2", cache.read("k"))
    }

    @Test
    fun `returnerer null hvis exception i valkey-kall`() {
        val cache = Valkey(valkeyConfig)

        runBlocking {
            cache.cached(Cacheomraade.NORG, "k", ttl = { throw RuntimeException("test exception") }) { "v1" }
            assertNull(cache.read("{NORG-k}"))
        }
    }

    @Test
    fun `kan lese navenheter`() {
        val key = "A12345"
        val value = """[{"id":"1","navn":"Nav 1"},{"id":"2","navn":"Nav 2"},{"id":"3","navn":"Nav 3"}]"""
        val cache = Valkey(valkeyConfig)

        cache.update(key, value, 10.minutes)

        assertEquals(
            value,
            cache.read(key)
        )
    }


    companion object {
        @JvmStatic
        @AfterClass
        fun stopValkey() {
            valkeyContainer.stop()
        }
    }

}