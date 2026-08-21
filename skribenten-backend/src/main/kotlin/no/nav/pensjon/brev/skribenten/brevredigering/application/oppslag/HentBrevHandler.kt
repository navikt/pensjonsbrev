package no.nav.pensjon.brev.skribenten.brevredigering.application.oppslag

import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId

class HentBrevHandler(
    private val brevtilgang: Brevtilgang,
    private val brevmalService: BrevmalService,
    private val brevdataService: BrevdataService,
) {

    data class Request(
        val brevId: BrevId,
        val saksId: SaksId,
        val reserverForRedigering: Boolean = false,
    )

    suspend operator fun invoke(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        if (!request.reserverForRedigering) {
            brevtilgang.forLesing(request.brevId, request.saksId) { success(brev.tilDto()) }
        } else {
            brevtilgang.forRedigering(request.brevId, request.saksId) {
                val pesysdata = brevdataService.hentBrevdata(brev)
                val rendretBrev = brevmalService.renderMarkup(brev, pesysdata)
                brev.mergeRendretBrev(rendretBrev.markup)

                success(brev.tilDto(rendretBrev.letterDataUsage))
            }
        }
}
