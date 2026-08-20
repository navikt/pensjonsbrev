package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.*
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.*
import no.nav.pensjon.brev.skribenten.letter.*
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import org.jetbrains.exposed.v1.jdbc.Database

class HentRedigertVedleggHandler(
    private val brevmalService: BrevmalService,
    private val brevdataService: BrevdataService,
    reserverBrevHandler: ReserverBrevHandler,
    database: Database,
) : ReservertBrevHandler<HentRedigertVedleggHandler.Request, Edit.Attachment>(database, reserverBrevHandler) {

    data class Request(
        override val brevId: BrevId,
        override val saksId: SaksId,
        val vedleggId: VedleggId,
    ) : BrevredigeringRequest

    override suspend fun execute(request: Request): Outcome<Edit.Attachment, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findByIdAndSaksId(request.brevId, request.saksId) ?: return null

        val lagretVedlegg = brev.hentRedigertVedlegg(request.vedleggId)

        val pesysdata = brevdataService.hentBrevdata(brev)
        val malVedlegg = brevmalService.renderRedigerbartVedlegg(brev, pesysdata, request.vedleggId)
        // Om malen ikke lenger produserer vedlegget beholder vi saksbehandlers versjon uendret.
            ?: return lagretVedlegg?.let { success(it) }

        // Uten lagret overstyring returnerer vi vedlegget slik det produseres fra mal som utgangspunkt.
        return success(lagretVedlegg?.updateEditedAttachment(malVedlegg) ?: malVedlegg.toEdit())
    }
}
