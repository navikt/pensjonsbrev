package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10

data class OpplysningerBruktIBeregningUTLegacyDto(
    val pe: PEgruppe10,
    val ftl_12_2_3_ledd: Boolean = false,
) : VedleggData
