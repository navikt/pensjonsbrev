package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.brevredigering.domain.*
import no.nav.pensjon.brev.skribenten.brevredigering.domain.RedigerBrevPolicy.KanIkkeRedigere.LaastBrev
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.model.*
import org.jetbrains.exposed.v1.jdbc.Database

class EndreDistribusjonstypeHandler(
    private val redigerBrevPolicy: RedigerBrevPolicy,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    reserverBrevHandler: ReserverBrevHandler,
    database: Database,
) : ReservertBrevHandler<EndreDistribusjonstypeHandler.Request, Dto.BrevInfo>(database, reserverBrevHandler) {

    data class Request(override val brevId: BrevId, val type: Distribusjon) : BrevredigeringRequest

    override suspend fun execute(request: Request): Outcome<Dto.BrevInfo, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null
        val principal = PrincipalInContext.require()

        // Utfør kun endring om nødvendig
        if (brev.distribusjonstype != request.type) {
            redigerBrevPolicy.kanRedigere(brev, principal).onError(ignore = { it is LaastBrev }) { return failure(it) }

            brev.distribusjonstype = request.type
            brev.frigiReservasjon()
        }

        return success(brev.toBrevInfo(brevreservasjonPolicy))
    }
}