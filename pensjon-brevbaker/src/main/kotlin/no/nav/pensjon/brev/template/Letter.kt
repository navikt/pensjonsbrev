package no.nav.pensjon.brev.template

import no.nav.pensjon.brevbaker.api.model.Felles

// TODO: Look at * projections for LetterTemplate, we have to have it for the API endpoint, but perhaps not for internal usage.
// TODO: There is a bug where if you have a template with say EmptyBrevData as upper bound for ParameterType,
//        when passing it to Letter you can pass any value as argument, and a new upper bound of Any will be chosen.
data class Letter<ParameterType : Any>(val template: LetterTemplate<*, ParameterType, *>, val argument: ParameterType, val language: Language, val felles: Felles) {

    init {
        if (!template.language.supports(language)) {
            throw IllegalArgumentException("Language not supported by template: $language")
        }
    }

}