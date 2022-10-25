package no.nav.pensjon.brev.skribenten.services

import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import no.nav.pensjon.brev.skribenten.auth.AzureAdService

class PenService(config: Config, private val authService: AzureAdService) {
    private val penUrl = config.getString("url")
    private val penScope = config.getString("scope")

    private val client = HttpClient(CIO) {
        defaultRequest {
            url(penUrl)
        }
    }

    private suspend fun HttpRequestBuilder.addAuthorization(call: ApplicationCall) =
        authService.getOnBehalfOfToken(call, penScope).also {
            bearerAuth(it.accessToken)
        }

    private fun HttpRequestBuilder.addCallId(call: ApplicationCall) {
        headers {
            call.callId?.also { append("Nav-Call-Id", it) }
        }
    }

    // Demo request
    suspend fun hentSak(call: ApplicationCall, sakId: Long): String {
        return client.get("sak/$sakId") {
            addAuthorization(call)
            addCallId(call)
        }.body()
    }
}

