package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.base.BaseTemplate

//TODO: endre rekkefølge slik at lang kommer før title - sånn at typefeil blir annotert på title og ikke lang.
fun <Lang : LanguageCombination> createTemplate(
    name: String,
    title: Element.Text.Literal<Lang>,
    base: BaseTemplate,
    lang: Lang,
    init: LetterTemplateBuilder<Lang>.() -> Unit
): LetterTemplate<Lang> =
    with(LetterTemplateBuilder<Lang>().apply(init)) {
        return LetterTemplate(name, title, base, parameters, lang, outline, attachments)
    }

fun <Lang1 : Language> newText(lang1: Pair<Lang1, String>): Element.Text.Literal<LanguageCombination.Single<Lang1>> =
    Element.Text.Literal.create(lang1)

fun <Lang1 : Language, Lang2 : Language> newText(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
): Element.Text.Literal<LanguageCombination.Double<Lang1, Lang2>> =
    Element.Text.Literal.create(lang1, lang2)

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> newText(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    lang3: Pair<Lang3, String>,
): Element.Text.Literal<LanguageCombination.Triple<Lang1, Lang2, Lang3>> =
    Element.Text.Literal.create(lang1, lang2, lang3)


fun <Lang1 : Language> languages(lang1: Lang1) =
    LanguageCombination.Single(lang1)

fun <Lang1 : Language, Lang2 : Language> languages(lang1: Lang1, lang2: Lang2) =
    LanguageCombination.Double(lang1, lang2)

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> languages(lang1: Lang1, lang2: Lang2, lang3: Lang3) =
    LanguageCombination.Triple(lang1, lang2, lang3)

fun <Lang : LanguageCombination> staticParagraph(
    lang: Lang,
    init: TextOnlyBuilder<Lang>.() -> Element.Text<Lang>
): Element.Paragraph<Lang> =
    TextOnlyBuilder<Lang>()
        .apply { init() }
        .let { Element.Paragraph(it.children) }

@LetterTemplateMarker
class ParametersBuilder(val parameters: MutableSet<TemplateParameter> = mutableSetOf()) {
    fun required(init: () -> Parameter) =
        parameters.add(RequiredParameter(init()))

    fun optional(init: () -> Parameter) =
        parameters.add(OptionalParameter(init()))
}

@LetterTemplateMarker
open class LetterTemplateBuilder<Lang : LanguageCombination>(
    val parameters: MutableSet<TemplateParameter> = mutableSetOf(),
    val outline: MutableList<Element<Lang>> = mutableListOf(),
    val attachments: MutableList<AttachmentTemplate<Lang>> = mutableListOf(),
) {
    fun parameters(init: ParametersBuilder.() -> Unit): Unit {
        parameters.addAll(ParametersBuilder().apply(init).parameters)
    }

    fun outline(init: ContainerElementBuilder<Lang>.() -> Unit): Unit {
        outline.addAll(ContainerElementBuilder<Lang>().apply(init).children)
    }

    fun attachment(
        title: Element.Text.Literal<Lang>,
        includeSakspart: Boolean = false,
        outline: ContainerElementBuilder<Lang>.() -> Unit
    ): Unit {
        attachments.add(
            AttachmentTemplate(
                title,
                ContainerElementBuilder<Lang>().apply(outline).children,
                includeSakspart
            )
        )
    }

}

@LetterTemplateMarker
open class TextOnlyBuilder<Lang : LanguageCombination>(val children: MutableList<Element<Lang>> = mutableListOf()) {

    fun eval(expression: Expression<String>): Unit {
        children.add(Element.Text.Expression<Lang>(expression))
    }

    //TODO: Kan mest sannsynlig fjerne denne
    fun phrase(phrase: Phrase<Lang>) : Unit {
        children.add(Element.Text.Phrase(phrase))
    }

    fun newline(): Unit {
        children.add(Element.NewLine())
    }
}

fun <Lang1 : Language> TextOnlyBuilder<LanguageCombination.Single<Lang1>>.text(lang1: Pair<Lang1, String>): Unit {
    Element.Text.Literal.create(lang1).also { children.add(it) }
}

fun <Lang1 : Language, Lang2 : Language> TextOnlyBuilder<LanguageCombination.Double<Lang1, Lang2>>.text(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
): Unit {
    Element.Text.Literal.create(lang1, lang2).also { children.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> TextOnlyBuilder<LanguageCombination.Triple<Lang1, Lang2, Lang3>>.text(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    lang3: Pair<Lang3, String>,
): Unit {
    Element.Text.Literal.create(lang1, lang2, lang3).also { children.add(it) }
}


@LetterTemplateMarker
class ContainerElementBuilder<Lang : LanguageCombination> : TextOnlyBuilder<Lang>() {

    fun title1(init: TextOnlyBuilder<Lang>.() -> Unit): Unit {
        children.add(Element.Title1(TextOnlyBuilder<Lang>().apply(init).children))
    }

    fun paragraph(init: TextOnlyBuilder<Lang>.() -> Unit): Unit {
        children.add(Element.Paragraph(TextOnlyBuilder<Lang>().apply(init).children))
    }

    fun phraseParagraph(phrase: Element.Paragraph<Lang>): Unit {
        children.add(phrase)
    }

    fun formText(size: Int, prompt: Element.Text<Lang>, vspace: Boolean = true): Unit {
        children.add(Element.Form.Text(prompt, size, vspace))
    }

    fun formChoice(prompt: Element.Text<Lang>, vspace: Boolean = true, init: FormChoiceBuilder<Lang>.() -> Unit): Unit {
        FormChoiceBuilder<Lang>().apply(init)
            .let { Element.Form.MultipleChoice(prompt, it.choices, vspace) }
            .also { children.add(it) }
    }

    fun showIf(
        predicate: Expression<Boolean>,
        showIf: ContainerElementBuilder<Lang>.() -> Unit
    ): ShowElseBuilder<Lang> {
        val showElse = mutableListOf<Element<Lang>>()

        return ContainerElementBuilder<Lang>().apply { showIf() }
            .let { Element.Conditional(predicate, it.children, showElse) }
            .also { children.add(it) }
            .let { ShowElseBuilder(showElse) }
    }

}

@LetterTemplateMarker
class FormChoiceBuilder<Lang : LanguageCombination>(
    val choices: MutableList<Element.Text<Lang>> = mutableListOf()
)

fun <Lang1 : Language> FormChoiceBuilder<LanguageCombination.Single<Lang1>>.choice(lang1: Pair<Lang1, String>): Unit {
    Element.Text.Literal.create(lang1).also { choices.add(it) }
}

fun <Lang1 : Language, Lang2 : Language> FormChoiceBuilder<LanguageCombination.Double<Lang1, Lang2>>.choice(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
): Unit {
    Element.Text.Literal.create(lang1, lang2).also { choices.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> FormChoiceBuilder<LanguageCombination.Triple<Lang1, Lang2, Lang3>>.choice(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    lang3: Pair<Lang3, String>,
): Unit {
    Element.Text.Literal.create(lang1, lang2, lang3).also { choices.add(it) }
}


class ShowElseBuilder<Lang : LanguageCombination>(
    val showElse: MutableList<Element<Lang>> = mutableListOf()
) {
    infix fun orShow(init: ContainerElementBuilder<Lang>.() -> Unit): Unit =
        with(ContainerElementBuilder<Lang>().apply(init)) {
            showElse.addAll(children)
        }
}

@DslMarker
annotation class LetterTemplateMarker
