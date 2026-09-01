package no.nav.pensjon.brev.alder.maler

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.alder.model.ForespoerselOmDokumentasjonAvBotidINorgeAlderDto
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata

fun createForespoerselOmDokumentasjonAvBotidINorgeAlderDto() = ForespoerselOmDokumentasjonAvBotidINorgeAlderDto(
    saksbehandlerValg = lagSaksbehandlervalg("opplystOmBotid" to true),
    pesysData = EmptyFagsystemdata,
)