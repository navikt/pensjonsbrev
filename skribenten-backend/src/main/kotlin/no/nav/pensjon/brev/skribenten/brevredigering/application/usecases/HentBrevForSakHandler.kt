package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database

class HentBrevForSakHandler(
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    database: Database,
) : TransactionHandler<HentBrevForSakHandler.Request, List<Dto.BrevInfo>, Nothing>(database) {
    override suspend fun execute(request: Request): Outcome<List<Dto.BrevInfo>, Nothing> =
        success(
            BrevredigeringEntity.find { BrevredigeringTable.saksId eq request.saksId }
                .map { it.toBrevInfo(brevreservasjonPolicy) }
        )

    data class Request(val saksId: SaksId)
}
