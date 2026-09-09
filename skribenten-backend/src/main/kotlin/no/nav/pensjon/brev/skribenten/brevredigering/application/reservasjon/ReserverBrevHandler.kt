package no.nav.pensjon.brev.skribenten.brevredigering.application.reservasjon

import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Reservasjon
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId

class ReserverBrevHandler(private val brevtilgang: Brevtilgang) {

    data class Request(val brevId: BrevId, val saksId: SaksId)

    suspend operator fun invoke(request: Request): Outcome<Reservasjon, BrevredigeringError>? =
        brevtilgang.reserver(request.brevId, request.saksId)
}
