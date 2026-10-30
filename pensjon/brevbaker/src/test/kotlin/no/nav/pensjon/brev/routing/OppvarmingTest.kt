package no.nav.pensjon.brev.routing

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.testing.testApplication
import no.nav.brev.brevbaker.PDFByggerService
import no.nav.brev.brevbaker.PDFRequest
import no.nav.brev.brevbaker.pdfbygger.api.LetterPDFRequest
import no.nav.pensjon.brev.pensjonOgUfoereProductionTemplates
import no.nav.pensjon.brev.template.brevbakerJacksonObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class OppvarmingTest {
    @Test
    fun `brevdata og markup varmes opp uten feil`() {
        val oppvarming = Oppvarming(brevbakerJacksonObjectMapper())

        assertFalse(oppvarming.ferdig)
        oppvarming.varmOpp()

        assertTrue(oppvarming.ferdig)
        assertEquals(emptySet<String>(), oppvarming.feiledeSteg)
    }

    @Test
    fun `feil i oppvarming hindrer ikke at applikasjonen blir klar`() {
        val oppvarming = Oppvarming(ObjectMapper())

        oppvarming.varmOpp()

        assertTrue(oppvarming.ferdig)
        assertTrue("brevdata" in oppvarming.feiledeSteg)
    }

    @Test
    fun `readiness venter til oppvarming er ferdig`() = testApplication {
        val oppvarming = Oppvarming(brevbakerJacksonObjectMapper())
        environment { config = MapApplicationConfig() }
        application {
            attributes.put(OppvarmingKey, oppvarming)
            val pdfByggerService = object : PDFByggerService {
                override suspend fun producePDF(pdfRequest: PDFRequest) = error("Readiness must not call pdf-bygger")
                override suspend fun producePDFV2(pdfRequest: LetterPDFRequest) = error("Readiness must not call pdf-bygger")
            }
            brevRouting(null, pdfByggerService, pensjonOgUfoereProductionTemplates)
        }

        val venter = client.get("/isReady")
        assertEquals(HttpStatusCode.ServiceUnavailable, venter.status)
        assertEquals("Varmer opp", venter.bodyAsText())

        oppvarming.varmOpp()

        val klar = client.get("/isReady")
        assertEquals(HttpStatusCode.OK, klar.status)
        assertEquals("Ready!", klar.bodyAsText())
    }

}
