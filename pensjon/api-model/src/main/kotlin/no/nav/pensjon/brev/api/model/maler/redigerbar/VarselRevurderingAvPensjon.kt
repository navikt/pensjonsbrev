package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText
import no.nav.pensjon.brevbaker.api.model.Redigerbar

@Suppress("unused")
data class VarselRevurderingAvPensjonDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VarselRevurderingAvPensjonDto.SaksbehandlerValg, VarselRevurderingAvPensjonDto.PesysData> {
    data class SaksbehandlerValg(
        @DisplayText("Tittelvalg") val tittelValg: TittelValg
    ) : SaksbehandlerValgBrevdata{
        enum class TittelValg {
            @DisplayText("Revurdering av rett") RevurderingAvRett,
            @DisplayText("Revurdering reduksjon") RevurderingReduksjon
        }
    }

    data class PesysData(@Redigerbar val sakstype: Sakstype) : FagsystemBrevdata
}