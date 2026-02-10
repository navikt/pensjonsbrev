package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksbehandlerValg
import no.nav.pensjon.brev.skribenten.services.brev.BrevdataService
import no.nav.pensjon.brev.skribenten.services.brev.RenderService
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.success

class OppdaterBrevHandler(
    private val redigerBrevPolicy: RedigerBrevPolicy,
    private val renderService: RenderService,
    private val brevdataService: BrevdataService,
) : BrevredigeringHandler<OppdaterBrevHandler.Request, Dto.Brevredigering> {

    data class Request(
        override val brevId: Long,
        val nyeSaksbehandlerValg: SaksbehandlerValg? = null,
        val nyttRedigertbrev: Edit.Letter? = null,
        val frigiReservasjon: Boolean = false,
    ) : BrevredigeringRequest

    override suspend fun handle(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null
        val principal = PrincipalInContext.require()

        redigerBrevPolicy.kanRedigere(brev, principal).onError { return failure(it) }

        if (request.nyeSaksbehandlerValg != null) {
            brev.saksbehandlerValg = request.nyeSaksbehandlerValg
        }
        if (request.nyttRedigertbrev != null) {
            brev.oppdaterRedigertBev(request.nyttRedigertbrev, principal.navIdent)
        }

        val pesysdata = brevdataService.hentBrevdata(brev)
        val rendretBrev = renderService.renderMarkup(brev, pesysdata)
        brev.mergeRendretBrev(rendretBrev.markup)

        if (request.frigiReservasjon) {
            brev.redigeresAv = null
        }

        return success(brev.toDto(rendretBrev.letterDataUsage))
    }
}
