package no.nav.pensjon.brev.template

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeInfo
import no.nav.pensjon.brev.template.base.BaseTemplate

data class LetterTemplate<Lang : LanguageCombination>(
    val name: String,
    //TODO: Lag støtte for kombinert literal og expression
    val title: Element.Text.Literal<Lang>,
    val base: BaseTemplate,
    val parameters: Set<TemplateParameter>,
    val language: Lang,
    val outline: List<Element<Lang>>,
    val attachments: List<AttachmentTemplate<Lang>> = emptyList(),
) {

    fun render(letter: Letter) =
        base.render(letter)
}

data class AttachmentTemplate<Lang : LanguageCombination>(
    val title: Element.Text.Literal<Lang>,
    val outline: List<Element<Lang>>,
    val includeSakspart: Boolean = false,
)

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

    data class LetterProperty<out Out>(val select: Letter.() -> Out) : Expression<Out>() {
        override fun eval(letter: Letter): Out = letter.select()
    }

    data class SelectLetterData<ParameterType: Any, Out>(
        val selector: ParameterType.() -> Out
    ) : Expression<Out>() {
        override fun eval(letter: Letter): Out {
            @Suppress("UNCHECKED_CAST")
            return (letter.argument as ParameterType).selector()
        }
    }

    data class SelectFellesData<Out>(
        val selector: (felles: Felles) -> Out
    ) : Expression<Out>() {
        override fun eval(letter: Letter): Out {
            @Suppress("UNCHECKED_CAST")
            return selector.invoke(letter.felles)
        }
    }

    data class Select<In : Any, Out>(
        val value: Expression<In>,
        val select: In.() -> Out
    ) : Expression<Out>() {
        override fun eval(letter: Letter): Out = value.eval(letter).select()
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


}

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "schema",
    visible = true,
)
sealed class Element<Lang : LanguageCombination> {
    val schema: String = this::class.java.name.removePrefix(this::class.java.`package`.name + '.')

    // TODO: Consider if type of title1 and paragraph should be List<Element.Text<Lang>>.
    data class Title1<Lang : LanguageCombination>(val title1: List<Element<Lang>>) : Element<Lang>()
    data class Paragraph<Lang : LanguageCombination>(val paragraph: List<Element<Lang>>) : Element<Lang>()

    sealed class Form<Lang : LanguageCombination> : Element<Lang>() {
        data class Text<Lang : LanguageCombination>(val prompt: Element.Text<Lang>, val size: Int, val vspace: Boolean = true) : Form<Lang>()
        data class MultipleChoice<Lang: LanguageCombination>(val prompt: Element.Text<Lang>, val choices: List<Element.Text<Lang>>, val vspace: Boolean = true) : Element.Form<Lang>()
    }

    class NewLine<Lang : LanguageCombination>() : Element<Lang>()

    sealed class Text<Lang : LanguageCombination> : Element<Lang>() {
        data class Literal<Lang : LanguageCombination> private constructor(private val text: Map<Language, String>) :
            Text<Lang>() {

            fun text(language: Language): String =
                text[language]
                    ?: throw IllegalArgumentException("Text.Literal doesn't contain language: ${language::class.qualifiedName}")

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
