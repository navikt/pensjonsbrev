package no.nav.pensjon.brev.skribenten

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.skribenten.db.databaseObjectMapper
import org.junit.AfterClass
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import kotlin.jvm.java
import kotlin.test.assertEquals


val valkeyContainer = GenericContainer("valkey/valkey:8.0.0")

class CacheTest {

    val valkeyConfig: Config

    init {
        valkeyContainer.withExposedPorts(6379)
        valkeyContainer.waitingFor(Wait.forListeningPort())
        valkeyContainer.start()
        valkeyConfig = ConfigFactory.parseMap(mapOf(
            "host" to valkeyContainer.host,
            "port" to valkeyContainer.getMappedPort(6379).toString(),
            "username" to "default",
            "password" to "",
            "ssl" to "false",
        ))
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
        assertEquals(1, counter)
        assertEquals(123, cache.get(Cacheomraade.NORG, "k", Int::class.java) { databaseObjectMapper.readValue((it)) })
    }

    @Test
    fun `verdi som ikke er i cachen gir null for get`() {
        assertNull(Valkey(valkeyConfig).get(Cacheomraade.NORG, "mangler", String::class.java) {
            databaseObjectMapper.readValue(
                (it)
            )
        })
    }

    @Test
    fun `kan oppdatere verdi som fins i cachen`() {
        val cache = Valkey(valkeyConfig)
        val key = "k"
        val omraade = Cacheomraade.NORG
        cache.update(omraade, key, "verdi1", ttl(value))
        val v1 = cache.get(omraade, key, String::class.java) { databaseObjectMapper.readValue(it) }
        assertEquals("verdi1", v1)
        cache.update(omraade, key, "verdi2", ttl(value))
        assertEquals("verdi2", cache.get(omraade, "k", String::class.java) { databaseObjectMapper.readValue(it) })
    }

    @Test
    fun `returnerer null hvis exception i valkey-kall`() {
        val objectMapper = object : ObjectMapper() {
            override fun writeValueAsString(value: Any?): String {
                throw RuntimeException("test exception")
            }
        }
        val cache = Valkey(valkeyConfig, objectMapper)
        runBlocking {
            cache.cached(Cacheomraade.NORG, "k") {
                "v1"
            }
            assertNull(
                cache.get(Cacheomraade.NORG, "k", String::class.java, deserialize = { objectMapper.readValue(it, String::class.java)})
            )
        }
    }

    @Test
    fun `kan lese navenheter`() {
        val key = "A12345"
        val value = """[{"id":"1","navn":"Nav 1"},{"id":"2","navn":"Nav 2"},{"id":"3","navn":"Nav 3"}]"""
        val enheter = databaseObjectMapper.readValue(value, List::class.java)
        val cache = Valkey(valkeyConfig)

        cache.update(Cacheomraade.NAVANSATTENHET, key, enheter, ttl(value))

        assertEquals(enheter, cache.get(Cacheomraade.NAVANSATTENHET, key, List::class.java, deserialize = { databaseObjectMapper.readValue(it)}))
    }


    companion object {
        @JvmStatic
        @AfterClass
        fun stopValkey() {
            valkeyContainer.stop()
        }
    }

}