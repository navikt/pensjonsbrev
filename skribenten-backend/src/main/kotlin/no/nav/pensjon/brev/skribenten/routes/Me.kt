package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.getOrFail
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.BekreftNyhetsvarselHandler
import no.nav.pensjon.brev.skribenten.FjernFavorittHandler
import no.nav.pensjon.brev.skribenten.HentBekreftedeNyhetsvarslerHandler
import no.nav.pensjon.brev.skribenten.HentFavoritterHandler
import no.nav.pensjon.brev.skribenten.LeggTilFavorittHandler
import no.nav.pensjon.brev.skribenten.common.asSuccess
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.NyhetsvarselId
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brev.skribenten.services.NavansattService

context(app: Application)
fun Route.meRoute() {
    val navansattService: NavansattService by app.dependencies

    route("/me") {
        get("/userinfo") {
            val p = principal()
            call.respond(Api.UserInfo(
                name = p.fullName,
                navident = p.navIdent,
                erAttestant = p.isAttestant(),
            ))
        }

        val leggTilFavorittHandler: LeggTilFavorittHandler by app.dependencies
        post("/favourites") {
            val leggTil = leggTilFavorittHandler(LeggTilFavorittHandler.Request(principal().navIdent, RedigerbarBrevkode(call.receive<String>())))
            respondSuccess(leggTil?.asSuccess()) { respond(it) }
        }

        val fjernFavorittHandler: FjernFavorittHandler by app.dependencies
        delete("/favourites") {
            val fjern = fjernFavorittHandler(FjernFavorittHandler.Request(principal().navIdent, RedigerbarBrevkode(call.receive<String>())))
            respondSuccess(fjern?.asSuccess()) { respond(it) }
        }

        val hentFavoritterHandler: HentFavoritterHandler by app.dependencies
        get("/favourites") {
            val favoritter = hentFavoritterHandler(HentFavoritterHandler.Request(principal().navIdent))
            respondSuccess(favoritter?.asSuccess()) { respond(it) }
        }

        get("/enheter") {
            call.respond(navansattService.hentNavAnsattEnhetListe(principal().navIdent))
        }

        val hentBekreftedeNyhetsvarslerHandler: HentBekreftedeNyhetsvarslerHandler by app.dependencies
        get("/bekreftedeNyhetsvarsler") {
            val bekreftede = hentBekreftedeNyhetsvarslerHandler(HentBekreftedeNyhetsvarslerHandler.Request(principal().navIdent))
            respondSuccess(bekreftede?.asSuccess()) { respond(it) }
        }

        val bekreftNyhetsvarselHandler: BekreftNyhetsvarselHandler by app.dependencies
        put("/bekreftedeNyhetsvarsler/{nyhetsvarselId}") {
            val bekreft = bekreftNyhetsvarselHandler(
                BekreftNyhetsvarselHandler.Request(principal().navIdent, NyhetsvarselId(call.parameters.getOrFail("nyhetsvarselId")))
            )
            respondSuccess(bekreft?.asSuccess()) { respond(HttpStatusCode.NoContent) }
        }
    }
}