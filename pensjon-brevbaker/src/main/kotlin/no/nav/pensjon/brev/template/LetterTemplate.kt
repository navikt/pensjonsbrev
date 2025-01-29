package no.nav.pensjon.brev.template

import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate
import kotlin.reflect.KClass

data class LetterTemplate<Lang : LanguageSupport, out LetterData : Any, AltLetterData: Any?>(
    val name: String,
    val title: List<TextElement<Lang>>,
    val letterDataType: KClass<out LetterData>,
    val language: Lang,
    val outline: List<OutlineElement<Lang>>,
    val attachments: List<IncludeAttachment<Lang, *>> = emptyList(),
    val letterMetadata: LetterMetadata,
    val alternativeData: ((AltLetterData) -> LetterData)? = null,
) {
    val modelSpecification: TemplateModelSpecification = TemplateModelSpecificationFactory(letterDataType).build()

    init {
        if (title.isEmpty()) {
            throw MissingTitleInTemplateException("Missing title in template: $name")
        }
    }
}

class PreventToStringForExpressionException : Exception(
    "Expression.toString should not be used. " +
            "In most cases this means that a template contains string concatenation of a string literal with an Expression-object, e.g:" +
            "text(Bokmal to \"The year is \${year.format()} \")"
)

sealed class Expression<out Out> : StableHash {

    abstract fun eval(scope: ExpressionScope<*>): Out

    data class Literal<out Out>(val value: Out, val tags: Set<ElementTags> = emptySet()) : Expression<Out>() {
        override fun eval(scope: ExpressionScope<*>): Out = value
        override fun stableHashCode(): Int = stableHash(value).stableHashCode()

        private fun stableHash(v: Any?): StableHash =
            when (v) {
                is StableHash -> v
                is Enum<*> -> StableHash.of(v)
                is String -> StableHash.of(v)
                is Number -> StableHash.of(v)
                is IntValue -> StableHash.of(v.value)
                is Telefonnummer -> StableHash.of(v.value)
                is Boolean -> StableHash.of(v)
                is Collection<*> -> StableHash.of(v.map { stableHash(it) })
                is Pair<*, *> -> StableHash.of(stableHash(v.first), stableHash(v.second))
                is Unit -> StableHash.of("kotlin.Unit")
                is LocalDate -> StableHash.of(v)
                null -> StableHash.of(null)
                else -> throw IllegalArgumentException("Unable to calculate stableHashCode for type ${v::class.java}")
            }
    }

    sealed class FromScope<out Out> : Expression<Out>() {
        object Felles : FromScope<no.nav.pensjon.brevbaker.api.model.Felles>() {
            override fun eval(scope: ExpressionScope<*>) = scope.felles
            override fun stableHashCode(): Int = "FromScope.Felles".hashCode()
        }

        object Language : FromScope<no.nav.pensjon.brev.template.Language>() {
            override fun eval(scope: ExpressionScope<*>) = scope.language
            override fun stableHashCode(): Int = "FromScope.Language".hashCode()
        }

        class Argument<out Out> : FromScope<Out>() {
            @Suppress("UNCHECKED_CAST")
            override fun eval(scope: ExpressionScope<*>) = scope.argument as Out
            override fun equals(other: Any?): Boolean = other is Argument<*>
            override fun hashCode(): Int = javaClass.hashCode()
            override fun stableHashCode(): Int = "FromScope.Argument".hashCode()
        }

        data class Assigned<out Out>(val id: Int) : FromScope<Out>() {
            override fun eval(scope: ExpressionScope<*>): Out =
                if (scope is ExpressionScope.WithAssignment<*, *>) {
                    @Suppress("UNCHECKED_CAST")
                    (scope as ExpressionScope.WithAssignment<*, Out>).lookup(this)
                } else {
                    throw InvalidScopeTypeException("Requires scope to be ${this::class.qualifiedName}, but was: ${scope::class.qualifiedName}")
                }

