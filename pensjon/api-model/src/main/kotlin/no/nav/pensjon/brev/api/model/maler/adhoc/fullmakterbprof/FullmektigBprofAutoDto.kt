package no.nav.pensjon.brev.api.model.maler.adhoc.fullmakterbprof

import no.nav.pensjon.brev.api.model.maler.AutobrevData

@Suppress("unused")
data class FullmektigBprofAutoDto(
    val navnFullmaktsgiver: String,
): AutobrevData
