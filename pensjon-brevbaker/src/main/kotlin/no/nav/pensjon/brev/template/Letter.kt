package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.Felles

data class Letter<ParameterType : Any>(val template: LetterTemplate<*, *>, val argument: ParameterType, val language: Language, val felles: Felles) {

    init {
        if (!template.language.supports(language)) {
            throw IllegalArgumentException("Language not supported by template: $language")
        }
    }

    fun render(): RenderedLetter =
        template.render(this)

}