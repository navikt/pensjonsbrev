package no.nav.pensjon.brev.skribenten.routes

import com.typesafe.config.Config
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import no.nav.pensjon.brev.skribenten.getLoggedInNavIdent
import no.nav.pensjon.brev.skribenten.services.FavouritesService
import no.nav.pensjon.brev.skribenten.services.NavansattService

fun Route.meRoute(navansattConfig: Config,  authService: AzureADService) {

    val favouritesService = FavouritesService()
    val navansattService = NavansattService(navansattConfig, authService)

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
        get("/enheter") {
            getLoggedInNavIdent()?.let {
                call.respond(navansattService.hentNavAnsattEnhetListe(call, it))
            } ?: call.respond(HttpStatusCode.Unauthorized,"Ikke tilgang ved kall til Navansatt tjenesten")
        }
    }
}