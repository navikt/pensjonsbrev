package no.nav.pensjon.brev.skribenten

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.skribenten.db.databaseObjectMapper
import org.junit.AfterClass
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import kotlin.jvm.java
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.minutes


val valkeyContainer = GenericContainer("valkey/valkey:8.0.0")

class CacheTest {

    val valkeyConfig: Map<String, String?>
    val instanceName = "CACHE1"

    init {
        valkeyContainer.withExposedPorts(6379)
        valkeyContainer.waitingFor(Wait.forListeningPort())
        valkeyContainer.start()
        valkeyConfig = mapOf(
            "VALKEY_HOST_$instanceName" to valkeyContainer.host,
            "VALKEY_PORT_$instanceName" to valkeyContainer.getMappedPort(6379).toString(),
            "VALKEY_USERNAME_$instanceName" to "default",
            "VALKEY_PASSWORD_$instanceName" to "",
            "VALKEY_SSL_$instanceName" to "false",
        )
    }

    @Test
    fun `henter verdi foerste gang, gjenbruker seinere`() {
        val cache = Valkey(valkeyConfig, instanceName)
        var counter = 0
        runBlocking {
            (1..10).forEach { _ ->
                cache.cached(Cacheomraade.NORG, "k") {
                    counter++
                    123
                }
            }
        }
        assertEquals(1, counter)
        assertEquals(123, cache.get(Cacheomraade.NORG, "k", Int::class.java))
    }

    @Test
    fun `verdi som ikke er i cachen gir null for get`() {
        assertNull(Valkey(valkeyConfig, instanceName).get(Cacheomraade.NORG, "mangler", String::class.java))
    }

    @Test
    fun `kan oppdatere verdi som fins i cachen`() {
        val cache = Valkey(valkeyConfig, instanceName)
        val key = "k"
        val omraade = Cacheomraade.NORG
        cache.update(omraade, key, "verdi1", 10.minutes)
        val v1 = cache.get(omraade, key, String::class.java)
        assertEquals("verdi1", v1)
        cache.update(omraade, key, "verdi2")
        assertEquals("verdi2", cache.get(omraade, "k", String::class.java))
    }

    @Test
    fun `returnerer null hvis exception i valkey-kall`() {
        val objectMapper = object : ObjectMapper() {
            override fun writeValueAsString(value: Any?): String {
                throw RuntimeException("test exception")
            }
        }
        val cache = Valkey(valkeyConfig, instanceName, objectMapper)
        runBlocking {
            cache.cached(Cacheomraade.NORG, "k") {
                "v1"
            }
            assertNull(
                cache.get(Cacheomraade.NORG, "k", String::class.java)
            )
        }
    }

    @Test
    fun `kan lese navenheter`() {
        val key = "A12345"
        val value = """[{"id":"1","navn":"Nav 1"},{"id":"2","navn":"Nav 2"},{"id":"3","navn":"Nav 3"}]"""
        val enheter = databaseObjectMapper.readValue(value, List::class.java)
        val cache = Valkey(valkeyConfig, instanceName)

        cache.update(Cacheomraade.NAVANSATTENHET, key, enheter)

        assertEquals(enheter, cache.get(Cacheomraade.NAVANSATTENHET, key, List::class.java))
    }


    companion object {
        @JvmStatic
        @AfterClass
        fun stopValkey() {
            valkeyContainer.stop()
        }
    }

}