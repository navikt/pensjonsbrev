package no.nav.pensjon.brev.skribenten.brevredigering.application.p1

import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.PenClient
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.vedlegg.P1RedigerbarDto

class HentP1DataHandler(
    private val brevtilgang: Brevtilgang,
    private val penClient: PenClient,
) {

    data class Request(
        val brevId: BrevId,
        val saksId: SaksId,
    )

    suspend operator fun invoke(request: Request): Outcome<P1RedigerbarDto, Nothing>? =
        brevtilgang.forLesing(request.brevId, request.saksId) {
            success(brev.p1Data?.p1data ?: penClient.hentP1VedleggData(request.saksId, brev.spraak))
        }
}
