package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sivilstand

data class OrienteringOmRettigheterDto(
    val sivilstand: Sivilstand,
    val bor_i_norge: Boolean,
    val institusjon_gjeldende: Institusjon,
    val eps_bor_sammen_med_bruker_gjeldende: Boolean,
    val eps_institusjon_gjeldende: Institusjon,
    val har_barnetillegg_felles_barn_vedvirk: Boolean,
    val har_barnetillegg_for_saerkullsbarn_vedvirk: Boolean,
    val har_ektefelletillegg_vedvirk: Boolean,
    val saktype: Sakstype,
    val barnetillegg_beloep_gjeldendeBeregnetUTPerManed: Int,
) {
    constructor() : this(
        sivilstand = Sivilstand.ENSLIG,
        bor_i_norge = false,
        institusjon_gjeldende = Institusjon.INGEN,
        eps_bor_sammen_med_bruker_gjeldende = false,
        eps_institusjon_gjeldende = Institusjon.INGEN,
        har_barnetillegg_felles_barn_vedvirk = false,
        har_barnetillegg_for_saerkullsbarn_vedvirk = false,
        har_ektefelletillegg_vedvirk = false,
        saktype = Sakstype.UFOEREP,
        barnetillegg_beloep_gjeldendeBeregnetUTPerManed = 0,
    )
}
