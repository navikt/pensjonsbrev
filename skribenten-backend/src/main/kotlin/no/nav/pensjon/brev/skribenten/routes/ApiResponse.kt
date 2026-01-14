package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.OpprettBrevPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.services.Dto2ApiService
import no.nav.pensjon.brev.skribenten.usecase.Outcome


suspend fun RoutingContext.apiRespond(
    dto2ApiService: Dto2ApiService,
    outcome: Outcome<Dto.Brevredigering, BrevredigeringError>?,
    successStatus: HttpStatusCode = HttpStatusCode.OK
) {
    when (outcome) {
        is Outcome.Success -> call.respond(status = successStatus, dto2ApiService.toApi(outcome.value))
        is Outcome.Failure -> when (outcome.error) {
            is BrevreservasjonPolicy.ReservertAvAnnen ->
                call.respond(HttpStatusCode.Locked, dto2ApiService.toApi(outcome.error.eksisterende))

            is RedigerBrevPolicy.KanIkkeRedigere.ArkivertBrev ->
                call.respond(HttpStatusCode.Conflict, "Brev er arkivert med journalpostId: ${outcome.error.journalpostId}")

            is RedigerBrevPolicy.KanIkkeRedigere.IkkeReservert ->
                call.respond(HttpStatusCode.Conflict, "Brev er ikke reservert for redigering av deg")

            is RedigerBrevPolicy.KanIkkeRedigere.LaastBrev ->
                call.respond(HttpStatusCode.Locked, "Brev er låst for redigering")

            is OpprettBrevPolicy.KanIkkeOppretteBrev.BrevmalFinnesIkke ->
                call.respond(HttpStatusCode.BadRequest, "Brevmal finnes ikke: ${outcome.error.brevkode}")

            is OpprettBrevPolicy.KanIkkeOppretteBrev.BrevmalKreverVedtaksId ->
                call.respond(HttpStatusCode.BadRequest, "Brevmal krever vedtaksId: ${outcome.error.brevkode}")

            is OpprettBrevPolicy.KanIkkeOppretteBrev.IkkeTilgangTilEnhet ->
                call.respond(HttpStatusCode.BadRequest, "Ikke tilgang til enhet: ${outcome.error.enhetsId}")
        }
        null -> call.respond(HttpStatusCode.NotFound, "Fant ikke brev")
    }
}