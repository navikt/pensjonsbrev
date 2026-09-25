package no.nav.brev.brevbaker.markup.outline

import no.nav.brev.brevbaker.markup.Markup.Identifiable
import no.nav.brev.brevbaker.markup.Markup.TextContainer
import java.util.Objects

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

    class Title2 internal constructor(
        override val id: Int,
        override val content: List<Text>,
    ) : Block(), TextContainer {
        override val type: Type get() = Type.TITLE2

        internal fun copy(id: Int = this.id, content: List<Text> = this.content): Title2 = Title2(id, content)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Title2) return false
            return id == other.id && content == other.content
        }

        override fun hashCode(): Int = Objects.hash(id, content)
        override fun toString(): String = "Title2(id=$id, content=$content)"
    }

    class Title3 internal constructor(
        override val id: Int,
        override val content: List<Text>,
    ) : Block(), TextContainer {
        override val type: Type get() = Type.TITLE3

        internal fun copy(id: Int = this.id, content: List<Text> = this.content): Title3 = Title3(id, content)

        override fun equals(other: Any?) = other is Title3 && id == other.id && content == other.content
        override fun hashCode() = Objects.hash(id, content)
        override fun toString(): String = "Title3(id=$id, content=$content)"
    }

    class Title4 internal constructor(
        override val id: Int,
        override val content: List<Text>,
    ) : Block(), TextContainer {
        override val type: Type get() = Type.TITLE4

        internal fun copy(id: Int = this.id, content: List<Text> = this.content): Title4 = Title4(id, content)

        override fun equals(other: Any?) = other is Title4 && id == other.id && content == other.content
        override fun hashCode() = Objects.hash(id, content)
        override fun toString(): String = "Title4(id=$id, content=$content)"
    }

    class Paragraph internal constructor(
        override val id: Int,
        override val content: List<Text>,
    ) : Block(), TextContainer {
        override val type: Type get() = Type.PARAGRAPH

        internal fun copy(id: Int = this.id, content: List<Text> = this.content): Paragraph = Paragraph(id, content)

        override fun equals(other: Any?) = other is Paragraph && id == other.id && content == other.content
        override fun hashCode() = Objects.hash(id, content)
        override fun toString(): String = "Paragraph(id=$id, content=$content)"
    }

    class ItemList internal constructor(
        override val id: Int,
        val items: List<Item>,
    ) : Block() {
        override val type: Type get() = Type.ITEM_LIST

        internal fun copy(id: Int = this.id, items: List<Item> = this.items): ItemList = ItemList(id, items)

        override fun equals(other: Any?) = other is ItemList && id == other.id && items == other.items
        override fun hashCode() = Objects.hash(id, items)
        override fun toString(): String = "ItemList(id=$id, items=$items)"
    }

    class NumberedList internal constructor(
        override val id: Int,
        val items: List<Item>,
    ) : Block() {
        override val type: Type get() = Type.NUMBERED_LIST

        internal fun copy(id: Int = this.id, items: List<Item> = this.items): NumberedList = NumberedList(id, items)

        override fun equals(other: Any?) = other is NumberedList && id == other.id && items == other.items
        override fun hashCode() = Objects.hash(id, items)
        override fun toString(): String = "NumberedList(id=$id, items=$items)"
    }

    class Item internal constructor(
        override val id: Int,
        override val content: List<Text>,
    ) : Identifiable, TextContainer {

        internal fun copy(id: Int = this.id, content: List<Text> = this.content): Item = Item(id, content)

        override fun equals(other: Any?) = other is Item && id == other.id && content == other.content
        override fun hashCode() = Objects.hash(id, content)
        override fun toString(): String = "Item(id=$id, content=$content)"
    }

    class Table internal constructor(
        override val id: Int,
        val rows: List<Row>,
        val header: Header,
    ) : Block() {
        override val type: Type get() = Type.TABLE

        internal fun copy(id: Int = this.id, rows: List<Row> = this.rows, header: Header = this.header): Table =
            Table(id, rows, header)

        override fun equals(other: Any?) = other is Table && id == other.id && rows == other.rows
        override fun hashCode() = Objects.hash(id, rows, header)
        override fun toString(): String = "Table(id=$id, rows=$rows, header=$header)"

        class Row internal constructor(
            override val id: Int,
            val cells: List<Cell>,
        ) : Identifiable {
            override fun equals(other: Any?) = other is Row && id == other.id && cells == other.cells
            override fun hashCode() = Objects.hash(id, cells)
            override fun toString(): String = "Row(id=$id, cells=$cells)"
        }

        class Cell internal constructor(
            override val id: Int,
            override val content: List<Text>,
        ) : Identifiable, TextContainer {
            override fun equals(other: Any?) = other is Cell && id == other.id && content == other.content
            override fun hashCode() = Objects.hash(id, content)
            override fun toString(): String = "Cell(id=$id, content=$content)"
        }

        class Header internal constructor(
            override val id: Int,
            val colSpec: List<ColumnSpec>,
        ) : Identifiable {
            override fun equals(other: Any?) = other is Header && id == other.id && colSpec == other.colSpec
            override fun hashCode() = Objects.hash(id, colSpec)
            override fun toString(): String = "Header(id=$id, colSpec=$colSpec)"
        }

        class ColumnSpec internal constructor(
            override val id: Int,
            override val content: List<Text>,
            val alignment: ColumnAlignment,
            val span: Int,
        ) : Identifiable, TextContainer {
            override fun equals(other: Any?) =
                other is ColumnSpec && id == other.id && content == other.content && alignment == other.alignment && span == other.span

            override fun hashCode() = Objects.hash(id, content, alignment, span)
            override fun toString(): String = "ColumnSpec(id=$id, content=$content, alignment=$alignment, span=$span)"
        }

        enum class ColumnAlignment { LEFT, RIGHT }
    }

    class FormText internal constructor(
        override val id: Int,
        override val content: List<Text>,
        val size: Size,
        val vspace: Boolean,
    ) : Block(), TextContainer {
        override val type: Type get() = Type.FORM_TEXT

        enum class Size { NONE, SHORT, LONG, FILL }

        internal fun copy(
            id: Int = this.id,
            content: List<Text> = this.content,
            size: Size = this.size,
            vspace: Boolean = this.vspace,
        ): FormText = FormText(id, content, size, vspace)

        override fun equals(other: Any?) =
            other is FormText && id == other.id && size == other.size && content == other.content && vspace == other.vspace

        override fun hashCode() = Objects.hash(id, content, size, vspace)
        override fun toString(): String = "FormText(id=$id, content=$content, size=$size, vspace=$vspace)"
    }

    class FormChoice internal constructor(
        override val id: Int,
        val prompt: List<Text>,
        val choices: List<Choice>,
        val vspace: Boolean,
    ) : Block() {
        override val type: Type get() = Type.FORM_CHOICE

        internal fun copy(
            id: Int = this.id,
            prompt: List<Text> = this.prompt,
            choices: List<Choice> = this.choices,
            vspace: Boolean = this.vspace,
        ): FormChoice = FormChoice(id, prompt, choices, vspace)

        override fun equals(other: Any?) =
            other is FormChoice && id == other.id && prompt == other.prompt && choices == other.choices && vspace == other.vspace

        override fun hashCode() = Objects.hash(id, prompt, choices, vspace)
        override fun toString() = "FormChoice(id=$id, prompt=$prompt, choices=$choices, vspace=$vspace)"

        class Choice internal constructor(
            override val id: Int,
            override val content: List<Text>,
        ) : Identifiable, TextContainer {
            override fun equals(other: Any?) = other is Choice && id == other.id && content == other.content
            override fun hashCode() = Objects.hash(id, content)
            override fun toString(): String = "Choice(id=$id, content=$content)"
        }
    }
}
