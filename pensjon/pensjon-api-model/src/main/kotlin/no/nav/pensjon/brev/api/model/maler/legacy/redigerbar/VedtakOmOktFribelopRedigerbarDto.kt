package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmOktFribelopData

data class VedtakOmOktFribelopRedigerbarDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : BrevdataMedSaksbehandlerValg<VedtakOmOktFribelopRedigerbarDto.PesysData> {
    data class PesysData(
        val vedtakData: VedtakOmOktFribelopData,
    ) : FagsystemBrevdata
}
