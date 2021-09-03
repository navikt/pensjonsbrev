package no.nav.pensjon.brev.template

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.io.OutputStream

data class LetterTemplate<Lang : LanguageCombination>(
    val name: String,
    val title: Element.Text.Literal<Lang>,
    val base: BaseTemplate,
    val parameters: Set<TemplateParameter>,
    val language: Lang,
    val outline: List<Element<Lang>>
) {
    init {
        validateArgumentExpressions()
    }

    fun render(letter: Letter) =
        base.render(letter)
}

sealed class TemplateParameter {
    companion object {
        @JsonCreator
        @JvmStatic
        fun creator(
            @JsonProperty("required") required: Parameter?,
            @JsonProperty("optional") optional: Parameter?
        ): TemplateParameter? =
            if (required != null) {
                RequiredParameter(required)
            } else if (optional != null) {
                OptionalParameter(optional)
            } else null

    }

    val parameter: Parameter
        @JsonIgnore
        get() = when (this) {
            is RequiredParameter -> required
            is OptionalParameter -> optional
        }
}

data class RequiredParameter(val required: Parameter) : TemplateParameter()
data class OptionalParameter(val optional: Parameter) : TemplateParameter()


@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "schema",
    visible = true,
)
sealed class Expression<out Out> {
    val schema: String = this::class.java.name.removePrefix(this::class.java.`package`.name + '.')

    abstract fun eval(letter: Letter): Out

    data class Literal<out Out>(val value: Out) : Expression<Out>() {
        override fun eval(letter: Letter): Out = value
    }

    data class Argument<Param, Out>(val parameter: Param) : Expression<Out>()
            where Param : Parameter,
                  Param : ParameterType<Out> {

        @Suppress("UNCHECKED_CAST")
        override fun eval(letter: Letter): Out =
            letter.untypedArg(parameter)
                .takeIf { it != null }
                .let { it as Out }
    }

    data class OptionalArgument<Param, Out>(val parameter: Param) : Expression<Out?>()
            where Param : Parameter,
                  Param : ParameterType<Out> {
        @Suppress("UNCHECKED_CAST")
        override fun eval(letter: Letter): Out? =
            letter.untypedArg(parameter).let { it as Out? }
    }

    data class UnaryInvoke<In, out Out>(
        val value: Expression<In>,
        val operation: UnaryOperation<In, Out>
    ) : Expression<Out>() {
        override fun eval(letter: Letter): Out = operation.apply(value.eval(letter))
    }

    data class BinaryInvoke<In1, In2, out Out>(
        val first: Expression<In1>,
        val second: Expression<In2>,
        val operation: BinaryOperation<In1, In2, Out>
    ) : Expression<Out>() {
        override fun eval(letter: Letter): Out {
            return operation.apply(first.eval(letter), second.eval(letter))
        }
    }

    // TODO: Dette er egentlig bare en special-case av UnaryInvoke
    data class Select<In : Any, Out>(
        val value: Expression<In>,
        val select: In.() -> Out
    ) : Expression<Out>() {
        override fun eval(letter: Letter): Out = value.eval(letter).select()
    }

}

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "schema",
    visible = true,
)
sealed class Element<Lang : LanguageCombination> {
    val schema: String = this::class.java.name.removePrefix(this::class.java.`package`.name + '.')

    // TODO: Consider if type of  title1 and paragraph should be List<Element.Text<Lang>>.
    data class Title1<Lang : LanguageCombination>(val title1: List<Element<Lang>>) : Element<Lang>()
    data class Paragraph<Lang : LanguageCombination>(val paragraph: List<Element<Lang>>) : Element<Lang>()

    sealed class Text<Lang : LanguageCombination> : Element<Lang>() {
        data class Literal<Lang : LanguageCombination> private constructor(private val text: Map<Language, String>) :
            Text<Lang>() {

            fun text(language: Language): String =
                text[language] ?: throw IllegalArgumentException("Text.Literal doesn't contain language: ${language::class.qualifiedName}")

            companion object {
                fun <Lang1 : Language> create(lang1: Pair<Lang1, String>) =
                    Literal<LanguageCombination.Single<Lang1>>(mapOf(lang1))

                fun <Lang1 : Language, Lang2 : Language> create(
                    lang1: Pair<Lang1, String>,
                    lang2: Pair<Lang2, String>,
                ) = Literal<LanguageCombination.Double<Lang1, Lang2>>(mapOf(lang1, lang2))

                fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> create(
                    lang1: Pair<Lang1, String>,
                    lang2: Pair<Lang2, String>,
                    lang3: Pair<Lang3, String>,
                ) = Literal<LanguageCombination.Triple<Lang1, Lang2, Lang3>>(mapOf(lang1, lang2, lang3))
            }
        }

        data class Phrase<Lang : LanguageCombination>(val phrase: no.nav.pensjon.brev.template.Phrase<Lang>) :
            Text<Lang>()

        data class Expression<Lang : LanguageCombination>(val expression: no.nav.pensjon.brev.template.Expression<String>) :
            Text<Lang>()
    }

    data class Conditional<Lang : LanguageCombination>(
        val predicate: Expression<Boolean>,
        val showIf: List<Element<Lang>>,
        val showElse: List<Element<Lang>>,
    ) : Element<Lang>()

}
