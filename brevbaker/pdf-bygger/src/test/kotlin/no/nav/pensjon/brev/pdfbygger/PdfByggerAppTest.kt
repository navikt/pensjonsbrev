package no.nav.pensjon.brev.pdfbygger

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PdfByggerAppTest {
    @Test
    fun appRuns() {
        testApplication {
            environment {
                config = ApplicationConfig(null)
            }

            val response = client.get("/isAlive")
            assertEquals(HttpStatusCode.OK, response.status)
        }
    }
}
