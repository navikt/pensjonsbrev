package no.nav.pensjon.brev.skribenten.brevredigering.application.vedlegg

import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.VedleggFinnesIkkeIMal
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.updateEditedAttachment
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId

class EndreRedigertVedleggHandler(
    private val brevtilgang: Brevtilgang,
    private val brevmalService: BrevmalService,
    private val brevdataService: BrevdataService,
) {

    data class Request(
        val brevId: BrevId,
        val saksId: SaksId,
        val vedleggId: VedleggId,
        val redigertVedlegg: Edit.Attachment,
        val frigiReservasjon: Boolean = false,
    )

    suspend operator fun invoke(request: Request): Outcome<Edit.Attachment, BrevredigeringError>? =
        brevtilgang.forRedigering(request.brevId, request.saksId, frigiReservasjon = request.frigiReservasjon) {
            val pesysdata = brevdataService.hentBrevdata(brev)
            val malVedlegg = brevmalService.renderRedigerbartVedlegg(brev, pesysdata, request.vedleggId)
                ?: return@forRedigering failure(VedleggFinnesIkkeIMal(request.brevId, request.vedleggId))

            val sammenslaatt = request.redigertVedlegg.updateEditedAttachment(malVedlegg)
            brev.settRedigertVedlegg(request.vedleggId, sammenslaatt)

            success(sammenslaatt)
        }
}
