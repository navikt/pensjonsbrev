package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDto.SaksbehandlerValg
import java.time.LocalDate

fun createInformasjonOmSaksbehandlingstidDto() =
    InformasjonOmSaksbehandlingstidDto(
        saksbehandlerValg = SaksbehandlerValg(
            mottattSoeknad = LocalDate.now().minusDays(6),
            ytelse = "AFP",
            land = null,
            inkluderVenterSvarAFP = null,
            svartidUker = 10,
        ),
        pesysData = EmptyBrevdata
    )