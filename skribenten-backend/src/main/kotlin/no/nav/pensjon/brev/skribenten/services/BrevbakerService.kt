package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.Year
import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.api.model.vedlegg.*
import no.nav.pensjon.brev.skribenten.auth.AzureAdService
import java.time.*

class BrevbakerException(msg: String) : Exception(msg)

class BrevbakerService(config: Config, private val authService: AzureAdService) {
    private val brevbakerUrl = config.getString("url")
    private val brevbakerScope = config.getString("scope")

    private val client = HttpClient(CIO) {
        defaultRequest {
            url(brevbakerUrl)
        }
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
            }
        }
        HttpResponseValidator {
            validateResponse { response ->
                // TODO: Gjør dette skikkelig når vi vet bruken.
                when (response.status) {
                    HttpStatusCode.BadRequest -> {
                        throw BrevbakerException("Bad request: ${response.body<String>()}")
                    }

                    HttpStatusCode.InternalServerError -> {
                        throw BrevbakerException("Brevbaker doesn't work: ${response.body<String>()}")
                    }
                }
            }
        }
    }

    private suspend fun HttpRequestBuilder.addAuthorization(call: ApplicationCall) =
        authService.getOnBehalfOfToken(call, brevbakerScope).also {
            bearerAuth(it.accessToken)
        }

    private fun HttpRequestBuilder.addCallId(call: ApplicationCall) {
        headers {
            call.callId?.also { append("Nav-Call-Id", it) }
        }
    }

    // Demo request
    suspend fun genererBrev(call: ApplicationCall): LetterResponse {
        return client.post("/letter/vedtak") {
            addAuthorization(call)
            addCallId(call)
            contentType(ContentType.Application.Json)
            setBody(
                VedtaksbrevRequest(
                    kode = Brevkode.Vedtak.OMSORG_EGEN_AUTO,
                    letterData = OmsorgEgenAutoDto(
                        Year(2020),
                        Year(2021),
                        EgenerklaeringOmsorgsarbeidDto(Year(2020), ReturAdresse("Fyrstikkallèen 1", "0664", "Oslo"))
                    ),
                    felles = Felles(
                        LocalDate.now(),
                        "1234",
                        NAVEnhet("nav.no", "NAV", Telefonnummer("22225555")),
                        Bruker(Foedselsnummer("12345678910"), LocalDate.of(2000, Month.JANUARY, 1), "Test", null, "Testeson")
                    ),
                    language = LanguageCode.BOKMAL,
                )
            )
        }.body()
    }
}