package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.VedleggBrevdata

data class DineRettigheterOgMulighetTilAaKlageDto(
    val sakstype: Sakstype,
    val brukerUnder18Aar: Boolean?,
) : VedleggBrevdata