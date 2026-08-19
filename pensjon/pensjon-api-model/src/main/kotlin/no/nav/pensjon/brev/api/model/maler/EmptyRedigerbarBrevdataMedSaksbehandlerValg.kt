package no.nav.pensjon.brev.api.model.maler

data class EmptyRedigerbarBrevdataMedSaksbehandlerValg(
    override val pesysData: EmptyFagsystemdata = EmptyFagsystemdata,
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
) : BrevdataMedSaksbehandlerValgUtenFagsystemdata
