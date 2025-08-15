package no.nav.pensjon.brev.skribenten.services

import io.ktor.client.*
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brev.skribenten.auth.AzureAdOnBehalfOf
import no.nav.pensjon.brev.skribenten.context.CallIdFromContext

fun HttpClientConfig<*>.callIdAndOnBehalfOfClient(scope: String, authService: AuthService) {
    install(CallIdFromContext)
    install(AzureAdOnBehalfOf) {
        this.scope = scope
        this.authService = authService
    }
}