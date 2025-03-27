package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata

@Suppress("unused")
data class VarselRevurderingAvPensjonDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VarselRevurderingAvPensjonDto.SaksbehandlerValg, VarselRevurderingAvPensjonDto.PesysData> {
    data class SaksbehandlerValg(
      val tittelValg: TittelValg
    ) : BrevbakerBrevdata{
        enum class TittelValg{
            RevurderingAvRett, RevurderingReduksjon
        }
    }

    data class PesysData(val sakstype: Sakstype) : BrevbakerBrevdata
}