package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.maler.legacy.PE

@Suppress("unused")
data class EtteroppgjoerEtterbetalingAutoDto(
    val pe: PE
): BrevbakerBrevdata
