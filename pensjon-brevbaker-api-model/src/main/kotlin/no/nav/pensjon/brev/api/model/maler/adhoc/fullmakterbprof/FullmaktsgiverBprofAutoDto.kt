package no.nav.pensjon.brev.api.model.maler.adhoc.fullmakterbprof

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata

@Suppress("unused")
data class FullmaktsgiverBprofAutoDto(
    val navnFullmektig: String,
): BrevbakerBrevdata
