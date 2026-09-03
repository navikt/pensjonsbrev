package no.nav.pensjon.brev.skribenten.brevredigering.application.attestering

import no.nav.pensjon.brev.skribenten.brevredigering.application.pdf.BrevPdfService
import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.IngenFoersteside
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.fagsystem.Fagsak
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId

class HentEllerOpprettAttesteringPdfHandler(
    private val brevtilgang: Brevtilgang,
    private val brevPdfService: BrevPdfService,
) {

    data class Request(
        val brevId: BrevId,
        val saksId: SaksId,
        val fagsak: Fagsak,
    )

    suspend operator fun invoke(request: Request): Outcome<Dto.HentDocumentResult, IngenFoersteside>? =
        brevtilgang.forLesing(request.brevId, request.saksId) {
            brevPdfService.hentEllerOpprett(brev, request.fagsak, sjekkOmRendretBrevErEndret = false)
        }
}
