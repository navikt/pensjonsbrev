package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata

@Suppress("unused")
data class VarselRevurderingAvPensjonDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: EmptyBrevdata
) : RedigerbarBrevdata<VarselRevurderingAvPensjonDto.SaksbehandlerValg, EmptyBrevdata> {
    data class SaksbehandlerValg(
        val revurderingAvRett: Boolean = false,
        val revurderingReduksjon: Boolean = false,
    ) : BrevbakerBrevdata
}