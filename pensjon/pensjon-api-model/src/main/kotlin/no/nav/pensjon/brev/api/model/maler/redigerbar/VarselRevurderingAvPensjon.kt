package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL

@Suppress("unused")
data class VarselRevurderingAvPensjonDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : RedigerbarBrevdataMedSaksbehandlerValg<VarselRevurderingAvPensjonDto.PesysData> {

    enum class TittelValg(override val displayText: String) : SaksbehandlerValgEnum {
        RevurderingAvRett("Revurdering av rett"),
        RevurderingReduksjon("Revurdering reduksjon"),
    }

    data class PesysData(val sakstype: Sakstype) : FagsystemBrevdata
}