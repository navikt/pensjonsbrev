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

        override fun equals(other: Any?) = other is Title2 && id == other.id && content == other.content
        override fun hashCode() = Objects.hash(id, content)
        override fun toString() = "Title2(id=$id, content=$content)"

        fun copy(content: List<Text>) = Title2(id = id, content = content)
    }

    class Title3 internal constructor(
        override val id: Int,
        override val content: List<Text>,
    ) : Block(), TextContainer {
        override val type: Type get() = Type.TITLE3

        override fun equals(other: Any?) = other is Title3 && id == other.id && content == other.content
        override fun hashCode() = Objects.hash(id, content)
        override fun toString() = "Title3(id=$id, content=$content)"

        fun copy(content: List<Text>) = Title3(id = id, content = content)
    }

    class Title4 internal constructor(
        override val id: Int,
        override val content: List<Text>,
    ) : Block(), TextContainer {
        override val type: Type get() = Type.TITLE4

        override fun equals(other: Any?) = other is Title4 && id == other.id && content == other.content
        override fun hashCode() = Objects.hash(id, content)
        override fun toString() = "Title4(id=$id, content=$content)"

        fun copy(content: List<Text>) = Title4(id = id, content = content)
    }

    class Paragraph internal constructor(
        override val id: Int,
        override val content: List<Text>,
    ) : Block(), TextContainer {
        override val type: Type get() = Type.PARAGRAPH

        override fun equals(other: Any?) = other is Paragraph && id == other.id && content == other.content
        override fun hashCode() = Objects.hash(id, content)
        override fun toString() = "Paragraph(id=$id, content=$content)"

        fun copy(content: List<Text>) = Paragraph(id = id, content = content)
    }

    class ItemList internal constructor(
        override val id: Int,
        val items: List<Item>,
    ) : Block() {
        override val type: Type get() = Type.ITEM_LIST

        override fun equals(other: Any?) = other is ItemList && id == other.id && items == other.items
        override fun hashCode() = Objects.hash(id, items)
        override fun toString() = "ItemList(id=$id, items=$items)"
    }

    class NumberedList internal constructor(
        override val id: Int,
        val items: List<Item>,
    ) : Block() {
        override val type: Type get() = Type.NUMBERED_LIST

        override fun equals(other: Any?) = other is NumberedList && id == other.id && items == other.items
        override fun hashCode() = Objects.hash(id, items)
        override fun toString() = "NumberedList(id=$id, items=$items)"
    }

    class Item internal constructor(
        override val id: Int,
        override val content: List<Text>,
    ) : Identifiable, TextContainer {
        override fun equals(other: Any?) = other is Item && id == other.id && content == other.content
        override fun hashCode() = Objects.hash(id, content)
        override fun toString() = "Item(id=$id, content=$content)"
    }

    class Table internal constructor(
        override val id: Int,
        val rows: List<Row>,
        val header: Header,
    ) : Block() {
        override val type: Type get() = Type.TABLE

        override fun equals(other: Any?) =
            other is Table && id == other.id && rows == other.rows && header == other.header

        override fun hashCode() = Objects.hash(id, rows, header)
        override fun toString() = "Table(id=$id, rows=$rows, header=$header)"

        class Row internal constructor(
            override val id: Int,
            val cells: List<Cell>,
        ) : Identifiable {
            override fun equals(other: Any?) = other is Row && id == other.id && cells == other.cells
            override fun hashCode() = Objects.hash(id, cells)
            override fun toString() = "Row(id=$id, cells=$cells)"
        }

        class Cell internal constructor(
            override val id: Int,
            override val content: List<Text>,
        ) : Identifiable, TextContainer {
            override fun equals(other: Any?) = other is Cell && id == other.id && content == other.content
            override fun hashCode() = Objects.hash(id, content)
            override fun toString() = "Cell(id=$id, content=$content)"
        }

        class Header internal constructor(
            override val id: Int,
            val colSpec: List<ColumnSpec>,
        ) : Identifiable {
            override fun equals(other: Any?) = other is Header && id == other.id && colSpec == other.colSpec
            override fun hashCode() = Objects.hash(id, colSpec)
            override fun toString() = "Header(id=$id, colSpec=$colSpec)"
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
            override fun toString() = "ColumnSpec(id=$id, content=$content, alignment=$alignment, span=$span)"
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

        override fun equals(other: Any?) =
            other is FormText && id == other.id && content == other.content && size == other.size && vspace == other.vspace

        override fun hashCode() = Objects.hash(id, content, size, vspace)
        override fun toString() = "FormText(id=$id, content=$content, size=$size, vspace=$vspace)"
    }

    class FormChoice internal constructor(
        override val id: Int,
        val prompt: List<Text>,
        val choices: List<Choice>,
        val vspace: Boolean,
    ) : Block() {
        override val type: Type get() = Type.FORM_CHOICE

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
            override fun toString() = "Choice(id=$id, content=$content)"
        }
    }
}
