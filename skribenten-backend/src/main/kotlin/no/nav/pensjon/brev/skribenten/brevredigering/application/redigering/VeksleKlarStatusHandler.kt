package no.nav.pensjon.brev.skribenten.brevredigering.application.redigering

import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Brevredigering
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.FerdigRedigertPolicy
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId

class VeksleKlarStatusHandler(
    private val brevtilgang: Brevtilgang,
    private val ferdigRedigertPolicy: FerdigRedigertPolicy,
) {

    data class Request(val brevId: BrevId, val saksId: SaksId, val klar: Boolean)

    suspend operator fun invoke(request: Request): Outcome<Dto.BrevInfo, BrevredigeringError>? {
        fun trengerEndring(brev: Brevredigering) = brev.laastForRedigering != request.klar

        return if (request.klar) {
            brevtilgang.forStatusendring(request.brevId, request.saksId, trengerEndring = ::trengerEndring) {
                ferdigRedigertPolicy.erFerdigRedigert(brev).onError { return@forStatusendring failure(it) }

                brev.markerSomKlar()

                success(Unit)
            }
        } else {
            brevtilgang.forStatusendring(request.brevId, request.saksId, trengerEndring = ::trengerEndring) {
                brev.markerSomKladd()

                success(Unit)
            }
        }
    }
}
