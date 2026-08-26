package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL

data class KlageOrienteringOmSaksbehandlingstidDto(
    override val pesysData: EmptyFagsystemdata,
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
) : BrevdataMedSaksbehandlerValg<EmptyFagsystemdata> {
    enum class Saksbehandlingstid(override val displayText: String) : SaksbehandlerValgEnum {
        SAKSBEHANDLINGSTID_VED_NFP_ELLER_NAY("Saksbehandlingstid ved NFP eller NAY"),
        SAKSBEHANDLINGSTID_VED_NAV_KLAGEINSTANS("Saksbehandlingstid ved Nav Klageinstans"),
    }
}