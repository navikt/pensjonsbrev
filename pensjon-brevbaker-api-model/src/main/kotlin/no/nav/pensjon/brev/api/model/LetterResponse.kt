package no.nav.pensjon.brev.api.model

@Suppress("unused")
data class LetterResponse(val base64pdf: String, val letterMetadata: LetterMetadata)