package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.getLoggedInNavIdent
import no.nav.pensjon.brev.skribenten.services.FavouritesService

fun Route.favoritesRoute() {
    val favouritesService = FavouritesService()
    route("/favourites") {

        post {
            getLoggedInNavIdent()?.let { call.respond(favouritesService.addFavourite(it, call.receive<String>())) }
                ?: call.respond(HttpStatusCode.Unauthorized)
        }

        delete {
            getLoggedInNavIdent()?.let { call.respond(favouritesService.removeFavourite(it, call.receive<String>())) }
                ?: call.respond(HttpStatusCode.Unauthorized)
        }

        get {
            getLoggedInNavIdent()?.let { call.respond(favouritesService.getFavourites(it)) }
                ?: call.respond(HttpStatusCode.Unauthorized)
        }
    }
}