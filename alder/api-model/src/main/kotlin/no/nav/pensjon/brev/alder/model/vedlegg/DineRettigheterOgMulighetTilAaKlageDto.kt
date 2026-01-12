package no.nav.pensjon.brev.alder.model.vedlegg

import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.VedleggData

data class DineRettigheterOgMulighetTilAaKlageDto(
    val sakstype: Sakstype,
    val brukerUnder18Aar: Boolean?,
) : VedleggData