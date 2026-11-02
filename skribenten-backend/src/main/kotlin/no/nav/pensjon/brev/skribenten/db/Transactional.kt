package no.nav.pensjon.brev.skribenten.db

import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.observability.BrevoppdateringObservability.Companion.fase
import no.nav.pensjon.brev.skribenten.observability.BrevoppdateringObservability.Transaksjon
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

class Transactional(private val database: Database) {

    suspend fun <T : Outcome<*, *>?> rollbackOnFailure(
        isolationLevel: Int? = null,
        observasjon: Transaksjon? = null,
        block: suspend JdbcTransaction.() -> T,
    ): T = fase(observasjon?.total) {
        val measuredBlock: suspend JdbcTransaction.() -> T = {
            fase(observasjon?.blokk) { rollbackIfFailure(block()) }
        }
        // The outer measurement includes Exposed's automatic flush/commit and all attempts.
        if (isolationLevel != null) {
            suspendTransaction(db = database, transactionIsolation = isolationLevel, statement = measuredBlock)
        } else {
            suspendTransaction(db = database, statement = measuredBlock)
        }
    }

    private fun <T : Outcome<*, *>?> JdbcTransaction.rollbackIfFailure(result: T): T =
        result.also { if (it is Outcome.Failure<*>) rollback() }
}
