package no.nav.pensjon.brev.template

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.template.Element.OutlineContent
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Form
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Expression.FromScope
import no.nav.pensjon.brev.template.Expression.Literal
import no.nav.pensjon.brevbaker.api.model.ElementTags
import no.nav.pensjon.brevbaker.api.model.IntValue
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import java.time.LocalDate
import kotlin.reflect.KClass

@InterneDataklasser
data class LetterTemplateImpl<Lang : LanguageSupport, out LetterData : Any>(
    override val name: String,
    override val title: List<TextElement<Lang>>,
    override val letterDataType: KClass<out LetterData>,
    override val language: Lang,
    override val outline: List<OutlineElement<Lang>>,
    override val attachments: List<IncludeAttachment<Lang, *>> = emptyList(),
    override val letterMetadata: LetterMetadata,
) : LetterTemplate<Lang, LetterData> {
    init {
        if (title.isEmpty()) {
            throw MissingTitleInTemplateException("Missing title in template: $name")
        }
    }
}

@InterneDataklasser
object ExpressionImpl {

    @InterneDataklasser
    data class LiteralImpl<out Out>(override val value: Out, override val tags: Set<ElementTags> = emptySet()) :
        Literal<Out> {
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

        override fun toString(): String {
            throw PreventToStringForExpressionException()
        }
    }

    @InterneDataklasser
    object FromScopeImpl {

        @InterneDataklasser
        object FellesImpl : FromScope.Felles {
            override fun eval(scope: ExpressionScope<*>) = scope.felles
            override fun stableHashCode(): Int = "FromScope.Felles".hashCode()
        }

        @InterneDataklasser
        class ArgumentImpl<out Out> : FromScope.Argument<Out> {
            @Suppress("UNCHECKED_CAST")
            override fun eval(scope: ExpressionScope<*>) = scope.argument as Out
            override fun equals(other: Any?): Boolean = other is FromScope.Argument<*>
            override fun hashCode(): Int = FromScope.Argument::class.java.hashCode()
            override fun stableHashCode(): Int = "FromScope.Argument".hashCode()

            override fun toString(): String {
                throw PreventToStringForExpressionException()
            }
        }

        @InterneDataklasser
        data class AssignedImpl<out Out>(override val id: Int) : FromScope.Assigned<Out> {
            override fun eval(scope: ExpressionScope<*>): Out =
                if (scope is ExpressionScope.WithAssignment<*, *>) {
                    @Suppress("UNCHECKED_CAST")
                    (scope as ExpressionScope.WithAssignment<*, Out>).lookup(this)
                } else {
                    throw InvalidScopeTypeException("Requires scope to be ${this::class.qualifiedName}, but was: ${scope::class.qualifiedName}")
                }

            override fun stableHashCode() = hashCode()

            override fun toString(): String {
                throw PreventToStringForExpressionException()
            }
        }
    }

    @InterneDataklasser
    data class UnaryInvokeImpl<In, Out>(
        override val value: Expression<In>,
        override val operation: UnaryOperation<In, Out>,
    ) : Expression.UnaryInvoke<In, Out>, StableHash by StableHash.of(value, operation) {
        override fun eval(scope: ExpressionScope<*>): Out = operation.apply(value.eval(scope))

        override fun toString(): String {
            throw PreventToStringForExpressionException()
        }

        override fun equals(other: Any?): Boolean {
            return other is Expression.UnaryInvoke<*, *> && (value == other.value && operation == other.operation)
        }
        override fun hashCode(): Int = stableHashCode()
        override fun stableHashCode(): Int = "UnaryInvoke".hashCode()
    }

    @InterneDataklasser
    data class BinaryInvokeImpl<In1, In2, out Out>(
        override val first: Expression<In1>,
        override val second: Expression<In2>,
        override val operation: BinaryOperation<In1, In2, Out>,
    ) : Expression.BinaryInvoke<In1, In2, Out>, StableHash by StableHash.of(first, second, operation) {
        override fun eval(scope: ExpressionScope<*>): Out = operation.apply(first.eval(scope), second.eval(scope))

        override fun toString(): String {
            throw PreventToStringForExpressionException()
        }
    }

}

