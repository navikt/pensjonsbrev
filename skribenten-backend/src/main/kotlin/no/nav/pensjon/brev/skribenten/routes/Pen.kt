package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.services.PenService
import no.nav.pensjon.brev.skribenten.services.respondWithResult


fun Route.penRoute(penService: PenService) {
    route("/pen") {
        //TODO Check access using /tilganger(?). Is there an on behalf of endpoint which checks access?
        get("/sak/{sakId}") {
            val sakId = call.parameters.getOrFail("sakId")
            respondWithResult(penService.hentSak(call, sakId))
        }
    }


}