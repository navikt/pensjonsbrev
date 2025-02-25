package no.nav.pensjon.brevbaker.api.model

import no.nav.brev.InterneDataklasser

@Suppress("unused")
data class LetterMarkup(val title: String, val sakspart: Sakspart, val blocks: List<Block>, val signatur: Signatur) {

    interface Attachment {
        val title: List<ParagraphContent.Text>
        val blocks: List<Block>
        val includeSakspart: Boolean
    }

    @InterneDataklasser
    data class AttachmentImpl(
        override val title: List<ParagraphContent.Text>,
        override val blocks: List<Block>,
        override val includeSakspart: Boolean,
    ) : Attachment

    interface Sakspart {
        val gjelderNavn: String
        val gjelderFoedselsnummer: String
        val saksnummer: String
        val dokumentDato: String
    }

    @InterneDataklasser
    data class SakspartImpl(
        override val gjelderNavn: String,
        override val gjelderFoedselsnummer: String,
        override val saksnummer: String,
        override val dokumentDato: String
    ) : Sakspart

    interface Signatur {
        val hilsenTekst: String
        val saksbehandlerRolleTekst: String
        val saksbehandlerNavn: String
        val attesterendeSaksbehandlerNavn: String?
        val navAvsenderEnhet: String
    }

    @InterneDataklasser
    data class SignaturImpl(
        override val hilsenTekst: String,
        override val saksbehandlerRolleTekst: String,
        override val saksbehandlerNavn: String,
        override val attesterendeSaksbehandlerNavn: String?,
        override val navAvsenderEnhet: String,
    ) : Signatur

    sealed interface Block {
        val id: Int
        val type: Type
        val editable: Boolean

        enum class Type {
            TITLE1, TITLE2, PARAGRAPH,
        }

        interface Title1 : Block {
            val content: List<ParagraphContent.Text>
            override val type: Type
                get() = Type.TITLE1
        }

        @InterneDataklasser
        data class Title1Impl(override val id: Int, override val editable: Boolean = true, override val content: List<ParagraphContent.Text>) : Title1 {
            override val type = Type.TITLE1
        }

        interface Title2 : Block {
            val content: List<ParagraphContent.Text>
            override val type: Type
                get() = Type.TITLE2
        }

        @InterneDataklasser
        data class Title2Impl(override val id: Int, override val editable: Boolean = true, override val content: List<ParagraphContent.Text>) : Title2 {
            override val type = Type.TITLE2
        }

        interface Paragraph : Block {
            val content: List<ParagraphContent>
            override val type: Type
                get() = Type.PARAGRAPH
        }

        @InterneDataklasser
        data class ParagraphImpl(override val id: Int, override val editable: Boolean = true, override val content: List<ParagraphContent>) : Paragraph {
            override val type = Type.PARAGRAPH
        }
    }

    sealed interface ParagraphContent {
        val id: Int
        val type: Type
        enum class Type {
            ITEM_LIST, LITERAL, VARIABLE, TABLE, FORM_TEXT, FORM_CHOICE, NEW_LINE
        }

        interface ItemList : ParagraphContent {
            val items: List<Item>
            interface Item {
                val id: Int
                val content: List<Text>
            }
        }

        @InterneDataklasser
        data class ItemListImpl(override val id: Int, override val items: List<ItemList.Item>) : ItemList {
            override val type = Type.ITEM_LIST

            data class ItemImpl(override val id: Int, override val content: List<Text>) : ItemList.Item
        }

        sealed interface Text : ParagraphContent {
            val text: String
            val fontType: FontType

            enum class FontType { PLAIN, BOLD, ITALIC }

            interface Literal : Text {
                val tags: Set<ElementTags>
                override val type: Type
                    get() = Type.LITERAL
            }

            @InterneDataklasser
            data class LiteralImpl(
                override val id: Int,
                override val text: String,
                override val fontType: FontType = FontType.PLAIN,
                override val tags: Set<ElementTags> = emptySet()
            ) : Literal {
                override val type: Type = Type.LITERAL
            }

            interface Variable : Text {
                override val type: Type
                    get() = Type.VARIABLE
            }

            @InterneDataklasser
            data class VariableImpl(
                override val id: Int,
                override val text: String,
                override val fontType: FontType = FontType.PLAIN
            ) : Variable {
                override val type = Type.VARIABLE
            }

            interface NewLine : Text {
                override val type: Type
                    get() = Type.NEW_LINE
            }

            @InterneDataklasser
            data class NewLineImpl(override val id: Int) : NewLine {
                override val fontType = FontType.PLAIN
                override val text: String = ""
                override val type = Type.NEW_LINE
            }
        }

        data class Table(override val id: Int, val rows: List<Row>, val header: Header) : ParagraphContent {
            override val type = Type.TABLE

            data class Row(val id: Int, val cells: List<Cell>)
            data class Cell(val id: Int, val text: List<Text>)
            data class Header(val id: Int, val colSpec: List<ColumnSpec>)
            data class ColumnSpec(val id: Int, val headerContent: Cell, val alignment: ColumnAlignment, val span: Int)
            enum class ColumnAlignment { LEFT, RIGHT }
        }

        sealed interface Form : ParagraphContent {
            data class Text(override val id: Int, val prompt: List<ParagraphContent.Text>, val size: Size, val vspace: Boolean) : Form {
                override val type = Type.FORM_TEXT
                enum class Size { NONE, SHORT, LONG }
            }

            data class MultipleChoice(override val id: Int, val prompt: List<ParagraphContent.Text>, val choices: List<Choice>, val vspace: Boolean) : Form {
                override val type = Type.FORM_CHOICE
                data class Choice(val id: Int, val text: List<ParagraphContent.Text>)
            }
        }
    }
}
