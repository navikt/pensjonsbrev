package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brev.api.model.maler.Brevkode

@Deprecated(message = "Erstattes med VedtaksbrevRequest")
data class LetterRequest(val template: String, val letterData: Any, val felles: Felles, val language: LanguageCode)
data class VedtaksbrevRequest(val kode: Brevkode.Vedtak, val letterData: Any, val felles: Felles, val language: LanguageCode)