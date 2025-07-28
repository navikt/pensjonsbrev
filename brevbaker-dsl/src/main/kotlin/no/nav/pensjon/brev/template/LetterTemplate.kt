package no.nav.pensjon.brev.template

import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brevbaker.api.model.ElementTags
import no.nav.pensjon.brevbaker.api.model.IntValue
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import java.time.LocalDate
import java.util.Objects
import kotlin.reflect.KClass

class LetterTemplate<Lang : LanguageSupport, out LetterData : Any> internal constructor(
    val name: String,
    val title: List<TextElement<Lang>>,
    val letterDataType: KClass<out LetterData>,
    val language: Lang,
    val outline: List<OutlineElement<Lang>>,
    val attachments: List<IncludeAttachment<Lang, *>> = emptyList(),
    val pdfAttachments: List<PDFTemplate<*>> = emptyList(),
    val letterMetadata: LetterMetadata,
) {
    init {
        if (title.isEmpty()) {
            throw MissingTitleInTemplateException("Missing title in template: $name")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is LetterTemplate<*, *>) return false
        return name == other.name && title == other.title && letterDataType == other.letterDataType
                && language == other.language && outline == other.outline && attachments == other.attachments
                && letterMetadata == other.letterMetadata
    }
    override fun hashCode() = Objects.hash(name, title, letterDataType, language, outline, attachments, letterMetadata)
    override fun toString() =
        "LetterTemplate(name='$name', title=$title, letterDataType=$letterDataType, language=$language, outline=$outline, attachments=$attachments, letterMetadata=$letterMetadata)"
}

sealed class Expression<out Out> : StableHash {

    abstract fun eval(scope: ExpressionScope<*>): Out

    class Literal<out Out> @InternKonstruktoer constructor(val value: Out, val tags: Set<ElementTags> = emptySet()) : Expression<Out>() {
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

        override fun equals(other: Any?): Boolean {
            if (other !is Literal<*>) return false
            return value == other.value && tags == other.tags
        }
        override fun hashCode() = Objects.hash(value, tags)
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

        class Argument<out Out> @InternKonstruktoer constructor(): FromScope<Out> () {
            @Suppress("UNCHECKED_CAST")
            override fun eval(scope: ExpressionScope<*>) = scope.argument as Out
            override fun equals(other: Any?): Boolean = other is Argument<*>
            override fun hashCode(): Int = javaClass.hashCode()
            override fun stableHashCode(): Int = "FromScope.Argument".hashCode()
        }

        class Assigned<out Out> internal constructor(val id: Int) : FromScope<Out>() {
            override fun eval(scope: ExpressionScope<*>): Out =
                if (scope is ExpressionScope.WithAssignment<*, *>) {
                    @Suppress("UNCHECKED_CAST")
                    (scope as ExpressionScope.WithAssignment<*, Out>).lookup(this)
                } else {
                    throw InvalidScopeTypeException("Requires scope to be ${this::class.qualifiedName}, but was: ${scope::class.qualifiedName}")
                }

            override fun stableHashCode() = hashCode()

            override fun equals(other: Any?): Boolean {
                if (other !is Assigned<*>) return false
                return id == other.id
            }
            override fun hashCode() = Objects.hash(id)
        }
    }

    class UnaryInvoke<In, Out>(
        val value: Expression<In>,
        val operation: UnaryOperation<In, Out>,
    ) : Expression<Out>(), StableHash by StableHash.of(value, operation) {
        override fun eval(scope: ExpressionScope<*>): Out = operation.apply(value.eval(scope))

        override fun equals(other: Any?): Boolean {
            if (other !is UnaryInvoke<*, *>) return false
            return value == other.value && operation == other.operation
        }
        override fun hashCode() = Objects.hash(value, operation)
    }

    class NullSafeApplication<In : Any, Out> private constructor(
        val input: Expression<In?>,
        val assigned: FromScope.Assigned<In>,
        val application: Expression<Out?>,
    ) : Expression<Out?>(), StableHash by StableHash.of(input, assigned, application) {

        companion object {
            operator fun <In: Any, Out> invoke(value: Expression<In?>, block: Expression<In>.() -> Expression<Out>): NullSafeApplication<In, Out> =
                FromScope.Assigned<In>(value.stableHashCode()).let {
                    NullSafeApplication(value, it, it.block())
                }
        }

        override fun eval(scope: ExpressionScope<*>): Out? =
            input.eval(scope)?.let {
                application.eval(scope.assign(it, assigned))
            }

        override fun equals(other: Any?): Boolean {
            if (other !is NullSafeApplication<*, *>) return false
            return input == other.input && assigned == other.assigned && application == other.application
        }

        override fun hashCode() = Objects.hash(input, assigned, application)

    }

