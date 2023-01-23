package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDto
import java.time.LocalDate

fun createInformasjonOmSaksbehandlingstidDto() =
    InformasjonOmSaksbehandlingstidDto(
        mottattSoeknad = LocalDate.now().minusDays(6),
        ytelse = "AFP",
        land = null,
        inkluderVenterSvarAFP = null,
        svartidUker = 10,
    )