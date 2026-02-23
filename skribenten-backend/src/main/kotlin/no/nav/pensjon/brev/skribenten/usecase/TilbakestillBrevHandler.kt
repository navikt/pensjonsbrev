package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.domain.*
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.services.BrevbakerService
import no.nav.pensjon.brev.skribenten.services.brev.BrevdataService
import no.nav.pensjon.brev.skribenten.services.brev.RenderService
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.success

class TilbakestillBrevHandler(
    private val redigerBrevPolicy: RedigerBrevPolicy,
    private val brevbakerService: BrevbakerService,
    private val renderService: RenderService,
    private val brevdataService: BrevdataService,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
) : BrevredigeringHandler<TilbakestillBrevHandler.Request, Dto.Brevredigering> {

    data class Request(
        override val brevId: BrevId,
    ) : BrevredigeringRequest

    override suspend fun handle(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null
        val principal = PrincipalInContext.require()

        redigerBrevPolicy.kanRedigere(brev, principal).onError { return failure(it) }

        val modelSpec = brevbakerService.getModelSpecification(brev.brevkode)
            ?: return failure(BrevmalFinnesIkke(brev.brevkode))
        brev.tilbakestillSaksbehandlerValg(modelSpec)

        val pesysdata = brevdataService.hentBrevdata(brev)
        val rendretBrev = renderService.renderMarkup(brev, pesysdata)
        brev.oppdaterRedigertBev(rendretBrev.markup.toEdit(), principal.navIdent)

        return success(brev.toDto(brevreservasjonPolicy, rendretBrev.letterDataUsage))
    }

    override fun requiresReservasjon(request: Request) = true
}


