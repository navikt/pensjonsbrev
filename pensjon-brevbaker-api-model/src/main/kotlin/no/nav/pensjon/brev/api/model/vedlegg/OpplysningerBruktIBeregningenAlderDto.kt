package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.MetaforceSivilstand

@Suppress("unused")
data class OpplysningerBruktIBeregningenAlderDto(
    val beregnetPensjonPerManedVedVirk: AlderspensjonPerManed,
){
    data class AlderspensjonPerManed(
        val brukersSivilstand: MetaforceSivilstand
    )

}