    class BinaryInvoke<In1, In2, out Out>(
        val first: Expression<In1>,
        val second: Expression<In2>,
        val operation: BinaryOperation<In1, In2, Out>
    ) : Expression<Out>(), StableHash by StableHash.of(first, second, operation) {
        override fun eval(scope: ExpressionScope<*>): Out = operation.apply(first.eval(scope), second.eval(scope))

        override fun equals(other: Any?): Boolean {
            if (other !is BinaryInvoke<*, *, *>) return false
            return first == other.first && second == other.second && operation == other.operation
        }
        override fun hashCode() = Objects.hash(first, second, operation)
    }

    final override fun toString(): String {
        throw PreventToStringForExpressionException()
    }
}

typealias StringExpression = Expression<String>

sealed class ContentOrControlStructure<out Lang : LanguageSupport, out C : Element<Lang>> : StableHash {
    class Content<out Lang : LanguageSupport, C : Element<Lang>> internal constructor(
        val content: C,
    ) : ContentOrControlStructure<Lang, C>(), StableHash by StableHash.of(content) {
        override fun equals(other: Any?): Boolean {
            if (other !is Content<*,*>) return false
            return content == other.content
        }
        override fun hashCode() = Objects.hash(content)
        override fun toString() = "Content(content=$content)"
    }

    class Conditional<out Lang : LanguageSupport, out C : Element<Lang>> internal constructor(
        val predicate: Expression<Boolean>,
        val showIf: List<ContentOrControlStructure<Lang, C>>,
        val showElse: List<ContentOrControlStructure<Lang, C>>,
    ) : ContentOrControlStructure<Lang, C>(), StableHash by StableHash.of(predicate, StableHash.of(showIf), StableHash.of(showElse)) {
        override fun equals(other: Any?): Boolean {
            if (other !is Conditional<*, *>) return false
            return predicate == other.predicate && showIf == other.showIf && showElse == other.showElse
        }
        override fun hashCode() = Objects.hash(predicate, showIf, showElse)
        override fun toString() = "Conditional(predicate=$predicate, showIf=$showIf, showElse=$showElse)"
    }

    class ForEach<out Lang : LanguageSupport, C : Element<Lang>, Item : Any> internal constructor(
        val items: Expression<Collection<Item>>,
        val body: List<ContentOrControlStructure<Lang, C>>,
        val next: Expression.FromScope.Assigned<Item>
    ) : ContentOrControlStructure<Lang, C>(), StableHash by StableHash.of(items, StableHash.of(body), next) {
        override fun equals(other: Any?): Boolean {
            if (other !is ForEach<*, *, *>) return false
            return items == other.items && body == other.body && next == other.next
        }
        override fun hashCode() = Objects.hash(items, body, next)
        override fun toString() = "ForEach(items=$items, body=$body, next=$next)"
    }
}

typealias TextElement<Lang> = ContentOrControlStructure<Lang, Element.OutlineContent.ParagraphContent.Text<Lang>>
typealias TableRowElement<Lang> = ContentOrControlStructure<Lang, Element.OutlineContent.ParagraphContent.Table.Row<Lang>>
typealias ParagraphContentElement<Lang> = ContentOrControlStructure<Lang, Element.OutlineContent.ParagraphContent<Lang>>
typealias ListItemElement<Lang> = ContentOrControlStructure<Lang, Element.OutlineContent.ParagraphContent.ItemList.Item<Lang>>
typealias OutlineElement<Lang> = ContentOrControlStructure<Lang, Element.OutlineContent<Lang>>

sealed class Element<out Lang : LanguageSupport> : StableHash {

    sealed class OutlineContent<out Lang : LanguageSupport> : Element<Lang>() {
        class Title1<out Lang : LanguageSupport> internal constructor(val text: List<TextElement<Lang>>) : OutlineContent<Lang>(), StableHash by StableHash.of(text) {
            override fun equals(other: Any?): Boolean {
                if (other !is Title1<*>) return false
                return text == other.text
            }
            override fun hashCode() = Objects.hash(text)
            override fun toString() = "Title1(text=$text)"
        }

        class Title2<out Lang : LanguageSupport> internal constructor(val text: List<TextElement<Lang>>) : OutlineContent<Lang>(), StableHash by StableHash.of(text) {
            override fun equals(other: Any?): Boolean {
                if (other !is Title2<*>) return false
                return text == other.text
            }
            override fun hashCode() = Objects.hash(text)
            override fun toString(): String = "Title2(text=$text)"
        }

        class Paragraph<out Lang : LanguageSupport> internal constructor(val paragraph: List<ParagraphContentElement<Lang>>) : OutlineContent<Lang>(), StableHash by StableHash.of(paragraph) {
            override fun equals(other: Any?): Boolean {
                if (other !is Paragraph<*>) return false
                return paragraph == other.paragraph
            }
            override fun hashCode() = Objects.hash(paragraph)
            override fun toString(): String = "Paragraph(paragraph=$paragraph)"
        }

