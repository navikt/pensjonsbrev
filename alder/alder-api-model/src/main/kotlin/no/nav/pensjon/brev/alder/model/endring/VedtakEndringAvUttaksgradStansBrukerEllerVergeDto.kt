package no.nav.pensjon.brev.alder.model.endring

import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import java.time.LocalDate

data class VedtakEndringAvUttaksgradStansBrukerEllerVergeDto(
    val krav: Krav,
    val alderspensjonVedVirk: AlderspensjonVedVirk,
    val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto
) : FagsystemBrevdata {
    data class Krav(
        val virkDatoFom: LocalDate
    )

    data class AlderspensjonVedVirk(
        val skjermingstilleggInnvilget: Boolean,
        val regelverkType: AlderspensjonRegelverkType
    )
}