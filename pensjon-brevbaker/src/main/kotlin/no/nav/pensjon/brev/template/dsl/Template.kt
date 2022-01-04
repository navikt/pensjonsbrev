package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.api.model.Felles
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.base.BaseTemplate
import kotlin.reflect.KClass

fun <Lang : LanguageCombination, LetterData : Any> createTemplate(
    name: String,
    base: BaseTemplate,
    letterDataType: KClass<LetterData>,
    lang: Lang,
    title: Element.Text.Literal<Lang>,
    letterMetadata: LetterMetadata,
    init: TemplateRootScope<Lang, LetterData>.() -> Unit
): LetterTemplate<Lang, LetterData> =
    with(TemplateRootScope<Lang, LetterData>().apply(init)) {
        return LetterTemplate(name, title, base, letterDataType, lang, outline, attachments, letterMetadata)
    }


open class TemplateGlobalScope<LetterData : Any> {
    fun argument(): Expression<LetterData> =
        Expression.FromScope(ExpressionScope<LetterData, *>::argument)

    fun felles(): Expression<Felles> =
        Expression.FromScope(ExpressionScope<LetterData, *>::felles)
}

@LetterTemplateMarker
open class TemplateRootScope<Lang : LanguageCombination, LetterData : Any>(
    val outline: MutableList<Element<Lang>> = mutableListOf(),
    val attachments: MutableList<AttachmentTemplate<Lang, LetterData>> = mutableListOf(),
) : TemplateGlobalScope<LetterData>() {

    fun outline(init: TemplateContainerScope<Lang, LetterData>.() -> Unit) {
        outline.addAll(TemplateContainerScope<Lang, LetterData>().apply(init).children)
    }

    fun attachment(
        title: Element.Text.Literal<Lang>,
        includeSakspart: Boolean = false,
        outline: TemplateContainerScope<Lang, LetterData>.() -> Unit
    ) {
        attachments.add(
            AttachmentTemplate(
                title,
                TemplateContainerScope<Lang, LetterData>().apply(outline).children,
                includeSakspart
            )
        )
    }

}

@LetterTemplateMarker
open class TemplateTextOnlyScope<Lang : LanguageCombination, LetterData : Any>(val children: MutableList<Element<Lang>> = mutableListOf()) :
    TemplateGlobalScope<LetterData>() {

    fun eval(expression: StringExpression) {
        children.add(Element.Text.Expression(expression))
    }

    fun eval(expressionInit: () -> StringExpression) {
        children.add(Element.Text.Expression(expressionInit()))
    }

    fun newline() {
        children.add(Element.NewLine())
    }
}

