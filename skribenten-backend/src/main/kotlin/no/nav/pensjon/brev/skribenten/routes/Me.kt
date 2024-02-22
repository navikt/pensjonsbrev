package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.getLoggedInNavIdent
import no.nav.pensjon.brev.skribenten.services.FavouritesService

fun Route.meRoute() {

    val favouritesService = FavouritesService()

    route("/me") {
        post("/favourites") {
            getLoggedInNavIdent()?.let { call.respond(favouritesService.addFavourite(it, call.receive<String>())) }
                ?: call.respond(HttpStatusCode.Unauthorized)
        }
        delete("/favourites") {
            getLoggedInNavIdent()?.let { call.respond(favouritesService.removeFavourite(it, call.receive<String>())) }
                ?: call.respond(HttpStatusCode.Unauthorized)
        }
        get("/favourites") {
            getLoggedInNavIdent()?.let { call.respond(favouritesService.getFavourites(it)) }
                ?: call.respond(HttpStatusCode.Unauthorized)
        }
    }
}