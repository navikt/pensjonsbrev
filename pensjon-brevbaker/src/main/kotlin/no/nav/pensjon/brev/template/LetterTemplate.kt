package no.nav.pensjon.brev.template

import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import kotlin.reflect.KClass

data class LetterTemplate<Lang : LanguageSupport, LetterData : Any>(
    val name: String,
    val title: List<TextElement<Lang>>,
    val letterDataType: KClass<LetterData>,
    val language: Lang,
    val outline: List<OutlineElement<Lang>>,
    val attachments: List<IncludeAttachment<Lang, *>> = emptyList(),
    val letterMetadata: LetterMetadata,
) {
    val modelSpecification: TemplateModelSpecification = LetterModelSpecificationFactory(letterDataType).build()

    init {
        if (title.isEmpty()) {
            throw MissingTitleInTemplateException("Missing title in template: $name")
        }
    }
}

sealed class Expression<out Out> {

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
        val operation: UnaryOperation<In, Out>,
    ) : Expression<Out>() {
        override fun eval(scope: ExpressionScope<*, *>): Out = operation.apply(value.eval(scope))
    }

    data class BinaryInvoke<In1, In2, out Out>(
        val first: Expression<In1>,
        val second: Expression<In2>,
        val operation: BinaryOperation<In1, In2, Out>
    ) : Expression<Out>() {
        override fun eval(scope: ExpressionScope<*, *>): Out = operation.apply(first.eval(scope), second.eval(scope))
    }

}

typealias StringExpression = Expression<String>

sealed class ContentOrControlStructure<out Lang : LanguageSupport, out C : Element<Lang>> {
    data class Content<out Lang : LanguageSupport, C : Element<Lang>>(
        val content: C,
    ) : ContentOrControlStructure<Lang, C>()

    data class Conditional<out Lang : LanguageSupport, out C : Element<Lang>>(
        val predicate: Expression<Boolean>,
        val showIf: List<ContentOrControlStructure<Lang, C>>,
        val showElse: List<ContentOrControlStructure<Lang, C>>,
    ) : ContentOrControlStructure<Lang, C>()

    @Suppress("DataClassPrivateConstructor")
    data class ForEach<out Lang : LanguageSupport, C : Element<Lang>, Item : Any> private constructor(
        val items: Expression<Collection<Item>>,
        val body: Collection<ContentOrControlStructure<Lang, C>>,
        private val next: NextExpression<Item>
    ) : ContentOrControlStructure<Lang, C>() {

        fun render(
            scope: ExpressionScope<*, *>,
            renderElement: (scope: ExpressionScope<*, *>, element: ContentOrControlStructure<Lang, C>) -> Unit
        ) {
            items.eval(scope).forEach { item ->
                val iteratorScope = ForEachExpressionScope(item to next, scope)
                body.forEach { element ->
                    renderElement(iteratorScope, element)
                }
            }
        }

        companion object {
            fun <Lang : LanguageSupport, C : Element<Lang>, Item : Any> create(
                items: Expression<Collection<Item>>,
                createView: (item: Expression<Item>) -> Collection<ContentOrControlStructure<Lang, C>>
            ): ForEach<Lang, C, Item> =
                NextExpression<Item>().let { ForEach(items, createView(it), it) }
        }

        private class NextExpression<Item : Any> : Expression<Item>() {
            override fun eval(scope: ExpressionScope<*, *>): Item =
                if (scope is ForEachExpressionScope<*>) {
                    @Suppress("UNCHECKED_CAST")
                    (scope as ForEachExpressionScope<Item>).evalNext(this)
                } else {
                    throw InvalidScopeTypeException("Requires scope to be ForEachExpressionScope, but was: ${scope::class.qualifiedName}")
                }
        }

        private class ForEachExpressionScope<Item : Any>(
            val next: Pair<Item, NextExpression<Item>>,
            val parent: ExpressionScope<*, *>
        ) : ExpressionScope<Any, Language>(parent.argument, parent.felles, parent.language) {
            fun evalNext(expr: NextExpression<Item>): Item =
                if (expr == next.second) {
                    next.first
                } else if (parent is ForEachExpressionScope<*>) {
                    @Suppress("UNCHECKED_CAST")
                    (parent as ForEachExpressionScope<Item>).evalNext(expr)
                } else {
                    throw MissingScopeForNextItemEvaluationException("Could not find scope matching: $expr")
                }
        }
    }
}

