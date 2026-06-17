package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.VedleggId

class HentRedigertVedleggHandler(
    private val brevmalService: BrevmalService,
    private val brevdataService: BrevdataService,
) : BrevredigeringHandler<HentRedigertVedleggHandler.Request, Edit.Attachment> {

    data class Request(
        override val brevId: BrevId,
        val vedleggId: VedleggId,
    ) : BrevredigeringRequest

    override suspend fun handle(request: Request): Outcome<Edit.Attachment, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null

        brev.hentRedigertVedlegg(request.vedleggId)?.let { return success(it) }

        // Ingen overstyring lagret: returner vedlegget slik det produseres fra mal som utgangspunkt.
        val pesysdata = brevdataService.hentBrevdata(brev)
        val malVedlegg = brevmalService.renderRedigerbartVedlegg(brev, pesysdata, request.vedleggId) ?: return null
        return success(malVedlegg.toEdit())
    }

    override fun requiresReservasjon(request: Request) = false
}
