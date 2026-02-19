package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import io.ktor.server.routing.RoutingContext
import no.nav.brev.BrevExceptionDto
import no.nav.pensjon.brev.skribenten.domain.AttesterBrevPolicy
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.KlarTilSendingPolicy
import no.nav.pensjon.brev.skribenten.domain.OpprettBrevPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.domain.Reservasjon
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.services.Dto2ApiService
import no.nav.pensjon.brev.skribenten.usecase.Outcome

@JvmName("apiRespondBrevredigering")
suspend fun RoutingContext.apiRespond(
    dto2ApiService: Dto2ApiService,
    outcome: Outcome<Dto.Brevredigering, BrevredigeringError>?,
    successStatus: HttpStatusCode = HttpStatusCode.OK,
) {
    respondOutcome(dto2ApiService, outcome) { brevredigering ->
        respond(status = successStatus, dto2ApiService.toApi(brevredigering))
    }
}

@JvmName("apiRespondBrevInfo")
suspend fun RoutingContext.apiRespond(
    dto2ApiService: Dto2ApiService,
    outcome: Outcome<Dto.BrevInfo, BrevredigeringError>?,
    successStatus: HttpStatusCode = HttpStatusCode.OK,
) {
    respondOutcome(dto2ApiService, outcome) { brevInfo ->
        respond(status = successStatus, dto2ApiService.toApi(brevInfo))
    }
}

@JvmName("apiRespondHentDocumentResult")
suspend fun RoutingContext.apiRespond(
    dto2ApiService: Dto2ApiService,
    outcome: Outcome<Dto.HentDocumentResult, BrevredigeringError>?,
    successStatus: HttpStatusCode = HttpStatusCode.OK,
) {
    respondOutcome(dto2ApiService, outcome) {
        respond(status = successStatus, dto2ApiService.toApi(it))
    }
}

@JvmName("apiRespondReservasjon")
suspend fun RoutingContext.apiRespond(
    dto2ApiService: Dto2ApiService,
    outcome: Outcome<Reservasjon, BrevredigeringError>?,
    successStatus: HttpStatusCode = HttpStatusCode.OK,
) {
    respondOutcome(dto2ApiService, outcome) {
        respond(status = successStatus, dto2ApiService.toApi(it))
    }
}

suspend fun <T> RoutingContext.respondOutcome(
    dto2ApiService: Dto2ApiService,
    outcome: Outcome<T, BrevredigeringError>?,
    successResponse: suspend RoutingCall.(T) -> Unit
) {
    when (outcome) {
        is Outcome.Success -> call.successResponse(outcome.value)

        is Outcome.Failure -> when (outcome.error) {
            is BrevreservasjonPolicy.ReservertAvAnnen ->
                call.respond(HttpStatusCode.Locked, dto2ApiService.toApi(outcome.error.eksisterende))

            is RedigerBrevPolicy.KanIkkeRedigere.ArkivertBrev ->
                call.respond(HttpStatusCode.Conflict, "Brev er arkivert med journalpostId: ${outcome.error.journalpostId}")

            // TODO: Muligens burde dette være internal server error siden det burde indikere at koden ikke forsøkte å reserver eller at feil fra reservasjon ble ignorert
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

            is KlarTilSendingPolicy.IkkeKlarTilSending.FritekstFelterUredigert ->
                call.respond(
                    status = HttpStatusCode.UnprocessableEntity,
                    message = BrevExceptionDto(tittel = "Brev ikke klart", melding = "Brevet inneholder fritekst-felter som ikke er endret")
                )

            is AttesterBrevPolicy.KanIkkeAttestere.HarIkkeAttestantrolle ->
                call.respond(HttpStatusCode.Forbidden, "Bruker ${outcome.error.navIdent} har ikke attestantrolle")

            is AttesterBrevPolicy.KanIkkeAttestere.KanIkkeAttestereEgetBrev ->
                call.respond(HttpStatusCode.Forbidden, "Bruker ${outcome.error.navIdent} kan ikke attestere sitt eget brev ${outcome.error.brevId}")

            is AttesterBrevPolicy.KanIkkeAttestere.AlleredeAttestertAvAnnen ->
                call.respond(HttpStatusCode.Conflict, "Brev ${outcome.error.brevId} er allerede attestert av ${outcome.error.attestertAv}")
        }

        null -> call.respond(HttpStatusCode.NotFound, "Fant ikke brev")
    }
}