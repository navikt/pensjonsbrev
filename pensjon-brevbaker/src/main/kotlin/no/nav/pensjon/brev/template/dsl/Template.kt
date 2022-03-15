package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.api.model.Felles
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.Text.FontType
import no.nav.pensjon.brev.template.base.BaseTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import kotlin.reflect.KClass

fun <Lang : LanguageSupport, LetterData : Any> createTemplate(
    name: String,
    base: BaseTemplate,
    letterDataType: KClass<LetterData>,
    title: Element.Text.Literal<Lang>,
    letterMetadata: LetterMetadata,
    init: TemplateRootScope<Lang, LetterData>.() -> Unit
): LetterTemplate<Lang, LetterData> =
    with(TemplateRootScope<Lang, LetterData>().apply(init)) {
        return LetterTemplate(name, title, base, letterDataType, title.languages, outline, attachments, letterMetadata)
    }

open class TemplateGlobalScope<LetterData : Any> {
    fun argument(): Expression<LetterData> =
        Expression.FromScope(ExpressionScope<LetterData, *>::argument)

    fun felles(): Expression<Felles> =
        Expression.FromScope(ExpressionScope<LetterData, *>::felles)

}

@LetterTemplateMarker
open class TemplateRootScope<Lang : LanguageSupport, LetterData : Any>(
    val outline: MutableList<Element<Lang>> = mutableListOf(),
    val attachments: MutableList<IncludeAttachment<Lang, *>> = mutableListOf(),
) : TemplateGlobalScope<LetterData>() {

    fun outline(init: TemplateContainerScope<Lang, LetterData>.() -> Unit) {
        outline.addAll(TemplateContainerScope<Lang, LetterData>().apply(init).children)
    }

    fun <AttachmentData : Any> includeAttachment(
        template: AttachmentTemplate<Lang, AttachmentData>,
        attachmentData: Expression<AttachmentData>
    ) {
        attachments.add(IncludeAttachment(attachmentData, template))
    }

}

