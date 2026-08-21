package no.nav.pensjon.brev.skribenten.brevredigering.application.livssyklus

import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId

class SlettBrevHandler(private val brevtilgang: Brevtilgang) {

    data class Request(val brevId: BrevId, val saksId: SaksId)

    suspend operator fun invoke(request: Request): Outcome<Unit, BrevredigeringError>? =
        brevtilgang.forSletting(request.brevId, request.saksId) {
            brev.delete()
            success(Unit)
        }
}
