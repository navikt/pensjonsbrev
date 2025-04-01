package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata

@Suppress("unused")
data class InformasjonOmGjenlevenderettigheterDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: EmptyBrevdata
) : RedigerbarBrevdata<InformasjonOmGjenlevenderettigheterDto.SaksbehandlerValg, EmptyBrevdata> {
    data class SaksbehandlerValg(
        val id: Int // TODO
    ) : BrevbakerBrevdata
}