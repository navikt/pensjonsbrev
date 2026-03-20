package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.auth.AuthorizeAnsattSakTilgangForBrev
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringFacade
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.OppdaterBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.ReserverBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.TilbakestillBrevHandler
import no.nav.pensjon.brev.skribenten.fagsystem.FagsakService
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.services.Dto2ApiService
import no.nav.pensjon.brev.skribenten.services.PdlService

fun Route.brev(
    pdlService: PdlService,
    fagsakService: FagsakService,
    brevredigeringFacade: BrevredigeringFacade,
    dto2ApiService: Dto2ApiService,
) {
    route("/brev/{brevId}") {
        install(AuthorizeAnsattSakTilgangForBrev) {
            this.pdlService = pdlService
            this.fagsakService = fagsakService
            this.hentBrevService = brevredigeringFacade
        }

        get("/info") {
            val brevId = call.parameters.brevId()
            val brev = brevredigeringFacade.hentBrevInfo(brevId)?.let { dto2ApiService.toApi(it) }

            if (brev != null) {
                call.respond(HttpStatusCode.OK, brev)
            } else {
                call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
            }
        }

        put<Edit.Letter>("/redigertBrev") { request ->
            val frigiReservasjon = call.request.queryParameters["frigiReservasjon"].toBoolean()
            val resultat = brevredigeringFacade.oppdaterBrev(
                OppdaterBrevHandler.Request(
                    brevId = call.parameters.brevId(),
                    nyeSaksbehandlerValg = null,
                    nyttRedigertbrev = request,
                    frigiReservasjon = frigiReservasjon,
                )
            )
            apiRespond(dto2ApiService, resultat)
        }

        get("/reservasjon") {
            val brevId = call.parameters.brevId()
            val reservasjon = brevredigeringFacade.reserverBrev(ReserverBrevHandler.Request(brevId = brevId))
            apiRespond(dto2ApiService, reservasjon)
        }

        post("/tilbakestill") {
            val brevId = call.parameters.brevId()
            val resultat = brevredigeringFacade.tilbakestillBrev(TilbakestillBrevHandler.Request(brevId = brevId))
            apiRespond(dto2ApiService, resultat)
        }
    }
}

fun Parameters.brevId() = BrevId(getOrFail<Long>("brevId"))