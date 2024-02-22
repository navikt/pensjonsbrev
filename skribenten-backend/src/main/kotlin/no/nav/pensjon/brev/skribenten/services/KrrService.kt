package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.skribenten.auth.AzureADOnBehalfOfAuthorizedHttpClient
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import org.slf4j.LoggerFactory

class KrrService(config: Config, authService: AzureADService): ServiceStatus {
    private val krrUrl = config.getString("url")
    private val krrScope = config.getString("scope")
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val client = AzureADOnBehalfOfAuthorizedHttpClient(krrScope, authService) {
        defaultRequest {
            url(krrUrl)
        }
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
            }
        }
    }

    @Suppress("EnumEntryName")
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class KontaktinfoKRRResponse(val spraak: SpraakKode? = null) {
        enum class SpraakKode {
            nb, // bokmål
            nn, //nynorsk
            en, //engelsk
            se, //nord-samisk
        }
    }

    data class KontaktinfoResponse(val spraakKode: SpraakKode?, val failure: FailureType?) {
        constructor(failure: FailureType) : this(null, failure)
        constructor(spraakKode: SpraakKode?) : this(spraakKode, null)

        enum class FailureType {
            NOT_FOUND,
            ERROR,
        }
    }

    suspend fun getPreferredLocale(call: ApplicationCall, pid: String): KontaktinfoResponse {
        return client.get(call, "/rest/v1/person") {
            headers {
                accept(ContentType.Application.Json)
                header("Nav-Personident", pid)
            }
        }.toServiceResult<KontaktinfoKRRResponse>()
            .map {
                KontaktinfoResponse(
                    when (it.spraak) {
                        KontaktinfoKRRResponse.SpraakKode.nb -> SpraakKode.NB
                        KontaktinfoKRRResponse.SpraakKode.nn -> SpraakKode.NN
                        KontaktinfoKRRResponse.SpraakKode.en -> SpraakKode.EN
                        KontaktinfoKRRResponse.SpraakKode.se -> SpraakKode.SE
                        null -> null
                    }
                )
            }.catch { message, status ->
                KontaktinfoResponse(
                    if (status == HttpStatusCode.NotFound) {
                        KontaktinfoResponse.FailureType.NOT_FOUND
                    } else {
                        logger.error("Feil ved henting av kontaktinformasjon. Status: $status Melding: $message")
                        KontaktinfoResponse.FailureType.ERROR
                    }
                )
            }
    }

    override val name = "KRR"
    override suspend fun ping(call: ApplicationCall): ServiceResult<Boolean> =
        client.get(call, "/internal/health/readiness")
            .toServiceResult<String>()
            .map { true }
}