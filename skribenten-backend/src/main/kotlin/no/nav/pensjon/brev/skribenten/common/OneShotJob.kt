package no.nav.pensjon.brev.skribenten.common

import no.nav.pensjon.brev.skribenten.brevredigering.domain.MottakerType
import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
import no.nav.pensjon.brev.skribenten.db.MottakerTable
import no.nav.pensjon.brev.skribenten.db.OneShotJobTable
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.services.LeaderService
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.vendors.ForUpdateOption
import org.jetbrains.exposed.v1.jdbc.insert
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

fun JobConfig.updateMottaker() {
    val alleMottakerIder = transaction {
        MottakerTable.select(MottakerTable.id).map { it[MottakerTable.id] }
    }

    alleMottakerIder.forEach { mottakerId ->
        // Leser og oppdaterer én og én rad, låst med SELECT ... FOR UPDATE innenfor samme
        // transaksjon. Uten låsen kan EndreMottakerHandler committe en endring av begge
        // kolonnesettene mellom vår lesing og skriving, og vi ville da overskrevet de krypterte
        // kolonnene med utdaterte klartekstverdier fra før endringen. Låsen sikrer at vi alltid
        // backfiller fra raden slik den er akkurat nå, og at en samtidig skriving enten er ferdig
        // før vi leser, eller må vente til vi har commitet.
        transaction {
            logger.debug("Oppdaterer {}", mottakerId)
            val rad = MottakerTable
                .select(
                    MottakerTable.navn,
                    MottakerTable.postnummer,
                    MottakerTable.poststed,
                    MottakerTable.adresselinje1,
                    MottakerTable.adresselinje2,
                    MottakerTable.adresselinje3,
                    MottakerTable.landkode,
                    MottakerTable.manueltAdressertTil,
                    MottakerTable.adresse,
                    MottakerTable.type,
                    MottakerTable.tssId,
                )
                .where { MottakerTable.id eq mottakerId }
                .forUpdate(ForUpdateOption.ForUpdate)
                .singleOrNull() ?: return@transaction

            MottakerTable.update({ MottakerTable.id eq mottakerId }) { update ->
                val type = rad[MottakerTable.type]
                val mottaker = when (type) {
                    MottakerType.SAMHANDLER -> Dto.Mottaker.samhandler(rad[MottakerTable.tssId]!!)
                    MottakerType.NORSK_ADRESSE -> Dto.Mottaker.norskAdresse(
                        navn = rad[MottakerTable.navn]!!,
                        postnummer = rad[MottakerTable.postnummer]!!,
                        poststed = rad[MottakerTable.poststed]!!,
                        adresselinje1 = rad[MottakerTable.adresselinje1],
                        adresselinje2 = rad[MottakerTable.adresselinje2],
                        adresselinje3 = rad[MottakerTable.adresselinje3],
                        manueltAdressertTil = rad[MottakerTable.manueltAdressertTil]
                    )

                    MottakerType.UTENLANDSK_ADRESSE -> Dto.Mottaker.utenlandskAdresse(
                        navn = rad[MottakerTable.navn]!!,
                        adresselinje1 = rad[MottakerTable.adresselinje1]!!,
                        adresselinje2 = rad[MottakerTable.adresselinje2],
                        adresselinje3 = rad[MottakerTable.adresselinje3],
                        landkode = rad[MottakerTable.landkode]!!,
                        manueltAdressertTil = rad[MottakerTable.manueltAdressertTil]
                    )
                }
                update[adresse] = mottaker
            }
        }
    }
}

fun JobConfig.updateBrevredigeringJson() {
    val alleBrevIder = transaction {
        BrevredigeringTable.select(BrevredigeringTable.id).map { it[BrevredigeringTable.id] }
    }
    val ikkeAktivtReservertTidspunkt = Instant.now().minus(15.minutes.toJavaDuration())
    var antallOppdaterte = 0

    alleBrevIder.forEach { brevId ->
        transaction {
            val rad = BrevredigeringTable
                .select(BrevredigeringTable.sistReservert, BrevredigeringTable.saksbehandlerValg)
                .where { BrevredigeringTable.id eq brevId }
                .forUpdate(ForUpdateOption.ForUpdate)
                .singleOrNull() ?: return@transaction

            if (rad[BrevredigeringTable.sistReservert]?.isBefore(ikkeAktivtReservertTidspunkt) == false) {
                return@transaction
            }
            logger.debug("Oppdaterer {}", brevId)
            BrevredigeringTable.update({ BrevredigeringTable.id eq brevId }) { update ->
                update[BrevredigeringTable.saksbehandlerValgKryptert] = rad[BrevredigeringTable.saksbehandlerValg]
            }
            antallOppdaterte++
        }
    }

    if (alleBrevIder.size != antallOppdaterte) {
        logger.info("Oppdaterte $antallOppdaterte av ${alleBrevIder.size} brevredigeringer med ikke-aktive reservasjoner.")
        completed = false
    }
}