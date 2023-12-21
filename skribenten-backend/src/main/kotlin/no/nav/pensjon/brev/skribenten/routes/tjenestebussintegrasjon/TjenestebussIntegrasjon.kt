package no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.BestillBrevRequestDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.FinnSamhandlerRequestDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.HentSamhandlerRequestDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.RedigerDokumentRequestDto
import no.nav.pensjon.brev.skribenten.services.ServiceResult
import no.nav.pensjon.brev.skribenten.services.TjenestebussIntegrasjonService

fun Route.tjenestebussIntegrasjonRoute(tjenestebussIntegrasjonService: TjenestebussIntegrasjonService) {

    post("/finnSamhandler") {
        val requestDto = call.receive<FinnSamhandlerRequestDto>()

        val response =
            tjenestebussIntegrasjonService.finnSamhandler(call, requestDto.samhandlerType, requestDto.navn)

        when (response) {
            is ServiceResult.AuthorizationError -> call.respond(HttpStatusCode.Unauthorized, response)
            is ServiceResult.Error -> call.respond(HttpStatusCode.InternalServerError, response)
            is ServiceResult.Ok -> call.respond(HttpStatusCode.OK, response)
        }
    }
    post("/hentSamhandler") {
        val requestDto = call.receive<HentSamhandlerRequestDto>()

        val response =
            tjenestebussIntegrasjonService.hentSamhandler(call, requestDto.idTSSEkstern, requestDto.hentDetaljert)

        when (response) {
            is ServiceResult.AuthorizationError -> call.respond(HttpStatusCode.Unauthorized, response)
            is ServiceResult.Error -> call.respond(HttpStatusCode.InternalServerError, response)
            is ServiceResult.Ok -> call.respond(HttpStatusCode.OK, response)
        }
    }

    post("/bestillbrev") {
        val requestDto = call.receive<BestillBrevRequestDto>()
        when (val response = tjenestebussIntegrasjonService.bestillBrev(call, requestDto)) {
            is ServiceResult.AuthorizationError -> call.respond(HttpStatusCode.Unauthorized, response)
            is ServiceResult.Error -> call.respond(HttpStatusCode.InternalServerError, response)
            is ServiceResult.Ok -> call.respond(HttpStatusCode.OK, response)
        }
    }

    post("/redigerbrev") {
        val requestDto = call.receive<RedigerDokumentRequestDto>()
        when (val response = tjenestebussIntegrasjonService.redigerDokument(call, requestDto.journalpostId, requestDto.dokumentId)) {
            is ServiceResult.AuthorizationError -> call.respond(HttpStatusCode.Unauthorized, response)
            is ServiceResult.Error -> call.respond(HttpStatusCode.InternalServerError, response)
            is ServiceResult.Ok -> call.respond(HttpStatusCode.OK, response)
        }
    }
}
