package no.nav.pensjon.brev.skribenten.foerstesidegenerator

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.jackson.jackson
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brev.skribenten.services.ServiceException
import no.nav.pensjon.brev.skribenten.services.callIdAndOnBehalfOfClient
import no.nav.pensjon.brev.skribenten.services.installRetry
import org.slf4j.LoggerFactory

class FoerstesidegeneratorClient(config: Config, authService: AuthService) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val foerstesidegeratorUrl = config.getString("url")
    private val foerstesidegeratorScope = config.getString("scope")

    private val client = HttpClient(CIO) {
        defaultRequest {
            url(foerstesidegeratorUrl)
        }
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            }
        }
        installRetry(logger)
        callIdAndOnBehalfOfClient(foerstesidegeratorScope, authService)

    }

    suspend fun genererFoersteside(request: GenererFoerstesideRequest): GenererFoerstesideResponse {
        val response = client.post("/api/foerstesidegenerator/v1/foersteside") {
            setBody(request)
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Pdf)
            header("Nav-Consumer-Id", "skribenten-backend")
        }

        if (response.status.isSuccess()) {
            return response.body()
        } else {
            throw FoerstesidegeneratorException("Klarte ikke lage førsteside for ${request.arkivtittel}: ${response.status} - ${response.bodyAsText()}")
        }
    }

    class FoerstesidegeneratorException(message: String) : ServiceException(message)
}