package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import java.time.LocalDate

data class PaaminnelseLeveattestDto(
    override val saksbehandlerValg: EmptyBrevdata,
    override val pesysData: Pesysdata,
) : RedigerbarBrevdata<EmptyBrevdata, PaaminnelseLeveattestDto.Pesysdata> {

    // PE_LeveattestHistorikkListe_Leveattesthistorikk_UtlopsDatoPurring_Dagensdato:
    // Value = DateAdd("d",42, SYS_DateCurrent)
    data class Pesysdata(val fristdato: LocalDate = LocalDate.now().plusDays(42)) : BrevbakerBrevdata
}