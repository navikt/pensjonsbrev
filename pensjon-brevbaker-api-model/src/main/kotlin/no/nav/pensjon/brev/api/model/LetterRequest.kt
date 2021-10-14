package no.nav.pensjon.brev.api.model

data class LetterRequest(val template: String, val letterData: Any, val felles: Felles, val language: LanguageCode)