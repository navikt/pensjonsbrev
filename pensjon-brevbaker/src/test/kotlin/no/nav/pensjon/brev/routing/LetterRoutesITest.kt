package no.nav.pensjon.brev.routing

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.contains
import com.natpryce.hamkrest.containsSubstring
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDto
import no.nav.pensjon.brev.fixtures.createLetterExampleDto
import no.nav.pensjon.brev.maler.example.LetterExample
import no.nav.pensjon.brev.maler.redigerbar.InformasjonOmSaksbehandlingstid
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.render.Letter2Markup
import no.nav.pensjon.brev.template.toScope
import no.nav.pensjon.brev.testBrevbakerApp
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.INTEGRATION_TEST)
class LetterRoutesITest {
    private val autoBrevRequest = BestillBrevRequest(
        kode = LetterExample.kode,
        letterData = createLetterExampleDto(),
        felles = Fixtures.fellesAuto,
        language = LanguageCode.BOKMAL,
    )
    private val bestillMarkupRequest = BestillBrevRequest(
        kode = InformasjonOmSaksbehandlingstid.kode,
        letterData = Fixtures.create<InformasjonOmSaksbehandlingstidDto>(),
        felles = Fixtures.felles,
        language = LanguageCode.BOKMAL,
    )
    private val redigertBestilling = Letter(
        template = InformasjonOmSaksbehandlingstid.template,
        argument = bestillMarkupRequest.letterData,
        language = Language.Bokmal,
        felles = bestillMarkupRequest.felles
    ).let { Letter2Markup.renderLetterOnly(it.toScope(), it.template) }
        .let {
            with(bestillMarkupRequest) {
                BestillRedigertBrevRequest(kode, letterData, felles, language, it)
            }
        }

    @Test
    fun isAlive() = testBrevbakerApp { client ->
        val response = client.get("/isAlive")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Alive!", response.bodyAsText())
    }

    @Test
    fun `render html can respond with raw html`() = testBrevbakerApp { client ->
        val response = client.post("/letter/autobrev/html") {
            accept(ContentType.Text.Html)
            setBody(autoBrevRequest)
        }
        assertEquals(ContentType.Text.Html.withCharset(Charsets.UTF_8), response.contentType())
        assertEquals(HttpStatusCode.OK, response.status)
        assertThat(response.bodyAsText(), contains(Regex("<html.*>")))
    }

    @Test
    fun `render html can respond with json`() = testBrevbakerApp { client ->
        val responseBody = client.post("/letter/autobrev/html") {
            accept(ContentType.Application.Json)
            setBody(autoBrevRequest)
        }.body<LetterResponse>()

        assertEquals(ContentType.Text.Html.withCharset(Charsets.UTF_8).toString(), responseBody.contentType)
        assertThat(String(responseBody.file, Charsets.UTF_8), contains(Regex("<html.*>")))
    }

    @Test
    fun `render pdf can respond with raw pdf`() = testBrevbakerApp { client ->
        val response = client.post("/letter/autobrev/pdf") {
            accept(ContentType.Application.Pdf)
            setBody(autoBrevRequest)
        }
        assertEquals(ContentType.Application.Pdf, response.contentType())
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `render pdf can respond with json`() = testBrevbakerApp { client ->
        val responseBody = client.post("/letter/autobrev/pdf") {
            accept(ContentType.Application.Json)
            setBody(autoBrevRequest)
        }.body<LetterResponse>()

        assertEquals(ContentType.Application.Pdf.toString(), responseBody.contentType)
    }

    @Test
    fun `render markup responds with markup`() = testBrevbakerApp { client ->
        val response = client.post("/letter/redigerbar/markup") {
            accept(ContentType.Application.Json)
            setBody(bestillMarkupRequest)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(redigertBestilling.letterMarkup, response.body<LetterMarkup>())
    }

    @Test
    fun `can render html from markup`() = testBrevbakerApp { client ->
        val response = client.post("/letter/redigerbar/html") {
            accept(ContentType.Application.Json)
            setBody(redigertBestilling)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.body<LetterResponse>()
        assertThat(String(body.file, Charsets.UTF_8), containsSubstring(redigertBestilling.letterMarkup.title))
    }

    @Test
    fun `can render pdf from markup`() = testBrevbakerApp { client ->
        val response = client.post("/letter/redigerbar/pdf") {
            accept(ContentType.Application.Json)
            setBody(redigertBestilling)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.body<LetterResponse>()
        assertEquals(ContentType.Application.Pdf.toString(), body.contentType)
    }
}