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
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto,
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
    val harFradrag: Boolean,
    val harFratrukketBeloepFraAnnenForelder: Boolean, //todo hør med ingrid om navn. Hva betyr dette?
    val beloepBrutto: Kroner,
    val beloepNetto: Kroner,
    val fribeloep: Kroner,
    val inntektAnnenForelder: Kroner,
    val inntektBruktIAvkortning: Kroner,
    val inntektstak: Kroner,
    val harJusteringsbeloep: Boolean
)

data class BarnetilleggSaerkullsbarn(
    val gjelderFlereBarn: Boolean,
    val harFradrag: Boolean,
    val beloepBrutto: Kroner,
    val beloepNetto: Kroner,
    val fribeloep: Kroner,
    val inntektBruktIAvkortning: Kroner,
    val inntektstak: Kroner,
    val harJusteringsbeloep: Boolean,
)
