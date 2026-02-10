package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy.KanIkkeRedigere.LaastBrev
import no.nav.pensjon.brev.skribenten.model.Distribusjonstype
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.success

class EndreDistribusjonstypeHandler(
    private val redigerBrevPolicy: RedigerBrevPolicy,
) : BrevredigeringHandler<EndreDistribusjonstypeHandler.Request, Dto.Brevredigering> {

    data class Request(override val brevId: Long, val type: Distribusjonstype) : BrevredigeringRequest

    override suspend fun handle(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null
        val principal = PrincipalInContext.require()

        // Utfør kun endring om nødvendig
        if (brev.distribusjonstype != request.type) {
            redigerBrevPolicy.kanRedigere(brev, principal).onError(ignore = { it is LaastBrev }) { return failure(it) }

            brev.distribusjonstype = request.type
            brev.redigeresAv = null
        }

        return success(brev.toDto(null))
    }
}