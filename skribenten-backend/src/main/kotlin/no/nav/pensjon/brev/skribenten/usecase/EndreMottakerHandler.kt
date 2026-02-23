package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.services.brev.BrevdataService
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.success

class EndreMottakerHandler(
    private val redigerBrevPolicy: RedigerBrevPolicy,
    private val brevdataService: BrevdataService,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
) : BrevredigeringHandler<EndreMottakerHandler.Request, Dto.BrevInfo> {

    data class Request(override val brevId: BrevId, val mottaker: Dto.Mottaker?) : BrevredigeringRequest

    override suspend fun handle(request: Request): Outcome<Dto.BrevInfo, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null

        val principal = PrincipalInContext.require()
        redigerBrevPolicy.kanRedigere(brev, principal).onError { return failure(it) }

        brev.settMottaker(request.mottaker, request.mottaker?.hentAnnenMottakerNavn())
        brev.redigeresAv = null

        return success(brev.toBrevInfo(brevreservasjonPolicy))
    }

    override fun requiresReservasjon(request: Request) = true

    private suspend fun Dto.Mottaker.hentAnnenMottakerNavn(): String? =
        brevdataService.hentAnnenMottakerNavn(this)

}