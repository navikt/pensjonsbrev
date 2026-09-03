package no.nav.pensjon.brev.skribenten.brevredigering.application.oppslag

import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto

class HentBrevInfoHandler(private val brevtilgang: Brevtilgang) {

    data class Request(val brevId: BrevId)

    suspend operator fun invoke(request: Request): Outcome<Dto.BrevInfo, Nothing>? =
        brevtilgang.forLesing(request.brevId, saksId = null) { success(brev.tilBrevInfo()) }
}
