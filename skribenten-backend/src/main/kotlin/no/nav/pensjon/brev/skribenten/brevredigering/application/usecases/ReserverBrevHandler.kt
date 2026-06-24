package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Reservasjon
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId
import java.time.Instant

class ReserverBrevHandler(
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
) : UseCaseHandler<ReserverBrevHandler.Request, Reservasjon, BrevredigeringError> {

    data class Request(override val brevId: BrevId, override val saksId: SaksId) : BrevredigeringRequest

    override suspend fun handle(request: Request): Outcome<Reservasjon, BrevredigeringError>? {
        val principal = PrincipalInContext.require()

        return BrevredigeringEntity.findByIdAndSaksId(request.brevId, request.saksId)
            ?.reserver(Instant.now(), principal.navIdent, brevreservasjonPolicy)
    }

}