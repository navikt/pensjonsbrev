package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.Kroner


@Suppress("unused")
data class EndringAvUttaksgradAutoDto(
    val alderspensjonVedVirk: AlderspensjonVedVirk,
    val harFlereBeregningsperioder: Boolean,
    val regelverkType: AlderspensjonRegelverkType,
    val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto?,
    val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto?,
    val maanedligPensjonFoerSkattAP2025Dto: MaanedligPensjonFoerSkattAP2025Dto?,
): BrevbakerBrevdata {

    data class AlderspensjonVedVirk(
        val privatAFPerBrukt: Boolean,
        val skjermingstilleggInnvilget: Boolean,
        val totalpensjon: Kroner,
        val uforeKombinertMedAlder: Boolean,
        val uttaksgrad: Int
    )
}