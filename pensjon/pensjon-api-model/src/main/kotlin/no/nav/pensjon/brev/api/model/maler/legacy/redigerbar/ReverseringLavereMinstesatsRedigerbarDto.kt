package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.legacy.ReverseringLavereMinstesatsDto

data class ReverseringLavereMinstesatsRedigerbarDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : BrevdataMedSaksbehandlerValg<ReverseringLavereMinstesatsRedigerbarDto.PesysData> {
    data class PesysData(
        val data: ReverseringLavereMinstesatsDto
    ) : FagsystemBrevdata
}