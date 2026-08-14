package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdataMedSaksbehandlerValgUtenFagsystemdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL

data class OrienteringOmSaksbehandlingstidDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: EmptyFagsystemdata = EmptyFagsystemdata,
) : RedigerbarBrevdataMedSaksbehandlerValgUtenFagsystemdata