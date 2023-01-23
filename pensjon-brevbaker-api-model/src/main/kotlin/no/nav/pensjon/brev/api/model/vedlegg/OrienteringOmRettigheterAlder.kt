package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.Sivilstand

@Suppress("unused")
data class OrienteringOmRettigheterAlderDto(
    val bruker_sivilstand: Sivilstand,
    val bruker_borINorge: Boolean,
    val harTilleggForFlereBarn: Boolean,
    val institusjon_gjeldende: Institusjon,
    val eps_borSammenMedBrukerGjeldende: Boolean,
    val instutisjon_epsInstitusjonGjeldende: Institusjon,
    val barnetilleggVedvirk_innvilgetBarnetillegFellesbarn: Boolean,
    val barnetilleggVedvirk_innvilgetBarnetilleggSaerkullsbarn: Boolean,
    val ektefelletilleggVedvirk_innvilgetEktefelletillegg: Boolean,
)
