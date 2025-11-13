@file:Suppress("unused")

package no.nav.pensjon.brev.api.model.maler

data object EmptyBrevdata : PesysBrevdata

data object EmptySaksbehandlerValg : SaksbehandlerValgBrevdata

data object EmptyRedigerbarBrevdata : RedigerbarBrevdata<EmptySaksbehandlerValg, EmptyBrevdata> {
    override val saksbehandlerValg = EmptySaksbehandlerValg
    override val pesysData = EmptyBrevdata
}

data object EmptyVedleggData : VedleggData