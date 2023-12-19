package no.nav.pensjon.brev.skribenten

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.callid.*
import io.ktor.util.pipeline.*
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal

private const val NAV_IDENT_CLAIM_KEY = "NAVident"

fun HeadersBuilder.callId(call: ApplicationCall) {
    call.callId?.also {
        append("Nav-Call-Id", it)
        append("X-Request-ID", it)
    }
}

fun PipelineContext<Unit, ApplicationCall>.getLoggedInUserId(): String? =
    call.authentication.principal<UserPrincipal>()?.getUserId()

fun PipelineContext<Unit, ApplicationCall>.getLoggedInNavIdent(): String? =
    getClaim(NAV_IDENT_CLAIM_KEY)

fun PipelineContext<Unit, ApplicationCall>.getClaim(claim: String): String? =
    call.authentication.principal<UserPrincipal>()?.jwtPayload?.getClaim(claim)?.asString()