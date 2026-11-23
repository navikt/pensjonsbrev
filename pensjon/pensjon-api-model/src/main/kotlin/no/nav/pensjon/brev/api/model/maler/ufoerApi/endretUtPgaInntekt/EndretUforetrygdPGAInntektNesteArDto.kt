package no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.FribelopUtvikling
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Fribelopsperiode
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class EndretUforetrygdPGAInntektNesteArDto(
    val uforetrygd: Uforetrygd,
    val btfbEndret: Boolean,
    val btsbEndret: Boolean,
    val gjtEndret: Boolean,
    val barnetilleggFellesbarn: BarnetilleggFellesbarn?,
    val barnetilleggSaerkullsbarn: BarnetilleggSaerkullsbarn?,
    val gjenlevendetillegg: Gjenlevendetillegg?,
    val virkningFom: LocalDate,
    val opphorDato: LocalDate?,
    val totalNetto: Kroner,
    val totalNettoAr: Kroner,
    val datoForNormertPensjonsalder: LocalDate,
    val sokerMottarApIlaAret: Boolean,
    val brukerBorINorge: Boolean,
    val pe: PEgruppe10,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto

) {
    data class Uforetrygd(
        val erGradert: Boolean,
        val netto: Kroner,
        val endringsbelop: Kroner,
        val inntektBruktIAvkortning: Kroner,
        val inntektstak: Kroner,
        val inntektEtterUforhet: Kroner,
        val fribelop: Kroner,
        val bunnfradrag: Kroner,
        val okningUforegradVedArskjoring: Boolean?,
        val fribelopsperioder: List<Fribelopsperiode>,
        val fribelopUtvikling: FribelopUtvikling,
        )

    data class BarnetilleggFellesbarn(
        val netto: Kroner,
        val inntektBruker: Kroner,
        val inntektAnnenForelder: Kroner,
        val periodisert: Boolean,
    )

    data class BarnetilleggSaerkullsbarn(
        val netto: Kroner,
        val inntektBruktIAvkortning: Kroner,
        val periodisert: Boolean,
    )

    data class Gjenlevendetillegg(
        val belop: Kroner,
    )
}