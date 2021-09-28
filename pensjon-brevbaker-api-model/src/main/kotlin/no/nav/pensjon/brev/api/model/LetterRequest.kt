package no.nav.pensjon.brev.api.model

import com.fasterxml.jackson.databind.node.ObjectNode

data class LetterRequest(val template: String, val letterData: ObjectNode, val felles: Felles, val language: LanguageCode)