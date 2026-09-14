package no.nav.pensjon.brev.api.model.maler

data class EmptyRedigerbarBrevdata(
    override val pesysData: EmptyFagsystemdata = EmptyFagsystemdata,
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
) : RedigerbarBrevdata<EmptyFagsystemdata>
