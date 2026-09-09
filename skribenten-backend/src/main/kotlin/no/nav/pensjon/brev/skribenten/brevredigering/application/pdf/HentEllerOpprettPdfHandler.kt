package no.nav.pensjon.brev.skribenten.brevredigering.application.pdf

import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.IngenFoersteside
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.fagsystem.Fagsak
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId

class HentEllerOpprettPdfHandler(
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
            brevPdfService.hentEllerOpprett(brev, request.fagsak, sjekkOmRendretBrevErEndret = true)
        }
}

// Disse må være i sync med api-modellen
const val P1_BREVKODE = "P1_SAMLET_MELDING_OM_PENSJONSVEDTAK_V2"
const val P1_VEDLEGG_KEY = "p1Vedlegg"
