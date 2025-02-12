package no.nav.pensjon.brev.skribenten

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.auth.UnauthorizedException
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal

fun ApplicationCall.principal(): UserPrincipal =
    authentication.principal() ?: throw UnauthorizedException("ApplicationCall doesn't have a UserPrincipal")

fun RoutingContext.principal(): UserPrincipal =
    call.principal()
