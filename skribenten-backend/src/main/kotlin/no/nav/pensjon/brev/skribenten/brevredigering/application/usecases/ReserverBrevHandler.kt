package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.brevredigering.domain.*
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId
import org.jetbrains.exposed.v1.jdbc.Database
import java.sql.Connection
import java.time.Instant

class ReserverBrevHandler(
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    database: Database,
) : TransactionHandler<BrevredigeringRequest, Reservasjon, BrevredigeringError>(database) {

    data class Request(override val brevId: BrevId, override val saksId: SaksId) : BrevredigeringRequest

    override fun transactionIsolation() = Connection.TRANSACTION_REPEATABLE_READ

    override suspend fun execute(request: BrevredigeringRequest): Outcome<Reservasjon, BrevredigeringError>? =
        BrevredigeringEntity.findByIdAndSaksId(request.brevId, request.saksId)
            ?.reserver(Instant.now(), PrincipalInContext.require().navIdent, brevreservasjonPolicy)

}