package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText
import no.nav.pensjon.brevbaker.api.model.EnumMedDisplayText

@Suppress("unused")
data class VarselRevurderingAvPensjonDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VarselRevurderingAvPensjonDto.SaksbehandlerValg, VarselRevurderingAvPensjonDto.PesysData> {
    data class SaksbehandlerValg(
        @DisplayText("Tittelvalg") val tittelValg: TittelValg
    ) : BrevbakerBrevdata{
        enum class TittelValg(override val displayText: String) : EnumMedDisplayText {
            RevurderingAvRett("Revurdering av rett"),
            RevurderingReduksjon("Revurdering reduksjon");
        }
    }

    data class PesysData(val sakstype: Sakstype) : BrevbakerBrevdata
}