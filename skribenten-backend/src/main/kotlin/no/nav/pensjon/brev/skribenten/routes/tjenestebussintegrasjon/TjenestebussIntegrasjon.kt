package no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.FinnSamhandlerRequestDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.HentSamhandlerRequestDto
import no.nav.pensjon.brev.skribenten.services.TjenestebussIntegrasjonService

fun Route.tjenestebussIntegrasjonRoute(tjenestebussIntegrasjonService: TjenestebussIntegrasjonService) {

    post("/finnSamhandler") {
        val requestDto = call.receive<FinnSamhandlerRequestDto>()
        call.respond(tjenestebussIntegrasjonService.finnSamhandler(call, requestDto.samhandlerType, requestDto.navn))
    }
    post("/hentSamhandler") {
        val requestDto = call.receive<HentSamhandlerRequestDto>()
        call.respond(tjenestebussIntegrasjonService.hentSamhandler(call, requestDto.idTSSEkstern, requestDto.hentDetaljert))
    }
}
