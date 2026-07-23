package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import org.jetbrains.exposed.v1.jdbc.Database

class LeggVedFoerstesideHandler(
    private val redigerBrevPolicy: RedigerBrevPolicy,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    reserverBrevHandler: ReserverBrevHandler,
    database: Database,
) : ReservertBrevHandler<LeggVedFoerstesideHandler.Request, Dto.BrevInfo>(database, reserverBrevHandler) {
    data class Request(override val brevId: BrevId, val leggVedFoersteside: Boolean) : BrevredigeringRequest

    override suspend fun execute(request: Request): Outcome<Dto.BrevInfo, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null
        redigerBrevPolicy.kanRedigere(brev, PrincipalInContext.require()).onError { return failure(it) }
        brev.leggVedFoersteside = request.leggVedFoersteside
        brev.frigiReservasjon()
        return success(brev.toBrevInfo(brevreservasjonPolicy))
    }

    override fun requiresReservasjon(request: Request) = true
}
