package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.auth.AuthorizeAnsattSakTilgangForBrev
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.services.*
import no.nav.pensjon.brev.skribenten.usecase.OppdaterBrevHandler

fun Route.brev(
    brevredigeringService: BrevredigeringService,
    pdlService: PdlService,
    penService: PenService,
    brevredigeringFacade: BrevredigeringFacade,
    dto2ApiService: Dto2ApiService,
) {

    suspend fun RoutingContext.respond(brevResponse: Dto.Brevredigering?) {
        if (brevResponse != null) {
            call.respond(dto2ApiService.toApi(brevResponse))
        } else {
            call.respond(HttpStatusCode.NotFound, "Fant ikke brev")
        }
    }

    route("/brev/{brevId}") {
        install(AuthorizeAnsattSakTilgangForBrev) {
            this.pdlService = pdlService
            this.penService = penService
            this.brevredigeringService = brevredigeringService
        }

        get("/info") {
            val brevId = call.parameters.getOrFail<Long>("brevId")
            val brev = brevredigeringService.hentBrevInfo(brevId)?.let { dto2ApiService.toApi(it) }

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
                    brevId = call.parameters.getOrFail<Long>("brevId"),
                    nyeSaksbehandlerValg = null,
                    nyttRedigertbrev = request,
                    frigiReservasjon = frigiReservasjon,
                )
            )
            apiRespond(dto2ApiService, resultat)
        }

        get("/reservasjon") {
            val brevId = call.parameters.getOrFail<Long>("brevId")
            brevredigeringService.fornyReservasjon(brevId)
                ?.also { call.respond(it) }
                ?: call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
        }

        post("/tilbakestill") {
            val brevId = call.parameters.getOrFail<Long>("brevId")
            respond(brevredigeringService.tilbakestill(brevId))
        }
    }
}
