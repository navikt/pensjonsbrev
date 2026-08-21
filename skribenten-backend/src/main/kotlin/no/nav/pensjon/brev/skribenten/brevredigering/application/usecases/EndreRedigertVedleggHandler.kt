package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.brevredigering.domain.*
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.updateEditedAttachment
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import org.jetbrains.exposed.v1.jdbc.Database


class EndreRedigertVedleggHandler(
    private val redigerBrevPolicy: RedigerBrevPolicy,
    private val brevmalService: BrevmalService,
    private val brevdataService: BrevdataService,
    reserverBrevHandler: ReserverBrevHandler,
    database: Database,
) : ReservertBrevHandler<EndreRedigertVedleggHandler.Request, Edit.Attachment>(database, reserverBrevHandler) {

    data class Request(
        override val brevId: BrevId,
        override val saksId: SaksId,
        val vedleggId: VedleggId,
        val redigertVedlegg: Edit.Attachment,
        val frigiReservasjon: Boolean = false,
    ) : BrevredigeringRequest

    override suspend fun execute(request: Request): Outcome<Edit.Attachment, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findByIdAndSaksId(request.brevId, request.saksId) ?: return null

        val principal = PrincipalInContext.require()
        redigerBrevPolicy.kanRedigere(brev, principal).onError { return failure(it) }

        val pesysdata = brevdataService.hentBrevdata(brev)
        val malVedlegg = brevmalService.renderRedigerbartVedlegg(brev, pesysdata, request.vedleggId)
            ?: return failure(VedleggFinnesIkkeIMal(request.brevId, request.vedleggId))

        val sammenslaatt = request.redigertVedlegg.updateEditedAttachment(malVedlegg)
        brev.settRedigertVedlegg(request.vedleggId, sammenslaatt)

        if (request.frigiReservasjon) {
            brev.frigiReservasjon()
        }

        return success(sammenslaatt)
    }
}
