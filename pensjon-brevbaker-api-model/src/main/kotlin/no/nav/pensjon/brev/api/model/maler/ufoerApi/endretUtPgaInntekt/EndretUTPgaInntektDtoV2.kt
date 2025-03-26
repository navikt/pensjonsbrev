package no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import java.time.LocalDate

data class EndretUTPgaInntektDtoV2 (
    val uforetrygd: Uforetrygd,
    val btfbEndret: Boolean,
    val btsbEndret: Boolean,
    val barnetilleggFellesbarn: BarnetilleggFellesbarn?,
    val barnetilleggSaerkullsbarn: BarnetilleggSaerkullsbarn?,
    val gjenlevendetillegg: Gjenlevendetillegg?,
    val forventetInntekt: Int?,
    val virkningFom: LocalDate,
    val totalNetto: Int,
    val datoForNormertPensjonsalder: LocalDate,
    val sokerMottarApIlaAret: Boolean,
    val settingAvInntektForNesteAar: Boolean,

    ) : BrevbakerBrevdata
{
    data class Uforetrygd(
        val netto: Int,
        val endringsbelop: Int,
        val inntektBruktIAvkortning: Int,
        val uforegrad: Int,
        val inntektstak: Int,
        val inntektsgrense: Int,
    )

    data class BarnetilleggFellesbarn(
        val netto: Int,
        val endringsbelop: Int,
        val totalInntektBruktIAvkortning: Int,
        val inntektBruker: Int,
        val inntektAnnenForelder: Int,
        val fribelop: Int,
        val inntektstak: Int,
        val antallBarn: Int,
    )

    data class BarnetilleggSaerkullsbarn(
        val netto: Int,
        val endringsbelop: Int,
        val inntektBruktIAvkortning: Int,
        val fribelop: Int,
        val inntektstak: Int,
        val antallBarn: Int,
    )

    data class Gjenlevendetillegg (
        val belop: Int,
    )
}