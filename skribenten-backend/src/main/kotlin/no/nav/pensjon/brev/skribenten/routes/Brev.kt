package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.auth.AuthorizeAnsattSakTilgangForBrev
import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksbehandlerValg
import no.nav.pensjon.brev.skribenten.services.*
import no.nav.pensjon.brev.skribenten.usecase.UpdateLetterHandler

context(dto2ApiService: Dto2ApiService)
fun Route.brev(
    brevredigeringService: BrevredigeringService,
    pdlService: PdlService,
    penService: PenService,
    brevredigeringFacade: BrevredigeringFacade,
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
                UpdateLetterHandler.Request(
                    brevId = call.parameters.getOrFail<Long>("brevId"),
                    saksbehandler = PrincipalInContext.require().navIdent,
                    nyeSaksbehandlerValg = null,
                    nyttRedigertbrev = request,
                    frigiReservasjon = frigiReservasjon,
                )
            )
            apiRespond(resultat)
        }

        put<SaksbehandlerValg>("/saksbehandlerValg") { request ->
            val frigiReservasjon = call.request.queryParameters["frigiReservasjon"].toBoolean()
            val resultat = brevredigeringFacade.oppdaterBrev(
                UpdateLetterHandler.Request(
                    brevId = call.parameters.getOrFail<Long>("brevId"),
                    saksbehandler = PrincipalInContext.require().navIdent,
                    nyeSaksbehandlerValg = request,
                    nyttRedigertbrev = null,
                    frigiReservasjon = frigiReservasjon,
                )
            )
            apiRespond(resultat)
        }

        put<String>("/signatur") { signatur ->
            if (signatur.trim().isNotEmpty()) {
                respond(brevredigeringService.oppdaterSignatur(brevId = call.parameters.getOrFail<Long>("brevId"), signaturSignerende = signatur))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Signatur kan ikke være tom")
            }
        }

        put<String>("/attestant") { signatur ->
            if (signatur.trim().isNotEmpty()) {
                respond(brevredigeringService.oppdaterSignaturAttestant(brevId = call.parameters.getOrFail<Long>("brevId"), signaturAttestant = signatur))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Signatur kan ikke være tom")
            }
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
