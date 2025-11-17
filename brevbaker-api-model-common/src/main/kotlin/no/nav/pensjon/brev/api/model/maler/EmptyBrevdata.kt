@file:Suppress("unused")

package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brevbaker.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brevbaker.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brevbaker.api.model.maler.VedleggData

data object EmptyAutobrevdata : AutobrevData

data object EmptyFagsystemdata : FagsystemBrevdata

data object EmptySaksbehandlerValg : SaksbehandlerValgBrevdata

data object EmptyRedigerbarBrevdata : RedigerbarBrevdata<EmptySaksbehandlerValg, EmptyFagsystemdata> {
    override val saksbehandlerValg = EmptySaksbehandlerValg
    override val pesysData = EmptyFagsystemdata
}

data object EmptyVedleggData : VedleggData