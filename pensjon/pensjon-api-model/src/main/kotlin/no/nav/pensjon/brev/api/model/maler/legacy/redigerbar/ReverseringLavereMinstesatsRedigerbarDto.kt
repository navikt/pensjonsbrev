package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.ReverseringLavereMinstesatsDto

data class ReverseringLavereMinstesatsRedigerbarDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, ReverseringLavereMinstesatsRedigerbarDto.PesysData> {
    data class PesysData(
        val data: ReverseringLavereMinstesatsDto
    ) : FagsystemBrevdata
}