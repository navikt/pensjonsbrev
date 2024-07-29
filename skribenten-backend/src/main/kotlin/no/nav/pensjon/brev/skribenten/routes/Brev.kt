package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.services.BrevredigeringService

fun Route.brev(brevredigeringService: BrevredigeringService) {
    route("/brev") {
        put<Edit.Letter>("/{brevId}/redigertBrev") { request ->
            val brevId = call.parameters.getOrFail<Long>("brevId")

            brevredigeringService.oppdaterBrev(call = call, saksId = null, brevId = brevId, nyeSaksbehandlerValg = null, nyttRedigertbrev = request)
                ?.onOk { brev -> call.respond(HttpStatusCode.OK, brev)}
                ?.onError { message, statusCode ->
                    call.application.log.error("$statusCode - Feil ved oppdatering av brev ${brevId}: $message")
                    call.respond(HttpStatusCode.InternalServerError, "Feil ved oppdatering av brev.")
                }
                ?: call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
        }

        get("/{brevId}/reservasjon") {
            val brevId = call.parameters.getOrFail<Long>("brevId")
            brevredigeringService.fornyReservasjon(call, brevId)
                ?.also { call.respond(it) }
                ?: call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
        }
    }
}