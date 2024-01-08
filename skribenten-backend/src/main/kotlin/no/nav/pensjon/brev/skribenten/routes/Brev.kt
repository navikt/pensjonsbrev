package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.getLoggedInNavIdent
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.BestillBrevRequestDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.SakskontekstDto
import no.nav.pensjon.brev.skribenten.services.PenService
import no.nav.pensjon.brev.skribenten.services.TjenestebussIntegrasjonService

fun Route.bestillBrevRoute(penService: PenService, tjenestebussIntegrasjonService: TjenestebussIntegrasjonService) {
    post("/bestillBrev/extream") {
            // TODO skal vi validere metadata?
            val request = call.receive<OrderLetterRequest>()

            //TODO try to get extra claims when authorizing user instead of using graph service.
            val name = getLoggedInNavIdent()

            // TODO create respond on error or similar function to avoid boilerplate. RespondOnError?


            //TODO better error handling.
            // TODO access controls for e-blanketter
            tjenestebussIntegrasjonService.bestillBrev(call, BestillBrevRequestDto(
                brevKode = "", brevGruppe = "", isRedigerbart = false, sprakkode = "", sakskontekstDto = SakskontekstDto(
                    journalenhet = "0001",
                    gjelder = "",
                    dokumenttype = "",
                    dokumentdato =,
                    fagsystem = "",
                    fagomradekode = "",
                    innhold = "",
                    kategori = "",
                    saksid = "",
                    saksbehandlernavn = "",
                    saksbehandlerId = "",
                    sensitivitet = ""
                )
            ), name, onPremisesSamAccountName).map { journalpostId ->
                val error = safService.waitForJournalpostStatusUnderArbeid(call, journalpostId)
                if (error != null) {
                    if (error.type == SafService.JournalpostLoadingError.ErrorType.TIMEOUT) {
                        call.respond(HttpStatusCode.RequestTimeout, error.error)
                    } else {
                        call.respondText(text = error.error, status = HttpStatusCode.InternalServerError)
                    }
                } else {
                    respondWithResult(penService.redigerExtreamBrev(call, journalpostId))
                }
            }    }

    post("/bestillBrev/doksys") {

    }

}