typealias TextElement<Lang> = ContentOrControlStructure<Lang, Element.OutlineContent.ParagraphContent.Text<Lang>>
typealias TableRowElement<Lang> = ContentOrControlStructure<Lang, Element.OutlineContent.ParagraphContent.Table.Row<Lang>>
typealias ParagraphContentElement<Lang> = ContentOrControlStructure<Lang, Element.OutlineContent.ParagraphContent<Lang>>
typealias ListItemElement<Lang> = ContentOrControlStructure<Lang, Element.OutlineContent.ParagraphContent.ItemList.Item<Lang>>
typealias OutlineElement<Lang> = ContentOrControlStructure<Lang, Element.OutlineContent<Lang>>

sealed class Element<out Lang : LanguageSupport> {
    sealed class OutlineContent<out Lang : LanguageSupport> : Element<Lang>() {
        data class Title1<out Lang : LanguageSupport>(val text: List<TextElement<Lang>>) : OutlineContent<Lang>()

        data class Paragraph<out Lang : LanguageSupport>(val paragraph: List<ParagraphContentElement<Lang>>) : OutlineContent<Lang>()

        sealed class ParagraphContent<out Lang : LanguageSupport> : Element<Lang>() {

            data class ItemList<out Lang : LanguageSupport>(
                val items: List<ContentOrControlStructure<Lang, Item<Lang>>>
            ) : ParagraphContent<Lang>() {
                init {
                    if (items.flatMap { getItems(it) }.isEmpty()) throw InvalidListDeclarationException("List has no items")
                }

                data class Item<out Lang : LanguageSupport>(
                    val text: List<TextElement<Lang>>
                ) : Element<Lang>()

                private fun getItems(item: ContentOrControlStructure<Lang, Item<Lang>>): List<Item<Lang>> =
                    when (item) {
                        is ContentOrControlStructure.Conditional -> item.showIf.plus(item.showElse).flatMap { getItems(it) }
                        is ContentOrControlStructure.ForEach<Lang, Item<Lang>, *> -> item.body.flatMap { getItems(it) }
                        is ContentOrControlStructure.Content -> listOf(item.content)
                    }
            }

            // TODO: Siden tabellene skal passe inn i et brev, så bør vi ha en maksimumsgrense på antall-kolonner (evt. bare total bredde)
            data class Table<out Lang : LanguageSupport>(
                val rows: List<TableRowElement<Lang>>,
                val header: Header<Lang>,
            ) : ParagraphContent<Lang>() {

                init {
                    if (rows.flatMap { getRows(it) }.isEmpty()) {
                        throw InvalidTableDeclarationException("A table must have at least one row")
                    }
                }

                private fun getRows(row: TableRowElement<Lang>): List<Row<Lang>> =
                    when (row) {
                        is ContentOrControlStructure.Conditional -> row.showIf.plus(row.showElse).flatMap { getRows(it) }
                        is ContentOrControlStructure.ForEach<Lang, Row<Lang>, *> -> row.body.flatMap { getRows(it) }
                        is ContentOrControlStructure.Content -> listOf(row.content)
                    }

                data class Row<out Lang : LanguageSupport>(
                    val cells: List<Cell<Lang>>,
                    val colSpec: List<ColumnSpec<Lang>>
                ) : Element<Lang>() {
                    init {
                        if (cells.isEmpty()) {
                            throw InvalidTableDeclarationException("Rows need at least one cell")
                        }
                        if (cells.size != colSpec.size) {
                            throw InvalidTableDeclarationException("The number of cells in the row(${cells.size}) does not match the number of columns in the specification(${colSpec.size})")
                        }
                    }
                }

                data class Header<out Lang : LanguageSupport>(val colSpec: List<ColumnSpec<Lang>>) {
                    init {
                        if (colSpec.isEmpty()) {
                            throw InvalidTableDeclarationException("Table column specification needs at least one column")
                        }
                    }
                }

                data class Cell<out Lang : LanguageSupport>(
                    val text: List<TextElement<Lang>>
                )

                data class ColumnSpec<out Lang : LanguageSupport>(
                    val headerContent: Cell<Lang>,
                    val alignment: ColumnAlignment,
                    val columnSpan: Int = 1
                )

                enum class ColumnAlignment {
                    LEFT, RIGHT
                }
            }

            sealed class Text<out Lang : LanguageSupport> : ParagraphContent<Lang>() {
                abstract val fontType: FontType

