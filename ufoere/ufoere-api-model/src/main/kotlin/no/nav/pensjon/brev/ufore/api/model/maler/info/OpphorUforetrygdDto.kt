package no.nav.pensjon.brev.ufore.api.model.maler.info

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL

data class OpphorUforetrygdDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: EmptyFagsystemdata,
) : RedigerbarBrevdata<EmptyFagsystemdata> {
    enum class Arsak(override val displayText: String) : SaksbehandlerValgEnum {
        EGET_ONSKE("Etter eget ønske"),
        FLYTTET_FRA_NORGE("Flyttet fra Norge"),
        IKKE_MOTTATT_LEVEATTEST("Ikke mottatt leveattest"),
    }
}
