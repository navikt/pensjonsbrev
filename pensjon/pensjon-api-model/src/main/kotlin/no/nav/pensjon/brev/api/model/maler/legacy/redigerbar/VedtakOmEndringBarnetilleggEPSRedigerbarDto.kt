package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSData

data class VedtakOmEndringBarnetilleggEPSRedigerbarDto(
    val vedtakData: VedtakOmEndringBarnetilleggEPSData,
) : FagsystemBrevdata
