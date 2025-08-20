package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.maler.VedleggDto

data class InformasjonOmMedlemskapOgHelserettigheterDto(
    val erEOSLand: Boolean
) : VedleggDto