package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Reservasjon
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.model.BrevId
import java.time.Instant

class ReserverBrevHandler(
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
) : UseCaseHandler<ReserverBrevHandler.Request, Reservasjon, BrevredigeringError> {

    data class Request(override val brevId: BrevId) : BrevredigeringRequest

    override suspend fun handle(request: Request): Outcome<Reservasjon, BrevredigeringError>? {
        val principal = PrincipalInContext.require()

        return BrevredigeringEntity.findById(request.brevId)
            ?.reserver(Instant.now(), principal.navIdent, brevreservasjonPolicy)
    }

}