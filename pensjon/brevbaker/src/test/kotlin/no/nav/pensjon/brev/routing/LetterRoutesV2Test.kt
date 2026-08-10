package no.nav.pensjon.brev.routing

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.pensjon.brev.api.model.maler.BestillBrevRequest
import no.nav.pensjon.brev.fixtures.createEksempelbrevRedigerbartDto
import no.nav.pensjon.brev.fixtures.createLetterExampleDto
import no.nav.pensjon.brev.maler.example.EksempelbrevRedigerbart
import no.nav.pensjon.brev.maler.example.LetterExample
import no.nav.pensjon.brev.testBrevbakerApp
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Ruter-nivå-tester for v2-endepunktene (`/v2/letter/...`) som rendrer
 * [no.nav.brev.brevbaker.markup.LetterMarkup] (v2). Speiler dekningen i [LetterRoutesITest] (v1) der det
 * er meningsfullt, uten å redusere v1-dekningen der. Ende-til-ende PDF-produksjon (som krever en kjørende
 * pdf-bygger) dekkes i [LetterRoutesV2ITest].
 */
class LetterRoutesV2Test {
    private val autoBrevRequest = BestillBrevRequest(
        kode = LetterExample.kode,
        letterData = createLetterExampleDto(),
        felles = FellesFactory.fellesAuto,
        language = LanguageCode.BOKMAL,
    )
    private val bestillMarkupRequest = BestillBrevRequest(
        kode = EksempelbrevRedigerbart.kode,
        letterData = createEksempelbrevRedigerbartDto(),
        felles = FellesFactory.felles,
        language = LanguageCode.BOKMAL,
    )

    @Test
    fun `v2 autobrev json responds with markup v2`() = testBrevbakerApp(isIntegrationTest = false) { client ->
        val response = client.post("/v2/letter/autobrev/json") {
            accept(ContentType.Application.Json)
            setBody(autoBrevRequest)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.body<LetterMarkup>()
        assertEquals(true, body.title1.isNotEmpty())
    }

    @Test
    fun `v2 redigerbar markup responds with markup v2`() = testBrevbakerApp(isIntegrationTest = false) { client ->
        val response = client.post("/v2/letter/redigerbar/markup") {
            accept(ContentType.Application.Json)
            setBody(bestillMarkupRequest)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.body<LetterMarkup>()
        assertEquals(true, body.title1.isNotEmpty())
    }

    @Test
    fun `v2 redigerbar markup-usage responds with usage data`() = testBrevbakerApp(isIntegrationTest = false) { client ->
        val response = client.post("/v2/letter/redigerbar/markup-usage") {
            accept(ContentType.Application.Json)
            setBody(bestillMarkupRequest)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `v2 redigerbar vedlegg titler responds with titler`() = testBrevbakerApp(isIntegrationTest = false) { client ->
        val response = client.post("/v2/letter/redigerbar/redigerbare-vedlegg/titler") {
            accept(ContentType.Application.Json)
            setBody(bestillMarkupRequest)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `v2 redigerbar vedlegg markup responds 404 for unknown vedleggId`() = testBrevbakerApp(isIntegrationTest = false) { client ->
        val response = client.post("/v2/letter/redigerbar/redigerbare-vedlegg/ikke-en-id") {
            accept(ContentType.Application.Json)
            setBody(bestillMarkupRequest)
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }
}
