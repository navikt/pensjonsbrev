package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmOktFribelopData

data class VedtakOmOktFribelopRedigerbarDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VedtakOmOktFribelopRedigerbarDto.PesysData> {
    data class PesysData(
        val vedtakData: VedtakOmOktFribelopData,
    ) : FagsystemBrevdata
}
