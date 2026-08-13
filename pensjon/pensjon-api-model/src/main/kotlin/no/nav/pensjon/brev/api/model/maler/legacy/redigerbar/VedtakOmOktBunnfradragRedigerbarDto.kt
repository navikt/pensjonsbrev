package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmOktBunnfradragData

data class VedtakOmOktBunnfradragRedigerbarDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, VedtakOmOktBunnfradragRedigerbarDto.PesysData> {
    data class PesysData(
        val vedtakData: VedtakOmOktBunnfradragData,
    ) : FagsystemBrevdata
}
