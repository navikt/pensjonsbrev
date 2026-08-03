package no.nav.pensjon.brev.maler.example

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.testBrevbakerApp
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import org.assertj.core.api.Assertions.assertThat
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDate

@Tag(TestTags.INTEGRATION_TEST)
class SaksbehandlervalgIDSLTestbrevTest {

    @Test
    fun `render med default-verdier`() {
        testBrevbakerApp { client ->
            val dto = SaksbehandlervalgIDSLTestbrevDto(
                pesysData = SaksbehandlervalgIDSLTestbrevDto.PesysData(
                    saksnummer = "12345678",
                    opprettet = LocalDate.of(2024, 1, 15),
                ),
                saksbehandlerValg = lagSaksbehandlervalg(),
            )
            val letterMarkup = genererMarkup(dto, client)
            val doc = genererHTML(client, dto, letterMarkup)

            assertThat(doc.select("h1.pensjonsbrev-tittel").text()).isEqualTo("Testbrev saksbehandlarval (IDSL)")
            assertThat(paragraphTekster(doc)).containsExactly(
                "Testbrev for saksbehandlarval via SaksbehandlervalgIDSL",
                "Saksnummer: 12345678, oppretta 15. januar 2024",
                "boolMedDefault = true",
                "intMedDefault = 42",
                "tekstMedDefault = standardtekst",
                "enumMedDefault = ALTERNATIV_EN",
            )
        }
    }

    @Test
    fun `render med satte verdier`() {
        testBrevbakerApp { client ->
            val dto = SaksbehandlervalgIDSLTestbrevDto(
                pesysData = SaksbehandlervalgIDSLTestbrevDto.PesysData(
                    saksnummer = "87654321",
                    opprettet = LocalDate.of(2024, 6, 1),
                ),
                saksbehandlerValg = lagSaksbehandlervalg(
                    "bool" to true,
                    "boolMedDefault" to false,
                    "intUtenDefault" to 7,
                    "intMedDefault" to 13,
                    "tekstUtenDefault" to "hei",
                    "tekstMedDefault" to "annet",
                    "enumUtenDefault" to SaksbehandlervalgIDSLTestValg.ALTERNATIV_TO.name,
                    "enumMedDefault" to SaksbehandlervalgIDSLTestValg.ALTERNATIV_TO.name,
                ),
            )
            val letterMarkup = genererMarkup(dto, client)
            val doc = genererHTML(client, dto, letterMarkup)

            assertThat(paragraphTekster(doc)).containsExactly(
                "Testbrev for saksbehandlarval via SaksbehandlervalgIDSL",
                "Saksnummer: 87654321, oppretta 1. juni 2024",
                "bool = true",
                "intUtenDefault = 7",
                "intMedDefault = 13",
                "tekstUtenDefault = hei",
                "tekstMedDefault = annet",
                "enumUtenDefault = ALTERNATIV_TO",
                "enumMedDefault = ALTERNATIV_TO",
            )
        }
    }

    private suspend fun genererMarkup(
        dto: SaksbehandlervalgIDSLTestbrevDto,
        client: HttpClient,
    ): LetterMarkup {
        val bestillMarkupRequest = BestillBrevRequest(
            kode = SaksbehandlervalgIDSLTestbrev.kode,
            letterData = dto,
            felles = FellesFactory.felles,
            language = LanguageCode.NYNORSK,
        )
        val response = client.post("/letter/redigerbar/markup") {
            accept(ContentType.Application.Json)
            setBody(bestillMarkupRequest)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        return response.body<LetterMarkup>()
    }

    private suspend fun genererHTML(
        client: HttpClient,
        dto: SaksbehandlervalgIDSLTestbrevDto,
        letterMarkup: LetterMarkup,
    ): Document {
        val response = client.post("/letter/redigerbar/html") {
            accept(ContentType.Application.Json)
            setBody(
                BestillRedigertBrevRequest(
                    kode = SaksbehandlervalgIDSLTestbrev.kode,
                    letterData = dto,
                    felles = FellesFactory.felles,
                    language = LanguageCode.NYNORSK,
                    letterMarkup = letterMarkup,
                    alltidValgbareVedlegg = emptyList(),
                    redigerteVedlegg = emptyMap()
                )
            )
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.body<LetterResponse>()
        return Jsoup.parse(String(body.file))
    }

    private fun paragraphTekster(doc: Document): List<String> =
        doc.select("div.pensjonsbrev-paragraph").map { it.text() }
}
