package no.nav.pensjon.brev.api.model

data class LetterResponse(val base64pdf: String, val letterMetadata: LetterMetadata)