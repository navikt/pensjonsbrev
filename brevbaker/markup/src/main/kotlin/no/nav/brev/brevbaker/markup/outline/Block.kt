package no.nav.brev.brevbaker.markup.outline

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Et innholdselement på blokk-nivå i brødteksten: overskrifter, avsnitt, lister, tabeller og
 * skjemafelter. Hver blokk har en unik [id] og en [type] for enkel oppslag uten `is`-sjekk.
 */
@Serializable
sealed class Block {
    abstract val id: Int
    abstract val type: Type

    enum class Type {
        TITLE2, TITLE3, TITLE4, PARAGRAPH, ITEM_LIST, NUMBERED_LIST, TABLE, FORM_TEXT, FORM_CHOICE,
    }

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("TITLE2")
    data class Title2 internal constructor(
        override val id: Int,
        val content: List<Text>,
    ) : Block() {
        override val type: Type get() = Type.TITLE2
    }

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("TITLE3")
    data class Title3 internal constructor(
        override val id: Int,
        val content: List<Text>,
    ) : Block() {
        override val type: Type get() = Type.TITLE3
    }

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("TITLE4")
    data class Title4 internal constructor(
        override val id: Int,
        val content: List<Text>,
    ) : Block() {
        override val type: Type get() = Type.TITLE4
    }

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("PARAGRAPH")
    data class Paragraph internal constructor(
        override val id: Int,
        val content: List<Text>,
    ) : Block() {
        override val type: Type get() = Type.PARAGRAPH
    }

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("ITEM_LIST")
    data class ItemList internal constructor(
        override val id: Int,
        val items: List<Item>,
    ) : Block() {
        override val type: Type get() = Type.ITEM_LIST
    }

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("NUMBERED_LIST")
    data class NumberedList internal constructor(
        override val id: Int,
        val items: List<Item>,
    ) : Block() {
        override val type: Type get() = Type.NUMBERED_LIST
    }

    @ConsistentCopyVisibility
    @Serializable
    data class Item internal constructor(
        val id: Int,
        val content: List<Text>,
    )

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("TABLE")
    data class Table internal constructor(
        override val id: Int,
        val rows: List<Row>,
        val header: Header,
    ) : Block() {
        override val type: Type get() = Type.TABLE

        @ConsistentCopyVisibility
        @Serializable
        data class Row internal constructor(
            val id: Int,
            val cells: List<Cell>,
        )

        @ConsistentCopyVisibility
        @Serializable
        data class Cell internal constructor(
            val id: Int,
            val text: List<Text>,
        )

        @ConsistentCopyVisibility
        @Serializable
        data class Header internal constructor(
            val id: Int,
            val colSpec: List<ColumnSpec>,
        )

        @ConsistentCopyVisibility
        @Serializable
        data class ColumnSpec internal constructor(
            val id: Int,
            val headerContent: Cell,
            val alignment: ColumnAlignment,
            val span: Int,
        )

        enum class ColumnAlignment { LEFT, RIGHT }
    }

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("FORM_TEXT")
    data class FormText internal constructor(
        override val id: Int,
        val prompt: List<Text>,
        val size: Size,
        val vspace: Boolean,
    ) : Block() {
        override val type: Type get() = Type.FORM_TEXT

        enum class Size { NONE, SHORT, LONG, FILL }
    }

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("FORM_CHOICE")
    data class FormChoice internal constructor(
        override val id: Int,
        val prompt: List<Text>,
        val choices: List<Choice>,
        val vspace: Boolean,
    ) : Block() {
        override val type: Type get() = Type.FORM_CHOICE

        @ConsistentCopyVisibility
        @Serializable
        data class Choice internal constructor(
            val id: Int,
            val text: List<Text>,
        )
    }
}
