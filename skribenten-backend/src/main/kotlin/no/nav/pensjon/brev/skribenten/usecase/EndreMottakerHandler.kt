package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.services.brev.BrevdataService
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.success
import java.time.Instant

class EndreMottakerHandler(
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    private val redigerBrevPolicy: RedigerBrevPolicy,
    private val brevdataService: BrevdataService,
) {
    data class Request(val brevId: Long, val mottaker: Dto.Mottaker?)

    suspend fun endreMottaker(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null

        val principal = PrincipalInContext.require()
        brev.reserver(Instant.now(), principal.navIdent, brevreservasjonPolicy).onError { return failure(it) }
        redigerBrevPolicy.kanRedigere(brev, principal).onError { return failure(it) }

        brev.settMottaker(request.mottaker, request.mottaker?.hentAnnenMottakerNavn())
        brev.redigeresAv = null

        return success(brev.toDto(null))
    }

    private suspend fun Dto.Mottaker.hentAnnenMottakerNavn(): String? =
        brevdataService.hentAnnenMottakerNavn(this)

}