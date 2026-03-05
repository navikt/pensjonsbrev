package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto

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
        brev.frigiReservasjon()

        return success(brev.toBrevInfo(brevreservasjonPolicy))
    }

    override fun requiresReservasjon(request: Request) = true

    private suspend fun Dto.Mottaker.hentAnnenMottakerNavn(): String? =
        brevdataService.hentAnnenMottakerNavn(this)

}