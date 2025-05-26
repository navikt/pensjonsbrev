package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.maler.VedleggBrevdata

@Suppress("unused")
data class OrienteringOmRettigheterAfpDto(
    val bruker_sivilstand: Sivilstand,
    val bruker_borINorge: Boolean,
    val institusjon_gjeldende: Institusjon,
) : VedleggBrevdata