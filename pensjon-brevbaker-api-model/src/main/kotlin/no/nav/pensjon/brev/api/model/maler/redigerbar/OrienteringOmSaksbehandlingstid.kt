package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import java.time.LocalDate

data class OrienteringOmSaksbehandlingstidDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: EmptyBrevdata,
) : RedigerbarBrevdata<OrienteringOmSaksbehandlingstidDto.SaksbehandlerValg, EmptyBrevdata> {
    data class SaksbehandlerValg(val mottattSoeknad: LocalDate, val soeknadOversendesTilUtlandet: Boolean): BrevbakerBrevdata
}