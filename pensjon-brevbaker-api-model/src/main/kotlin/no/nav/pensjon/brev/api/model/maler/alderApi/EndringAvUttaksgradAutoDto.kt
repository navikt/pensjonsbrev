package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


@Suppress("unused")
data class EndringAvUttaksgradAutoDto(
    val alderspensjonVedVirk: AlderspensjonVedVirk,  // v5.Alderspensjon
    val harFlereBeregningsperioder: Boolean,  // v1.BeregnetPensjonPerManed
    val kravVirkDatoFom: LocalDate,  // v3.Krav
    val regelverkType: AlderspensjonRegelverkType,
    val maanedligPensjonFoerSkattAP2025Dto: MaanedligPensjonFoerSkattAP2025Dto?,
    val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto?,
    val opplysningerBruktIBeregningenEndretUttaksgradDto: OpplysningerBruktIBeregningenEndretUttaksgradDto?,
    val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto?,
): BrevbakerBrevdata {

    data class AlderspensjonVedVirk(
        val privatAFPerBrukt: Boolean,
        val skjermingstilleggInnvilget: Boolean,
        val totalPensjon: Kroner,
        val ufoereKombinertMedAlder: Boolean,
        val uttaksgrad: Int
    )
}