package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.legacy.EPSEndringIBarnetilleggData

data class EPSEndringIBarnetilleggRedigerbarDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EPSEndringIBarnetilleggRedigerbarDto.PesysData> {
    data class PesysData(
        val vedtakData: EPSEndringIBarnetilleggData,
    ) : FagsystemBrevdata
}
