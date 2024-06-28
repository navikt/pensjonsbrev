package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.auth.AuthorizeAnsattSakTilgang
import no.nav.pensjon.brev.skribenten.db.Brevredigering
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brev.skribenten.services.BrevredigeringService

fun Route.sakBrev(brevredigeringService: BrevredigeringService) =
    route("/brev") {
        post<Api.OpprettBrevRequest> { request ->
            val sak: Pen.SakSelection = call.attributes[AuthorizeAnsattSakTilgang.sakKey]

            brevredigeringService.opprettBrev(call, sak, request.brevkode, request.saksbehandlerValg, ::mapBrev)
                .onOk { brev ->
                    call.respond(HttpStatusCode.Created, brev)
                }.onError { message, statusCode ->
                    call.application.log.error("$statusCode - Feil ved oppretting av brev ${request.brevkode}: $message")
                    call.respond(HttpStatusCode.InternalServerError, "Feil ved oppretting av brev.")
                }
        }

        post<Api.OppdaterBrevRequest>("/{brevId}") { request ->
            val brevId = call.parameters.getOrFail<Long>("brevId")
            val sak: Pen.SakSelection = call.attributes[AuthorizeAnsattSakTilgang.sakKey]

            brevredigeringService.oppdaterBrev(call, sak, brevId, request.saksbehandlerValg, request.redigertBrev, ::mapBrev,)
                .onOk { brev ->
                    if (brev == null) {
                        call.respond(HttpStatusCode.NotFound, "Brev med brevid: $brevId ikke funnet")
                    } else call.respond(HttpStatusCode.OK, brev)
                }.onError { message, statusCode ->
                    call.application.log.error("$statusCode - Feil ved oppdatering av brev ${request.brevkode}: $message")
                    call.respond(HttpStatusCode.InternalServerError, "Feil ved oppdatering av brev.")
                }
        }

        delete("/{brevId}") {
            val brevId = call.parameters.getOrFail<Long>("brevId")

            if (brevredigeringService.slettBrev(brevId)) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, "Brev med id $brevId ikke funnet")
            }
        }

        get("/{brevId}") {
            val brevId = call.parameters.getOrFail<Long>("brevId")
            val sak: Pen.SakSelection = call.attributes[AuthorizeAnsattSakTilgang.sakKey]

            brevredigeringService.hentBrev(call, sak, brevId, ::mapBrev,)
                .onOk { brev ->
                    if (brev != null) {
                        call.respond(HttpStatusCode.OK, brev)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Brev not found")
                    }
                }.onError { message, statusCode ->
                    call.application.log.error("$statusCode - Feil ved oppdatering av brev: $message")
                    call.respond(HttpStatusCode.InternalServerError, "Feil ved oppdatering av brev.")
                }
        }

        get {
            val sak: Pen.SakSelection = call.attributes[AuthorizeAnsattSakTilgang.sakKey]

            call.respond(
                HttpStatusCode.OK,
                brevredigeringService.hentBrevForSak(sak.saksId, ::mapBrevInfo)
            )
        }

        post("/{brevId}/ferdigstill") {
            val brevId = call.parameters.getOrFail<Long>("brevId")

            brevredigeringService.ferdigstill(call, brevId)
            call.respond(HttpStatusCode.OK)
        }

        get("/{brevId}/pdf") {
            val brevId = call.parameters.getOrFail<Long>("brevId")
            call.respond(HttpStatusCode.OK, brevredigeringService.hentPdf(brevId))
        }
    }

internal fun mapBrev(brev: Brevredigering): Api.BrevResponse = with(brev) {
    Api.BrevResponse(
        info = mapBrevInfo(this),
        redigertBrev = redigertBrev,
        saksbehandlerValg = saksbehandlerValg,
    )
}

private fun mapBrevInfo(brev: Brevredigering): Api.BrevInfo = with(brev) {
    Api.BrevInfo(
        id = id.value,
        opprettetAv = opprettetAvNavIdent,
        opprettet = opprettet,
        sistredigertAv = sistRedigertAvNavIdent,
        sistredigert = sistredigert,
        brevkode = brevkode,
        redigeresAv = redigeresAvNavIdent,
    )
}
