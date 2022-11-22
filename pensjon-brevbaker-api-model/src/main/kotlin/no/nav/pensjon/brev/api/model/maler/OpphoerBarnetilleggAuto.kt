package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import java.time.LocalDate

@Suppress("unused")
data class OpphoerBarnetilleggAutoDto(
    val foedselsdatoPaaBarnetilleggOpphoert: LocalDate,
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
    val ufoertrygdUtbetalt: Int,
    val utbetaltPerMaaned: Kroner,
    val ektefelletilleggUtbeltalt: Kroner?,
    val gjenlevendetilleggUtbetalt: Kroner?,
    val harUtbetalingsgrad: Boolean,

    )

data class BarnetilleggFellesbarn(
    val antallFellesbarnInnvilget: Int,
    val beloepFratrukketAnnenForeldersInntekt: Kroner,
    val beloepNettoFellesbarn: Kroner,
    val fradragFellesbarn: Kroner,
    val fribeloepFellesbarn: Kroner,
    val inntektAnnenForelderFellesbarn: Kroner,
    val inntektBruktIAvkortningFellesbarn: Kroner,
    val inntektstakFellesbarn: Kroner,
    val justeringsbeloepFellesbarn: Kroner
)

data class BarnetilleggSaerkullsbarn(
    val antallSaerkullsbarnbarnInnvilget: Int,
    val beloepNettoSaerkullsbarn: Kroner,
    val fradragSaerkullsbarn: Kroner,
    val fribeloepSaerkullsbarn: Kroner,
    val inntektBruktIAvkortningSaerkullsbarn: Kroner,
    val inntektstakSaerkullsbarn: Kroner,
    val justeringsbeloepSaerkullsbarn: Kroner
)
