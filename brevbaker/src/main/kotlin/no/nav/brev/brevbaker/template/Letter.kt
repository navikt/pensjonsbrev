package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brevbaker.api.model.Felles

// TODO: Look at * projections for LetterTemplate, we have to have it for the API endpoint, but perhaps not for internal usage.
data class Letter<ParameterType : Any>(val template: LetterTemplate<*, ParameterType>, val argument: ParameterType, val language: Language, val felles: Felles) {

    init {
        if (!template.language.supports(language)) {
            throw IllegalArgumentException("Language not supported by template: $language")
        }
    }

}