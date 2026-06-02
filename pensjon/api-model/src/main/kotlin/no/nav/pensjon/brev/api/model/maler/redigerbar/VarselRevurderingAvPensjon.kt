package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brevbaker.api.model.DisplayText

@Suppress("unused")
data class VarselRevurderingAvPensjonDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : RedigerbarBrevdataMedSaksbehandlerValg<VarselRevurderingAvPensjonDto.PesysData> {
    data class SaksbehandlerValg(
        @DisplayText("Tittelvalg") val tittelValg: TittelValg
    ) : SaksbehandlerValgBrevdata{
        enum class TittelValg(override val displayText: String) : SaksbehandlerValgEnum {
            @DisplayText("Revurdering av rett") RevurderingAvRett("Revurdering av rett"),
            @DisplayText("Revurdering reduksjon") RevurderingReduksjon("Revurdering reduksjon"),
        }
    }

    data class PesysData(val sakstype: Sakstype) : FagsystemBrevdata
}