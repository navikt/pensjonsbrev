package no.nav.pensjon.brev.skribenten.brevredigering.application.redigering

import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Brevredigering
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId

class VeksleKlarStatusHandler(private val brevtilgang: Brevtilgang) {

    data class Request(val brevId: BrevId, val saksId: SaksId, val klar: Boolean)

    suspend operator fun invoke(request: Request): Outcome<Dto.BrevInfo, BrevredigeringError>? {
        // Policy sjekkes kun når statusen faktisk endres, ellers er dette en no-op.
        val erEndring = { brev: Brevredigering -> brev.laastForRedigering != request.klar }

        return if (request.klar) {
            brevtilgang.forKlarmarkering(request.brevId, request.saksId, sjekkPolicyNaar = erEndring) {
                if (erEndring(brev)) {
                    brev.markerSomKlar()
                    brev.frigiReservasjon()
                }
                success(brev.tilBrevInfo())
            }
        } else {
            brevtilgang.forRedigering(request.brevId, request.saksId, tillatKlarmarkertBrev = true, sjekkPolicyNaar = erEndring) {
                if (erEndring(brev)) {
                    brev.markerSomKladd()
                    brev.frigiReservasjon()
                }
                success(brev.tilBrevInfo())
            }
        }
    }
}
