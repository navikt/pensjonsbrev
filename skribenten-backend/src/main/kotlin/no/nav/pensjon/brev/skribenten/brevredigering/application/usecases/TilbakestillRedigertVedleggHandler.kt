package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.VedleggFinnesIkkeIMal
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import org.jetbrains.exposed.v1.jdbc.Database

class TilbakestillRedigertVedleggHandler(
    private val redigerBrevPolicy: RedigerBrevPolicy,
    private val brevmalService: BrevmalService,
    private val brevdataService: BrevdataService,
    reserverBrevHandler: ReserverBrevHandler,
    database: Database,
) : ReservertBrevHandler<TilbakestillRedigertVedleggHandler.Request, Edit.Attachment>(database, reserverBrevHandler) {

    data class Request(
        override val brevId: BrevId,
        override val saksId: SaksId,
        val vedleggId: VedleggId,
    ) : BrevredigeringRequest

    override suspend fun execute(request: Request): Outcome<Edit.Attachment, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findByIdAndSaksId(request.brevId, request.saksId) ?: return null

        val principal = PrincipalInContext.require()
        redigerBrevPolicy.kanRedigere(brev, principal).onError { return failure(it) }

        val pesysdata = brevdataService.hentBrevdata(brev)
        val malVedlegg = brevmalService.renderRedigerbartVedlegg(brev, pesysdata, request.vedleggId)
            ?: return failure(VedleggFinnesIkkeIMal(request.brevId, request.vedleggId))

        brev.slettRedigertVedlegg(request.vedleggId)

        return success(malVedlegg.toEdit())
    }
}