@InterneDataklasser
object ContentOrControlStructureImpl {

    @InterneDataklasser
    data class ContentImpl<out Lang : LanguageSupport, C : Element<Lang>>(
        override val content: C,
    ) : ContentOrControlStructure.Content<Lang, C>, StableHash by StableHash.of(content)

    @InterneDataklasser
    data class ConditionalImpl<out Lang : LanguageSupport, out C : Element<Lang>>(
        override val predicate: Expression<Boolean>,
        override val showIf: List<ContentOrControlStructure<Lang, C>>,
        override val showElse: List<ContentOrControlStructure<Lang, C>>,
    ) : ContentOrControlStructure.Conditional<Lang, C>,
        StableHash by StableHash.of(predicate, StableHash.of(showIf), StableHash.of(showElse))

    @InterneDataklasser
    data class ForEachImpl<out Lang : LanguageSupport, C : Element<Lang>, Item : Any>(
        override val items: Expression<Collection<Item>>,
        override val body: List<ContentOrControlStructure<Lang, C>>,
        override val next: FromScope.Assigned<Item>,
    ) : ContentOrControlStructure.ForEach<Lang, C, Item>, StableHash by StableHash.of(items, StableHash.of(body), next)
}

@InterneDataklasser
object ElementImpl {
    @InterneDataklasser
    object OutlineContentImpl {
        @InterneDataklasser
        data class Title1Impl<out Lang : LanguageSupport>(override val text: List<TextElement<Lang>>) : OutlineContent.Title1<Lang>,
            StableHash by StableHash.of(text)

        @InterneDataklasser
        data class Title2Impl<out Lang : LanguageSupport>(override val text: List<TextElement<Lang>>) : OutlineContent.Title2<Lang>,
            StableHash by StableHash.of(text)

        @InterneDataklasser
        data class ParagraphImpl<out Lang : LanguageSupport>(override val paragraph: List<ParagraphContentElement<Lang>>) :
            OutlineContent.Paragraph<Lang>, StableHash by StableHash.of(paragraph)

        @InterneDataklasser
        object ParagraphContentImpl {

            @InterneDataklasser
            data class ItemListImpl<out Lang : LanguageSupport>(
                override val items: List<ListItemElement<Lang>>
            ) : ParagraphContent.ItemList<Lang>, StableHash by StableHash.of(items) {
                init {
                    if (items.flatMap { getItems(it) }.isEmpty()) throw InvalidListDeclarationException("List has no items")
                }

                @InterneDataklasser
                data class ItemImpl<out Lang : LanguageSupport>(
                    override val text: List<TextElement<Lang>>
                ) : ParagraphContent.ItemList.Item<Lang>, StableHash by StableHash.of(text)

                private fun getItems(item: ListItemElement<Lang>): List<ParagraphContent.ItemList.Item<Lang>> =
                    when (item) {
                        is ContentOrControlStructure.Conditional -> item.showIf.plus(item.showElse).flatMap { getItems(it) }
                        is ContentOrControlStructure.ForEach<Lang, ParagraphContent.ItemList.Item<Lang>, *> -> item.body.flatMap { getItems(it) }
                        is ContentOrControlStructure.Content -> listOf(item.content)
                    }
            }

