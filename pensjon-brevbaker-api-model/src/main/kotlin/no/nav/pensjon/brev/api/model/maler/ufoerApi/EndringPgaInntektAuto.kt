package no.nav.pensjon.brev.api.model.maler.ufoerApi

import no.nav.pensjon.brev.api.model.BorMedSivilstand
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


@Suppress("unused")
data class EndringPgaInntektAutoDto(
    val virkningsDatoFom: LocalDate,
    val barnetilleggFellesbarn: BarnetilleggFellesbarn?,
    val barnetilleggSaerkullsbarn: BarnetilleggSaerkullsbarn?,
    val sivilstand: Sivilstand,
    val ufoeretrygd: Ufoeretrygd,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
    val opplysningerBruktIBeregningUT: OpplysningerBruktIBeregningUTDto,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,

    ): BrevbakerBrevdata

data class Ufoeretrygd(
    val harBeloepEndringUfoeretrygd: Boolean,
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




