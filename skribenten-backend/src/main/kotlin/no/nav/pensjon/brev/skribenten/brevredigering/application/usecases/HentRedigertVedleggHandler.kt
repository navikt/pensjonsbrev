package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.BrevId

class HentRedigertVedleggHandler : BrevredigeringHandler<HentRedigertVedleggHandler.Request, Edit.Attachment> {

    data class Request(
        override val brevId: BrevId,
        val vedleggId: String,
    ) : BrevredigeringRequest

    override suspend fun handle(request: Request): Outcome<Edit.Attachment, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null
        val redigertVedlegg = brev.hentRedigertVedlegg(request.vedleggId) ?: return null
        return success(redigertVedlegg)
    }

    override fun requiresReservasjon(request: Request) = false
}
