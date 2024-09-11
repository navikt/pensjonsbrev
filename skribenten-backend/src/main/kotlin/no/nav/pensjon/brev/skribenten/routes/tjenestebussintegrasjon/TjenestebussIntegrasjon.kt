package no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.FinnSamhandlerRequestDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.HentSamhandlerAdresseRequestDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.HentSamhandlerRequestDto
import no.nav.pensjon.brev.skribenten.services.SamhandlerService
import no.nav.pensjon.brev.skribenten.services.TjenestebussIntegrasjonService

fun Route.tjenestebussIntegrasjonRoute(samhandlerService: SamhandlerService, tjenestebussIntegrasjonService: TjenestebussIntegrasjonService) {

    post("/finnSamhandler") {
        val requestDto = call.receive<FinnSamhandlerRequestDto>()
        call.respond(samhandlerService.finnSamhandler(call, requestDto))
    }
    post("/hentSamhandler") {
        val requestDto = call.receive<HentSamhandlerRequestDto>()
        call.respond(samhandlerService.hentSamhandler(call, requestDto.idTSSEkstern))
    }
    post("/hentSamhandlerAdresse") {
        val requestDto = call.receive<HentSamhandlerAdresseRequestDto>()
        call.respond(tjenestebussIntegrasjonService.hentSamhandlerAdresse(call, requestDto.idTSSEkstern))
    }
}
