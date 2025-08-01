package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.auth.AuthorizeAnsattSakTilgangForBrev
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksbehandlerValg
import no.nav.pensjon.brev.skribenten.services.*
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("no.nav.brev.skribenten.routes.Brev")

fun Route.brev(brevredigeringService: BrevredigeringService, dto2ApiService: Dto2ApiService, pdlService: PdlService, penService: PenService) {

    suspend fun RoutingContext.respond(brevResponse: ServiceResult<Dto.Brevredigering>?) {
        brevResponse?.map { dto2ApiService.toApi(it) }
            ?.onOk { brev -> call.respond(HttpStatusCode.OK, brev) }
            ?.onError { message, statusCode ->
                logger.error("$statusCode - Feil ved oppdatering av brev: $message")
                call.respond(HttpStatusCode.InternalServerError, "Feil ved oppdatering av brev.")
            }
            ?: call.respond(HttpStatusCode.NotFound, "Fant ikke brev")
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
            respond(
                brevredigeringService.oppdaterBrev(
                    saksId = null,
                    brevId = call.parameters.getOrFail<Long>("brevId"),
                    nyeSaksbehandlerValg = null,
                    nyttRedigertbrev = request,
                    frigiReservasjon = frigiReservasjon,
                )
            )
        }

        put<SaksbehandlerValg>("/saksbehandlerValg") { request ->
            val frigiReservasjon = call.request.queryParameters["frigiReservasjon"].toBoolean()
            respond(
                brevredigeringService.oppdaterBrev(
                    saksId = null,
                    brevId = call.parameters.getOrFail<Long>("brevId"),
                    nyeSaksbehandlerValg = request,
                    nyttRedigertbrev = null,
                    signatur = null,
                    frigiReservasjon = frigiReservasjon,
                )
            )
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
