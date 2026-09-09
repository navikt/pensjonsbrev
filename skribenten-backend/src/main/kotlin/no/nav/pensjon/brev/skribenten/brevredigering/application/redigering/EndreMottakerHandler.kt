package no.nav.pensjon.brev.skribenten.brevredigering.application.redigering

import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId

class EndreMottakerHandler(
    private val brevtilgang: Brevtilgang,
    private val brevdataService: BrevdataService,
) {

    data class Request(val brevId: BrevId, val saksId: SaksId, val mottaker: Dto.Mottaker?)

    suspend operator fun invoke(request: Request): Outcome<Dto.BrevInfo, BrevredigeringError>? =
        brevtilgang.forRedigering(request.brevId, request.saksId, frigiReservasjon = true) {
            brev.settMottaker(request.mottaker, request.mottaker?.let { brevdataService.hentAnnenMottakerNavn(it) })

            success(brev.tilBrevInfo())
        }
}
