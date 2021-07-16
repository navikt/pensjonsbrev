package no.nav.pensjon.brev.template

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.io.OutputStream

data class LetterTemplate(
    val name: String,
    val base: BaseTemplate,
    val parameters: Set<TemplateParameter>,
    val outline: List<Element>
) {
    init {
        validateArgumentExpressions()
    }

    fun render(letter: Letter, out: OutputStream) =
        base.render(letter, out)
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

    val type: Parameter
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
        override fun eval(letter: Letter): Out? = letter.untypedArg(parameter).let { it as Out? }
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
sealed class Element {
    val schema: String = this::class.java.name.removePrefix(this::class.java.`package`.name + '.')

    data class Title1(val title1: List<Element>) : Element()
    data class Section(val section: List<Element>) : Element()

    sealed class Text : Element() {
        data class Literal(val text: String) : Text()
        data class Phrase(val phrase: no.nav.pensjon.brev.template.Phrase) : Text()
        data class Expression(val expression: no.nav.pensjon.brev.template.Expression<String>) : Text()
    }

    data class Conditional(
        val predicate: Expression<Boolean>,
        val showIf: List<Element>,
        val showElse: List<Element>,
    ) : Element()

}
