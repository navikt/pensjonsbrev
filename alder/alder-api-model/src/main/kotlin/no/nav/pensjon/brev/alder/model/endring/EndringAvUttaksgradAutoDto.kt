package no.nav.pensjon.brev.alder.model.endring

import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.alder.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate


@Suppress("unused")
data class EndringAvUttaksgradAutoDto(
    val alderspensjonVedVirk: AlderspensjonVedVirk, // v5.Alderspensjon
    val harFlereBeregningsperioder: Boolean, // v1.BeregnetPensjonPerManed
    val kravVirkDatoFom: LocalDate, // v3.Krav
    val regelverkType: AlderspensjonRegelverkType,
    val maanedligPensjonFoerSkattAP2025Dto: MaanedligPensjonFoerSkattAP2025Dto?,
    val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto?,
    val opplysningerBruktIBeregningenEndretUttaksgradDto: OpplysningerBruktIBeregningenEndretUttaksgradDto?,
    val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto?,
): AutobrevData {

    data class AlderspensjonVedVirk(
        val privatAFPerBrukt: Boolean,
        val skjermingstilleggInnvilget: Boolean,
        val totalPensjon: Kroner,
        val ufoereKombinertMedAlder: Boolean,
        val uttaksgrad: Int
    )
}