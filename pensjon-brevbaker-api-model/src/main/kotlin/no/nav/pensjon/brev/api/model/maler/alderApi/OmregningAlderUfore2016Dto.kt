package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import java.time.LocalDate

@Suppress("unused")
data class OmregningAlderUfore2016Dto(
    val virkFom: LocalDate,
):BrevbakerBrevdata