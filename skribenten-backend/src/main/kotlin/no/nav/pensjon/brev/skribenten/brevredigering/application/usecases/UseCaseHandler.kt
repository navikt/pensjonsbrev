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

interface UseCaseHandler<Request, Response, Error> {
    suspend operator fun invoke(request: Request): Outcome<Response, Error>?
}

abstract class TransactionHandler<Request, Response, Error>(private val database: Database): UseCaseHandler<Request, Response, Error> {
    abstract suspend fun execute(request: Request): Outcome<Response, Error>?
    open fun transactionIsolation(): Int? = null

    final override suspend fun invoke(request: Request): Outcome<Response, Error>? =
        isolatedTransaction(database = database, isolation = transactionIsolation()) {
            execute(request)?.onError { rollback() }
        }
}

abstract class ReservertBrevHandler<Request : BrevredigeringRequest, Response>(
    private val database: Database,
    private val brevreservasjonPolicy: BrevreservasjonPolicy
) : UseCaseHandler<Request, Response, BrevredigeringError> {

    open fun transactionIsolation(): Int? = null
    open fun requiresReservasjon(request: Request) = true
    abstract suspend fun execute(request: Request): Outcome<Response, BrevredigeringError>?

    final override suspend fun invoke(request: Request): Outcome<Response, BrevredigeringError>? {
        if (requiresReservasjon(request)) {
            // Forsøk å reservere brevet før vi kjører handleren, om reservasjonen feiler returner feilen eller om brevet ikke finnes returner null.
            reserverBrev(request)
                ?.onError { return failure(it) }
                ?: return null
        }

        return isolatedTransaction(database = database, isolation = transactionIsolation()) {
            execute(request)?.onError { rollback() }
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

private suspend fun <T> isolatedTransaction(database: Database, isolation: Int?, block: suspend JdbcTransaction.() -> T): T {
    return if (isolation != null) {
        suspendTransaction(db = database, transactionIsolation = isolation, statement = block)
    } else {
        suspendTransaction(db = database, statement = block)
    }
}

interface BrevredigeringRequest {
    val brevId: BrevId
}