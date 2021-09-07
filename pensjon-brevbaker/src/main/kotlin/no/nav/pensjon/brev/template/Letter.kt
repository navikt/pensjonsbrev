package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.Language

typealias LetterArguments = Map<Parameter, Any>

data class Letter(val template: LetterTemplate<*>, val arguments: LetterArguments, val language: Language) {

    init {
        val missing: List<RequiredParameter> = (template.base.parameters + template.parameters)
            .filterIsInstance<RequiredParameter>()
            .filter { !arguments.containsKey(it.parameter) }

        if (missing.isNotEmpty()) {
            val names = missing.joinToString(", ") { it.parameter.name }
            throw IllegalArgumentException("Missing required arguments: $names")
        }

        if (!template.language.supports(language)) {
            throw IllegalArgumentException("Language not supported by template: $language")
        }
    }

    fun render(): RenderedLetter =
        template.render(this)

    fun untypedArg(type: Parameter): Any? {
        if (!(template.parameters + template.base.parameters).any { it.parameter == type }) {
            throw IllegalArgumentException("Template doesn't declare requested parameter: ${type.name}")
        }

        return arguments[type]
    }

    inline fun <reified T, P> requiredArg(type: P): T
            where P : ParameterType<T>,
                  P : Parameter =
        untypedArg(type).let {
            when (it) {
                is T -> it
                null -> throw IllegalStateException("Letter is missing required argument: ${type.name}")
                else -> throw IllegalStateException("Expected letter argument '${type.name} to be ${T::class.qualifiedName}, but was: ${it::class.qualifiedName}")
            }
        }

    inline fun <reified T, P> optionalArg(type: P): T?
            where P : ParameterType<T>,
                  P : Parameter =
        untypedArg(type)?.let {
            when (it) {
                is T -> it
                else -> throw IllegalStateException("Expected letter argument '${type.name} to be ${T::class.qualifiedName}, but was: ${it::class.qualifiedName}")
            }
        }
}