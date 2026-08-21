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

    suspend operator fun invoke(request: Request): Outcome<Dto.BrevInfo, BrevredigeringError>? {
        // Utfør kun endring om nødvendig, og sjekk derfor også policy kun da.
        val erEndring = { brev: Brevredigering -> brev.distribusjonstype != request.type }

        return brevtilgang.forRedigering(request.brevId, request.saksId, tillatKlarmarkertBrev = true, sjekkPolicyNaar = erEndring) {
            if (erEndring(brev)) {
                brev.distribusjonstype = request.type
                brev.frigiReservasjon()
            }

            success(brev.tilBrevInfo())
        }
    }
}