            override fun stableHashCode() = hashCode()
        }
    }

    data class UnaryInvoke<In, Out>(
        val value: Expression<In>,
        val operation: UnaryOperation<In, Out>,
    ) : Expression<Out>(), StableHash by StableHash.of(value, operation) {
        override fun eval(scope: ExpressionScope<*>): Out = operation.apply(value.eval(scope))
    }

    data class BinaryInvoke<In1, In2, out Out>(
        val first: Expression<In1>,
        val second: Expression<In2>,
        val operation: BinaryOperation<In1, In2, Out>
    ) : Expression<Out>(), StableHash by StableHash.of(first, second, operation) {
        override fun eval(scope: ExpressionScope<*>): Out = operation.apply(first.eval(scope), second.eval(scope))
    }

    final override fun toString(): String {
        throw PreventToStringForExpressionException()
    }
}

typealias StringExpression = Expression<String>

sealed class ContentOrControlStructure<out Lang : LanguageSupport, out C : Element<Lang>> : StableHash {
    data class Content<out Lang : LanguageSupport, C : Element<Lang>>(
        val content: C,
    ) : ContentOrControlStructure<Lang, C>(), StableHash by StableHash.of(content)

    data class Conditional<out Lang : LanguageSupport, out C : Element<Lang>>(
        val predicate: Expression<Boolean>,
        val showIf: List<ContentOrControlStructure<Lang, C>>,
        val showElse: List<ContentOrControlStructure<Lang, C>>,
    ) : ContentOrControlStructure<Lang, C>(), StableHash by StableHash.of(predicate, StableHash.of(showIf), StableHash.of(showElse))

    data class ForEach<out Lang : LanguageSupport, C : Element<Lang>, Item : Any>(
        val items: Expression<Collection<Item>>,
        val body: List<ContentOrControlStructure<Lang, C>>,
        val next: Expression.FromScope.Assigned<Item>
    ) : ContentOrControlStructure<Lang, C>(), StableHash by StableHash.of(items, StableHash.of(body), next)
}

typealias TextElement<Lang> = ContentOrControlStructure<Lang, Element.OutlineContent.ParagraphContent.Text<Lang>>
typealias TableRowElement<Lang> = ContentOrControlStructure<Lang, Element.OutlineContent.ParagraphContent.Table.Row<Lang>>
typealias ParagraphContentElement<Lang> = ContentOrControlStructure<Lang, Element.OutlineContent.ParagraphContent<Lang>>
typealias ListItemElement<Lang> = ContentOrControlStructure<Lang, Element.OutlineContent.ParagraphContent.ItemList.Item<Lang>>
typealias OutlineElement<Lang> = ContentOrControlStructure<Lang, Element.OutlineContent<Lang>>

sealed class Element<out Lang : LanguageSupport> : StableHash {

    sealed class OutlineContent<out Lang : LanguageSupport> : Element<Lang>() {
        data class Title1<out Lang : LanguageSupport>(val text: List<TextElement<Lang>>) : OutlineContent<Lang>(), StableHash by StableHash.of(text)

        data class Title2<out Lang : LanguageSupport>(val text: List<TextElement<Lang>>) : OutlineContent<Lang>(), StableHash by StableHash.of(text)

        data class Paragraph<out Lang : LanguageSupport>(val paragraph: List<ParagraphContentElement<Lang>>) : OutlineContent<Lang>(), StableHash by StableHash.of(paragraph)

        sealed class ParagraphContent<out Lang : LanguageSupport> : Element<Lang>() {

