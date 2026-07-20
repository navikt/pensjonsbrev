package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.application.HentBrevForAlleSakerService
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

class HentBrevForAlleSakerHandler(
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    private val database: Database,
) : UseCaseHandler<HentBrevForAlleSakerHandler.Request, List<Dto.BrevInfo>, Nothing>, HentBrevForAlleSakerService {

    data class Request(val saksIder: Set<SaksId>)

    override suspend fun invoke(request: Request): Outcome<List<Dto.BrevInfo>, Nothing> =
        suspendTransaction(db = database) {
            success(
                BrevredigeringEntity.find { BrevredigeringTable.saksId inList request.saksIder }
                    .map { it.toBrevInfo(brevreservasjonPolicy) }
            )
        }
}
