package no.nav.pensjon.brev.skribenten.brevredigering.application.attestering

import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.application.vedlegg.RedigerbareVedleggService
import no.nav.pensjon.brev.skribenten.brevredigering.application.vedlegg.RedigerbartVedleggInfo
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId

class HentRedigerbareVedleggAttesteringHandler(
    private val brevtilgang: Brevtilgang,
    private val redigerbareVedleggService: RedigerbareVedleggService,
) {

    data class Request(
        val brevId: BrevId,
        val saksId: SaksId,
    )

    suspend operator fun invoke(request: Request): Outcome<List<RedigerbartVedleggInfo>, Nothing>? =
        brevtilgang.forLesing(request.brevId, request.saksId) {
            success(redigerbareVedleggService.hentTitler(brev, mergeMotMal = false) ?: return@forLesing null)
        }
}
