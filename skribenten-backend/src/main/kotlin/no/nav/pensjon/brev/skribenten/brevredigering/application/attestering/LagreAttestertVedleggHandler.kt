package no.nav.pensjon.brev.skribenten.brevredigering.application.attestering

import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.application.vedlegg.RedigerbareVedleggService
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId

class LagreAttestertVedleggHandler(
    private val brevtilgang: Brevtilgang,
    private val redigerbareVedleggService: RedigerbareVedleggService,
) {

    data class Request(
        val brevId: BrevId,
        val saksId: SaksId,
        val vedleggId: VedleggId,
        val redigertVedlegg: Edit.Attachment,
        val frigiReservasjon: Boolean = false,
    )

    suspend operator fun invoke(request: Request): Outcome<Edit.Attachment, BrevredigeringError>? =
        brevtilgang.forAttestering(request.brevId, request.saksId, frigiReservasjon = request.frigiReservasjon) {
            redigerbareVedleggService.lagre(brev, request.vedleggId, request.redigertVedlegg, mergeMotMal = false)
        }
}
