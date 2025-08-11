package no.nav.pensjon.brev.skribenten

import com.typesafe.config.Config
import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
import no.nav.pensjon.brev.skribenten.db.OneShotJobTable
import no.nav.pensjon.brev.skribenten.services.LeaderService
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
            transaction {
                val existing = OneShotJobTable.selectAll().where { OneShotJobTable.id eq uniqeName }.singleOrNull()
                if (existing == null) {
                    logger.info("One-shot job started: '$uniqeName'")
                    JobConfig(uniqeName).apply(block)
                    OneShotJobTable.insert {
                        it[id] = uniqeName
                        it[completedAt] = Instant.now()
                    }
                    logger.info("One-shot job completed: '$uniqeName'")
                } else {
                    logger.info("One-shot job '$uniqeName' has already been executed. Skipping.")
                }
            }
        } catch (e: Throwable) {
            logger.error("Error executing one-shot job '$uniqeName': ${e.message}", e)
            throw e // Re-throw to ensure the error is propagated
        }
    }
}


@OneShotJobDsl
class JobConfig(val jobName: String)

private val logger = LoggerFactory.getLogger("OneShotJob")

/**
 * Executes one-shot jobs if this instance is the leader in a leader election setup.
 * If leader election is disabled, it assumes single-instance and executes the jobs.
 *
 * @param skribentenConfig The configuration for Skribenten, including leader service settings.
 * @param block The block of one-shot job definitions to execute.
 */
suspend fun oneShotJobs(skribentenConfig: Config, block: OneShotJobConfig.() -> Unit) =
    oneShotJobs(LeaderService(skribentenConfig.getConfig("services.leader")), block)

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
        val kanOppdateres = BrevredigeringTable.select(BrevredigeringTable.id, BrevredigeringTable.redigertBrev)
            .where { BrevredigeringTable.sistReservert less Instant.now().minus(15.minutes.toJavaDuration()) }
            .toList()

        kanOppdateres.forEach {
            val brevId = it[BrevredigeringTable.id]
            val redigertBrev = it[BrevredigeringTable.redigertBrev]
            BrevredigeringTable.update({ BrevredigeringTable.id eq brevId }) { update ->
                update[BrevredigeringTable.redigertBrev] = redigertBrev.copy()
            }
        }
    }
}