            @InterneDataklasser
            // TODO: Siden tabellene skal passe inn i et brev, så bør vi ha en maksimumsgrense på antall-kolonner (evt. bare total bredde)
            data class TableImpl<out Lang : LanguageSupport>(
                override val rows: List<TableRowElement<Lang>>,
                override val header: ParagraphContent.Table.Header<Lang>,
            ) : ParagraphContent.Table<Lang>, StableHash by StableHash.of(StableHash.of(rows), header) {

                init {
                    if (rows.flatMap { getRows(it) }.isEmpty()) {
                        throw InvalidTableDeclarationException("A table must have at least one row")
                    }
                }

                private fun getRows(row: TableRowElement<Lang>): List<ParagraphContent.Table.Row<Lang>> =
                    when (row) {
                        is ContentOrControlStructure.Conditional -> row.showIf.plus(row.showElse).flatMap { getRows(it) }
                        is ContentOrControlStructure.ForEach<Lang, ParagraphContent.Table.Row<Lang>, *> -> row.body.flatMap { getRows(it) }
                        is ContentOrControlStructure.Content -> listOf(row.content)
                    }

                @InterneDataklasser
                data class RowImpl<out Lang : LanguageSupport>(
                    override val cells: List<ParagraphContent.Table.Cell<Lang>>,
                    override val colSpec: List<ParagraphContent.Table.ColumnSpec<Lang>>
                ) : ParagraphContent.Table.Row<Lang>, StableHash by StableHash.of(StableHash.of(cells), StableHash.of(colSpec)) {
                    init {
                        if (cells.isEmpty()) {
                            throw InvalidTableDeclarationException("Rows need at least one cell")
                        }
                        if (cells.size != colSpec.size) {
                            throw InvalidTableDeclarationException("The number of cells in the row(${cells.size}) does not match the number of columns in the specification(${colSpec.size})")
                        }
                    }
                }

                @InterneDataklasser
                data class HeaderImpl<out Lang : LanguageSupport>(
                    override val colSpec: List<ParagraphContent.Table.ColumnSpec<Lang>>) : ParagraphContent.Table.Header<Lang>, StableHash by StableHash.of(colSpec)
                {
                    init {
                        if (colSpec.isEmpty()) {
                            throw InvalidTableDeclarationException("Table column specification needs at least one column")
                        }
                    }
                }

                @InterneDataklasser
                data class CellImpl<out Lang : LanguageSupport>(
                    override val text: List<TextElement<Lang>>
                ) : ParagraphContent.Table.Cell<Lang>, StableHash by StableHash.of(text)


                @InterneDataklasser
                data class ColumnSpecImpl<out Lang : LanguageSupport>(
                    override val headerContent: ParagraphContent.Table.Cell<Lang>,
                    override val alignment: ParagraphContent.Table.ColumnAlignment,
                    override val columnSpan: Int = 1
                ) : ParagraphContent.Table.ColumnSpec<Lang>, StableHash by StableHash.of(headerContent, StableHash.of(alignment), StableHash.of(columnSpan))
            }

            @InterneDataklasser
            object TextImpl {
                @ConsistentCopyVisibility
                data class LiteralImpl<out Lang : LanguageSupport> private constructor(
                    override val text: Map<Language, String>,
                    override val languages: Lang,
                    override val fontType: Text.FontType,
                ) : Text.Literal<Lang>, StableHash by StableHash.of(StableHash.of(text), StableHash.of(languages), StableHash.of(fontType)) {

                    override fun text(language: Language): String =
                        text[language]
                            ?: throw IllegalArgumentException("Text.Literal doesn't contain language: ${language::class.qualifiedName}")

                    companion object {
                        fun <Lang1 : Language> create(
                            lang1: Pair<Lang1, String>,
                            fontType: Text.FontType = Text.FontType.PLAIN
                        ) = LiteralImpl<LanguageSupport.Single<Lang1>>(
                            text = mapOf(lang1),
                            languages = LanguageCombination.Single(lang1.first),
                            fontType = fontType
                        )

                        fun <Lang1 : Language, Lang2 : Language> create(
                            lang1: Pair<Lang1, String>,
                            lang2: Pair<Lang2, String>,
                            fontType: Text.FontType = Text.FontType.PLAIN,
                        ) = LiteralImpl<LanguageSupport.Double<Lang1, Lang2>>(
                            text = mapOf(lang1, lang2),
                            languages = LanguageCombination.Double(lang1.first, lang2.first),
                            fontType = fontType
                        )

                        fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> create(
                            lang1: Pair<Lang1, String>,
                            lang2: Pair<Lang2, String>,
                            lang3: Pair<Lang3, String>,
                            fontType: Text.FontType = Text.FontType.PLAIN,
                        ) = LiteralImpl<LanguageSupport.Triple<Lang1, Lang2, Lang3>>(
                            text = mapOf(lang1, lang2, lang3),
                            languages = LanguageCombination.Triple(lang1.first, lang2.first, lang3.first),
                            fontType = fontType
                        )
                    }
                }

