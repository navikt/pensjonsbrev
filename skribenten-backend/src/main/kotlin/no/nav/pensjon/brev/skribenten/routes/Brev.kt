package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.auth.UnauthorizedException
import no.nav.pensjon.brev.skribenten.getLoggedInName
import no.nav.pensjon.brev.skribenten.getLoggedInNavIdent
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.OrderLetterRequest

fun Route.bestillBrevRoute(
    legacyBrevService: LegacyBrevService
) {

    post("/bestillbrev") {
        val request = call.receive<OrderLetterRequest>()
        val navIdent = getLoggedInNavIdent() ?: throw UnauthorizedException("Fant ikke ident på innlogget bruker i claim")
        val navn = getLoggedInName() ?: throw UnauthorizedException("Fant ikke navn på innlogget bruker i claim")

        call.respond(
            legacyBrevService.bestillBrev(
                call = call,
                request = request,
                navIdent = navIdent,
                navn = navn
            )
        )
    }
}
