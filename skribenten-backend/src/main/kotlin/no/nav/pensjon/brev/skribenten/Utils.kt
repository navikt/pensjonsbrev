package no.nav.pensjon.brev.skribenten

import io.ktor.client.request.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.auth.UnauthorizedException
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.context.CallIdInContext

suspend fun HttpRequestBuilder.callIdHeaders() {
    CallIdInContext.get()?.also {
        headers {
            append("Nav-Call-Id", it)
            append("X-Request-ID", it)
        }
    }
}

fun ApplicationCall.principal(): UserPrincipal =
    authentication.principal() ?: throw UnauthorizedException("ApplicationCall doesn't have a UserPrincipal")

fun RoutingContext.principal(): UserPrincipal =
    call.principal()