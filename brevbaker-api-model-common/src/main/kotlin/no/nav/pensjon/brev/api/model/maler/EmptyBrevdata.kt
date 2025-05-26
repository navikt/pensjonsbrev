@file:Suppress("unused")

package no.nav.pensjon.brev.api.model.maler

data object EmptyBrevdata : BrevbakerBrevdata

data object EmptyRedigerbarBrevdata : RedigerbarBrevdata<EmptyBrevdata, EmptyBrevdata> {
    override val saksbehandlerValg = EmptyBrevdata
    override val pesysData = EmptyBrevdata
}

data object EmptyVedleggBrevdata : VedleggBrevdata