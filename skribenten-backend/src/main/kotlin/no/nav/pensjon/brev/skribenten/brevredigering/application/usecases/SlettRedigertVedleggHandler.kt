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
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import org.jetbrains.exposed.v1.jdbc.Database

class SlettRedigertVedleggHandler(
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    private val redigerBrevPolicy: RedigerBrevPolicy,
    database: Database,
) : ReservertBrevHandler<SlettRedigertVedleggHandler.Request, Dto.Brevredigering>(database, brevreservasjonPolicy) {

    data class Request(
        override val brevId: BrevId,
        val vedleggId: VedleggId,
    ) : BrevredigeringRequest

    override suspend fun execute(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null

        val principal = PrincipalInContext.require()
        redigerBrevPolicy.kanRedigere(brev, principal).onError { return failure(it) }

        brev.slettRedigertVedlegg(request.vedleggId)
        brev.frigiReservasjon()

        return success(brev.toDto(brevreservasjonPolicy, null))
    }
}
