package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.application.RedigerbareVedleggService
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import org.jetbrains.exposed.v1.jdbc.Database

class HentRedigertVedleggAttesteringHandler(
    private val redigerbareVedleggService: RedigerbareVedleggService,
    reserverBrevHandler: ReserverBrevHandler,
    database: Database,
) : ReservertBrevHandler<HentRedigertVedleggAttesteringHandler.Request, Edit.Attachment>(database, reserverBrevHandler) {

    data class Request(
        override val brevId: BrevId,
        override val saksId: SaksId,
        val vedleggId: VedleggId,
    ) : BrevredigeringRequest

    override suspend fun execute(request: Request): Outcome<Edit.Attachment, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findByIdAndSaksId(request.brevId, request.saksId) ?: return null

        return redigerbareVedleggService.hent(brev, request.vedleggId, mergeMotMal = false)
    }
}
