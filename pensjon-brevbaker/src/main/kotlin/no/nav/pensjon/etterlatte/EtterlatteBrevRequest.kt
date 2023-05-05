package no.nav.pensjon.etterlatte

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.LetterTemplate

enum class EtterlatteBrevKode { A_LETTER, BARNEPENSJON_VEDTAK }

data class EtterlatteBrevRequest(val kode: EtterlatteBrevKode, val letterData: Any, val felles: Felles, val language: LanguageCode)
