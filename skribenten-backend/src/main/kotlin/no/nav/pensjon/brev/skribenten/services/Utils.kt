package no.nav.pensjon.brev.skribenten.services

import io.ktor.client.*
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import no.nav.pensjon.brev.skribenten.auth.AzureAdOnBehalfOf
import no.nav.pensjon.brev.skribenten.context.CallIdFromContext

fun HttpClientConfig<*>.callIdAndOnBehalfOfClient(scope: String, authService: AzureADService) {
    install(CallIdFromContext)
    install(AzureAdOnBehalfOf) {
        this.scope = scope
        this.authService = authService
    }
}