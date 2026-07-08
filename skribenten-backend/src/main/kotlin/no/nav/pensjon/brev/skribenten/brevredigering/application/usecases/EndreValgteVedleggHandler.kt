package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.brevredigering.domain.*
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggBrevkode
import org.jetbrains.exposed.v1.jdbc.Database

class EndreValgteVedleggHandler(
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    private val redigerBrevPolicy: RedigerBrevPolicy,
    reserverBrevHandler: ReserverBrevHandler,
    database: Database,
) : ReservertBrevHandler<EndreValgteVedleggHandler.Request, Dto.Brevredigering>(database, reserverBrevHandler) {

    data class Request(
        override val brevId: BrevId,
        val alltidValgbareVedlegg: List<AlltidValgbartVedleggBrevkode>,
    ) : BrevredigeringRequest

    override suspend fun execute(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null

        val principal = PrincipalInContext.require()
        redigerBrevPolicy.kanRedigere(brev, principal).onError { return failure(it) }

        brev.valgteVedlegg = request.alltidValgbareVedlegg
        brev.frigiReservasjon()

        return success(brev.toDto(brevreservasjonPolicy, null))
    }
}