                @Suppress("DataClassPrivateConstructor")
                data class Literal<out Lang : LanguageSupport> private constructor(
                    private val text: Map<Language, String>,
                    val languages: Lang,
                    override var fontType: FontType,
                ) : Text<Lang>() {

                    fun text(language: Language): String =
                        text[language]
                            ?: throw IllegalArgumentException("Text.Literal doesn't contain language: ${language::class.qualifiedName}")

                    companion object {
                        fun <Lang1 : Language> create(
                            lang1: Pair<Lang1, String>,
                            fontType: FontType = FontType.PLAIN
                        ) = Literal<LanguageSupport.Single<Lang1>>(
                            text = mapOf(lang1),
                            languages = LanguageCombination.Single(lang1.first),
                            fontType = fontType
                        )

                        fun <Lang1 : Language, Lang2 : Language> create(
                            lang1: Pair<Lang1, String>,
                            lang2: Pair<Lang2, String>,
                            fontType: FontType = FontType.PLAIN,
                        ) = Literal<LanguageSupport.Double<Lang1, Lang2>>(
                            text = mapOf(lang1, lang2),
                            languages = LanguageCombination.Double(lang1.first, lang2.first),
                            fontType = fontType
                        )

                        fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> create(
                            lang1: Pair<Lang1, String>,
                            lang2: Pair<Lang2, String>,
                            lang3: Pair<Lang3, String>,
                            fontType: FontType = FontType.PLAIN,
                        ) = Literal<LanguageSupport.Triple<Lang1, Lang2, Lang3>>(
                            text = mapOf(lang1, lang2, lang3),
                            languages = LanguageCombination.Triple(lang1.first, lang2.first, lang3.first),
                            fontType = fontType
                        )
                    }
                }

                enum class FontType {
                    PLAIN,
                    BOLD,
                    ITALIC
                }

                data class Expression<out Lang : LanguageSupport>(
                    val expression: StringExpression,
                    override val fontType: FontType = FontType.PLAIN
                ) : Text<Lang>() {

                    @Suppress("DataClassPrivateConstructor")
                    data class ByLanguage<out Lang : LanguageSupport> private constructor(
                        val expression: Map<Language, StringExpression>,
                        val languages: Lang,
                        override val fontType: FontType
                    ) : Text<Lang>() {

                        fun expr(language: Language): StringExpression =
                            expression[language]
                                ?: throw IllegalArgumentException("Text.Expression.ByLanguage doesn't contain language: ${language::class.qualifiedName}")

                        companion object {
                            fun <Lang1 : Language> create(
                                lang1: Pair<Lang1, StringExpression>,
                                fontType: FontType = FontType.PLAIN
                            ) = ByLanguage<LanguageSupport.Single<Lang1>>(mapOf(lang1), LanguageCombination.Single(lang1.first), fontType)

                            fun <Lang1 : Language, Lang2 : Language> create(
                                lang1: Pair<Lang1, StringExpression>,
                                lang2: Pair<Lang2, StringExpression>,
                                fontType: FontType = FontType.PLAIN,
                            ) = ByLanguage<LanguageSupport.Double<Lang1, Lang2>>(mapOf(lang1, lang2), LanguageCombination.Double(lang1.first, lang2.first), fontType)

                            fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> create(
                                lang1: Pair<Lang1, StringExpression>,
                                lang2: Pair<Lang2, StringExpression>,
                                lang3: Pair<Lang3, StringExpression>,
                                fontType: FontType = FontType.PLAIN,
                            ) = ByLanguage<LanguageSupport.Triple<Lang1, Lang2, Lang3>>(mapOf(lang1, lang2, lang3), LanguageCombination.Triple(lang1.first, lang2.first, lang3.first), fontType)
                        }
                    }
                }

                class NewLine<out Lang : LanguageSupport> : Text<Lang>() {
                    override val fontType = FontType.PLAIN
                }
            }

            sealed class Form<out Lang : LanguageSupport> : ParagraphContent<Lang>() {
                data class Text<out Lang : LanguageSupport>(
                    val prompt: TextElement<Lang>,
                    val size: Size,
                    val vspace: Boolean = true,
                ) : Form<Lang>() {
                    enum class Size { NONE, SHORT, LONG }
                }

                data class MultipleChoice<out Lang : LanguageSupport>(
                    // TODO: Denne bør ikke være TextElement, bør være Element.OutlineContent.ParagraphContent.Text
                    val prompt: TextElement<Lang>,
                    val choices: List<ParagraphContent.Text<Lang>>,
                    val vspace: Boolean = true,
                ) : Form<Lang>()
            }
        }
    }

}

abstract class TemplateValidationException(msg: String) : Exception(msg)
class MissingScopeForNextItemEvaluationException(msg: String) : TemplateValidationException(msg)
class InvalidScopeTypeException(msg: String) : TemplateValidationException(msg)
class InvalidTableDeclarationException(msg: String) : TemplateValidationException(msg)
class InvalidListDeclarationException(msg: String) : TemplateValidationException(msg)
class MissingTitleInTemplateException(msg: String) : TemplateValidationException(msg)