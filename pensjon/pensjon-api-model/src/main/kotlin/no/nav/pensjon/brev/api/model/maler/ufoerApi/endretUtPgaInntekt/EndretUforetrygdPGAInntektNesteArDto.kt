package no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import java.time.LocalDate

data class EndretUforetrygdPGAInntektNesteArDto (
    val uforetrygd: Uforetrygd,
    val btfbEndret: Boolean,
    val btsbEndret: Boolean,
    val gjtEndret: Boolean,
    val barnetilleggFellesbarn: BarnetilleggFellesbarn?,
    val barnetilleggSaerkullsbarn: BarnetilleggSaerkullsbarn?,
    val gjenlevendetillegg: Gjenlevendetillegg?,
    val virkningFom: LocalDate,
    val totalNetto: Int,
    val totalNettoAr: Int,
    val datoForNormertPensjonsalder: LocalDate,
    val sokerMottarApIlaAret: Boolean,
    val brukerBorINorge: Boolean,
    val pe: PEgruppe10,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto

    ) : AutobrevData
{
    data class Uforetrygd(
        val netto: Int,
        val endringsbelop: Int,
        val inntektBruktIAvkortning: Int,
        val inntektstak: Int,
        val inntektsgrense: Int,
        val okningUforegradVedArsjoring: Boolean?,
    )

    data class BarnetilleggFellesbarn(
        val netto: Int,
        val inntektBruker: Int,
        val inntektAnnenForelder: Int,
        val periodisert: Boolean,
    )

    data class BarnetilleggSaerkullsbarn(
        val netto: Int,
        val inntektBruktIAvkortning: Int,
        val periodisert: Boolean,
    )

    data class Gjenlevendetillegg (
        val belop: Int,
    )
}
