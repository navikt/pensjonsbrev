package no.nav.pensjon.brev.skribenten.db

import no.nav.pensjon.brev.skribenten.common.Outcome
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

class Transactional(private val database: Database) {

    suspend fun <T : Outcome<*, *>?> rollbackOnFailure(
        isolationLevel: Int? = null,
        block: suspend JdbcTransaction.() -> T,
    ): T =
        if (isolationLevel != null) {
            suspendTransaction(db = database, transactionIsolation = isolationLevel) { rollbackIfFailure(block()) }
        } else {
            suspendTransaction(db = database) { rollbackIfFailure(block()) }
        }

    private fun <T : Outcome<*, *>?> JdbcTransaction.rollbackIfFailure(result: T): T =
        result.also { if (it is Outcome.Failure<*>) rollback() }
}
