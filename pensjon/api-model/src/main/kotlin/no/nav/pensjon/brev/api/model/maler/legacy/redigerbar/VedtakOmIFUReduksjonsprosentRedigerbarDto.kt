package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentData

data class VedtakOmIFUReduksjonsprosentRedigerbarDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, VedtakOmIFUReduksjonsprosentRedigerbarDto.PesysData> {
    data class PesysData(
        val vedtakData: VedtakOmIFUReduksjonsprosentData,
    ) : FagsystemBrevdata
}
