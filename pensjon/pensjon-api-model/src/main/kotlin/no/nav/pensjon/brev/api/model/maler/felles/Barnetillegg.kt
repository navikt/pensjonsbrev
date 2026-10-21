package no.nav.pensjon.brev.api.model.maler.felles

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

data class Barnetillegg(
    val btFellesbarn: BarnetilleggFellesbarn?,
    val btSerkullsbarn: BarnetilleggSerkullsbarn?,
    val antallForekomsterAvBarnetillegg: Int,
) {
    data class BarnetilleggFellesbarn(
        val innvilget: Boolean,
        val netto: Kroner,
        val brutto: Kroner,
        val justeringsbelopPerAr: Kroner?,
        val inntektAnnenForelder: Kroner?,
        val brukersInntektTilAvkorting: Kroner?,
        val inntektstak: Kroner?,
        val fradrag: Kroner?,
        val fribelop: Kroner?,
        val inntektBruktIAvkorting: Kroner?,
        val belopFratrukketAnnenForeldersInntekt: Kroner?,
        val antallBarn: Int,
    )

    data class BarnetilleggSerkullsbarn(
        val innvilget: Boolean,
        val netto: Kroner,
        val brutto: Kroner,
        val justeringsbelopPerAr: Kroner?,
        val inntektstak: Kroner?,
        val fradrag: Kroner?,
        val fribelop: Kroner?,
        val inntektBruktIAvkorting: Kroner?,
        val antallBarn: Int,
    )
}