package no.nav.pensjon.brev.routing

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.LetterTestRenderer
import no.nav.brev.brevbaker.TestTags
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequestV2
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.fixtures.createEksempelbrevRedigerbartDto
import no.nav.pensjon.brev.fixtures.createLetterExampleDto
import no.nav.pensjon.brev.maler.example.EksempelRedigerbartDto
import no.nav.pensjon.brev.maler.example.EksempelbrevRedigerbart
import no.nav.pensjon.brev.maler.example.LetterExample
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LetterImpl
import no.nav.pensjon.brev.testBrevbakerApp
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.ResourceLock

/**
 * Ende-til-ende-tester for v2 PDF-rutene: brevbaker rendrer [no.nav.pensjon.brevbaker.api.model.LetterMarkupV2]
 * og sender den til den kjørende pdf-bygger-containeren via `/v2/produserBrev`. Verifiserer dermed at
 * hele v2-kjeden (renderer -> PDFRequestV2 -> pdf-bygger -> PDF) er koblet sammen.
 */
@Tag(TestTags.INTEGRATION_TEST)
@ResourceLock("FeatureToggleHandler")
class LetterRoutesV2ITest {
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
    private val redigertBestillingV2 = LetterImpl(
        template = EksempelbrevRedigerbart.template,
        argument = bestillMarkupRequest.letterData,
        language = Language.Bokmal,
        felles = bestillMarkupRequest.felles,
    ).let { LetterTestRenderer.renderLetterOnlyV2(it) }
        .let { markup ->
            with(bestillMarkupRequest) {
                BestillRedigertBrevRequestV2(kode, letterData as EksempelRedigerbartDto, felles, language, markup, listOf())
            }
        }

    @Test
    fun `v2 autobrev pdf can respond with raw pdf`() = testBrevbakerApp { client ->
        val response = client.post("/v2/letter/autobrev/pdf") {
            accept(ContentType.Application.Pdf)
            setBody(autoBrevRequest)
        }

        assertEquals(ContentType.Application.Pdf, response.contentType())
        assertEquals(HttpStatusCode.OK, response.status)
        assertThat(response.bodyAsBytes().toString(Charsets.ISO_8859_1)).startsWith("%PDF-")
    }

    @Test
    fun `v2 autobrev pdf can respond with json`() = testBrevbakerApp { client ->
        val responseBody = client.post("/v2/letter/autobrev/pdf") {
            accept(ContentType.Application.Json)
            setBody(autoBrevRequest)
        }.body<LetterResponse>()

        assertEquals(ContentType.Application.Pdf.toString(), responseBody.contentType)
        assertThat(responseBody.file.toString(Charsets.ISO_8859_1)).startsWith("%PDF-")
    }

    @Test
    fun `v2 redigerbar can render pdf from redigert markup`() = testBrevbakerApp { client ->
        val response = client.post("/v2/letter/redigerbar/pdf") {
            accept(ContentType.Application.Json)
            setBody(redigertBestillingV2)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.body<LetterResponse>()
        assertEquals(ContentType.Application.Pdf.toString(), body.contentType)
        assertThat(body.file.toString(Charsets.ISO_8859_1)).startsWith("%PDF-")
    }
}
