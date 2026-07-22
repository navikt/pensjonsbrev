package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import org.jetbrains.exposed.v1.jdbc.Database

class HentBrevInfoHandler(
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    database: Database,
) : TransactionHandler<HentBrevInfoHandler.Request, Dto.BrevInfo, Nothing>(database) {

    data class Request(val brevId: BrevId)

    override suspend fun execute(request: Request): Outcome<Dto.BrevInfo, Nothing>? =
        BrevredigeringEntity.findById(request.brevId)?.let { success(it.toBrevInfo(brevreservasjonPolicy)) }
}
