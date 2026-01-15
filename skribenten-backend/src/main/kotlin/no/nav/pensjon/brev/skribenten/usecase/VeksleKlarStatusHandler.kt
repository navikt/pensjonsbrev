package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.KlarTilSendingPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.success
import java.time.Instant

class VeksleKlarStatusHandler(
    private val klarTilSendingPolicy: KlarTilSendingPolicy,
    private val redigerBrevPolicy: RedigerBrevPolicy,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
) {

    data class Request(val brevId: Long, val klar: Boolean)

    suspend fun handle(req: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(req.brevId) ?: return null

        if (brev.laastForRedigering == req.klar) {
            return success(brev.toDto(null))
        }

        val principal = PrincipalInContext.require()
        return if (req.klar) {
            settBrevTilKlar(brev, principal)
        } else {
            settBrevTilKladd(brev, principal)
        }
    }

    private fun settBrevTilKlar(brev: BrevredigeringEntity, principal: UserPrincipal): Outcome<Dto.Brevredigering, BrevredigeringError> {
        brev.reserver(Instant.now(), principal.navIdent, brevreservasjonPolicy).onError { return failure(it) }
        redigerBrevPolicy.kanRedigere(brev, principal).onError { return failure(it) }
        klarTilSendingPolicy.kanSettesTilKlar(brev).onError { return failure(it) }

        brev.markerSomKlar()
        brev.redigeresAv = null
        return success(brev.toDto(null))
    }

    private fun settBrevTilKladd(brev: BrevredigeringEntity, principal: UserPrincipal): Outcome<Dto.Brevredigering, BrevredigeringError> {
        brev.reserver(Instant.now(), principal.navIdent, brevreservasjonPolicy).onError { return failure(it) }
        redigerBrevPolicy.kanRedigere(brev, principal).onError {
            // Formålet er å låse opp brevet, så vi ignorerer denne feilen
            if (it !is RedigerBrevPolicy.KanIkkeRedigere.LaastBrev) {
                return failure(it)
            }
        }

        brev.markerSomKladd()
        brev.redigeresAv = null
        return success(brev.toDto(null))
    }

}