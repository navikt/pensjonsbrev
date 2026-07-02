package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.model.BrevId
import org.slf4j.LoggerFactory

class FrigiReservasjonHandler(
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
) : UseCaseHandler<FrigiReservasjonHandler.Request, Unit, BrevredigeringError> {

    data class Request(override val brevId: BrevId) : BrevredigeringRequest

    override suspend operator fun invoke(request: Request): Outcome<Unit, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null
        val principal = PrincipalInContext.require()
        val reservasjon = brev.gjeldendeReservasjon(brevreservasjonPolicy) ?: return success(Unit)

        if (reservasjon.reservertAv != principal.navIdent) {
            logger.info(
                "Ignorerer forsøk på å frigi reservasjon for brevId={} av bruker={} fordi reservasjonen eies av={}.",
                request.brevId,
                principal.navIdent,
                reservasjon.reservertAv,
            )
            return success(Unit)
        }

        brev.frigiReservasjon()
        return success(Unit)
    }

    private companion object {
        private val logger = LoggerFactory.getLogger(FrigiReservasjonHandler::class.java)
    }
}