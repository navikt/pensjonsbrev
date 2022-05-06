package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sivilstand

data class OrienteringOmRettigheterAlderDto(
    val bruker_sivilstand: Sivilstand,
    val bruker_borINorge: Boolean,
    val institusjon_gjeldende: Institusjon,
    val eps_borSammenMedBrukerGjeldende: Boolean,
    val instutisjon_epsInstitusjonGjeldende: Institusjon,
    val barnetilleggVedvirk_innvilgetBarnetillegFellesbarn: Boolean,
    val barnetilleggVedvirk_innvilgetBarnetilleggSaerkullsbarn: Boolean,
    val ektefelletilleggVedvirk_innvilgetEktefelletillegg: Boolean,
    val saktype: Sakstype,
) {
    constructor() : this(
        bruker_sivilstand = Sivilstand.ENSLIG,
        bruker_borINorge = false,
        institusjon_gjeldende = Institusjon.INGEN,
        eps_borSammenMedBrukerGjeldende = false,
        instutisjon_epsInstitusjonGjeldende = Institusjon.INGEN,
        barnetilleggVedvirk_innvilgetBarnetillegFellesbarn = false,
        barnetilleggVedvirk_innvilgetBarnetilleggSaerkullsbarn = false,
        ektefelletilleggVedvirk_innvilgetEktefelletillegg = false,
        saktype = Sakstype.UFOEREP,
    )
}
