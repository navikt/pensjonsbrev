@file:Suppress("unused")

package no.nav.pensjon.brev.api.model.maler

data object EmptyAutobrevdata : AutobrevData

data object EmptyFagsystemdata : FagsystemBrevdata

data object EmptySaksbehandlerValg : SaksbehandlerValgBrevdata

data object EmptyRedigerbarBrevdata : RedigerbarBrevdata<EmptySaksbehandlerValg, EmptyFagsystemdata> {
    override val saksbehandlerValg = EmptySaksbehandlerValg
    override val pesysData = EmptyFagsystemdata
}

@ConsistentCopyVisibility
data class EmptyRedigerbarBrevdataMedSaksbehandlerValg internal constructor(
    override val pesysData: EmptyFagsystemdata = EmptyFagsystemdata,
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
) : RedigerbarBrevdataMedSaksbehandlerValgUtenFagsystemdata

data object EmptyVedleggData : VedleggData