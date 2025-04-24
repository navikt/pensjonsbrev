package no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import java.time.LocalDate

data class VedleggSlikErUforetrygdenDinBeregnetDto (
    val virkningFom: LocalDate,
) : BrevbakerBrevdata