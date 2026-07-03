package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.brevredigering.domain.*
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.model.BrevId
import org.jetbrains.exposed.v1.jdbc.Database
import java.time.Instant

class ReserverBrevHandler(
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    database: Database,
) : TransactionHandler<ReserverBrevHandler.Request, Reservasjon, BrevredigeringError>(database) {

    data class Request(override val brevId: BrevId) : BrevredigeringRequest

    override suspend fun execute(request: Request): Outcome<Reservasjon, BrevredigeringError>? =
        BrevredigeringEntity.findById(request.brevId)
            ?.reserver(Instant.now(), PrincipalInContext.require().navIdent, brevreservasjonPolicy)

}