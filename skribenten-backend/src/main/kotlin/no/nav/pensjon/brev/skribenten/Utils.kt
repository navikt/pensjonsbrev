package no.nav.pensjon.brev.skribenten

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.callid.*
import io.ktor.util.pipeline.*
import no.nav.pensjon.brev.skribenten.auth.UnauthorizedException
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal

fun HeadersBuilder.callId(call: ApplicationCall) {
    call.callId?.also {
        append("Nav-Call-Id", it)
        append("X-Request-ID", it)
    }
}

fun ApplicationCall.principal(): UserPrincipal =
    authentication.principal() ?: throw UnauthorizedException("ApplicationCall doesn't have a UserPrincipal")

fun PipelineContext<Unit, ApplicationCall>.principal(): UserPrincipal =
    call.principal()