@file:OptIn(InternKonstruktoer::class)

package no.nav.pensjon.brev.skribenten.fagsystem.pesys

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.OboClientConfig
import no.nav.pensjon.brev.skribenten.auth.FakeAuthService
import no.nav.pensjon.brev.skribenten.model.JournalpostId
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.services.EnhetId
import no.nav.pensjon.brev.skribenten.services.httpClientTest
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate

class PenClientTest {

    private val config = OboClientConfig(url = "http://pen.test", scope = "test-scope")

    private val templateDescription = TemplateDescription.Redigerbar(
        name = "INFORMASJONSBREV",
        letterDataClass = "template letter data class",
        languages = listOf(LanguageCode.ENGLISH),
        metadata = LetterMetadata(
            displayTitle = "Et informasjonsbrev",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        ),
        kategori = TemplateDescription.Redigerbar.Brevkategori("INFORMASJONSBREV"),
        brevkontekst = TemplateDescription.Brevkontekst.ALLE,
        sakstyper = setOf(TemplateDescription.Redigerbar.Sakstype("S1")),
    )

    private val request = Pen.SendRedigerbartBrevRequest(
        templateDescription = templateDescription,
        dokumentDato = LocalDate.now(),
        saksId = SaksId(1234L),
        brevkode = RedigerbarBrevkode("INFORMASJONSBREV"),
        enhetsId = EnhetId("1234"),
        pdf = byteArrayOf(1, 2, 3),
        eksternReferanseId = "skribenten:1",
        mottaker = null,
    )

    private fun penClient(engine: MockEngine) = PentHttpClient(config, FakeAuthService, engine)

    @Test
    fun `sendbrev returnerer BestillBrevResponse ved suksess`() {
        val expected = Pen.BestillBrevResponse(journalpostId = null, error = null)
        val objectMapper = jacksonObjectMapper()
        var capturedPath: String? = null
        var capturedDistribuerParam: String? = null
        var capturedMethod: HttpMethod? = null

        val engine = MockEngine { request ->
            capturedPath = request.url.fullPath
            capturedDistribuerParam = request.url.parameters["distribuer"]
            capturedMethod = request.method
            respond(
                content = objectMapper.writeValueAsString(expected),
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type", "application/json"),
            )
        }

        httpClientTest(Unit) {
            val actual = penClient(engine).sendbrev(request, distribuer = true)

            assertThat(actual).isEqualTo(expected)
            assertThat(capturedMethod).isEqualTo(HttpMethod.Post)
            assertThat(capturedPath).isEqualTo("/brev/skribenten/sendbrev?distribuer=true")
            assertThat(capturedDistribuerParam).isEqualTo("true")
        }
    }

    @Test
    fun `sendbrev sender distribuer=false videre til PEN`() {
        val expected = Pen.BestillBrevResponse(journalpostId = JournalpostId(42L), error = null)
        val objectMapper = jacksonObjectMapper()
        var capturedDistribuerParam: String? = null

        val engine = MockEngine { request ->
            capturedDistribuerParam = request.url.parameters["distribuer"]
            respond(
                content = objectMapper.writeValueAsString(expected),
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type", "application/json"),
            )
        }

        httpClientTest(Unit) {
            val actual = penClient(engine).sendbrev(request, distribuer = false)

            assertThat(actual.journalpostId?.id).isEqualTo(42L)
            assertThat(capturedDistribuerParam).isEqualTo("false")
        }
    }

    @Test
    fun `sendbrev kaster PenAdresseManglerException naar PEN svarer med AdresseMangler`() {
        val feilrespons = Pen.BestillBrevResponse(
            journalpostId = null,
            error = Pen.BestillBrevResponse.Error(brevIkkeStoettet = null, tekniskgrunn = "AdresseMangler", beskrivelse = "Adresse mangler"),
        )
        val objectMapper = jacksonObjectMapper()
        val engine = MockEngine {
            respond(
                content = objectMapper.writeValueAsString(feilrespons),
                status = HttpStatusCode.UnprocessableEntity,
                headers = headersOf("Content-Type", "application/json"),
            )
        }

        httpClientTest(Unit) {
            assertThrows<PenAdresseManglerException> {
                penClient(engine).sendbrev(request, distribuer = true)
            }
        }
    }

    @Test
    fun `sendbrev kaster PenServiceException ved annen unprocessable entity-feil`() {
        val feilrespons = Pen.BestillBrevResponse(
            journalpostId = null,
            error = Pen.BestillBrevResponse.Error(brevIkkeStoettet = null, tekniskgrunn = "AnnenFeil", beskrivelse = "Noe gikk galt"),
        )
        val objectMapper = jacksonObjectMapper()
        val engine = MockEngine {
            respond(
                content = objectMapper.writeValueAsString(feilrespons),
                status = HttpStatusCode.UnprocessableEntity,
                headers = headersOf("Content-Type", "application/json"),
            )
        }

        httpClientTest(Unit) {
            assertThrows<PenServiceException> {
                penClient(engine).sendbrev(request, distribuer = true)
            }
        }
    }

    @Test
    fun `sendbrev kaster PenServiceException ved feil-status fra PEN`() {
        val engine = MockEngine {
            respond(
                content = "Internal server error",
                status = HttpStatusCode.InternalServerError,
            )
        }

        httpClientTest(Unit) {
            assertThrows<PenServiceException> {
                penClient(engine).sendbrev(request, distribuer = true)
            }
        }
    }
}