            data class ItemList<out Lang : LanguageSupport>(
                val items: List<ListItemElement<Lang>>
            ) : ParagraphContent<Lang>(), StableHash by StableHash.of(items) {
                init {
                    if (items.flatMap { getItems(it) }.isEmpty()) throw InvalidListDeclarationException("List has no items")
                }

                data class Item<out Lang : LanguageSupport>(
                    val text: List<TextElement<Lang>>
                ) : Element<Lang>(), StableHash by StableHash.of(text)

                private fun getItems(item: ListItemElement<Lang>): List<Item<Lang>> =
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
            ) : ParagraphContent<Lang>(), StableHash by StableHash.of(StableHash.of(rows), header) {

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
                ) : Element<Lang>(), StableHash by StableHash.of(StableHash.of(cells), StableHash.of(colSpec)) {
                    init {
                        if (cells.isEmpty()) {
                            throw InvalidTableDeclarationException("Rows need at least one cell")
                        }
                        if (cells.size != colSpec.size) {
                            throw InvalidTableDeclarationException("The number of cells in the row(${cells.size}) does not match the number of columns in the specification(${colSpec.size})")
                        }
                    }
                }

                data class Header<out Lang : LanguageSupport>(val colSpec: List<ColumnSpec<Lang>>) : StableHash by StableHash.of(colSpec) {
                    init {
                        if (colSpec.isEmpty()) {
                            throw InvalidTableDeclarationException("Table column specification needs at least one column")
                        }
                    }
                }

                data class Cell<out Lang : LanguageSupport>(
                    val text: List<TextElement<Lang>>
                ) : StableHash by StableHash.of(text)

                data class ColumnSpec<out Lang : LanguageSupport>(
                    val headerContent: Cell<Lang>,
                    val alignment: ColumnAlignment,
                    val columnSpan: Int = 1
                ) : StableHash by StableHash.of(headerContent, StableHash.of(alignment), StableHash.of(columnSpan))

                enum class ColumnAlignment {
                    LEFT, RIGHT
                }
            }

            sealed class Text<out Lang : LanguageSupport> : ParagraphContent<Lang>() {
                abstract val fontType: FontType

                @ConsistentCopyVisibility
                data class Literal<out Lang : LanguageSupport> private constructor(
                    val text: Map<Language, String>,
                    val languages: Lang,
                    override val fontType: FontType,
                ) : Text<Lang>(), StableHash by StableHash.of(StableHash.of(text), StableHash.of(languages), StableHash.of(fontType)) {

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
                ) : Text<Lang>(), StableHash by StableHash.of(expression, StableHash.of(fontType)) {

                    @ConsistentCopyVisibility
                    data class ByLanguage<out Lang : LanguageSupport> private constructor(
                        val expression: Map<Language, StringExpression>,
                        val languages: Lang,
                        override val fontType: FontType
                    ) : Text<Lang>(), StableHash by StableHash.of(StableHash.of(expression), languages, StableHash.of(fontType)) {

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

                data class NewLine<out Lang : LanguageSupport>(
                    val index: Int, // To be able to distinguish between newLine-elements
                ) : Text<Lang>(), StableHash by StableHash.of(StableHash.of("Element.OutlineContent.ParagraphContent.Text.NewLine"), StableHash.of(index)) {
                    override val fontType = FontType.PLAIN
                }
            }

            sealed class Form<out Lang : LanguageSupport> : ParagraphContent<Lang>() {
                data class Text<out Lang : LanguageSupport>(
                    val prompt: TextElement<Lang>,
                    val size: Size,
                    val vspace: Boolean = true,
                ) : Form<Lang>(), StableHash by StableHash.of(prompt, StableHash.of(size), StableHash.of(vspace)) {
                    enum class Size { NONE, SHORT, LONG }
                }

                data class MultipleChoice<out Lang : LanguageSupport>(
                    // TODO: Denne bør ikke være TextElement, bør være Element.OutlineContent.ParagraphContent.Text
                    val prompt: TextElement<Lang>,
                    val choices: List<ParagraphContent.Text<Lang>>,
                    val vspace: Boolean = true,
                ) : Form<Lang>(), StableHash by StableHash.of(prompt, StableHash.of(choices), StableHash.of(vspace))
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