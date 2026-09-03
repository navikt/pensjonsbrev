package no.nav.pensjon.brev.skribenten.brevredigering.application.vedlegg

import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId

class HentRedigerbareVedleggHandler(
    private val brevtilgang: Brevtilgang,
    private val redigerbareVedleggService: RedigerbareVedleggService,
) {

    data class Request(
        val brevId: BrevId,
        val saksId: SaksId,
    )

    suspend operator fun invoke(request: Request): Outcome<List<RedigerbartVedleggInfo>, Nothing>? =
        brevtilgang.forLesing(request.brevId, request.saksId) {
            success(redigerbareVedleggService.hentTitler(brev, mergeMotMal = true) ?: return@forLesing null)
        }
}
