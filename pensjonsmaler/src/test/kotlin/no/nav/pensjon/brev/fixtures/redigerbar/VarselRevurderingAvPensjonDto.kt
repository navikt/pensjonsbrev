package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselRevurderingAvPensjonDto


fun createVarselRevurderingAvPensjonDto() = VarselRevurderingAvPensjonDto(
    saksbehandlerValg = VarselRevurderingAvPensjonDto.SaksbehandlerValg(
        revurderingAvRett = false,
        revurderingReduksjon = false
    ),
    pesysData = VarselRevurderingAvPensjonDto.PesysData(sakstype = Sakstype.ALDER),
)
