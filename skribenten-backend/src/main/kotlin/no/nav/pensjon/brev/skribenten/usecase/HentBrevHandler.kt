package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.services.brev.BrevdataService
import no.nav.pensjon.brev.skribenten.services.brev.RenderService
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.success

class HentBrevHandler(
    private val redigerBrevPolicy: RedigerBrevPolicy,
    private val renderService: RenderService,
    private val brevdataService: BrevdataService,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
) : BrevredigeringHandler<HentBrevHandler.Request, Dto.Brevredigering> {

    data class Request(
        override val brevId: Long,
        val reserverForRedigering: Boolean = false,
    ) : BrevredigeringRequest

    override suspend fun handle(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null

        if (!request.reserverForRedigering) {
            return success(brev.toDto(brevreservasjonPolicy, null))
        }

        val principal = PrincipalInContext.require()
        redigerBrevPolicy.kanRedigere(brev, principal).onError { return failure(it) }

        val pesysdata = brevdataService.hentBrevdata(brev)
        val rendretBrev = renderService.renderMarkup(brev, pesysdata)
        brev.mergeRendretBrev(rendretBrev.markup)

        return success(brev.toDto(brevreservasjonPolicy, rendretBrev.letterDataUsage))
    }

    override fun requiresReservasjon(request: Request): Boolean =
        request.reserverForRedigering
}
