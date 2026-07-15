package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.HentFavoritterHandler
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brev.skribenten.db.FavouritesRepository
import no.nav.pensjon.brev.skribenten.services.Dto2ApiService
import no.nav.pensjon.brev.skribenten.services.NavansattService

context(app: Application)
fun Route.meRoute() {
    val navansattService: NavansattService by app.dependencies
    val dto2ApiService: Dto2ApiService by app.dependencies
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
            call.respond(favouritesRepository.addFavourite(principal().navIdent, RedigerbarBrevkode(call.receive<String>())))
        }
        delete("/favourites") {
            call.respond(favouritesRepository.removeFavourite(principal().navIdent, RedigerbarBrevkode(call.receive<String>())))
        }

        val hentFavoritterHandler: HentFavoritterHandler by app.dependencies
        get("/favourites") {
            val favoritter = hentFavoritterHandler(HentFavoritterHandler.Request(principal().navIdent))
            respondOutcome(dto2ApiService, favoritter) { respond(it) }
        }

        get("/enheter") {
            call.respond(navansattService.hentNavAnsattEnhetListe(principal().navIdent))
        }
    }
}