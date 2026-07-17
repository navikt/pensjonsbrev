package no.nav.brev.brevbaker.markup.outline

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Block {
    abstract val id: Int

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("TITLE2")
    data class Title2 internal constructor(
        override val id: Int,
        val content: List<Text>,
    ) : Block()

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("TITLE3")
    data class Title3 internal constructor(
        override val id: Int,
        val content: List<Text>,
    ) : Block()

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("TITLE4")
    data class Title4 internal constructor(
        override val id: Int,
        val content: List<Text>,
    ) : Block()

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("PARAGRAPH")
    data class Paragraph internal constructor(
        override val id: Int,
        val content: List<Text>,
    ) : Block()

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("ITEM_LIST")
    data class ItemList internal constructor(
        override val id: Int,
        val items: List<Item>,
    ) : Block()

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("NUMBERED_LIST")
    data class NumberedList internal constructor(
        override val id: Int,
        val items: List<Item>,
    ) : Block()

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

        @ConsistentCopyVisibility
        @Serializable
        data class Choice internal constructor(
            val id: Int,
            val text: List<Text>,
        )
    }
}
