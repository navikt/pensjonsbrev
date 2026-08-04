package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata

data class KlageOrienteringOmSaksbehandlingstidDto(
    override val pesysData: EmptyFagsystemdata, override val saksbehandlerValg: EmptySaksbehandlerValg
) : RedigerbarBrevdata<EmptySaksbehandlerValg, EmptyFagsystemdata>