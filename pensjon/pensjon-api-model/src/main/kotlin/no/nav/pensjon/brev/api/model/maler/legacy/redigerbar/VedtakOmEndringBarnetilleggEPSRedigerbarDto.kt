package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSData

data class VedtakOmEndringBarnetilleggEPSRedigerbarDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VedtakOmEndringBarnetilleggEPSRedigerbarDto.PesysData> {
    data class PesysData(
        val vedtakData: VedtakOmEndringBarnetilleggEPSData,
    ) : FagsystemBrevdata
}
