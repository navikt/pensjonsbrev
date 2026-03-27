package no.nav.pensjon.brev.alder.model.vedlegg

import no.nav.pensjon.brev.api.model.maler.VedleggData

data class AfpPrivatSokerUforeTrygdVedleggDto(
    val utTilattestering: Boolean,
) : VedleggData
