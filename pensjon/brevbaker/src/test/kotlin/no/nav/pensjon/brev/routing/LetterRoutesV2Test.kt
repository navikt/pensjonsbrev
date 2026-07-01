package no.nav.pensjon.brev.routing

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import no.nav.brev.brevbaker.FellesFactory
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.fixtures.createEksempelbrevRedigerbartDto
import no.nav.pensjon.brev.fixtures.createLetterExampleDto
import no.nav.pensjon.brev.maler.example.EksempelbrevRedigerbart
import no.nav.pensjon.brev.maler.example.LetterExample
import no.nav.pensjon.brev.testBrevbakerApp
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2
import no.nav.pensjon.brevbaker.api.model.RedigerbareVedleggTitler
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

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
        val body = response.body<LetterMarkupV2>()
        assertEquals(true, body.title1.isNotEmpty())
    }

    @Test
    fun `v2 redigerbar markup responds with markup v2`() = testBrevbakerApp(isIntegrationTest = false) { client ->
        val response = client.post("/v2/letter/redigerbar/markup") {
            accept(ContentType.Application.Json)
            setBody(bestillMarkupRequest)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.body<LetterMarkupV2>()
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
    fun `v2 redigerbare-vedlegg titler responds ok`() = testBrevbakerApp(isIntegrationTest = false) { client ->
        val response = client.post("/v2/letter/redigerbar/redigerbare-vedlegg/titler") {
            accept(ContentType.Application.Json)
            setBody(bestillMarkupRequest)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        response.body<RedigerbareVedleggTitler>()
    }

    @Test
    fun `v2 redigerbart vedlegg responds not found for unknown vedleggId`() = testBrevbakerApp(isIntegrationTest = false) { client ->
        val response = client.post("/v2/letter/redigerbar/redigerbare-vedlegg/ukjentVedlegg") {
            accept(ContentType.Application.Json)
            setBody(bestillMarkupRequest)
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }
}
