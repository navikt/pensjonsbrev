package no.nav.pensjon.brev.skribenten

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.milliseconds

class CacheTest {
    @Test
    fun `cached vil hente ny verdi om den ikke er cached fra foer`(): Unit =
        runBlocking {
            val cache = Cache<String, Int>()
            assertEquals(9, cache.cached("aaa") { 9 })
        }

    @Test
    fun `cached vil bruke eksisterende verdi`(): Unit =
        runBlocking {
            val cache = Cache<String, Int>()
            cache.cached("aaa") { 9 }

            assertEquals(9, cache.cached("aaa") { -1 })
        }

    @Test
    fun `cached vil hente ny verdi om eksisterende er utloept`(): Unit =
        runBlocking {
            val cache = Cache<String, Int>(ttl = 50.milliseconds)
            cache.cached("aaa") { 10 }
            delay(100.milliseconds)

            assertEquals(-1, cache.cached("aaa") { -1 })
        }
}
