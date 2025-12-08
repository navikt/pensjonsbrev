@file:Suppress("unused")

package no.nav.pensjon.brev.api.model.maler

data object EmptyAutobrevdata : AutobrevData

data object EmptyFagsystemdata : FagsystemBrevdata

data object EmptySaksbehandlerValg : SaksbehandlerValgBrevdata

data object EmptyRedigerbarBrevdata : RedigerbarBrevdata<EmptySaksbehandlerValg, EmptyFagsystemdata> {
    override val saksbehandlerValg = EmptySaksbehandlerValg
    override val pesysData = EmptyFagsystemdata
}

data object EmptyVedleggData : VedleggData