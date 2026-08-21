package no.nav.pensjon.brev.skribenten.brevredigering.application.redigering

import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId

class LeggVedFoerstesideHandler(private val brevtilgang: Brevtilgang) {

    data class Request(val brevId: BrevId, val saksId: SaksId, val leggVedFoersteside: Boolean)

    suspend operator fun invoke(request: Request): Outcome<Dto.BrevInfo, BrevredigeringError>? =
        brevtilgang.forRedigering(request.brevId, request.saksId) {
            brev.leggVedFoersteside = request.leggVedFoersteside
            brev.frigiReservasjon()

            success(brev.tilBrevInfo())
        }
}
