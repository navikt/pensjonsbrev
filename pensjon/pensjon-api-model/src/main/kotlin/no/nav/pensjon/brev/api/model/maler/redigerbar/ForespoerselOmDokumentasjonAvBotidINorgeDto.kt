package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.BrevdataMedSaksbehandlerValgUtenFagsystemdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL

data class ForespoerselOmDokumentasjonAvBotidINorgeDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: EmptyFagsystemdata = EmptyFagsystemdata,
) : BrevdataMedSaksbehandlerValgUtenFagsystemdata
