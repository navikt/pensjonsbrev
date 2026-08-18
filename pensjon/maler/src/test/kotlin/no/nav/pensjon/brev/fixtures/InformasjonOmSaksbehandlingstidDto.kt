package no.nav.pensjon.brev.fixtures

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDto

fun createInformasjonOmSaksbehandlingstidDto() =
    InformasjonOmSaksbehandlingstidDto(
        saksbehandlerValg = lagSaksbehandlervalg(
            "soeknadMottattFraUtland" to false,
            "venterPaaSvarAFP" to false,
        ),
        pesysData = EmptyFagsystemdata
    )