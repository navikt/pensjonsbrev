package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.model.BrevId

interface UseCaseHandler<Request, Success, Failure> {
    suspend fun handle(request: Request): Outcome<Success, Failure>?
}

interface BrevredigeringHandler<Request : BrevredigeringRequest, Response> : UseCaseHandler<Request, Response, BrevredigeringError> {
    fun requiresReservasjon(request: Request): Boolean = true
}

interface BrevredigeringRequest {
    val brevId: BrevId
}