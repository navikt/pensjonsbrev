package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class EndringOpptjeningAutoDto(
    val barnetilleggFellesbarn: BarnetilleggFellesbarn?,
    val barnetilleggSaerkullsbarn: BarnetilleggSaerkullsbarn?,
    val endringIOpptjening: EndringIOpptjening,
    val folketrygdloven: Folketrygdloven, // in use
    val ufoeretrygd: Ufoeretrygd,  // in use
    val virkningsDato: LocalDate,  // in use
) {

    data class EndringIOpptjening(
        val ufoerBeloepOekt: Boolean,  // in use
        val ufoerBeloepRedusert: Boolean,  // in use
    )

    data class Folketrygdloven(
        val harYrkesskadeGradUtbetaling: Boolean, // yrkesskadeGrad > 0  - in use
        val innvilgetEktefelletillegg: Boolean,  // in use
        val innvilgetFellesbarntillegg: Boolean,  // in use
        val innvilgetGjenlevendetillegg: Boolean,  // in use
        val innvilgetSaerkullsbarntillegg: Boolean,  // in use
    )

    data class Ufoeretrygd(
        val ektefelletilleggUtbeltalt: Kroner?,
        val gjenlevendetilleggUtbetalt: Kroner?,
        val harUtbetalingsgrad: Boolean,
        val ufoertrygdUtbetalt: Kroner,
        val utbetaltPerMaaned: Kroner,
    )

    data class BarnetilleggFellesbarn(
        val beloepBrutto: Kroner,
        val beloepNetto: Kroner,
        val fribeloep: Kroner,
        val gjelderFlereBarn: Boolean,
        val harFradrag: Boolean,
        val harFratrukketBeloepFraAnnenForelder: Boolean,
        val harJusteringsbeloep: Boolean,
        val inntektAnnenForelder: Kroner,
        val inntektBruktIAvkortning: Kroner,
        val inntektstak: Kroner,
        val innvilgetTilleggFellesbarn: Boolean,  // in use
    )

    data class BarnetilleggSaerkullsbarn(
        val beloepBrutto: Kroner,
        val beloepNetto: Kroner,
        val fribeloep: Kroner,
        val gjelderFlereBarn: Boolean,
        val harFradrag: Boolean,
        val harJusteringsbeloep: Boolean,
        val inntektBruktIAvkortning: Kroner,
        val inntektstak: Kroner,
        val innvilgetTilleggSaerkullsbarn: Boolean,  // in use
    )
}