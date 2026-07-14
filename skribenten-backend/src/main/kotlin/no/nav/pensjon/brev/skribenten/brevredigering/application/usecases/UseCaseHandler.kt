package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.model.BrevId
import org.jetbrains.exposed.v1.jdbc.*
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

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

abstract class PartlyTransactionHandler<Request, ResponseTransaction, Response, Error>(private val database: Database): UseCaseHandler<Request, Response, Error> {
    abstract suspend fun execute(request: Request): Outcome<ResponseTransaction, Error>?
    abstract suspend fun executeOutsideTransaction(request: Request, response: ResponseTransaction?): Outcome<Response, Error>?

    suspend fun executeOutsideTransaction(request: Request, response: Outcome<ResponseTransaction, Error>?): Outcome<Response, Error>? = when (response) {
        is Outcome.Success -> executeOutsideTransaction(request, response.value)
        is Outcome.Failure -> response
        null -> null
    }

    open fun transactionIsolation(): Int? = null
    final override suspend fun invoke(request: Request): Outcome<Response, Error>? {
        val transaction = isolatedTransaction(database = database, isolation = transactionIsolation()) {
            execute(request)?.onError { rollback() }
        }
        return executeOutsideTransaction(request, transaction)
    }
}

abstract class ReservertBrevHandler<Request : BrevredigeringRequest, Response>(
    private val database: Database,
    private val reserverBrev: ReserverBrevHandler,
) : UseCaseHandler<Request, Response, BrevredigeringError> {

    open fun transactionIsolation(): Int? = null
    open fun requiresReservasjon(request: Request) = true
    protected abstract suspend fun execute(request: Request): Outcome<Response, BrevredigeringError>?

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