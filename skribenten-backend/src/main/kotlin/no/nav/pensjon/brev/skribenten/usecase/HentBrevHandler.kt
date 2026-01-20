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
import java.time.Instant

class HentBrevHandler(
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    private val redigerBrevPolicy: RedigerBrevPolicy,
    private val renderService: RenderService,
    private val brevdataService: BrevdataService,
) {
    data class Request(
        val brevId: Long,
        val reserverForRedigering: Boolean = false,
    )

    suspend fun handle(req: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(req.brevId) ?: return null

        if (!req.reserverForRedigering) {
            return success(brev.toDto(null))
        }

        val principal = PrincipalInContext.require()
        brev.reserver(Instant.now(), principal.navIdent, brevreservasjonPolicy).onError { return failure(it) }
        redigerBrevPolicy.kanRedigere(brev, principal).onError { return failure(it) }

        val pesysdata = brevdataService.hentBrevdata(brev)
        val rendretBrev = renderService.renderMarkup(brev, pesysdata)
        brev.mergeRendretBrev(rendretBrev.markup)

        return success(brev.toDto(rendretBrev.letterDataUsage))
    }
}
