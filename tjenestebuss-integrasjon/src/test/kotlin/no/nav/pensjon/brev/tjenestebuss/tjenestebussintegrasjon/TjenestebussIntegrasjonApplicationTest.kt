package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.junit.Test
import kotlin.test.assertEquals

class TjenestebussIntegrasjonApplicationTest {
    @Test
    fun isAlive() =
        testApplication {
            val response = client.get("/isAlive")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals("Alive!", response.bodyAsText())
        }

    @Test
    fun isReady() =
        testApplication {
            val response = client.get("/isReady")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals("Ready!", response.bodyAsText())
        }
}
