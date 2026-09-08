package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum

@Suppress("unused")
data class VarselRevurderingAvPensjonDto(
    val sakstype: Sakstype
) : FagsystemBrevdata {
    enum class TittelValg(override val displayText: String) : SaksbehandlerValgEnum {
        RevurderingAvRett("Revurdering av rett"),
        RevurderingReduksjon("Revurdering reduksjon"),
    }
}