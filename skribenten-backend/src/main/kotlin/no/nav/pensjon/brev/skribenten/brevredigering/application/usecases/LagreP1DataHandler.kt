package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.P1Data
import no.nav.pensjon.brev.skribenten.brevredigering.domain.P1RedigerbarDto
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.db.P1DataTable
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database

class LagreP1DataHandler(
    database: Database,
) : TransactionHandler<LagreP1DataHandler.Request, P1RedigerbarDto, Nothing>(database) {

    data class Request(
        val brevId: BrevId,
        val saksId: SaksId,
        val p1Data: P1RedigerbarDto,
    )

    override suspend fun execute(request: Request): Outcome<P1RedigerbarDto, Nothing>? {
        val brevredigering = BrevredigeringEntity.findByIdAndSaksId(request.brevId, request.saksId) ?: return null

        val entity = P1Data.findSingleByAndUpdate(P1DataTable.id eq brevredigering.id) { p1Data ->
            p1Data.p1data = request.p1Data
        } ?: P1Data.new(request.brevId) {
            p1data = request.p1Data
        }

        return success(entity.p1data)
    }
}
