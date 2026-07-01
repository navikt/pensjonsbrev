package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.model.BrevId
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import java.sql.Connection
import java.time.Instant

interface UseCaseHandler<Request, Success, Failure> {
    suspend fun handle(request: Request): Outcome<Success, Failure>?
}

interface BrevredigeringHandler<Request : BrevredigeringRequest, Response> : UseCaseHandler<Request, Response, BrevredigeringError> {
    fun requiresReservasjon(request: Request): Boolean
    fun transactionIsolation(): Int? = null
}

abstract class ReservertBrevHandler<Request : BrevredigeringRequest, Response>(
    private val database: Database,
    private val brevreservasjonPolicy: BrevreservasjonPolicy
) : BrevredigeringHandler<Request, Response> {

    suspend operator fun invoke(request: Request): Outcome<Response, BrevredigeringError>? = handle(request)

    final override suspend fun handle(request: Request): Outcome<Response, BrevredigeringError>? {
        if (requiresReservasjon(request)) {
            // Forsøk å reservere brevet før vi kjører handleren, om reservasjonen feiler returner feilen eller om brevet ikke finnes returner null.
            reserverBrev(request)
                ?.onError { return failure(it) }
                ?: return null
        }

        return isolatedTransaction {
            execute(request)?.onError { rollback() }
        }
    }

    abstract suspend fun execute(request: Request): Outcome<Response, BrevredigeringError>?

    private suspend fun <T> isolatedTransaction(block: suspend JdbcTransaction.() -> T): T {
        val isolation = transactionIsolation()
        return if (isolation != null) {
            suspendTransaction(db = database, transactionIsolation = isolation, statement = block)
        } else {
            suspendTransaction(db = database, statement = block)
        }
    }

    private suspend fun reserverBrev(request: Request) =
        suspendTransaction(db = database, transactionIsolation = Connection.TRANSACTION_REPEATABLE_READ) {
            val principal = PrincipalInContext.require()

            BrevredigeringEntity.findById(request.brevId)
                ?.reserver(Instant.now(), principal.navIdent, brevreservasjonPolicy)
                ?.onError { rollback() }
        }
}

interface BrevredigeringRequest {
    val brevId: BrevId
}