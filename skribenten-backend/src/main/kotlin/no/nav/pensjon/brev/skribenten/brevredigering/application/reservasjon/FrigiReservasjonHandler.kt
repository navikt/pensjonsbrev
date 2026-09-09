package no.nav.pensjon.brev.skribenten.brevredigering.application.reservasjon

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId
import org.slf4j.LoggerFactory

class FrigiReservasjonHandler(
    private val brevtilgang: Brevtilgang,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
) {

    data class Request(val brevId: BrevId, val saksId: SaksId)

    suspend operator fun invoke(request: Request): Outcome<Unit, BrevredigeringError>? =
        brevtilgang.forLesing(request.brevId, request.saksId) {
            val principal = PrincipalInContext.require()
            val reservasjon = brev.gjeldendeReservasjon(brevreservasjonPolicy) ?: return@forLesing success(Unit)

            if (reservasjon.reservertAv != principal.navIdent) {
                logger.info(
                    "Ignorerer forsøk på å frigi reservasjon for brevId={} av bruker={} fordi reservasjonen eies av={}.",
                    request.brevId,
                    principal.navIdent,
                    reservasjon.reservertAv,
                )
                return@forLesing success(Unit)
            }

            brev.frigiReservasjon()
            success(Unit)
        }

    private companion object {
        private val logger = LoggerFactory.getLogger(FrigiReservasjonHandler::class.java)
    }
}
