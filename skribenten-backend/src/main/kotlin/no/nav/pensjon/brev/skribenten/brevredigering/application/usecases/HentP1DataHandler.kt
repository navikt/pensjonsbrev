package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.PenClient
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId
import org.jetbrains.exposed.v1.jdbc.Database

class HentP1DataHandler(
    private val penClient: PenClient,
    database: Database,
) : TransactionHandler<HentP1DataHandler.Request, Api.GeneriskBrevdata, Nothing>(database) {

    data class Request(
        val brevId: BrevId,
        val saksId: SaksId,
    )

    override suspend fun execute(request: Request): Outcome<Api.GeneriskBrevdata, Nothing>? {
        val brevredigering = BrevredigeringEntity.findByIdAndSaksId(request.brevId, request.saksId) ?: return null

        val p1Data = brevredigering.p1Data?.p1data
            ?: penClient.hentP1VedleggData(request.saksId, brevredigering.spraak)

        return success(p1Data)
    }
}
