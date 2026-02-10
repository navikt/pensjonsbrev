package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.KlarTilSendingPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy.KanIkkeRedigere.LaastBrev
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.success

class VeksleKlarStatusHandler(
    private val klarTilSendingPolicy: KlarTilSendingPolicy,
    private val redigerBrevPolicy: RedigerBrevPolicy,
) : BrevredigeringHandler<VeksleKlarStatusHandler.Request, Dto.Brevredigering> {

    data class Request(override val brevId: Long, val klar: Boolean) : BrevredigeringRequest

    override suspend fun handle(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null

        // Om ingen endring, returner vellykket uten å gjøre noe
        if (brev.laastForRedigering == request.klar) {
            return success(brev.toDto(null))
        }

        val principal = PrincipalInContext.require()
        return if (request.klar) {
            settBrevTilKlar(brev, principal)
        } else {
            settBrevTilKladd(brev, principal)
        }
    }

    private fun settBrevTilKlar(brev: BrevredigeringEntity, principal: UserPrincipal): Outcome<Dto.Brevredigering, BrevredigeringError> {
        redigerBrevPolicy.kanRedigere(brev, principal).onError { return failure(it) }
        klarTilSendingPolicy.kanSettesTilKlar(brev).onError { return failure(it) }

        brev.markerSomKlar()
        brev.redigeresAv = null
        return success(brev.toDto(null))
    }

    private fun settBrevTilKladd(brev: BrevredigeringEntity, principal: UserPrincipal): Outcome<Dto.Brevredigering, BrevredigeringError> {
        redigerBrevPolicy.kanRedigere(brev, principal).onError(ignore = { it is LaastBrev }) { return failure(it) }

        brev.markerSomKladd()
        brev.redigeresAv = null
        return success(brev.toDto(null))
    }

}