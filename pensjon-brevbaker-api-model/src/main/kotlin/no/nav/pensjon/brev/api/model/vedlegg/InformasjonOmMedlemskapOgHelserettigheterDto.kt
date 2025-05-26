package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.maler.VedleggBrevdata

data class InformasjonOmMedlemskapOgHelserettigheterDto(
    val erEOSLand: Boolean
) : VedleggBrevdata