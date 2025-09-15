package no.nav.pensjon.brev.skribenten

import com.typesafe.config.Config
import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
import no.nav.pensjon.brev.skribenten.db.EditLetterHash
import no.nav.pensjon.brev.skribenten.db.OneShotJobTable
import no.nav.pensjon.brev.skribenten.services.LeaderService
import no.nav.pensjon.brev.skribenten.services.NaisLeaderService
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.slf4j.LoggerFactory
import java.time.Instant
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

@DslMarker
annotation class OneShotJobDsl

@OneShotJobDsl
class OneShotJobConfig {

    /**
     * The name should be unique across all one-shot jobs, and is used to verify if a job has already been executed.
     * The preferred naming scheme has a date prefix followed by a descriptive name, e.g. "2025-08-10-brev-title-as-markup".
     */
    fun job(uniqeName: String, block: JobConfig.() -> Unit) {
        try {
            val existing = transaction {
                OneShotJobTable.selectAll().where { OneShotJobTable.id eq uniqeName }.singleOrNull()
            }
            if (existing == null) {
                logger.info("One-shot job started: '$uniqeName'")
                val job = JobConfig(uniqeName).apply(block)

                transaction {
                    if (job.completed) {
                        OneShotJobTable.insert {
                            it[id] = uniqeName
                            it[completedAt] = Instant.now()
                        }
                        logger.info("One-shot job '$uniqeName' completed successfully.")
                    } else {
                        logger.warn("One-shot job '$uniqeName' did not complete successfully. It may need to be re-run.")
                    }
                }
            } else {
                logger.info("One-shot job '$uniqeName' has already been executed. Skipping.")
            }
        } catch (e: Throwable) {
            logger.error("Error executing one-shot job '$uniqeName': ${e.message}", e)
            throw e // Re-throw to ensure the error is propagated
        }
    }
}


@OneShotJobDsl
class JobConfig(val jobName: String) {
    var completed: Boolean = true
}

private val logger = LoggerFactory.getLogger("OneShotJob")

/**
 * Executes one-shot jobs if this instance is the leader in a leader election setup.
 * If leader election is disabled, it assumes single-instance and executes the jobs.
 *
 * @param skribentenConfig The configuration for Skribenten, including leader service settings.
 * @param block The block of one-shot job definitions to execute.
 */
suspend fun oneShotJobs(skribentenConfig: Config, block: OneShotJobConfig.() -> Unit) =
    oneShotJobs(NaisLeaderService(skribentenConfig.getConfig("services.leader")), block)

/**
 * Executes one-shot jobs if this instance is the leader in a leader election setup.
 * If leader election is disabled, it assumes single-instance and executes the jobs.
 *
 * @param leaderService The service responsible for leader election.
 * @param block The block of one-shot job definitions to execute.
 */
suspend fun oneShotJobs(leaderService: LeaderService, block: OneShotJobConfig.() -> Unit) {
    if (leaderService.isLeaderElectionEnabled) {
        val leader = leaderService.electedLeader()
        if (leader?.isThisInstanceLeader == true) {
            logger.info("This instance is the leader: ${leader.thisInstanceName}. Executing one-shot jobs.")
            OneShotJobConfig().apply(block)
        } else {
            logger.info("This instance (${leader?.thisInstanceName}) is not the leader: ${leader?.leaderName}. Skipping one-shot jobs.")
        }
    } else {
        logger.info("Leader election is disabled, assuming single-instance and will execute one-shot jobs.")
        OneShotJobConfig().apply(block)
    }
}


fun JobConfig.updateBrevredigeringJson() {
    transaction {
        val alleBrev = BrevredigeringTable.select(
            BrevredigeringTable.id,
            BrevredigeringTable.sistReservert,
            BrevredigeringTable.redigertBrev
        ).toList()
        val ikkeAktivtReservertTidspunkt = Instant.now().minus(15.minutes.toJavaDuration())
        val kanOppdateres =
            alleBrev.filter { it[BrevredigeringTable.sistReservert]?.isBefore(ikkeAktivtReservertTidspunkt) ?: false }

        kanOppdateres.forEach {
            val brevId = it[BrevredigeringTable.id]
            logger.debug("Oppdaterer {}", brevId)
            val redigertBrev = it[BrevredigeringTable.redigertBrev]
            BrevredigeringTable.update({ BrevredigeringTable.id eq brevId }) { update ->
                update[BrevredigeringTable.redigertBrev] = redigertBrev
                update[BrevredigeringTable.redigertBrevHash] = EditLetterHash.read(redigertBrev)
                update[BrevredigeringTable.redigertBrevKryptert] = redigertBrev
                update[BrevredigeringTable.redigertBrevKryptertHash] = EditLetterHash.read(redigertBrev)
            }
        }

        val ulikHash = BrevredigeringTable.select(BrevredigeringTable.id, BrevredigeringTable.redigertBrevHash,
            BrevredigeringTable.redigertBrevKryptertHash).where({
            BrevredigeringTable.redigertBrevKryptertHash.neq(BrevredigeringTable.redigertBrevHash)
        }).map { it[BrevredigeringTable.id].value }
        if (ulikHash.isNotEmpty()) {
            logger.info("Fikk forskjellig hash mellom vanlig og kryptert for brevene ${ulikHash.joinToString(",")}")
            completed = false
        }
    }
}