package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.auth.*
import no.nav.pensjon.brev.skribenten.brevredigering.domain.*
import no.nav.pensjon.brev.skribenten.brevredigering.domain.RedigerBrevPolicy.KanIkkeRedigere.LaastBrev
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.model.*
import org.jetbrains.exposed.v1.jdbc.Database

class VeksleKlarStatusHandler(
    private val ferdigRedigertPolicy: FerdigRedigertPolicy,
    private val redigerBrevPolicy: RedigerBrevPolicy,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    reserverBrevHandler: ReserverBrevHandler,
    database: Database,
) : ReservertBrevHandler<VeksleKlarStatusHandler.Request, Dto.BrevInfo>(database, reserverBrevHandler) {

    data class Request(override val brevId: BrevId, val klar: Boolean) : BrevredigeringRequest

    override suspend fun execute(request: Request): Outcome<Dto.BrevInfo, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null

        // Om ingen endring, returner vellykket uten å gjøre noe
        if (brev.laastForRedigering == request.klar) {
            return success(brev.toBrevInfo(brevreservasjonPolicy))
        }

        val principal = PrincipalInContext.require()
        return if (request.klar) {
            settBrevTilKlar(brev, principal)
        } else {
            settBrevTilKladd(brev, principal)
        }
    }

    private suspend fun settBrevTilKlar(brev: BrevredigeringEntity, principal: UserPrincipal): Outcome<Dto.BrevInfo, BrevredigeringError> {
        redigerBrevPolicy.kanRedigere(brev, principal).onError { return failure(it) }
        ferdigRedigertPolicy.erFerdigRedigert(brev).onError { return failure(it) }

        brev.markerSomKlar()
        brev.frigiReservasjon()
        return success(brev.toBrevInfo(brevreservasjonPolicy))
    }

    private fun settBrevTilKladd(brev: BrevredigeringEntity, principal: UserPrincipal): Outcome<Dto.BrevInfo, BrevredigeringError> {
        redigerBrevPolicy.kanRedigere(brev, principal).onError(ignore = { it is LaastBrev }) { return failure(it) }

        brev.markerSomKladd()
        brev.frigiReservasjon()
        return success(brev.toBrevInfo(brevreservasjonPolicy))
    }

}