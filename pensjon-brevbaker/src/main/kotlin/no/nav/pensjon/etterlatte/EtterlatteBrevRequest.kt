package no.nav.pensjon.etterlatte

import no.nav.pensjon.brevbaker.api.model.*

enum class EtterlatteBrevKode {
    BARNEPENSJON_INNVILGELSE,
    BARNEPENSJON_REVURDERING_OPPHOER,
    BARNEPENSJON_REVURDERING_SOESKENJUSTERING,
    OMS_INNVILGELSE_MANUELL,
    OMS_INNVILGELSE_AUTO,
    OMS_OPPHOER_MANUELL
}

data class EtterlatteBrevRequest(val kode: EtterlatteBrevKode, val letterData: Any, val felles: Felles, val language: LanguageCode)
