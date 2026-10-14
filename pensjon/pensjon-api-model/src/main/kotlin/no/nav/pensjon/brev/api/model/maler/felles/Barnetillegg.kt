package no.nav.pensjon.brev.api.model.maler.felles

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

data class Barnetillegg(
    val btErInnvilget: Boolean,
    val btFellesbarn: BarnetilleggFellesbarn?,
    val btSerkullsbarn: BarnetilleggSerkullsbarn?
) {
    //todo ikke sjekket null her
    data class BarnetilleggFellesbarn(
        val innvilget: Boolean,
        val netto: Kroner?,
        val brutto: Kroner?,
        val justeringsbelopPerAr: Kroner?,
        val inntektAnnenForelder: Kroner?,
        val brukersInntektTilAvkorting: Kroner?,
        val inntektstak: Kroner?,
        val fradrag: Kroner?,
        val fribelop: Kroner?,
        val inntektBruktIAvkorting: Kroner?,
        val belopFratrukketAnnenForeldersInntekt: Kroner?,
    )

    data class BarnetilleggSerkullsbarn(
        val innvilget: Boolean,
        val netto: Kroner?,
        val brutto: Kroner?,
        val justeringsbelopPerAr: Kroner?,
        val inntektstak: Kroner?,
        val fradrag: Kroner?,
        val fribelop: Kroner?,
        val inntektBruktIAvkorting: Kroner?
    )
}