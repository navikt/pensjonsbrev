package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.application.RedigerbareVedleggService
import no.nav.pensjon.brev.skribenten.brevredigering.application.RedigerbartVedleggInfo
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId
import org.jetbrains.exposed.v1.jdbc.Database

class HentRedigerbareVedleggAttesteringHandler(
    private val redigerbareVedleggService: RedigerbareVedleggService,
    database: Database,
) : TransactionHandler<HentRedigerbareVedleggAttesteringHandler.Request, List<RedigerbartVedleggInfo>, Nothing>(database) {

    data class Request(
        override val brevId: BrevId,
        override val saksId: SaksId,
    ) : BrevredigeringRequest

    override suspend fun execute(request: Request): Outcome<List<RedigerbartVedleggInfo>, Nothing>? {
        val brev = BrevredigeringEntity.findByIdAndSaksId(request.brevId, request.saksId) ?: return null

        return success(redigerbareVedleggService.hentTitler(brev, mergeMotMal = false) ?: return null)
    }
}
