package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext
import no.nav.pensjon.brev.skribenten.domain.BrevedigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.services.Dto2ApiService
import no.nav.pensjon.brev.skribenten.usecase.Result


context(dto2ApiService: Dto2ApiService)
suspend fun RoutingContext.apiRespond(result: Result<Dto.Brevredigering, BrevedigeringError>?) {
    when (result) {
        is Result.Success -> call.respond(dto2ApiService.toApi(result.value))
        is Result.Failure -> when (result.error) {
            is BrevreservasjonPolicy.ReservertAvAnnen ->
                call.respond(HttpStatusCode.Locked, dto2ApiService.toApi(result.error.eksisterende))
            is RedigerBrevPolicy.KanIkkeRedigere.ArkivertBrev ->
                call.respond(HttpStatusCode.Conflict, "Brev er arkivert med journalpostId: ${result.error.journalpostId}")
            is RedigerBrevPolicy.KanIkkeRedigere.IkkeReservert ->
                call.respond(HttpStatusCode.Conflict, "Brev er ikke reservert for redigering av deg")
            is RedigerBrevPolicy.KanIkkeRedigere.LaastBrev ->
                call.respond(HttpStatusCode.Locked, "Brev er låst for redigering")
        }
        null -> call.respond(HttpStatusCode.NotFound, "Fant ikke brev")
    }
}