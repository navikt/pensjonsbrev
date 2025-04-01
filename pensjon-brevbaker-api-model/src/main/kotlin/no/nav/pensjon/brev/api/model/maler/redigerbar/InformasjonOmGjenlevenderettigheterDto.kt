package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata

@Suppress("unused")
data class InformasjonOmGjenlevenderettigheterDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData
) : RedigerbarBrevdata<InformasjonOmGjenlevenderettigheterDto.SaksbehandlerValg, InformasjonOmGjenlevenderettigheterDto.PesysData> {
    data class SaksbehandlerValg(
        val id: Int // TODO
    ) : BrevbakerBrevdata

    data class PesysData(
        val sakstype: Sakstype
    ) : BrevbakerBrevdata
}