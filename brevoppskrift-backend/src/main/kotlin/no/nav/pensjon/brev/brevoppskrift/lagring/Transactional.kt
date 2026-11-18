package no.nav.pensjon.brev.brevoppskrift.lagring

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

class Transactional(private val database: Database) {
    suspend fun <T> transaction(block: JdbcTransaction.() -> T): T = withContext(Dispatchers.IO) {
        suspendTransaction(db = database) {
            maxAttempts = 1
            block()
        }
    }
}
