package no.nav.brev.brevbaker.markup.outline

import no.nav.brev.brevbaker.markup.MarkupInternalApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import no.nav.brev.brevbaker.markup.Markup
import no.nav.brev.brevbaker.markup.Markup.Identifiable
import no.nav.brev.brevbaker.markup.Markup.TextContainer

@Serializable
sealed class Block : Identifiable {
    abstract override val id: Int

    /**
     * Diskriminator som identifiserer blokk-typen. Ligger som en egen egenskap i modellen (og dermed i
     * JSON-en) slik at konsumenter kan deserialisere polymorft uten spesialoppsett.
     */
    abstract val type: Type

    enum class Type {
        TITLE2,
        TITLE3,
        TITLE4,
        PARAGRAPH,
        ITEM_LIST,
        NUMBERED_LIST,
        TABLE,
        FORM_TEXT,
        FORM_CHOICE,
    }

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("TITLE2")
    data class Title2 @MarkupInternalApi constructor(
        override val id: Int,
        override val content: List<Text>,
    ) : Block(), TextContainer {
        override val type: Type get() = Type.TITLE2
    }

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("TITLE3")
    data class Title3 @MarkupInternalApi constructor(
        override val id: Int,
        override val content: List<Text>,
    ) : Block(), TextContainer {
        override val type: Type get() = Type.TITLE3
    }

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("TITLE4")
    data class Title4 @MarkupInternalApi constructor(
        override val id: Int,
        override val content: List<Text>,
    ) : Block(), TextContainer {
        override val type: Type get() = Type.TITLE4
    }

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("PARAGRAPH")
    data class Paragraph @MarkupInternalApi constructor(
        override val id: Int,
        override val content: List<Text>,
    ) : Block(), TextContainer {
        override val type: Type get() = Type.PARAGRAPH
    }

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("ITEM_LIST")
    data class ItemList @MarkupInternalApi constructor(
        override val id: Int,
        val items: List<Item>,
    ) : Block() {
        override val type: Type get() = Type.ITEM_LIST
    }

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("NUMBERED_LIST")
    data class NumberedList @MarkupInternalApi constructor(
        override val id: Int,
        val items: List<Item>,
    ) : Block() {
        override val type: Type get() = Type.NUMBERED_LIST
    }

    @ConsistentCopyVisibility
    @Serializable
    data class Item @MarkupInternalApi constructor(
        override val id: Int,
        override val content: List<Text>,
    ) : Identifiable, TextContainer

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("TABLE")
    data class Table @MarkupInternalApi constructor(
        override val id: Int,
        val rows: List<Row>,
        val header: Header,
    ) : Block() {
        override val type: Type get() = Type.TABLE


        @ConsistentCopyVisibility
        @Serializable
        data class Row @MarkupInternalApi constructor(
            override val id: Int,
            val cells: List<Cell>,
        ) : Identifiable

        @ConsistentCopyVisibility
        @Serializable
        data class Cell @MarkupInternalApi constructor(
            override val id: Int,
            override val content: List<Text>,
        ) : Identifiable, TextContainer

        @ConsistentCopyVisibility
        @Serializable
        data class Header @MarkupInternalApi constructor(
            override val id: Int,
            val colSpec: List<ColumnSpec>,
        ) : Identifiable

        @ConsistentCopyVisibility
        @Serializable
        data class ColumnSpec @MarkupInternalApi constructor(
            override val id: Int,
            override val content: List<Text>,
            val alignment: ColumnAlignment,
            val span: Int,
        ) : Identifiable, TextContainer

        enum class ColumnAlignment { LEFT, RIGHT }
    }

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("FORM_TEXT")
    data class FormText @MarkupInternalApi constructor(
        override val id: Int,
        override val content: List<Text>,
        val size: Size,
        val vspace: Boolean,
    ) : Block(), TextContainer {
        override val type: Type get() = Type.FORM_TEXT

        enum class Size { NONE, SHORT, LONG, FILL }
    }

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("FORM_CHOICE")
    data class FormChoice @MarkupInternalApi constructor(
        override val id: Int,
        val prompt: List<Text>,
        val choices: List<Choice>,
        val vspace: Boolean,
    ) : Block() {
        override val type: Type get() = Type.FORM_CHOICE

        @ConsistentCopyVisibility
        @Serializable
        data class Choice @MarkupInternalApi constructor(
            override val id: Int,
            override val content: List<Text>,
        ) : Identifiable, TextContainer
    }
}
