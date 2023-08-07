package no.nav.pensjon.etterlatte

import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.LanguageCode

enum class EtterlatteBrevKode {
    BARNEPENSJON_AVSLAG,
    BARNEPENSJON_INNVILGELSE,
    BARNEPENSJON_REVURDERING_ADOPSJON,
    BARNEPENSJON_REVURDERING_FENGSELSOPPHOLD,
    BARNEPENSJON_REVURDERING_HAR_STANSET,
    BARNEPENSJON_REVURDERING_OMGJOERING_AV_FARSKAP,
    BARNEPENSJON_REVURDERING_OPPHOER,
    BARNEPENSJON_REVURDERING_SOESKENJUSTERING,
    OMS_INNVILGELSE_MANUELL,
    OMS_INNVILGELSE_AUTO,
    OMS_OPPHOER_MANUELL
}

data class EtterlatteBrevRequest(val kode: EtterlatteBrevKode, val letterData: Any, val felles: Felles, val language: LanguageCode)
