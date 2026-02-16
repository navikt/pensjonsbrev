package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError

interface UseCaseHandler<Request, Success, Failure> {
    suspend fun handle(request: Request): Outcome<Success, Failure>?
}

interface BrevredigeringHandler<Request : BrevredigeringRequest, Response> : UseCaseHandler<Request, Response, BrevredigeringError> {
    fun requiresReservasjon(request: Request): Boolean = true
}

interface BrevredigeringRequest {
    val brevId: Long
}