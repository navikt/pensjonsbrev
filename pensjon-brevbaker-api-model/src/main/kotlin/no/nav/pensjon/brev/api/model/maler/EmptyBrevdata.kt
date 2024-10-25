@file:Suppress("unused")

package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.maler.legacy.PE

data object EmptyBrevdata : BrevbakerBrevdata

data object EmptyRedigerbarBrevdata : RedigerbarBrevdata<EmptyBrevdata, EmptyBrevdata> {
    override val saksbehandlerValg = EmptyBrevdata
    override val pesysData = EmptyBrevdata
}

data class LegacyBrevdata(
    override val saksbehandlerValg: EmptyBrevdata,
    override val pesysData: PEBrevdata,
) : RedigerbarBrevdata<EmptyBrevdata, LegacyBrevdata.PEBrevdata> {
    data class PEBrevdata(val pe: PE) : BrevbakerBrevdata
}