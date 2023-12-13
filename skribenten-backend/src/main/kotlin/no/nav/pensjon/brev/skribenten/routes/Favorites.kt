package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.getLoggedInUserId
import no.nav.pensjon.brev.skribenten.services.SkribentenDatabaseService

fun Route.favoritesRoute(databaseService: SkribentenDatabaseService) {
    route("/favourites") {

        post {
            getLoggedInUserId()?.let { call.respond(databaseService.addFavourite(it, call.receive<String>())) }
                ?: call.respond(HttpStatusCode.Unauthorized)
        }

        delete {
            getLoggedInUserId()?.let { call.respond(databaseService.removeFavourite(it, call.receive<String>())) }
                ?: call.respond(HttpStatusCode.Unauthorized)
        }

        get {
            getLoggedInUserId()?.let { call.respond(databaseService.getFavourites(it)) }
                ?: call.respond(HttpStatusCode.Unauthorized)
        }
    }
}