        sealed class ParagraphContent<out Lang : LanguageSupport> : Element<Lang>() {

            class ItemList<out Lang : LanguageSupport> internal constructor(
                val items: List<ListItemElement<Lang>>
            ) : ParagraphContent<Lang>(), StableHash by StableHash.of(items) {
                init {
                    if (items.flatMap { getItems(it) }.isEmpty()) throw InvalidListDeclarationException("List has no items")
                }

                override fun equals(other: Any?): Boolean {
                    if (other !is ItemList<*>) return false
                    return items == other.items
                }
                override fun hashCode() = Objects.hash(items)
                override fun toString() = "ItemList(items=$items)"

                class Item<out Lang : LanguageSupport> internal constructor(
                    val text: List<TextElement<Lang>>
                ) : Element<Lang>(), StableHash by StableHash.of(text) {
                    override fun equals(other: Any?): Boolean {
                        if (other !is Item<*>) return false
                        return text == other.text
                    }
                    override fun hashCode() = Objects.hash(text)
                    override fun toString() = "Item(text=$text)"
                }

                private fun getItems(item: ListItemElement<Lang>): List<Item<Lang>> =
                    when (item) {
                        is ContentOrControlStructure.Conditional -> item.showIf.plus(item.showElse).flatMap { getItems(it) }
                        is ContentOrControlStructure.ForEach<Lang, Item<Lang>, *> -> item.body.flatMap { getItems(it) }
                        is ContentOrControlStructure.Content -> listOf(item.content)
                    }

            }

            // TODO: Siden tabellene skal passe inn i et brev, så bør vi ha en maksimumsgrense på antall-kolonner (evt. bare total bredde)
            class Table<out Lang : LanguageSupport> internal constructor(
                val rows: List<TableRowElement<Lang>>,
                val header: Header<Lang>,
            ) : ParagraphContent<Lang>(), StableHash by StableHash.of(StableHash.of(rows), header) {

                init {
                    if (rows.flatMap { getRows(it) }.isEmpty()) {
                        throw InvalidTableDeclarationException("A table must have at least one row")
                    }
                }

                override fun equals(other: Any?): Boolean {
                    if (other !is Table<*>) return false
                    return rows == other.rows && header == other.header
                }
                override fun hashCode() = Objects.hash(rows, header)

                private fun getRows(row: TableRowElement<Lang>): List<Row<Lang>> =
                    when (row) {
                        is ContentOrControlStructure.Conditional -> row.showIf.plus(row.showElse).flatMap { getRows(it) }
                        is ContentOrControlStructure.ForEach<Lang, Row<Lang>, *> -> row.body.flatMap { getRows(it) }
                        is ContentOrControlStructure.Content -> listOf(row.content)
                    }

                class Row<out Lang : LanguageSupport> internal constructor(
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

                    override fun equals(other: Any?): Boolean {
                        if (other !is Row<*>) return false
                        return cells == other.cells && colSpec == other.colSpec
                    }
                    override fun hashCode() = Objects.hash(cells, colSpec)
                    override fun toString(): String = "Row(cells=$cells, colSpec=$colSpec)"
                }

                class Header<out Lang : LanguageSupport> internal constructor(val colSpec: List<ColumnSpec<Lang>>) : StableHash by StableHash.of(colSpec) {
                    init {
                        if (colSpec.isEmpty()) {
                            throw InvalidTableDeclarationException("Table column specification needs at least one column")
                        }
                    }

                    override fun equals(other: Any?): Boolean {
                        if (other !is Header<*>) return false
                        return colSpec == other.colSpec
                    }
                    override fun hashCode() = Objects.hash(colSpec)
                    override fun toString(): String = "Header(colSpec=$colSpec)"
                }

                class Cell<out Lang : LanguageSupport> internal constructor(
                    val text: List<TextElement<Lang>>
                ) : StableHash by StableHash.of(text) {
                    override fun equals(other: Any?): Boolean {
                        if (other !is Cell<*>) return false
                        return text == other.text
                    }
                    override fun hashCode() = Objects.hash(text)
                    override fun toString() = "Cell(text=$text)"
                }

                class ColumnSpec<out Lang : LanguageSupport> internal constructor(
                    val headerContent: Cell<Lang>,
                    val alignment: ColumnAlignment,
                    val columnSpan: Int = 1
                ) : StableHash by StableHash.of(headerContent, StableHash.of(alignment), StableHash.of(columnSpan)) {
                    override fun equals(other: Any?): Boolean {
                        if (other !is ColumnSpec<*>) return false
                        return headerContent == other.headerContent && alignment == other.alignment && columnSpan == other.columnSpan
                    }
                    override fun hashCode() = Objects.hash(headerContent, alignment, columnSpan)
                    override fun toString() = "ColumnSpec(headerContent=$headerContent, alignment=$alignment, columnSpan=$columnSpan)"
                }

                enum class ColumnAlignment {
                    LEFT, RIGHT
                }
            }

