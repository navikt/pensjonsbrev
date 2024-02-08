package no.nav.pensjon.brev.skribenten

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.callid.*
import io.ktor.util.pipeline.*
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal

private const val NAV_IDENT_CLAIM_KEY = "NAVident"
private const val LOGGER_IN_PERSON_NAME_KEY = "name"

fun HeadersBuilder.callId(call: ApplicationCall) {
    call.callId?.also {
        append("Nav-Call-Id", it)
        append("X-Request-ID", it)
    }
}

fun PipelineContext<Unit, ApplicationCall>.getLoggedInNavIdent(): String? =
    call.getLoggedInNavIdent()

fun ApplicationCall.getLoggedInNavIdent(): String? =
    getClaim(NAV_IDENT_CLAIM_KEY)

fun PipelineContext<Unit, ApplicationCall>.getLoggedInName(): String? =
    call.getLoggedInName()

fun ApplicationCall.getLoggedInName(): String? =
    getClaim(LOGGER_IN_PERSON_NAME_KEY)

fun PipelineContext<Unit, ApplicationCall>.getClaim(claim: String): String? =
    call.getClaim(claim)

fun PipelineContext<Unit, ApplicationCall>.isInGroup(claim: String): Boolean =
    call.isInGroup(claim)

fun ApplicationCall.isInGroup(claim: String): Boolean =
    authentication.principal<UserPrincipal>()?.jwtPayload?.getClaim("groups")
        ?.asList(String::class.java)?.contains(claim) ?: false

fun ApplicationCall.getClaim(claim: String): String? =
    authentication.principal<UserPrincipal>()?.jwtPayload?.getClaim(claim)?.asString()