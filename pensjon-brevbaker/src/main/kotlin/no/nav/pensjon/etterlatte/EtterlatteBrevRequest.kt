package no.nav.pensjon.etterlatte

import no.nav.pensjon.brevbaker.api.model.*

enum class EtterlatteBrevKode { A_LETTER, BARNEPENSJON_INNVILGELSE, OMS_INNVILGELSE_MANUELL }

data class EtterlatteBrevRequest(val kode: EtterlatteBrevKode, val letterData: Any, val felles: Felles, val language: LanguageCode)
