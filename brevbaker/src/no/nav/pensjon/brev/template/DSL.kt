package no.nav.pensjon.brev.template

fun createTemplate(name: String, base: BaseTemplate, init: LetterTemplateBuilder.() -> Unit): LetterTemplate =
    with(LetterTemplateBuilder().apply(init)) {
        return LetterTemplate(name, base, parameters, outline)
    }

@LetterTemplateMarker
class ParametersBuilder(val parameters: MutableSet<TemplateParameter> = mutableSetOf()) {
    fun required(init: () -> Parameter) =
        parameters.add(RequiredParameter(init()))

    fun optional(init: () -> Parameter) =
        parameters.add(OptionalParameter(init()))
}

@LetterTemplateMarker
open class LetterTemplateBuilder(
    val parameters: MutableSet<TemplateParameter> = mutableSetOf(),
    val outline: MutableList<Element> = mutableListOf(),
) {
    fun parameters(init: ParametersBuilder.() -> Unit) =
        parameters.addAll(ParametersBuilder().apply(init).parameters)

    fun outline(init: ContainerElementBuilder.() -> Unit) =
        outline.addAll(ContainerElementBuilder().apply(init).children)

}

fun Expression<Any>.str(): Expression<String> = Expression.UnaryInvoke(this, UnaryOperation.ToString())

infix fun <T: Comparable<T>> Expression<T>.greaterThan(other: Expression<T>) =
    Expression.BinaryInvoke(this, other, BinaryOperation.GreaterThan())

infix fun <T: Comparable<T>> Expression<T>.greaterThan(other: T) =
    Expression.BinaryInvoke(this, Expression.Literal(other), BinaryOperation.GreaterThan())

fun <Param, Out> argument(parameter: Param): Expression<Out>
        where Param : Parameter,
              Param : ParameterType<Out> =
    Expression.Argument(parameter)

@LetterTemplateMarker
open class TextOnlyBuilder(val children: MutableList<Element> = mutableListOf()) {
    fun text(str: String): Element.Text.Literal =
        Element.Text.Literal(str).also { children.add(it) }

    fun eval(expression: Expression<String>): Element.Text.Expression =
        Element.Text.Expression(expression).also { children.add(it) }

    fun phrase(phrase: Phrase): Element.Text.Phrase =
        Element.Text.Phrase(phrase).also { children.add(it) }

}

@LetterTemplateMarker
class ContainerElementBuilder: TextOnlyBuilder() {
    fun title1(init: TextOnlyBuilder.() -> Element.Text) {
        val textBuilder = TextOnlyBuilder().apply { init() }
        val text = if (textBuilder.children.isEmpty()) {
            listOf(textBuilder.let(init))
        } else {
            textBuilder.children
        }
        children.add(Element.Title1(text))
    }

    fun title1(str: String) = title1 { text(str) }
    fun title1(phrase: Phrase) = title1 { phrase(phrase) }

    // TODO: Denne må flyttes til et ytre scope slik at man ikke skrive `section { section {  } }`.
    fun section(init: ContainerElementBuilder.() -> Unit) =
        children.add(Element.Section(ContainerElementBuilder().apply(init).children))

    fun showIf(predicate: Expression<Boolean>, showIf: ContainerElementBuilder.() -> Unit): ShowElseBuilder {
        val showElse = mutableListOf<Element>()

        return ContainerElementBuilder().apply { showIf() }
            .let { Element.Conditional(predicate, it.children, showElse) }
            .also { children.add(it) }
            .let { ShowElseBuilder(showElse) }
    }

}

class ShowElseBuilder(
    val showElse: MutableList<Element> = mutableListOf()
) {
    infix fun orShow(init: ContainerElementBuilder.() -> Unit) =
        with(ContainerElementBuilder().apply(init)) {
            showElse.addAll(children)
        }
}

@DslMarker
annotation class LetterTemplateMarker
