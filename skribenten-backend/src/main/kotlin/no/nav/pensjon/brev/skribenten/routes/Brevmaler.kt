package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.auth.UnauthorizedException
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.services.BrevmetadataService

fun Route.brevmalerRoute(
    brevmetadataService: BrevmetadataService
) {
    get("/lettertemplates/e-blanketter") {
        val hasAccess = call.principal<UserPrincipal>()?.isPensjonUtlandGroup() ?: false
        if (!hasAccess)  call.respond(HttpStatusCode.Forbidden, "Ingen tilgang til e-blanketter")

        call.respond(brevmetadataService.getEblanketter())
    }
    get("/lettertemplates/{sakType}") {
        val sakType = call.parameters.getOrFail("sakType")
        call.respond(
            brevmetadataService.getRedigerbareBrev(sakType),
        )
    }
}