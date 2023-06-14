package no.nav.pensjon.etterlatte

import no.nav.pensjon.brevbaker.api.model.*

enum class EtterlatteBrevKode {
    BARNEPENSJON_INNVILGELSE,
    OMS_INNVILGELSE_MANUELL,
    OMS_INNVILGELSE_AUTO,
    BARNEPENSJON_REVURDERING_SOESKENJUSTERING
}

data class EtterlatteBrevRequest(val kode: EtterlatteBrevKode, val letterData: Any, val felles: Felles, val language: LanguageCode)
