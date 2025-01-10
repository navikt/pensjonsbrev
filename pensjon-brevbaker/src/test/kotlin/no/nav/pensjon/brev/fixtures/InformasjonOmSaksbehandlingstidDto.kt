package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDto.SaksbehandlerValg

fun createInformasjonOmSaksbehandlingstidDto() =
    InformasjonOmSaksbehandlingstidDto(
        saksbehandlerValg = SaksbehandlerValg(
            mottattFraAnnetLand = false,
            inkluderVenterPaaSvarAFP = false,
        ),
        pesysData = EmptyBrevdata
    )