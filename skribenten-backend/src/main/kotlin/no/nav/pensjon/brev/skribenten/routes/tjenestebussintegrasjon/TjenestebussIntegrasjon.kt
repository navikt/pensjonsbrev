package no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.FinnSamhandlerRequestDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.HentSamhandlerRequestDto
import no.nav.pensjon.brev.skribenten.services.TjenestebussIntegrasjonService
import no.nav.pensjon.brev.skribenten.services.respondWithResult

fun Route.tjenestebussIntegrasjonRoute(tjenestebussIntegrasjonService: TjenestebussIntegrasjonService) {

    post("/finnSamhandler") {
        val requestDto = call.receive<FinnSamhandlerRequestDto>()
        respondWithResult(tjenestebussIntegrasjonService.finnSamhandler(call, requestDto.samhandlerType, requestDto.navn))
    }
    post("/hentSamhandler") {
        val requestDto = call.receive<HentSamhandlerRequestDto>()
        respondWithResult(tjenestebussIntegrasjonService.hentSamhandler(call, requestDto.idTSSEkstern, requestDto.hentDetaljert))
    }
}
