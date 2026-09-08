package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brev.api.model.vedlegg.ReturAdresse

@Suppress("unused")
data class OmsorgEgenManuellDto(
    val returadresse: ReturAdresse,
) : VedleggData, FagsystemBrevdata