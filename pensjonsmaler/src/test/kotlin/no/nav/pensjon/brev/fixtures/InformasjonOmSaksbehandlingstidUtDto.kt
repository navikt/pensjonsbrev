package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidUtDto.SaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidUtDto

fun createInformasjonOmSaksbehandlingstidUtDto() =
    InformasjonOmSaksbehandlingstidUtDto(
        saksbehandlerValg = SaksbehandlerValg(
            forlengetSaksbehandlingstid = false,
        ),
        pesysData = EmptyBrevdata
    )