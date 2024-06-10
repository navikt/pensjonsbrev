package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.skribenten.auth.AuthorizeAnsattSakTilgang
import no.nav.pensjon.brev.skribenten.db.Brevredigering
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.services.BrevredigeringService
import no.nav.pensjon.brev.skribenten.services.PenService
import java.time.LocalDateTime

fun Route.sakBrev(brevredigeringService: BrevredigeringService) =
    route("/brev") {
        post<OpprettBrevRequest> { request ->
            val sak: PenService.SakSelection = call.attributes[AuthorizeAnsattSakTilgang.sakKey]

            brevredigeringService.opprettBrev(call, sak, request.brevkode, request.saksbehandlerValg) {
                OppprettBrevResponse(
                    id = id.value,
                    redigertBrev = redigertBrev,
                    sistredigert = sistredigert,
                    brevkode = Brevkode.Redigerbar.valueOf(brevkode),
                    saksbehandlerValg = saksbehandlerValg,
                )
            }.map { brev ->
                call.respond(HttpStatusCode.OK, brev)
            }.catch { message, statusCode ->
                call.application.log.error("$statusCode - Feil ved oppretting av brev ${request.brevkode}: $message")
                call.respond(HttpStatusCode.InternalServerError, "Feil ved oppretting av brev.")
            }
        }

        post<OppdaterBrevRequest>("/{brevId}") { request ->
            val brevId = call.parameters.getOrFail<Long>("brevId")
            brevredigeringService.oppdaterBrev(call, brevId, request.brevkode, request.saksbehandlerValg, request.redigertBrev) {
                OppprettBrevResponse(
                    id = brevId,
                    redigertBrev = redigertBrev,
                    sistredigert = sistredigert,
                    brevkode = request.brevkode,
                    saksbehandlerValg = saksbehandlerValg
                )
            }.map { brev ->
                call.respond(HttpStatusCode.OK, brev!!)
            }.catch { message, statusCode ->
                call.application.log.error("$statusCode - Feil ved oppdatering av brev ${request.brevkode}: $message")
                call.respond(HttpStatusCode.InternalServerError, "Feil ved oppdatering av brev.")
            }
        }

        delete("/{brevId}") {
            val brevId = call.parameters.getOrFail<Long>("brevId")
            brevredigeringService.slettBrev(brevId).map {
                call.respond(HttpStatusCode.OK)
            }.catch { message, httpStatusCode ->
                call.application.log.error("$httpStatusCode - Feil ved sletting av brev med id: $brevId, $message")
            }
        }

        get("/{brevId}") {
            val brevId = call.parameters.getOrFail<Long>("brevId")
            val response = brevredigeringService.hentBrev(brevId, mapper)

            if (response != null) {
                call.respond(HttpStatusCode.OK, response)
            } else {
                call.respond(HttpStatusCode.NotFound, "Brev not found")
            }
        }

        get("/{navident}") {
            val navident = call.parameters.getOrFail<String>("navident")
            val response = brevredigeringService.hentSaksbehandlersBrev(navident, mapper)
            if (response != null) {
                call.respond(HttpStatusCode.OK, response)
            } else {
                call.respond(HttpStatusCode.NotFound,  "Feil ved henting av saksbehandlers brev for $navident")
            }
        }
    }


private val mapper: Brevredigering.() -> OppprettBrevResponse = {
    OppprettBrevResponse(
        id = id.value,
        redigertBrev = redigertBrev,
        sistredigert = sistredigert,
        brevkode = Brevkode.Redigerbar.valueOf(brevkode),
        saksbehandlerValg = saksbehandlerValg,
    )
}

// TODO: Skriv tester FFS


data class GeneriskRedigerbarBrevdata(override val pesysData: BrevbakerBrevdata, override val saksbehandlerValg: BrevbakerBrevdata) :
    RedigerbarBrevdata<BrevbakerBrevdata, BrevbakerBrevdata>


data class OpprettBrevRequest(
    val brevkode: Brevkode.Redigerbar,
    val saksbehandlerValg: BrevbakerBrevdata,
)

data class OppdaterBrevRequest(
    val brevkode: Brevkode.Redigerbar,
    val saksbehandlerValg: BrevbakerBrevdata,
    val redigertBrev: Edit.Letter,
)

data class OppprettBrevResponse(
    val id: Long,
    val redigertBrev: Edit.Letter,
    val sistredigert: LocalDateTime,
    val brevkode: Brevkode.Redigerbar,
    val saksbehandlerValg: BrevbakerBrevdata,
)















