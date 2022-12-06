package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.phrases.InnvilgetBarnetillegg
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
    override val gjelderFlereBarn: Boolean,
    override val inntektstak: Kroner,
    override val utbetalt: Boolean,
    val beloepBrutto: Kroner,
    val beloepNetto: Kroner,
    val fribeloep: Kroner,
    val harFradrag: Boolean,
    val harFratrukketBeloepFraAnnenForelder: Boolean, //todo hør med ingrid om navn. Hva betyr dette?
    val harJusteringsbeloep: Boolean,
    val inntektAnnenForelder: Kroner,
    val inntektBruktIAvkortning: Kroner,
): InnvilgetBarnetillegg

data class BarnetilleggSaerkullsbarn(
    override val gjelderFlereBarn: Boolean,
    override val inntektstak: Kroner,
    override val utbetalt: Boolean,
    val beloepBrutto: Kroner,
    val beloepNetto: Kroner,
    val fribeloep: Kroner,
    val harFradrag: Boolean,
    val harJusteringsbeloep: Boolean,
    val inntektBruktIAvkortning: Kroner,
): InnvilgetBarnetillegg
