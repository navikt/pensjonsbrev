package no.nav.pensjon.brev.template

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brevbaker.api.model.Felles

// TODO: Look at * projections for LetterTemplate, we have to have it for the API endpoint, but perhaps not for internal usage.
// TODO: There is a bug where if you have a template with say EmptyBrevData as upper bound for ParameterType,
//        when passing it to Letter you can pass any value as argument, and a new upper bound of Any will be chosen.

@InterneDataklasser
data class LetterImpl<ParameterType : Any>(
    override val template: LetterTemplate<*, ParameterType>,
    override val argument: ParameterType,
    override val language: Language,
    override val felles: Felles,
) : Letter<ParameterType> {

    init {
        if (!template.language.supports(language)) {
            throw IllegalArgumentException("Language not supported by template: $language")
        }
    }
}

interface Letter<ParameterType : Any> {
    val template: LetterTemplate<*, ParameterType>
    val argument: ParameterType
    val language: Language
    val felles: Felles
}