package no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL

data class EndretUforetrygdPGAInntektNesteArRedigerbarDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EndretUforetrygdPGAInntektNesteArRedigerbarDto.PesysData> {
    data class PesysData(
        val vedtakData: EndretUforetrygdPGAInntektNesteArDto,
    ) : FagsystemBrevdata
}
