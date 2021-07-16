package no.nav.pensjon.brev.template

typealias LetterArguments = Map<Parameter, Any>

data class Letter(val template: LetterTemplate, private val arguments: LetterArguments) {

    init {
        val missing: List<RequiredParameter> = (template.base.parameters + template.parameters)
            .filterIsInstance<RequiredParameter>()
            .filter { !arguments.containsKey(it.type) }

        if (missing.isNotEmpty()) {
            val names = missing.joinToString(", ") { it.type.name }
            throw IllegalArgumentException("Missing required arguments: $names")
        }
    }

    fun untypedArg(type: Parameter): Any? {
        if (!(template.parameters + template.base.parameters).any { it.type == type }) {
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