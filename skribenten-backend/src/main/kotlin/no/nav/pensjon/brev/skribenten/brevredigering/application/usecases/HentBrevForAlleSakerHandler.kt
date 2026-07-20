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

class HentBrevForAlleSakerHandler(
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    database: Database,
) : TransactionHandler<HentBrevForAlleSakerHandler.Request, List<Dto.BrevInfo>, Nothing>(database), HentBrevForAlleSakerService {
    override suspend fun execute(request: Request): Outcome<List<Dto.BrevInfo>, Nothing> = success(
        BrevredigeringEntity.find { BrevredigeringTable.saksId inList request.saksIder }
            .map { it.toBrevInfo(brevreservasjonPolicy) }
    )

    data class Request(val saksIder: Set<SaksId>)
}
