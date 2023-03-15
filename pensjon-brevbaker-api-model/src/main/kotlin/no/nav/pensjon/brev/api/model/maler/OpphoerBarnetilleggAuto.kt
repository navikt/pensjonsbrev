package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import java.time.LocalDate

@Suppress("unused")
data class OpphoerBarnetilleggAutoDto(
    val foedselsdatoPaaBarnMedOpphoertBarnetillegg: List<LocalDate>,
    val oensketVirkningsDato: LocalDate,
    val barnetilleggFellesbarn: BarnetilleggFellesbarn?,
    val barnetilleggSaerkullsbarn: BarnetilleggSaerkullsbarn?,
    val brukerBorInorge: Boolean,
    val grunnbeloep: Kroner,
    val sivilstand: Sivilstand,
    val ufoeretrygd: Ufoeretrygd,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
    val opplysningerBruktIBeregningUT: OpplysningerBruktIBeregningUTDto,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
)

data class Ufoeretrygd(
    val ufoertrygdUtbetalt: Kroner,
    val utbetaltPerMaaned: Kroner,
    val ektefelletilleggUtbeltalt: Kroner?,
    val gjenlevendetilleggUtbetalt: Kroner?,
    val harUtbetalingsgrad: Boolean,
    )

data class BarnetilleggFellesbarn(
    val gjelderFlereBarn: Boolean,
    val inntektstak: Kroner,
    val beloepBrutto: Kroner,
    val beloepNetto: Kroner,
    val fribeloep: Kroner,
    val harFradrag: Boolean,
    val harFratrukketBeloepFraAnnenForelder: Boolean,
    val harJusteringsbeloep: Boolean,
    val inntektAnnenForelder: Kroner,
    val inntektBruktIAvkortning: Kroner,
)

data class BarnetilleggSaerkullsbarn(
    val gjelderFlereBarn: Boolean,
    val inntektstak: Kroner,
    val beloepBrutto: Kroner,
    val beloepNetto: Kroner,
    val fribeloep: Kroner,
    val harFradrag: Boolean,
    val harJusteringsbeloep: Boolean,
    val inntektBruktIAvkortning: Kroner,
)
