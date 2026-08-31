package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevPdfService
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.IngenFoersteside
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.fagsystem.Fagsak
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId
import org.jetbrains.exposed.v1.jdbc.Database

class HentEllerOpprettAttesteringPdfHandler(
    private val brevPdfService: BrevPdfService,
    database: Database,
) : TransactionHandler<HentEllerOpprettAttesteringPdfHandler.Request, Dto.HentDocumentResult, IngenFoersteside>(database) {

    data class Request(
        override val brevId: BrevId,
        override val saksId: SaksId,
        val fagsak: Fagsak,
    ) : BrevredigeringRequest

    override suspend fun execute(request: Request): Outcome<Dto.HentDocumentResult, IngenFoersteside>? {
        val brev = BrevredigeringEntity.findByIdAndSaksId(request.brevId, request.saksId) ?: return null

        return brevPdfService.hentEllerOpprett(brev, request.fagsak, sjekkOmRendretBrevErEndret = false)
    }
}