            sealed class Text<out Lang : LanguageSupport> : ParagraphContent<Lang>() {
                abstract val fontType: FontType

                class Literal<out Lang : LanguageSupport> private constructor(
                    val text: Map<Language, String>,
                    val languages: Lang,
                    override val fontType: FontType,
                ) : Text<Lang>(), StableHash by StableHash.of(StableHash.of(text), StableHash.of(languages), StableHash.of(fontType)) {

                    override fun equals(other: Any?): Boolean {
                        if (other !is Literal<*>) return false
                        return text == other.text && languages == other.languages && fontType == other.fontType
                    }
                    override fun hashCode() = Objects.hash(text, languages, fontType)
                    override fun toString() = "Literal(text=$text, languages=$languages, fontType=$fontType)"


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

                class Expression<out Lang : LanguageSupport> internal constructor(
                    val expression: StringExpression,
                    override val fontType: FontType = FontType.PLAIN
                ) : Text<Lang>(), StableHash by StableHash.of(expression, StableHash.of(fontType)) {

                    override fun equals(other: Any?): Boolean {
                        if (other !is Expression<*>) return false
                        return expression == other.expression && fontType == other.fontType
                    }
                    override fun hashCode() = Objects.hash(expression, fontType)
                    override fun toString() = "Expression(expression=$expression, fontType=$fontType)"

                    class ByLanguage<out Lang : LanguageSupport> private constructor(
                        val expression: Map<Language, StringExpression>,
                        val languages: Lang,
                        override val fontType: FontType
                    ) : Text<Lang>(), StableHash by StableHash.of(StableHash.of(expression), languages, StableHash.of(fontType)) {

                        override fun equals(other: Any?): Boolean {
                            if (other !is ByLanguage<*>) return false
                            return expression == other.expression && languages == other.languages && fontType == other.fontType
                        }
                        override fun hashCode() = Objects.hash(expression, languages, fontType)
                        override fun toString() = "ByLanguage(expression=$expression, languages=$languages, fontType=$fontType)"

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

                class NewLine<out Lang : LanguageSupport> internal constructor(
                    val index: Int, // To be able to distinguish between newLine-elements
                ) : Text<Lang>(), StableHash by StableHash.of(StableHash.of("Element.OutlineContent.ParagraphContent.Text.NewLine"), StableHash.of(index)) {
                    override val fontType = FontType.PLAIN

                    override fun equals(other: Any?): Boolean {
                        if (other !is NewLine<*>) return false
                        return index == other.index
                    }
                    override fun hashCode() = Objects.hash(index)
                    override fun toString() = "NewLine(index=$index, fontType=$fontType)"
                }
            }

            sealed class Form<out Lang : LanguageSupport> : ParagraphContent<Lang>() {
                class Text<out Lang : LanguageSupport> internal constructor(
                    val prompt: TextElement<Lang>,
                    val size: Size,
                    val vspace: Boolean = true,
                ) : Form<Lang>(), StableHash by StableHash.of(prompt, StableHash.of(size), StableHash.of(vspace)) {
                    enum class Size { NONE, SHORT, LONG }

                    override fun equals(other: Any?): Boolean {
                        if (other !is Text<*>) return false
                        return prompt == other.prompt && size == other.size && vspace == other.vspace
                    }
                    override fun hashCode() = Objects.hash(size, prompt, vspace)
                    override fun toString() = "Text(prompt=$prompt, size=$size, vspace=$vspace)"
                }

                class MultipleChoice<out Lang : LanguageSupport> internal constructor(
                    // TODO: Denne bør ikke være TextElement, bør være Element.OutlineContent.ParagraphContent.Text
                    val prompt: TextElement<Lang>,
                    val choices: List<ParagraphContent.Text<Lang>>,
                    val vspace: Boolean = true,
                ) : Form<Lang>(), StableHash by StableHash.of(prompt, StableHash.of(choices), StableHash.of(vspace)) {
                    override fun equals(other: Any?): Boolean {
                        if (other !is MultipleChoice<*>) return false
                        return prompt == other.prompt && choices == other.choices && vspace == other.vspace
                    }
                    override fun hashCode() = Objects.hash(choices, prompt, vspace)
                    override fun toString() = "MultipleChoice(prompt=$prompt, choices=$choices, vspace=$vspace)"
                }
            }
        }
    }

}