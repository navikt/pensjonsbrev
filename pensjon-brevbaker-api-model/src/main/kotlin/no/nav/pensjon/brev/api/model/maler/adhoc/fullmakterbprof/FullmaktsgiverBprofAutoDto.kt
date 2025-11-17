package no.nav.pensjon.brev.api.model.maler.adhoc.fullmakterbprof

import no.nav.pensjon.brevbaker.api.model.maler.AutobrevData

@Suppress("unused")
data class FullmaktsgiverBprofAutoDto(
    val navnFullmektig: String,
): AutobrevData