// TextOnlyBuilder.text()
//
//
fun <Lang1 : Language, ParameterType : Any> TemplateTextOnlyScope<LanguageCombination.Single<Lang1>, ParameterType>.text(
    lang1: Pair<Lang1, String>
) {
    Element.Text.Literal.create(lang1).also { children.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, ParameterType : Any> TemplateTextOnlyScope<LanguageCombination.Double<Lang1, Lang2>, ParameterType>.text(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
) {
    Element.Text.Literal.create(lang1, lang2).also { children.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, ParameterType : Any> TemplateTextOnlyScope<LanguageCombination.Triple<Lang1, Lang2, Lang3>, ParameterType>.text(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    lang3: Pair<Lang3, String>,
) {
    Element.Text.Literal.create(lang1, lang2, lang3).also { children.add(it) }
}

// TextOnlyBuilder.textExpr()
//
//
fun <Lang1 : Language, ParameterType : Any> TemplateTextOnlyScope<LanguageCombination.Single<Lang1>, ParameterType>.textExpr(
    lang1: Pair<Lang1, StringExpression>
) {
    Element.Text.Expression.ByLanguage.create(lang1).also { children.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, ParameterType : Any> TemplateTextOnlyScope<LanguageCombination.Double<Lang1, Lang2>, ParameterType>.textExpr(
    lang1: Pair<Lang1, StringExpression>,
    lang2: Pair<Lang2, StringExpression>,
) {
    Element.Text.Expression.ByLanguage.create(lang1, lang2).also { children.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, ParameterType : Any> TemplateTextOnlyScope<LanguageCombination.Triple<Lang1, Lang2, Lang3>, ParameterType>.textExpr(
    lang1: Pair<Lang1, StringExpression>,
    lang2: Pair<Lang2, StringExpression>,
    lang3: Pair<Lang3, StringExpression>,
) {
    Element.Text.Expression.ByLanguage.create(lang1, lang2, lang3).also { children.add(it) }
}

@LetterTemplateMarker
class TemplateContainerScope<Lang : LanguageCombination, LetterData : Any> :
    TemplateTextOnlyScope<Lang, LetterData>() {

    fun title1(init: TemplateTextOnlyScope<Lang, LetterData>.() -> Unit) {
        children.add(Element.Title1(TemplateTextOnlyScope<Lang, LetterData>().apply(init).children))
    }

    fun <PhraseData : Any> includePhrase(argument: Expression<PhraseData>, phrase: Phrase<PhraseData>) {
        children.add(Element.IncludePhrase(argument, phrase))
    }

    fun includePhrase(phrase: Phrase<Unit>) {
        children.add(Element.IncludePhrase(Unit.expr(), phrase))
    }

    fun list(init: TemplateContainerScope<Lang, LetterData>.() -> Unit) {
        children.add(Element.ItemList.Static(TemplateContainerScope<Lang, LetterData>().apply(init).children))
    }

    fun list(items: Expression<List<String>>) {
        children.add(Element.ItemList.Dynamic(items))
    }

    fun paragraph(init: TemplateContainerScope<Lang, LetterData>.() -> Unit) {
        children.add(Element.Paragraph(TemplateContainerScope<Lang, LetterData>().apply(init).children))
    }

    fun formText(size: Int, prompt: Element.Text<Lang>, vspace: Boolean = true) {
        children.add(Element.Form.Text(prompt, size, vspace))
    }

    fun formChoice(
        prompt: Element.Text<Lang>,
        vspace: Boolean = true,
        init: TemplateFormChoiceScope<Lang, LetterData>.() -> Unit
    ) {
        TemplateFormChoiceScope<Lang, LetterData>().apply(init)
            .let { Element.Form.MultipleChoice(prompt, it.choices, vspace) }
            .also { children.add(it) }
    }

    fun showIf(
        predicate: Expression<Boolean>,
        showIf: TemplateContainerScope<Lang, LetterData>.() -> Unit
    ): ShowElseBuilder<Lang, LetterData> {
        val showElse = mutableListOf<Element<Lang>>()

        return TemplateContainerScope<Lang, LetterData>().apply { showIf() }
            .let { Element.Conditional(predicate, it.children, showElse) }
            .also { children.add(it) }
            .let { ShowElseBuilder(showElse) }
    }

}

@LetterTemplateMarker
class TemplateFormChoiceScope<Lang : LanguageCombination, LetterData : Any>(
    val choices: MutableList<Element.Text<Lang>> = mutableListOf()
) : TemplateGlobalScope<LetterData>()

fun <Lang1 : Language, LetterData : Any> TemplateFormChoiceScope<LanguageCombination.Single<Lang1>, LetterData>.choice(
    lang1: Pair<Lang1, String>
) {
    Element.Text.Literal.create(lang1).also { choices.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, LetterData : Any> TemplateFormChoiceScope<LanguageCombination.Double<Lang1, Lang2>, LetterData>.choice(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
) {
    Element.Text.Literal.create(lang1, lang2).also { choices.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, LetterData : Any> TemplateFormChoiceScope<LanguageCombination.Triple<Lang1, Lang2, Lang3>, LetterData>.choice(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    lang3: Pair<Lang3, String>,
) {
    Element.Text.Literal.create(lang1, lang2, lang3).also { choices.add(it) }
}


class ShowElseBuilder<Lang : LanguageCombination, ParameterType : Any>(
    val showElse: MutableList<Element<Lang>> = mutableListOf()
) {
    infix fun orShow(init: TemplateContainerScope<Lang, ParameterType>.() -> Unit): Unit =
        with(TemplateContainerScope<Lang, ParameterType>().apply(init)) {
            showElse.addAll(children)
        }
}

@DslMarker
annotation class LetterTemplateMarker
