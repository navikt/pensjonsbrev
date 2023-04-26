package no.nav.pensjon.etterlatte

import no.nav.pensjon.brev.api.model.*

enum class EtterlatteBrevKode { A_LETTER }

data class EtterlatteBrevRequest(val kode: EtterlatteBrevKode, val letterData: Any, val felles: Felles, val language: LanguageCode)
