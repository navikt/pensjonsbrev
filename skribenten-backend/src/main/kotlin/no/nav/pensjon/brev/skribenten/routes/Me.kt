package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brev.skribenten.db.FavouritesRepository
import no.nav.pensjon.brev.skribenten.services.NavansattService

fun Route.meRoute(navansattService: NavansattService) {

    val favouritesRepository = FavouritesRepository()

    route("/me") {
        get("/userinfo") {
            val p = principal()
            call.respond(Api.UserInfo(
                name = p.fullName,
                navident = p.navIdent,
                erAttestant = p.isAttestant(),
            ))
        }
        post("/favourites") {
            call.respond(favouritesRepository.addFavourite(principal().navIdent, call.receive<String>()))
        }
        delete("/favourites") {
            call.respond(favouritesRepository.removeFavourite(principal().navIdent, call.receive<String>()))
        }
        get("/favourites") {
            call.respond(favouritesRepository.getFavourites(principal().navIdent))
        }
        get("/enheter") {
            call.respond(navansattService.hentNavAnsattEnhetListe(principal().navIdent.id))
        }
    }
}