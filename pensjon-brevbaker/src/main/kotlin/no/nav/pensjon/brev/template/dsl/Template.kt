package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.api.dto.Felles
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.base.BaseTemplate
import kotlin.reflect.KClass

fun <Lang : LanguageCombination, ParameterType : Any> createTemplate(
    name: String,
    base: BaseTemplate,
    parameterType: KClass<ParameterType>,
    lang: Lang,
    title: Element.Text.Literal<Lang>,
    init: LetterTemplateBuilder<Lang, ParameterType>.() -> Unit
): LetterTemplate<Lang, ParameterType> =
    with(LetterTemplateBuilder<Lang, ParameterType>().apply(init)) {
        return LetterTemplate(name, title, base, parameterType, lang, outline, attachments)
    }

@LetterTemplateMarker
open class LetterTemplateBuilder<Lang : LanguageCombination, LetterData : Any>(
    val outline: MutableList<Element<Lang>> = mutableListOf(),
    val attachments: MutableList<AttachmentTemplate<Lang, LetterData>> = mutableListOf(),
) {

    fun outline(init: ContainerElementBuilder<Lang, LetterData>.() -> Unit) {
        outline.addAll(ContainerElementBuilder<Lang, LetterData>().apply(init).children)
    }

    fun attachment(
        title: Element.Text.Literal<Lang>,
        includeSakspart: Boolean = false,
        outline: ContainerElementBuilder<Lang, LetterData>.() -> Unit
    ) {
        attachments.add(
            AttachmentTemplate(
                title,
                ContainerElementBuilder<Lang, LetterData>().apply(outline).children,
                includeSakspart
            )
        )
    }

    fun argument(): Expression<LetterData> =
        Expression.LetterProperty(Letter<LetterData>::argument)

    fun felles(): Expression<Felles> =
        Expression.LetterProperty(Letter<LetterData>::felles)

}

@LetterTemplateMarker
open class TextOnlyBuilder<Lang : LanguageCombination, LetterData : Any>(val children: MutableList<Element<Lang>> = mutableListOf()) {

    fun argument(): Expression<LetterData> =
        Expression.LetterProperty(Letter<LetterData>::argument)

    fun felles(): Expression<Felles> =
        Expression.LetterProperty(Letter<LetterData>::felles)

    fun eval(expression: StringExpression) {
        children.add(Element.Text.Expression(expression))
    }

    fun eval(expressionInit: () -> StringExpression) {
        children.add(Element.Text.Expression(expressionInit()))
    }

    //TODO: Kan mest sannsynlig fjerne denne
    fun phrase(phrase: Phrase<Lang>) {
        children.add(Element.Text.Phrase(phrase))
    }

    fun newline() {
        children.add(Element.NewLine())
    }
}

// TextOnlyBuilder.text()
//
//
fun <Lang1 : Language, ParameterType : Any> TextOnlyBuilder<LanguageCombination.Single<Lang1>, ParameterType>.text(lang1: Pair<Lang1, String>) {
    Element.Text.Literal.create(lang1).also { children.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, ParameterType : Any> TextOnlyBuilder<LanguageCombination.Double<Lang1, Lang2>, ParameterType>.text(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
) {
    Element.Text.Literal.create(lang1, lang2).also { children.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, ParameterType : Any> TextOnlyBuilder<LanguageCombination.Triple<Lang1, Lang2, Lang3>, ParameterType>.text(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    lang3: Pair<Lang3, String>,
) {
    Element.Text.Literal.create(lang1, lang2, lang3).also { children.add(it) }
}

// TextOnlyBuilder.textExpr()
//
//
fun <Lang1 : Language, ParameterType : Any> TextOnlyBuilder<LanguageCombination.Single<Lang1>, ParameterType>.textExpr(lang1: Pair<Lang1, StringExpression>) {
    Element.Text.Expression.ByLanguage.create(lang1).also { children.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, ParameterType : Any> TextOnlyBuilder<LanguageCombination.Double<Lang1, Lang2>, ParameterType>.textExpr(
    lang1: Pair<Lang1, StringExpression>,
    lang2: Pair<Lang2, StringExpression>,
) {
    Element.Text.Expression.ByLanguage.create(lang1, lang2).also { children.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, ParameterType : Any> TextOnlyBuilder<LanguageCombination.Triple<Lang1, Lang2, Lang3>, ParameterType>.textExpr(
    lang1: Pair<Lang1, StringExpression>,
    lang2: Pair<Lang2, StringExpression>,
    lang3: Pair<Lang3, StringExpression>,
) {
    Element.Text.Expression.ByLanguage.create(lang1, lang2, lang3).also { children.add(it) }
}

@LetterTemplateMarker
class ContainerElementBuilder<Lang : LanguageCombination, ParameterType : Any> :
    TextOnlyBuilder<Lang, ParameterType>() {

    fun title1(init: TextOnlyBuilder<Lang, ParameterType>.() -> Unit) {
        children.add(Element.Title1(TextOnlyBuilder<Lang, ParameterType>().apply(init).children))
    }

    fun paragraph(init: TextOnlyBuilder<Lang, ParameterType>.() -> Unit) {
        children.add(Element.Paragraph(TextOnlyBuilder<Lang, ParameterType>().apply(init).children))
    }

    fun formText(size: Int, prompt: Element.Text<Lang>, vspace: Boolean = true) {
        children.add(Element.Form.Text(prompt, size, vspace))
    }

    fun formChoice(prompt: Element.Text<Lang>, vspace: Boolean = true, init: FormChoiceBuilder<Lang>.() -> Unit) {
        FormChoiceBuilder<Lang>().apply(init)
            .let { Element.Form.MultipleChoice(prompt, it.choices, vspace) }
            .also { children.add(it) }
    }

    fun showIf(
        predicate: Expression<Boolean>,
        showIf: ContainerElementBuilder<Lang, ParameterType>.() -> Unit
    ): ShowElseBuilder<Lang, ParameterType> {
        val showElse = mutableListOf<Element<Lang>>()

        return ContainerElementBuilder<Lang, ParameterType>().apply { showIf() }
            .let { Element.Conditional(predicate, it.children, showElse) }
            .also { children.add(it) }
            .let { ShowElseBuilder(showElse) }
    }

}

@LetterTemplateMarker
class FormChoiceBuilder<Lang : LanguageCombination>(
    val choices: MutableList<Element.Text<Lang>> = mutableListOf()
)

fun <Lang1 : Language> FormChoiceBuilder<LanguageCombination.Single<Lang1>>.choice(lang1: Pair<Lang1, String>) {
    Element.Text.Literal.create(lang1).also { choices.add(it) }
}

fun <Lang1 : Language, Lang2 : Language> FormChoiceBuilder<LanguageCombination.Double<Lang1, Lang2>>.choice(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
) {
    Element.Text.Literal.create(lang1, lang2).also { choices.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> FormChoiceBuilder<LanguageCombination.Triple<Lang1, Lang2, Lang3>>.choice(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    lang3: Pair<Lang3, String>,
) {
    Element.Text.Literal.create(lang1, lang2, lang3).also { choices.add(it) }
}


class ShowElseBuilder<Lang : LanguageCombination, ParameterType : Any>(
    val showElse: MutableList<Element<Lang>> = mutableListOf()
) {
    infix fun orShow(init: ContainerElementBuilder<Lang, ParameterType>.() -> Unit): Unit =
        with(ContainerElementBuilder<Lang, ParameterType>().apply(init)) {
            showElse.addAll(children)
        }
}

@DslMarker
annotation class LetterTemplateMarker
