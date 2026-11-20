package no.nav.pensjon.brev.skribenten.common

import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
import no.nav.pensjon.brev.skribenten.db.Hash
import no.nav.pensjon.brev.skribenten.db.OneShotJobTable
import no.nav.pensjon.brev.skribenten.db.RedigertVedleggTable
import no.nav.pensjon.brev.skribenten.services.LeaderService
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.less
import org.jetbrains.exposed.v1.core.vendors.ForUpdateOption
import org.jetbrains.exposed.v1.jdbc.insertIgnore
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
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
     * The block receives a database-generated technical version that is preserved across retries.
     */
    fun job(uniqeName: String, block: JobConfig.(tekniskVersjon: Int) -> Unit) {
        try {
            val existing = transaction {
                OneShotJobTable.selectAll().where { OneShotJobTable.id eq uniqeName }.singleOrNull()
                    ?: run {
                        OneShotJobTable.insertIgnore {
                            it[id] = uniqeName
                        }
                        OneShotJobTable.selectAll().where { OneShotJobTable.id eq uniqeName }.single()
                    }
            }
            if (existing[OneShotJobTable.completedAt] == null) {
                logger.info("One-shot job started: '$uniqeName'")
                val job = JobConfig(uniqeName).apply { block(existing[OneShotJobTable.tekniskVersjon]) }

                transaction {
                    if (job.completed) {
                        OneShotJobTable.update({ OneShotJobTable.id eq uniqeName }) {
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

fun JobConfig.updateBrevredigeringJson(tekniskVersjon: Int) {
    val alleBrevIder = transaction {
        BrevredigeringTable.select(BrevredigeringTable.id)
            .where { BrevredigeringTable.tekniskVersjon less tekniskVersjon }.map { it[BrevredigeringTable.id] }
    }
    val ikkeAktivtReservertTidspunkt = Instant.now().minus(15.minutes.toJavaDuration())
    var antallOppdaterte = 0

    alleBrevIder.forEach { brevId ->
        transaction {
            val rad = BrevredigeringTable
                .select(
                    BrevredigeringTable.sistReservert,
                    BrevredigeringTable.redigertBrevKryptert,
                    BrevredigeringTable.rendretBrevKryptert,
                )
                .where { BrevredigeringTable.id eq brevId }
                .forUpdate(ForUpdateOption.ForUpdate)
                .singleOrNull() ?: return@transaction

            if (rad[BrevredigeringTable.sistReservert]?.isBefore(ikkeAktivtReservertTidspunkt) == false) {
                return@transaction
            }
            logger.debug("Oppdaterer {}", brevId)
            BrevredigeringTable.update({ BrevredigeringTable.id eq brevId }) { update ->
                val redigertBrev = rad[BrevredigeringTable.redigertBrevKryptert]
                update[BrevredigeringTable.redigertBrevKryptert] = redigertBrev
                update[BrevredigeringTable.redigertBrevKryptertHash] = Hash.read(redigertBrev)
                update[BrevredigeringTable.rendretBrevKryptert] = rad[BrevredigeringTable.rendretBrevKryptert]
                update[BrevredigeringTable.tekniskVersjon] = tekniskVersjon
            }
            antallOppdaterte++
        }
    }

    if (alleBrevIder.size != antallOppdaterte) {
        logger.info("Oppdaterte $antallOppdaterte av ${alleBrevIder.size} brevredigeringer med ikke-aktive reservasjoner.")
        completed = false
    }
}

fun JobConfig.updateRedigertVedleggJson(tekniskVersjon: Int) {
    val alleVedleggIder = transaction {
        RedigertVedleggTable.select(RedigertVedleggTable.brevredigering, RedigertVedleggTable.vedleggId)
            .where { RedigertVedleggTable.tekniskVersjon less tekniskVersjon }
            .map { it[RedigertVedleggTable.brevredigering] to it[RedigertVedleggTable.vedleggId] }
    }
    val ikkeAktivtReservertTidspunkt = Instant.now().minus(15.minutes.toJavaDuration())
    var antallOppdaterte = 0

    alleVedleggIder.forEach { vedleggIder ->
        val brevId = vedleggIder.first
        val vedleggId = vedleggIder.second
        val radFilter = (RedigertVedleggTable.brevredigering eq brevId) and (RedigertVedleggTable.vedleggId eq vedleggId)

        transaction {
            val rad = (RedigertVedleggTable innerJoin BrevredigeringTable)
                .select(
                    BrevredigeringTable.sistReservert,
                    RedigertVedleggTable.redigertVedleggKryptert,
                    RedigertVedleggTable.rendretVedlegg,
                )
                .where { radFilter }
                .forUpdate(ForUpdateOption.ForUpdate)
                .singleOrNull() ?: return@transaction

            if (rad[BrevredigeringTable.sistReservert]?.isBefore(ikkeAktivtReservertTidspunkt) == false) {
                return@transaction
            }
            logger.debug("Oppdaterer vedlegg {} for {}", vedleggId, brevId)
            RedigertVedleggTable.update({ radFilter }) { update ->
                val redigertVedlegg = rad[RedigertVedleggTable.redigertVedleggKryptert]
                update[RedigertVedleggTable.redigertVedleggKryptert] = redigertVedlegg
                update[RedigertVedleggTable.redigertVedleggKryptertHash] = Hash.read(redigertVedlegg)
                update[RedigertVedleggTable.rendretVedlegg] = rad[RedigertVedleggTable.rendretVedlegg]
                update[RedigertVedleggTable.tekniskVersjon] = tekniskVersjon
            }
            antallOppdaterte++
        }
    }

    if (alleVedleggIder.size != antallOppdaterte) {
        logger.info("Oppdaterte $antallOppdaterte av ${alleVedleggIder.size} redigerte vedlegg med ikke-aktive reservasjoner.")
        completed = false
    }
}