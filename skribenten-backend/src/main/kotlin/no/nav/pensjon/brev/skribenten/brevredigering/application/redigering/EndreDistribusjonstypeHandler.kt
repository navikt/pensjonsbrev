package no.nav.pensjon.brev.skribenten.brevredigering.application.redigering

import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Brevredigering
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Distribusjon
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId

class EndreDistribusjonstypeHandler(private val brevtilgang: Brevtilgang) {

    data class Request(val brevId: BrevId, val saksId: SaksId, val type: Distribusjon)

    suspend operator fun invoke(request: Request): Outcome<Dto.BrevInfo, BrevredigeringError>? =
        brevtilgang.forStatusendring(
            request.brevId,
            request.saksId,
            trengerEndring = { it.distribusjonstype != request.type },
        ) {
            brev.distribusjonstype = request.type

            success(Unit)
        }
}
