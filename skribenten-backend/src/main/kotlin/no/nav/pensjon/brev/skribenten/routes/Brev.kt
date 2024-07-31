package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.SaksbehandlerValg
import no.nav.pensjon.brev.skribenten.services.BrevredigeringService
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("no.nav.brev.skribenten.routes.Brev")

fun Route.brev(brevredigeringService: BrevredigeringService) {

    suspend fun oppdaterBrevOgSvar(call: ApplicationCall, saksbehandlerValg: SaksbehandlerValg? = null, redigertBrev: Edit.Letter? = null) {
        val brevId = call.parameters.getOrFail<Long>("brevId")

        brevredigeringService.oppdaterBrev(call = call, saksId = null, brevId = brevId, nyeSaksbehandlerValg = saksbehandlerValg, nyttRedigertbrev = redigertBrev)
            ?.onOk { brev -> call.respond(HttpStatusCode.OK, brev)}
            ?.onError { message, statusCode ->
                logger.error("$statusCode - Feil ved oppdatering av brev ${brevId}: $message")
                call.respond(HttpStatusCode.InternalServerError, "Feil ved oppdatering av brev.")
            }
            ?: call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
    }

    route("/brev") {
        put<Edit.Letter>("/{brevId}/redigertBrev") { request ->
            oppdaterBrevOgSvar(call, redigertBrev = request)
        }

        put<SaksbehandlerValg>("/{brevId}/saksbehandlerValg") { request ->
            oppdaterBrevOgSvar(call, saksbehandlerValg = request)
        }

        get("/{brevId}/reservasjon") {
            val brevId = call.parameters.getOrFail<Long>("brevId")
            brevredigeringService.fornyReservasjon(call, brevId)
                ?.also { call.respond(it) }
                ?: call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
        }
    }
}