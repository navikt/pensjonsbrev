package no.nav.pensjon.brev.skribenten.services

import kotlinx.coroutines.isActive
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class HttpClientFactoryTest {

    @Test
    fun `lukker httpclient`() {
        val klient = HttpClientFactory.lagHttpClient {}
        assertTrue(klient.isActive)
        HttpClientFactory.close()
        assertFalse(klient.isActive)
    }

    @Test
    fun `close og lagHttpClient fra flere tråder kaster ikke unntak`() {
        repeat(5) { HttpClientFactory.lagHttpClient {} }

        val antallTraader = 20
        val ferdig = CountDownLatch(antallTraader*2)

        repeat(antallTraader) {
            Thread {
                HttpClientFactory.lagHttpClient {}
                ferdig.countDown()
            }.start()
        }

        repeat(antallTraader) {
            Thread {
                HttpClientFactory.close()
                ferdig.countDown()
            }.start()
        }

        assertTrue(ferdig.await(3, TimeUnit.SECONDS), "Tråder ble ikke ferdige i tide")
        HttpClientFactory.close()
    }
}