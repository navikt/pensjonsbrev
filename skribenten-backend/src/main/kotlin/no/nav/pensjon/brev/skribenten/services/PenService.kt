package no.nav.pensjon.brev.skribenten.services

import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.skribenten.auth.*

class PenService(config: Config, authService: AzureADService) {
    private val penUrl = config.getString("url")
    private val penScope = config.getString("scope")

    private val client = AzureADOnBehalfOfAuthorizedHttpClient(penScope, authService) {
        defaultRequest {
            url(penUrl)
        }
    }

    // Demo request
    // TODO: Handle ResponseException and wrap in ServiceResult.Error. Design a type for possible errors.
    suspend fun hentSak(call: ApplicationCall, sakId: Long): ServiceResult<String, Any> =
        client.get(call, "sak/$sakId").toServiceResult()
}

