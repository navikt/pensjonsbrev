package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId

interface UseCaseHandler<Request, Success, Failure> {
    suspend fun handle(request: Request): Outcome<Success, Failure>?
}

interface BrevredigeringHandler<Request : BrevredigeringRequest, Response> : UseCaseHandler<Request, Response, BrevredigeringError> {
    fun requiresReservasjon(request: Request): Boolean
    fun transactionIsolation(): Int? = null
}

interface BrevredigeringRequest {
    val brevId: BrevId
    val saksId: SaksId
}