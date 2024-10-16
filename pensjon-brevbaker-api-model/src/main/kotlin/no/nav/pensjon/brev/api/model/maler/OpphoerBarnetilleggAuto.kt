package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.BorMedSivilstand
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class OpphoerBarnetilleggAutoDto(
    val foedselsdatoPaaBarnMedOpphoertBarnetillegg: List<LocalDate>,
    val oensketVirkningsDato: LocalDate,
    val barnetilleggFellesbarn: BarnetilleggFellesbarn?,
    val barnetilleggSaerkullsbarn: BarnetilleggSaerkullsbarn?,
    val brukerBorInorge: Boolean,
    val grunnbeloep: Kroner,
    val ufoeretrygd: Ufoeretrygd,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
    val opplysningerBruktIBeregningUT: OpplysningerBruktIBeregningUTDto,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
) : BrevbakerBrevdata

data class Ufoeretrygd(
    val ufoertrygdUtbetalt: Kroner,
    val utbetaltPerMaaned: Kroner,
    val ektefelletilleggUtbeltalt: Kroner?,
    val gjenlevendetilleggUtbetalt: Kroner?,
    val harUtbetalingsgrad: Boolean,
)

data class BarnetilleggFellesbarn(
    val brukerBorMed: BorMedSivilstand,
    val gjelderFlereBarn: Boolean,
    val inntektstak: Kroner,
    val beloepBrutto: Kroner,
    val beloepNetto: Kroner,
    val fribeloep: Kroner,
    val harFradrag: Boolean,
    val harFratrukketBeloepFraAnnenForelder: Boolean,
    val harJusteringsbeloep: Boolean,
    val inntektAnnenForelder: Kroner,
    val brukersIntektBruktIAvkortning: Kroner,
    val samletInntektBruktIAvkortning: Kroner,
)

data class BarnetilleggSaerkullsbarn(
    val brukerBorMed: BorMedSivilstand?,
    val gjelderFlereBarn: Boolean,
    val inntektstak: Kroner,
    val beloepBrutto: Kroner,
    val beloepNetto: Kroner,
    val fribeloep: Kroner,
    val harFradrag: Boolean,
    val harJusteringsbeloep: Boolean,
    val inntektBruktIAvkortning: Kroner,
)
