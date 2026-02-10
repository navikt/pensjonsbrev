package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.services.brev.BrevdataService
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.success

class EndreMottakerHandler(
    private val redigerBrevPolicy: RedigerBrevPolicy,
    private val brevdataService: BrevdataService,
) : BrevredigeringHandler<EndreMottakerHandler.Request, Dto.Brevredigering> {

    data class Request(override val brevId: Long, val mottaker: Dto.Mottaker?) : BrevredigeringRequest

    override suspend fun handle(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null

        val principal = PrincipalInContext.require()
        redigerBrevPolicy.kanRedigere(brev, principal).onError { return failure(it) }

        brev.settMottaker(request.mottaker, request.mottaker?.hentAnnenMottakerNavn())
        brev.redigeresAv = null

        return success(brev.toDto(null))
    }

    private suspend fun Dto.Mottaker.hentAnnenMottakerNavn(): String? =
        brevdataService.hentAnnenMottakerNavn(this)

}