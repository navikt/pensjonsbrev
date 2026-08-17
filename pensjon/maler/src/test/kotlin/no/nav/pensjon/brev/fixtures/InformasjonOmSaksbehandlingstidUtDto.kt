package no.nav.pensjon.brev.fixtures

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidUtDto

fun createInformasjonOmSaksbehandlingstidUtDto() =
    InformasjonOmSaksbehandlingstidUtDto(
        saksbehandlerValg = lagSaksbehandlervalg(
            "forlengetSaksbehandlingstid" to false,
        ),
        pesysData = EmptyFagsystemdata
    )