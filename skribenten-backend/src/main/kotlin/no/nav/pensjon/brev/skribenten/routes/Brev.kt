package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.auth.SakKey
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksbehandlerValg
import no.nav.pensjon.brev.skribenten.services.BrevredigeringService
import no.nav.pensjon.brev.skribenten.services.Dto2ApiService
import no.nav.pensjon.brev.skribenten.services.ServiceResult
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("no.nav.brev.skribenten.routes.Brev")

fun Route.brev(brevredigeringService: BrevredigeringService, dto2ApiService: Dto2ApiService) {

    suspend fun RoutingContext.respond(brevResponse: ServiceResult<Dto.Brevredigering>?) {
        brevResponse?.map { dto2ApiService.toApi(it) }
            ?.onOk { brev -> call.respond(HttpStatusCode.OK, brev) }
            ?.onError { message, statusCode ->
                logger.error("$statusCode - Feil ved oppdatering av brev: $message")
                call.respond(HttpStatusCode.InternalServerError, "Feil ved oppdatering av brev.")
            }
            ?: call.respond(HttpStatusCode.NotFound, "Fant ikke brev")
    }

    route("/brev") {
        put<Edit.Letter>("/{brevId}/redigertBrev") { request ->
            val frigiReservasjon = call.parameters["frigiReservasjon"].toBoolean()
            respond(
                brevredigeringService.oppdaterBrev(
                    saksId = null,
                    brevId = call.parameters.getOrFail<Long>("brevId"),
                    nyeSaksbehandlerValg = null,
                    nyttRedigertbrev = request,
                    signatur = null,
                    frigiReservasjon = frigiReservasjon,
                )
            )
        }

        put<SaksbehandlerValg>("/{brevId}/saksbehandlerValg") { request ->
            val frigiReservasjon = call.parameters["frigiReservasjon"].toBoolean()
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

        put<String>("/{brevId}/signatur") { signatur ->
            if (signatur.trim().isNotEmpty()) {
                respond(brevredigeringService.oppdaterSignatur(brevId = call.parameters.getOrFail<Long>("brevId"), signaturSignerende = signatur))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Signatur kan ikke være tom")
            }
        }

        route("/{brevId}/attestant") {
            put<String> { signatur ->
                if (signatur.trim().isNotEmpty()) {
                    respond(brevredigeringService.oppdaterSignaturAttestant(brevId = call.parameters.getOrFail<Long>("brevId"), signaturAttestant = signatur))
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Signatur kan ikke være tom")
                }
            }
            get {
                // TODO: Er kanskje ikke nødvendig. Kommer an på hvordan `GET /sak/{saksId}/brev/{brevId}/attester` blir.
                val brevId = call.parameters.getOrFail<Long>("brevId")
                val saksId = call.attributes[SakKey].saksId
                brevredigeringService.hentSignaturAttestant(saksId = saksId, brevId = brevId)
                    ?.onOk { lagretSignatur ->
                        if (lagretSignatur != null) {
                            call.respond(lagretSignatur)
                        } else {
                            val principal = PrincipalInContext.require()
                            call.respond(dto2ApiService.hentNavAnsatt(principal.navIdent).navn ?: principal.fullName)
                        }
                    }
                    ?.onError { error, _ -> call.respond(HttpStatusCode.InternalServerError, error) }
                    ?: call.respond(HttpStatusCode.NotFound, "Fant ikke brev")
            }
        }

        get("/{brevId}/reservasjon") {
            val brevId = call.parameters.getOrFail<Long>("brevId")
            brevredigeringService.fornyReservasjon(brevId)
                ?.also { call.respond(it) }
                ?: call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
        }

        post("/{brevId}/tilbakestill") {
            val brevId = call.parameters.getOrFail<Long>("brevId")
            respond(brevredigeringService.tilbakestill(brevId))
        }
    }
}
