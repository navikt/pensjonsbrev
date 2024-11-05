package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.PEDto
import no.nav.pensjon.brev.fixtures.createPE

fun createPEDto() = PEDto(
    saksbehandlerValg = EmptyBrevdata,
    pesysData = PEDto.PesysData(
        pe = createPE()
    )
)