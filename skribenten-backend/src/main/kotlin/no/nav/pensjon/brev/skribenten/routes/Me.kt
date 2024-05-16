package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brev.skribenten.services.FavouritesService
import no.nav.pensjon.brev.skribenten.services.NavansattService
import no.nav.pensjon.brev.skribenten.services.respondWithResult

fun Route.meRoute(navansattService: NavansattService) {

    val favouritesService = FavouritesService()

    route("/me") {
        post("/favourites") {
            call.respond(favouritesService.addFavourite(principal().navIdent, call.receive<String>()))
        }
        delete("/favourites") {
            call.respond(favouritesService.removeFavourite(principal().navIdent, call.receive<String>()))
        }
        get("/favourites") {
            call.respond(favouritesService.getFavourites(principal().navIdent))
        }
        get("/enheter") {
            respondWithResult(navansattService.hentNavAnsattEnhetListe(call, principal().navIdent))
        }
    }
}