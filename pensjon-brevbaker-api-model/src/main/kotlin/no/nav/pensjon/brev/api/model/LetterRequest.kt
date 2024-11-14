package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brevbaker.api.model.*

@Suppress("unused")
data class BestillBrevRequest<T : Enum<T>>(val kode: T, val letterData: BrevbakerBrevdata, val felles: Felles, val language: LanguageCode)

@Suppress("unused")
data class BestillBrevUtenDataRequest(
    val kode: String,
    val felles: Felles,
    val language: LanguageCode,
)

@Suppress("unused")
data class BestillRedigertBrevRequest<T : Enum<T>>(
    val kode: T,
    val letterData: BrevbakerBrevdata,
    val felles: Felles,
    val language: LanguageCode,
    val letterMarkup: LetterMarkup
)