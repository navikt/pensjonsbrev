package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.ForespoerselOmDokumentasjonAvBotidINorgeDto

fun createForespoerselOmDokumentasjonAvBotidINorgeDto() = ForespoerselOmDokumentasjonAvBotidINorgeDto(
    saksbehandlerValg = ForespoerselOmDokumentasjonAvBotidINorgeDto.SaksbehandlerValg(true),
    pesysData = EmptyFagsystemdata,
)