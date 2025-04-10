package no.nav.pensjon.brev.api.model.vedlegg

import java.time.LocalDate

@Suppress("unused")
data class OpplysningerOmAvdoedBruktIBeregningDto(
    val bruker: Bruker,
    val beregnetPensjonPerManedVedVirk: BeregnetPensjonPerManedVedVirk,
    val avdoed: Avdoed,
){
    data class Avdoed(
        val navn: String,
    )
    data class Bruker(
        val fodselsdato: LocalDate,
    )

    data class BeregnetPensjonPerManedVedVirk(
        val virkDatoFom: LocalDate,
    )
}