package no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
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
    val totalNettoAr: Int,
    val datoForNormertPensjonsalder: LocalDate,
    val sokerMottarApIlaAret: Boolean,
    val brukerBorINorge: Boolean,
    val pe: PE,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto

    ) : BrevbakerBrevdata
{
    data class Uforetrygd(
        val netto: Int,
        val endringsbelop: Int,
        val inntektBruktIAvkortning: Int,
        val uforegrad: Int,
        val inntektstak: Int,
        val inntektsgrense: Int,
        val nettoPerAr: Int,
        val nettoAkkumulert: Int,
        val nettoRestbelop: Int,
        val totalNettoInnevarendeAr: Int?,
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
        val periodisert: Boolean?,
        val totalNettoInnevarendeAr: Int?
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
