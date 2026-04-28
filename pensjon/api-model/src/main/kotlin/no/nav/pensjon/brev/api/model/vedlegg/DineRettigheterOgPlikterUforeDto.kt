package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.maler.VedleggData

data class DineRettigheterOgPlikterUforeDto(
    val utland: Boolean,
) : VedleggData
