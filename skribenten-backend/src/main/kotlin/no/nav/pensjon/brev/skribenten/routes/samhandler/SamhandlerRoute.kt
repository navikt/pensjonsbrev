package no.nav.pensjon.brev.skribenten.routes.samhandler

import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.routes.samhandler.dto.FinnSamhandlerRequestDto
import no.nav.pensjon.brev.skribenten.routes.samhandler.dto.HentSamhandlerAdresseRequestDto
import no.nav.pensjon.brev.skribenten.routes.samhandler.dto.HentSamhandlerRequestDto
import no.nav.pensjon.brev.skribenten.services.SamhandlerService

fun Route.samhandlerRoute(samhandlerService: SamhandlerService) {

    post("/finnSamhandler") {
        val requestDto = call.receive<FinnSamhandlerRequestDto>()
        call.respond(samhandlerService.finnSamhandler(requestDto))
    }
    post("/hentSamhandler") {
        val requestDto = call.receive<HentSamhandlerRequestDto>()
        call.respond(samhandlerService.hentSamhandler(requestDto.idTSSEkstern))
    }
    post("/hentSamhandlerAdresse") {
        val requestDto = call.receive<HentSamhandlerAdresseRequestDto>()
        call.respond(samhandlerService.hentSamhandlerAdresse(requestDto.idTSSEkstern))
    }
}
