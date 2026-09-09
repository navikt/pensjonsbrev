package no.nav.pensjon.brev.api.model.maler

data class EmptyRedigerbarRedigerbarBrevdata(
    override val pesysData: EmptyFagsystemdata = EmptyFagsystemdata,
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
) : BrevdataMedSaksbehandlerValgUtenFagsystemdata
