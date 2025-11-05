package no.nav.pensjon.brev.model.alder.vedlegg

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.VedleggData

data class DineRettigheterOgMulighetTilAaKlageDto(
    val sakstype: Sakstype,
    val brukerUnder18Aar: Boolean?,
) : VedleggData