package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidUtDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidUtDto.SaksbehandlerValg

fun createInformasjonOmSaksbehandlingstidUtDto() =
    InformasjonOmSaksbehandlingstidUtDto(
        saksbehandlerValg = SaksbehandlerValg(
            forlengetSaksbehandlingstid = false,
        ),
        pesysData = EmptyFagsystemdata
    )