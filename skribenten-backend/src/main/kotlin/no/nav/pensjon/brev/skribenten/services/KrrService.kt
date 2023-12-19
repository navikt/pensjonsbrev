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

class KrrService(config: Config, authService: AzureADService) {
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class KontaktinfoKRRResponse(val spraak: SpraakKode? = null) {
        enum class SpraakKode {
            nb, // bokmål
            nn, //nynorsk
            en, //engelsk
            se, //nord-samisk
        }
    }

    data class KontaktinfoKRRErrorResponse(
        val errors: List<Error>,
    ) {
        data class Error(val id: String, val status: String, val title: String, val detail: String) {
            fun prettyPrint() =
                """
                    Title: $title
                    Status: $status
                    Id: $id
                    Detail: $detail
                """.trimIndent()
        }
    }

    data class KontaktinfoResponse(val spraakKode: SpraakKode?)

    suspend fun getPreferredLocale(call: ApplicationCall, pid: String): ServiceResult<KontaktinfoResponse, String> {
        return client.get(call, "/rest/v1/person") {
            headers {
                accept(ContentType.Application.Json)
                header("Nav-Personident", pid)
            }
        }.toServiceResult<KontaktinfoKRRResponse, KontaktinfoKRRErrorResponse>()
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
            }.catch { error ->
                error.errors.joinToString { it.prettyPrint() }
                    .also {
                        logger.error(
                            """
                            Error(s) from krr proxy:
                            $it
                            """.trimIndent()
                        )
                    }
            }
    }
}