@LetterTemplateMarker
open class TemplateTextOnlyScope<Lang : LanguageSupport, LetterData : Any>(val children: MutableList<Element<Lang>> = mutableListOf()) :
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
fun <Lang1 : Language, ParameterType : Any> TemplateTextOnlyScope<LanguageSupport.Single<Lang1>, ParameterType>.text(
    lang1: Pair<Lang1, String>, fontType: FontType = FontType.PLAIN
) {
    Element.Text.Literal.create(lang1, fontType).also { children.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, ParameterType : Any> TemplateTextOnlyScope<LanguageSupport.Double<Lang1, Lang2>, ParameterType>.text(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    fontType: FontType = FontType.PLAIN,
) {
    Element.Text.Literal.create(lang1, lang2, fontType).also { children.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, ParameterType : Any> TemplateTextOnlyScope<LanguageSupport.Triple<Lang1, Lang2, Lang3>, ParameterType>.text(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    lang3: Pair<Lang3, String>,
    fontType: FontType = FontType.PLAIN,
) {
    Element.Text.Literal.create(lang1, lang2, lang3, fontType).also { children.add(it) }
}

// TextOnlyBuilder.textExpr()
//
//
fun <Lang1 : Language, ParameterType : Any> TemplateTextOnlyScope<LanguageSupport.Single<Lang1>, ParameterType>.textExpr(
    lang1: Pair<Lang1, StringExpression>
) {
    Element.Text.Expression.ByLanguage.create(lang1).also { children.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, ParameterType : Any> TemplateTextOnlyScope<LanguageSupport.Double<Lang1, Lang2>, ParameterType>.textExpr(
    lang1: Pair<Lang1, StringExpression>,
    lang2: Pair<Lang2, StringExpression>,
) {
    Element.Text.Expression.ByLanguage.create(lang1, lang2).also { children.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, ParameterType : Any> TemplateTextOnlyScope<LanguageSupport.Triple<Lang1, Lang2, Lang3>, ParameterType>.textExpr(
    lang1: Pair<Lang1, StringExpression>,
    lang2: Pair<Lang2, StringExpression>,
    lang3: Pair<Lang3, StringExpression>,
) {
    Element.Text.Expression.ByLanguage.create(lang1, lang2, lang3).also { children.add(it) }
}

@LetterTemplateMarker
class TemplateContainerScope<Lang : LanguageSupport, LetterData : Any> :
    TemplateTextOnlyScope<Lang, LetterData>() {

    fun title1(init: TemplateTextOnlyScope<Lang, LetterData>.() -> Unit) {
        children.add(Element.Title1(TemplateTextOnlyScope<Lang, LetterData>().apply(init).children))
    }

    fun <PhraseData : Any> includePhrase(argument: Expression<PhraseData>, phrase: Phrase<Lang, PhraseData>) {
        children.add(Element.IncludePhrase(argument, phrase))
    }

    fun includePhrase(phrase: Phrase<Lang, Unit>) {
        children.add(Element.IncludePhrase(Unit.expr(), phrase))
    }

    fun list(init: ListRootScope<Lang, LetterData>.() -> Unit) {
        children.add(Element.ItemList(ListRootScope<Lang, LetterData>().apply(init).children))
    }

    fun table(init: TableRootScope<Lang, LetterData>.() -> Unit) {
        TableRootScope<Lang, LetterData>().apply(init)
            .let {
                children.add(
                    Element.Table(
                        rows = it.rows,
                        columnHeader = it.columnHeader
                            ?: throw InvalidTableDeclarationException("Table is missing column header row")
                    )
                )
            }
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
    ): ShowElseBuilder<Lang, LetterData> =
        ShowElseBuilder.chainShowIfElse(children, predicate, showIf)


    fun <E1: Any> ifNotNull(expr1: Expression<E1?>, block: TemplateContainerScope<Lang, LetterData>.(Expression<E1>) -> Unit) {
        children.add(
            Element.Conditional(expr1.notNull(), TemplateContainerScope<Lang, LetterData>().apply {
                // Følgende er en trygg cast fordi `children` blir kun brukt om `expr1.notNull()` evaluerer til true.
                @Suppress("UNCHECKED_CAST")
                block(this, expr1 as Expression<E1>)
            }.children, emptyList())
        )
    }

    fun <E1: Any, E2: Any> ifNotNull(expr1: Expression<E1?>, expr2: Expression<E2?>, block: TemplateContainerScope<Lang, LetterData>.(Expression<E1>, Expression<E2>) -> Unit) {
        children.add(
            Element.Conditional(expr1.notNull() and expr2.notNull(), TemplateContainerScope<Lang, LetterData>().apply {
                // Følgende er en trygg cast fordi `children` blir kun brukt om `expr1.notNull() and expr2.notNull()` evaluerer til true.
                @Suppress("UNCHECKED_CAST")
                block(this, expr1 as Expression<E1>, expr2 as Expression<E2>)
            }.children, emptyList())
        )
    }

    fun <E1: Any, E2: Any, E3: Any> ifNotNull(expr1: Expression<E1?>, expr2: Expression<E2?>, expr3: Expression<E3?>, block: TemplateContainerScope<Lang, LetterData>.(Expression<E1>, Expression<E2>, Expression<E3>) -> Unit) {
        children.add(
            Element.Conditional(expr1.notNull() and expr2.notNull() and expr3.notNull(), TemplateContainerScope<Lang, LetterData>().apply {
                // Følgende er en trygg cast fordi `children` blir kun brukt om `expr1.notNull() and expr2.notNull() and expr3.notNull()` evaluerer til true.
                @Suppress("UNCHECKED_CAST")
                block(this, expr1 as Expression<E1>, expr2 as Expression<E2>, expr3 as Expression<E3>)
            }.children, emptyList())
        )
    }
}

@LetterTemplateMarker
class TemplateFormChoiceScope<Lang : LanguageSupport, LetterData : Any>(
    val choices: MutableList<Element.Text<Lang>> = mutableListOf()
) : TemplateGlobalScope<LetterData>()

fun <Lang1 : Language, LetterData : Any> TemplateFormChoiceScope<LanguageSupport.Single<Lang1>, LetterData>.choice(
    lang1: Pair<Lang1, String>,
    fontType: FontType = FontType.PLAIN
) {
    Element.Text.Literal.create(lang1, fontType).also { choices.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, LetterData : Any> TemplateFormChoiceScope<LanguageSupport.Double<Lang1, Lang2>, LetterData>.choice(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    fontType: FontType = FontType.PLAIN,
) {
    Element.Text.Literal.create(lang1, lang2, fontType).also { choices.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, LetterData : Any> TemplateFormChoiceScope<LanguageSupport.Triple<Lang1, Lang2, Lang3>, LetterData>.choice(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    lang3: Pair<Lang3, String>,
    fontType: FontType = FontType.PLAIN,
) {
    Element.Text.Literal.create(lang1, lang2, lang3, fontType).also { choices.add(it) }
}


class ShowElseBuilder<Lang : LanguageSupport, ParameterType : Any>(
    val showElse: MutableList<Element<Lang>> = mutableListOf()
) {
    infix fun orShow(init: TemplateContainerScope<Lang, ParameterType>.() -> Unit): Unit =
        with(TemplateContainerScope<Lang, ParameterType>().apply(init)) {
            showElse.addAll(children)
        }

    fun orShowIf(
        predicate: Expression<Boolean>,
        body: TemplateContainerScope<Lang, ParameterType>.() -> Unit
    ): ShowElseBuilder<Lang, ParameterType> =
        chainShowIfElse(showElse, predicate, body)

    companion object {
        fun <Lang : LanguageSupport, ParameterType : Any> chainShowIfElse(
            outer: MutableList<Element<Lang>>,
            predicate: Expression<Boolean>,
            showIf: TemplateContainerScope<Lang, ParameterType>.() -> Unit
        ): ShowElseBuilder<Lang, ParameterType> {
            val nextOr = mutableListOf<Element<Lang>>()

            outer.add(
                Element.Conditional(
                    predicate = predicate,
                    showIf = TemplateContainerScope<Lang, ParameterType>().apply(showIf).children,
                    showElse = nextOr,
                )
            )

            return ShowElseBuilder(nextOr)
        }
    }
}

@DslMarker
annotation class LetterTemplateMarker
