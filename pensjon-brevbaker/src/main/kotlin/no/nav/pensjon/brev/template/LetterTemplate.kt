package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.template.base.BaseTemplate
import kotlin.reflect.KClass

data class LetterTemplate<out Lang : LanguageSupport, LetterData : Any>(
    val name: String,
    //TODO: Lag støtte for kombinert literal og expression
    val title: Element.Text.Literal<Lang>,
    val base: BaseTemplate,
    val letterDataType: KClass<LetterData>,
    val language: Lang,
    val outline: List<Element<Lang>>,
    val attachments: List<IncludeAttachment<Lang, *>> = emptyList(),
    val letterMetadata: LetterMetadata,
) {

    fun render(letter: Letter<*>) =
        base.render(letter)
}

sealed class Expression<out Out> {
    val schema: String = this::class.java.name.removePrefix(this::class.java.`package`.name + '.')

    abstract fun eval(scope: ExpressionScope<*, *>): Out

    data class Literal<out Out>(val value: Out) : Expression<Out>() {
        override fun eval(scope: ExpressionScope<*, *>): Out = value
    }

    data class FromScope<ParameterType : Any, out Out>(val selector: ExpressionScope<ParameterType, *>.() -> Out) :
        Expression<Out>() {
        override fun eval(scope: ExpressionScope<*, *>): Out {
            @Suppress("UNCHECKED_CAST")
            return (scope as ExpressionScope<ParameterType, *>).selector()
        }
    }

    data class UnaryInvoke<In, Out>(
        val value: Expression<In>,
        val operation: UnaryOperation<In, Out>
    ) : Expression<Out>() {
        override fun eval(scope: ExpressionScope<*, *>): Out = operation.apply(value.eval(scope))
    }

    data class BinaryInvoke<In1, In2, out Out>(
        val first: Expression<In1>,
        val second: Expression<In2>,
        val operation: BinaryOperation<In1, In2, Out>
    ) : Expression<Out>() {
        override fun eval(scope: ExpressionScope<*, *>): Out {
            return operation.apply(first.eval(scope), second.eval(scope))
        }
    }

}

typealias StringExpression = Expression<String>

sealed class Element<out Lang : LanguageSupport> {
    val schema: String = this::class.java.name.removePrefix(this::class.java.`package`.name + '.')

    data class Title1<out Lang : LanguageSupport>(val title1: List<Element<Lang>>) : Element<Lang>()
    data class Paragraph<out Lang : LanguageSupport>(val paragraph: List<Element<Lang>>) : Element<Lang>()
    sealed class ItemList<out Lang : LanguageSupport> : Element<Lang>() {
        data class Dynamic<out Lang : LanguageSupport>(val items: Expression<List<String>>) : ItemList<Lang>()
        data class Static<out Lang : LanguageSupport>(val items: List<Element<Lang>>) : ItemList<Lang>()
    }

    sealed class Form<out Lang : LanguageSupport> : Element<Lang>() {
        data class Text<out Lang : LanguageSupport>(
            val prompt: Element.Text<Lang>,
            val size: Int,
            val vspace: Boolean = true,
        ) : Form<Lang>()

        data class MultipleChoice<out Lang : LanguageSupport>(
            val prompt: Element.Text<Lang>,
            val choices: List<Element.Text<Lang>>,
            val vspace: Boolean = true,
        ) : Form<Lang>()
    }

    data class IncludePhrase<out Lang : LanguageSupport, PhraseData : Any>(
        val data: Expression<PhraseData>,
        val phrase: Phrase<Lang, PhraseData>,
    ) : Element<Lang>()

    class NewLine<out Lang : LanguageSupport> : Element<Lang>()

    sealed class Text<out Lang : LanguageSupport> : Element<Lang>() {
        data class Literal<out Lang : LanguageSupport> private constructor(private val text: Map<Language, String>) :
            Text<Lang>() {

            fun text(language: Language): String =
                text[language]
                    ?: throw IllegalArgumentException("Text.Literal doesn't contain language: ${language::class.qualifiedName}")

            companion object {
                fun <Lang1 : Language> create(lang1: Pair<Lang1, String>) =
                    Literal<LanguageSupport.Single<Lang1>>(mapOf(lang1))

                fun <Lang1 : Language, Lang2 : Language> create(
                    lang1: Pair<Lang1, String>,
                    lang2: Pair<Lang2, String>,
                ) = Literal<LanguageSupport.Double<Lang1, Lang2>>(mapOf(lang1, lang2))

                fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> create(
                    lang1: Pair<Lang1, String>,
                    lang2: Pair<Lang2, String>,
                    lang3: Pair<Lang3, String>,
                ) = Literal<LanguageSupport.Triple<Lang1, Lang2, Lang3>>(mapOf(lang1, lang2, lang3))
            }
        }

        data class Expression<out Lang : LanguageSupport>(val expression: StringExpression) :
            Text<Lang>() {

            data class ByLanguage<out Lang : LanguageSupport> private constructor(
                val expression: Map<Language, StringExpression>
            ) : Text<Lang>() {

                fun expr(language: Language): StringExpression =
                    expression[language]
                        ?: throw IllegalArgumentException("Text.Expression.ByLanguage doesn't contain language: ${language::class.qualifiedName}")

                companion object {
                    fun <Lang1 : Language> create(lang1: Pair<Lang1, StringExpression>) =
                        ByLanguage<LanguageSupport.Single<Lang1>>(mapOf(lang1))

                    fun <Lang1 : Language, Lang2 : Language> create(
                        lang1: Pair<Lang1, StringExpression>,
                        lang2: Pair<Lang2, StringExpression>,
                    ) = ByLanguage<LanguageSupport.Double<Lang1, Lang2>>(mapOf(lang1, lang2))

                    fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> create(
                        lang1: Pair<Lang1, StringExpression>,
                        lang2: Pair<Lang2, StringExpression>,
                        lang3: Pair<Lang3, StringExpression>,
                    ) = ByLanguage<LanguageSupport.Triple<Lang1, Lang2, Lang3>>(mapOf(lang1, lang2, lang3))
                }
            }
        }


    }

    data class Conditional<out Lang : LanguageSupport>(
        val predicate: Expression<Boolean>,
        val showIf: List<Element<Lang>>,
        val showElse: List<Element<Lang>>,
    ) : Element<Lang>()

}
