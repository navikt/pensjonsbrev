package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brevbaker.api.model.*

@Deprecated("Replace with BestillBrevRequest")
data class AutobrevRequest(val kode: Brevkode.AutoBrev, val letterData: BrevbakerBrevdata, val felles: Felles, val language: LanguageCode)

@Deprecated("Replace with BestillRedigertBrevRequest")
data class RedigerbartbrevRequest(val kode: Brevkode.Redigerbar, val letterData: BrevbakerBrevdata, val felles: Felles, val language: LanguageCode)

@Suppress("unused")
data class BestillBrevRequest<T : Enum<T>>(val kode: T, val letterData: BrevbakerBrevdata, val felles: Felles, val language: LanguageCode)

@Suppress("unused")
data class BestillRedigertBrevRequest<T : Enum<T>>(
    val kode: T,
    val letterData: BrevbakerBrevdata,
    val felles: Felles,
    val language: LanguageCode,
    val letterMarkup: LetterMarkup
)