                @InterneDataklasser
                data class ExpressionImpl<out Lang : LanguageSupport>(
                    override val expression: StringExpression,
                    override val fontType: FontType = FontType.PLAIN
                ) : Text.Expression<Lang>, StableHash by StableHash.of(expression, StableHash.of(fontType)) {

                    @ConsistentCopyVisibility
                    @InterneDataklasser
                    data class ByLanguageImpl<out Lang : LanguageSupport> private constructor(
                        override val expression: Map<Language, StringExpression>,
                        override val languages: Lang,
                        override val fontType: FontType
                    ) : Text.Expression.ByLanguage<Lang>, StableHash by StableHash.of(StableHash.of(expression), languages, StableHash.of(fontType)) {

                        override fun expr(language: Language): StringExpression =
                            expression[language]
                                ?: throw IllegalArgumentException("Text.Expression.ByLanguage doesn't contain language: ${language::class.qualifiedName}")

                        companion object {
                            fun <Lang1 : Language> create(
                                lang1: Pair<Lang1, StringExpression>,
                                fontType: FontType = FontType.PLAIN
                            ) = ByLanguageImpl<LanguageSupport.Single<Lang1>>(mapOf(lang1), LanguageCombination.Single(lang1.first), fontType)

                            fun <Lang1 : Language, Lang2 : Language> create(
                                lang1: Pair<Lang1, StringExpression>,
                                lang2: Pair<Lang2, StringExpression>,
                                fontType: FontType = FontType.PLAIN,
                            ) = ByLanguageImpl<LanguageSupport.Double<Lang1, Lang2>>(mapOf(lang1, lang2), LanguageCombination.Double(lang1.first, lang2.first), fontType)

                            fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> create(
                                lang1: Pair<Lang1, StringExpression>,
                                lang2: Pair<Lang2, StringExpression>,
                                lang3: Pair<Lang3, StringExpression>,
                                fontType: FontType = FontType.PLAIN,
                            ) = ByLanguageImpl<LanguageSupport.Triple<Lang1, Lang2, Lang3>>(mapOf(lang1, lang2, lang3), LanguageCombination.Triple(lang1.first, lang2.first, lang3.first), fontType)
                        }
                    }
                }

                @InterneDataklasser
                data class NewLineImpl<out Lang : LanguageSupport>(
                    override val index: Int, // To be able to distinguish between newLine-elements
                ) : Text.NewLine<Lang>, StableHash by StableHash.of(StableHash.of("Element.OutlineContent.ParagraphContent.Text.NewLine"), StableHash.of(index)) {
                    override val fontType = FontType.PLAIN
                }
            }

            @InterneDataklasser
            object FormImpl {
                data class TextImpl<out Lang : LanguageSupport>(
                    override val prompt: TextElement<Lang>,
                    override val size: Form.Text.Size,
                    override val vspace: Boolean = true,
                ) : Form.Text<Lang>, StableHash by StableHash.of(prompt, StableHash.of(size), StableHash.of(vspace))

                data class MultipleChoiceImpl<out Lang : LanguageSupport>(
                    // TODO: Denne bør ikke være TextElement, bør være Element.OutlineContent.ParagraphContent.Text
                    override val prompt: TextElement<Lang>,
                    override val choices: List<Text<Lang>>,
                    override val vspace: Boolean = true,
                ) : Form.MultipleChoice<Lang>, StableHash by StableHash.of(prompt, StableHash.of(choices), StableHash.of(vspace))
            }
        }
    }
}