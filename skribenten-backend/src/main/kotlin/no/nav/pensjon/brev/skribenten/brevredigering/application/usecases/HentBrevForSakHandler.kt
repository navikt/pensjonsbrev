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
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

class HentBrevForSakHandler(
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    private val database: Database,
) : UseCaseHandler<HentBrevForSakHandler.Request, List<Dto.BrevInfo>, Nothing> {

    data class Request(val saksId: SaksId)

    override suspend fun invoke(request: Request): Outcome<List<Dto.BrevInfo>, Nothing> =
        suspendTransaction(db = database) {
            success(
                BrevredigeringEntity.find { BrevredigeringTable.saksId eq request.saksId }
                    .map { it.toBrevInfo(brevreservasjonPolicy) }
            )
        }
}
