package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.Year
import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.api.model.vedlegg.*
import no.nav.pensjon.brev.skribenten.auth.*
import java.time.*

class BrevbakerService(config: Config, authService: AzureADService) {
    private val brevbakerUrl = config.getString("url")

    private val client = AzureADOnBehalfOfAuthorizedHttpClient(config.getString("scope"), authService) {
        defaultRequest {
            url(brevbakerUrl)
        }
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
            }
        }
    }

    // Demo request
    // TODO: handle exceptions thrown by client, and wrap them in ServiceResult.Error. Design a type to represent errors.
    suspend fun genererBrev(call: ApplicationCall): ServiceResult<LetterResponse, Any> =
        client.post(call, "/letter/vedtak") {
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
                        Bruker(Foedselsnummer("12345678910"), LocalDate.of(2000, Month.JANUARY, 1), "Test", null, "Testeson"),
                        "Verge vergesen",
                    ),
                    language = LanguageCode.BOKMAL,
                )
            )
        }.toServiceResult()
}

