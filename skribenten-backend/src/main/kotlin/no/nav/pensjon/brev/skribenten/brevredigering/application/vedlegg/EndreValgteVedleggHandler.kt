package no.nav.pensjon.brev.skribenten.brevredigering.application.vedlegg

import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.MarkupVersjon
import no.nav.pensjon.brev.skribenten.brevredigering.domain.sjekkMarkupVersjon
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggBrevkode

class EndreValgteVedleggHandler(private val brevtilgang: Brevtilgang) {

    data class Request(
        val brevId: BrevId,
        val saksId: SaksId,
        val alltidValgbareVedlegg: List<AlltidValgbartVedleggBrevkode>,
        val markupVersjon: MarkupVersjon? = null,
    )

    suspend operator fun invoke(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        brevtilgang.forRedigering(request.brevId, request.saksId, frigiReservasjon = true) {
            brev.sjekkMarkupVersjon(request.markupVersjon)?.let { return@forRedigering failure(it) }
            brev.valgteVedlegg = request.alltidValgbareVedlegg

            success(brev.tilDto())
        }
}
