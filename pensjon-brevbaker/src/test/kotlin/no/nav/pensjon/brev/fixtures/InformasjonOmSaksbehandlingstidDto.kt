package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidPeDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidPeDto.SaksbehandlerValg

fun createInformasjonOmSaksbehandlingstidPeDto() =
    InformasjonOmSaksbehandlingstidPeDto(
        saksbehandlerValg = SaksbehandlerValg(
            soeknadMottattFraUtland = false,
            venterPaaSvarAFP = false,
        ),
        pesysData = EmptyBrevdata
    )