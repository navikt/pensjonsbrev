package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.getLoggedInNavIdent
import no.nav.pensjon.brev.skribenten.services.NavansattService

fun Route.navansattRoute(navansattService: NavansattService) {
    route("/navansatt") {

        get("/enheter") {
            getLoggedInNavIdent()?.let {
                call.respond(navansattService.hentNavAnsattEnhetListe(it))
            } ?: call.respond(HttpStatusCode.Unauthorized)
        }
    }

}