package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@Suppress("unused")
data class LetterResponse(val base64pdf: String, val letterMetadata: LetterMetadata)