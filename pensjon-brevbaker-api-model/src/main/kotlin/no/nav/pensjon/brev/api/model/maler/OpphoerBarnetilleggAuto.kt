package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import java.time.LocalDate

@Suppress("unused")
data class OpphoerBarnetilleggAutoDto(
    val barnetilleggFellesbarn: BarnetilleggFellesbarn?,
    val barnetilleggSaerkullsbarn: BarnetilleggSaerkullsbarn?,
    val brukerBorInorge: Boolean,
    val foedselsdatoPaaBarnMedOpphoertBarnetillegg: List<LocalDate>,
    val grunnbeloep: Kroner,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
    val oensketVirkningsDato: LocalDate,
    val opplysningerBruktIBeregningUT: OpplysningerBruktIBeregningUTDto,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
    val sivilstand: Sivilstand,
    val ufoeretrygd: Ufoeretrygd,
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
    val inntektstak: Kroner,
    val brukersInntektBruktIAvkortning: Kroner,
    val samletInntektBruktIAvkortning: Kroner,
)

data class BarnetilleggSaerkullsbarn(
    val beloepBrutto: Kroner,
    val beloepNetto: Kroner,
    val fribeloep: Kroner,
    val gjelderFlereBarn: Boolean,
    val harFradrag: Boolean,
    val harJusteringsbeloep: Boolean,
    val brukersInntektBruktIAvkortning: Kroner,
    val inntektstak: Kroner,
)
