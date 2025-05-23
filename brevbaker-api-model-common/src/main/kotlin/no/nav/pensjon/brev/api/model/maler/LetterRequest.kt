package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup

@Suppress("unused")
data class BestillBrevRequest<T : Brevkode<T>>(
    val kode: T,
    val letterData: BrevbakerBrevdata,
    val felles: Felles,
    val language: LanguageCode,
) : BrevRequest<T>

@Suppress("unused")
data class BestillBrevRequestAsync<T : Brevkode<T>>(
    val kode: T,
    val letterData: BrevbakerBrevdata,
    val felles: Felles,
    val language: LanguageCode,
    val messageId: String,
    val replyTopic: String,
) : BrevRequest<T>

@Suppress("unused")
data class BestillRedigertBrevRequest<T : Brevkode<T>>(
    val kode: T,
    val letterData: RedigerbarBrevdata<*, *>,
    val felles: Felles,
    val language: LanguageCode,
    val letterMarkup: LetterMarkup,
) : BrevRequest<T>

interface BrevRequest<T : Brevkode<T>>