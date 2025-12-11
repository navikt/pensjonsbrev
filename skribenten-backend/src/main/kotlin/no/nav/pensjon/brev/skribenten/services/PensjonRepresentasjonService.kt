package no.nav.pensjon.brev.skribenten.services

import com.typesafe.config.Config
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.jackson.jackson
import no.nav.pensjon.brev.skribenten.auth.AuthService
import org.slf4j.LoggerFactory

class PensjonRepresentasjonService(config: Config, authService: AuthService, clientEngine: HttpClientEngine = CIO.create()) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val pensjonPersondataURL = config.getString("url")
    private val scope = config.getString("scope")

    private val client = HttpClient(clientEngine) {
        defaultRequest {
            url(pensjonPersondataURL)
        }
        installRetry(logger)
        install(ContentNegotiation) { jackson() }
        callIdAndOnBehalfOfClient(scope, authService)
    }

    fun harVerge(fnr: String) {

    }
}