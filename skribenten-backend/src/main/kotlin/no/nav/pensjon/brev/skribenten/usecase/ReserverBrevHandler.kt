package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.Reservasjon
import java.time.Instant

class ReserverBrevHandler(
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
) : UseCaseHandler<ReserverBrevHandler.Request, Reservasjon, BrevredigeringError> {

    data class Request(override val brevId: Long) : BrevredigeringRequest

    override suspend fun handle(request: Request): Outcome<Reservasjon, BrevredigeringError>? {
        val principal = PrincipalInContext.require()

        return BrevredigeringEntity.findById(request.brevId)
            ?.reserver(Instant.now(), principal.navIdent, brevreservasjonPolicy)
    }

}