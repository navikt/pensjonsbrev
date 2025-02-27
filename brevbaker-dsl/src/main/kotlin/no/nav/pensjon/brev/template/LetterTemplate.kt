package no.nav.pensjon.brev.template

import no.nav.pensjon.brevbaker.api.model.ElementTags
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import kotlin.reflect.KClass

interface LetterTemplate<Lang : LanguageSupport, out LetterData : Any> {
    val name: String
    val title: List<TextElement<Lang>>
    val letterDataType: KClass<out LetterData>
    val language: Lang
    val outline: List<OutlineElement<Lang>>
    val attachments: List<IncludeAttachment<Lang, *>>
    val letterMetadata: LetterMetadata
}

sealed interface Expression<out Out> : StableHash {

    fun eval(scope: ExpressionScope<*>): Out

    interface Literal<out Out> : Expression<Out> {
        val value: Out
        val tags: Set<ElementTags>
            get() = emptySet()
    }

    sealed interface FromScope<out Out> : Expression<Out> {
        interface Felles : FromScope<no.nav.pensjon.brevbaker.api.model.Felles>

        interface Language : FromScope<no.nav.pensjon.brev.template.Language>

        interface Argument<out Out> : FromScope<Out> {
            @Suppress("UNCHECKED_CAST")
            override fun eval(scope: ExpressionScope<*>) = scope.argument as Out
            override fun equals(other: Any?): Boolean
            override fun hashCode(): Int
            override fun stableHashCode(): Int = "FromScope.Argument".hashCode()
        }

        interface Assigned<out Out> : FromScope<Out> {
            val id: Int
        }
    }

    interface UnaryInvoke<In, Out> : Expression<Out> {
        val value: Expression<In>
        val operation: UnaryOperation<In, Out>
    }

    interface BinaryInvoke<In1, In2, out Out> : Expression<Out> {
        val first: Expression<In1>
        val second: Expression<In2>
        val operation: BinaryOperation<In1, In2, Out>
    }
}

typealias StringExpression = Expression<String>

sealed interface ContentOrControlStructure<out Lang : LanguageSupport, out C : Element<Lang>> : StableHash {
    interface Content<out Lang : LanguageSupport, C : Element<Lang>> : ContentOrControlStructure<Lang, C> {
        val content: C
    }

    interface Conditional<out Lang : LanguageSupport, out C : Element<Lang>> : ContentOrControlStructure<Lang, C> {
        val predicate: Expression<Boolean>
        val showIf: List<ContentOrControlStructure<Lang, C>>
        val showElse: List<ContentOrControlStructure<Lang, C>>
    }

    interface ForEach<out Lang : LanguageSupport, C : Element<Lang>, Item : Any>: ContentOrControlStructure<Lang, C> {
        val items: Expression<Collection<Item>>
        val body: List<ContentOrControlStructure<Lang, C>>
        val next: Expression.FromScope.Assigned<Item>
    }
}

typealias TextElement<Lang> = ContentOrControlStructure<Lang, Element.OutlineContent.ParagraphContent.Text<Lang>>
typealias TableRowElement<Lang> = ContentOrControlStructure<Lang, Element.OutlineContent.ParagraphContent.Table.Row<Lang>>
typealias ParagraphContentElement<Lang> = ContentOrControlStructure<Lang, Element.OutlineContent.ParagraphContent<Lang>>
typealias ListItemElement<Lang> = ContentOrControlStructure<Lang, Element.OutlineContent.ParagraphContent.ItemList.Item<Lang>>
typealias OutlineElement<Lang> = ContentOrControlStructure<Lang, Element.OutlineContent<Lang>>

sealed interface Element<out Lang : LanguageSupport> : StableHash {

    sealed interface OutlineContent<out Lang : LanguageSupport> : Element<Lang> {
        interface Title1<out Lang : LanguageSupport> : OutlineContent<Lang> {
            val text: List<TextElement<Lang>>
        }

        interface Title2<out Lang : LanguageSupport> : OutlineContent<Lang> {
            val text: List<TextElement<Lang>>
        }

        interface Paragraph<out Lang : LanguageSupport> : OutlineContent<Lang> {
            val paragraph: List<ParagraphContentElement<Lang>>
        }

        sealed interface ParagraphContent<out Lang : LanguageSupport> : Element<Lang> {

            interface ItemList<out Lang : LanguageSupport> : ParagraphContent<Lang> {
                val items: List<ListItemElement<Lang>>

                interface Item<out Lang : LanguageSupport>: Element<Lang> {
                    val text: List<TextElement<Lang>>
                }
            }

            // TODO: Siden tabellene skal passe inn i et brev, så bør vi ha en maksimumsgrense på antall-kolonner (evt. bare total bredde)
            interface Table<out Lang : LanguageSupport> : ParagraphContent<Lang> {
                val rows: List<TableRowElement<Lang>>
                val header: Header<Lang>

                interface Row<out Lang : LanguageSupport> : Element<Lang> {
                    val cells: List<Cell<Lang>>
                    val colSpec: List<ColumnSpec<Lang>>
                }

                interface Header<out Lang : LanguageSupport> : StableHash {
                    val colSpec: List<ColumnSpec<Lang>>
                }

                interface Cell<out Lang : LanguageSupport> : StableHash {
                    val text: List<TextElement<Lang>>
                }

                interface ColumnSpec<out Lang : LanguageSupport> : StableHash {
                    val headerContent: Cell<Lang>
                    val alignment: ColumnAlignment
                    val columnSpan: Int
                }

                enum class ColumnAlignment {
                    LEFT, RIGHT
                }
            }

            sealed interface Text<out Lang : LanguageSupport> : ParagraphContent<Lang> {
                val fontType: FontType

                interface Literal<out Lang : LanguageSupport>: Text<Lang>, StableHash {
                    val text: Map<Language, String>
                    val languages: Lang
                    override val fontType: FontType

                    fun text(language: Language): String
                }

                enum class FontType {
                    PLAIN,
                    BOLD,
                    ITALIC
                }

                interface Expression<out Lang : LanguageSupport> : Text<Lang>, StableHash {
                    val expression: StringExpression
                    override val fontType: FontType

                    interface ByLanguage<out Lang : LanguageSupport> : Text<Lang>, StableHash {
                        val expression: Map<Language, StringExpression>
                        val languages: Lang
                        override val fontType: FontType

                        fun expr(language: Language): StringExpression
                    }
                }

                interface NewLine<out Lang : LanguageSupport> : Text<Lang>, StableHash {
                    val index: Int // To be able to distinguish between newLine-elements
                    override val fontType: FontType
                }
            }

            sealed interface Form<out Lang : LanguageSupport> : ParagraphContent<Lang> {
                interface Text<out Lang : LanguageSupport> : Form<Lang>, StableHash {
                    val prompt: TextElement<Lang>
                    val size: Size
                    val vspace: Boolean
                    enum class Size { NONE, SHORT, LONG }
                }

                interface MultipleChoice<out Lang : LanguageSupport> : Form<Lang>, StableHash {
                    // TODO: Denne bør ikke være TextElement, bør være Element.OutlineContent.ParagraphContent.Text
                    val prompt: TextElement<Lang>
                    val choices: List<ParagraphContent.Text<Lang>>
                    val vspace: Boolean
                }
            }
        }
    }

}