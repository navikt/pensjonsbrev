package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.success
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggKode

class EndreValgteVedleggHandler(
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    private val redigerBrevPolicy: RedigerBrevPolicy,
) : BrevredigeringHandler<EndreValgteVedleggHandler.Request, Dto.Brevredigering> {

    data class Request(
        override val brevId: BrevId,
        val alltidValgbareVedlegg: List<AlltidValgbartVedleggKode>,
    ) : BrevredigeringRequest

    override suspend fun handle(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null

        val principal = PrincipalInContext.require()
        redigerBrevPolicy.kanRedigere(brev, principal).onError { return failure(it) }

        // Om det er endringer i valgte vedlegg, så nullstiller vi dokumentet slik at det må rendres på nytt
        if (brev.valgteVedlegg != request.alltidValgbareVedlegg) {
            brev.document = null
        }

        brev.valgteVedlegg = request.alltidValgbareVedlegg
        brev.redigeresAv = null

        return success(brev.toDto(brevreservasjonPolicy, null))
    }

    override fun requiresReservasjon(request: Request) = true
}