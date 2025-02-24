package no.nav.pensjon.brevbaker.api.model

@Suppress("unused")
data class LetterMarkup(val title: String, val sakspart: Sakspart, val blocks: List<Block>, val signatur: Signatur) {

    data class Attachment(
        val title: List<ParagraphContent.Text>,
        val blocks: List<Block>,
        val includeSakspart: Boolean,
    )

    interface Sakspart {
        val gjelderNavn: String
        val gjelderFoedselsnummer: String
        val saksnummer: String
        val dokumentDato: String
    }

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

    data class SignaturImpl(
        override val hilsenTekst: String,
        override val saksbehandlerRolleTekst: String,
        override val saksbehandlerNavn: String,
        override val attesterendeSaksbehandlerNavn: String?,
        override val navAvsenderEnhet: String,
    ) : Signatur

    sealed class Block(open val id: Int, open val type: Type, open val editable: Boolean = true) {
        enum class Type {
            TITLE1, TITLE2, PARAGRAPH,
        }

        data class Title1(override val id: Int, override val editable: Boolean, val content: List<ParagraphContent.Text>) : Block(id, Type.TITLE1, editable)
        data class Title2(override val id: Int, override val editable: Boolean, val content: List<ParagraphContent.Text>) : Block(id, Type.TITLE2, editable)
        data class Paragraph(override val id: Int, override val editable: Boolean, val content: List<ParagraphContent>) : Block(id, Type.PARAGRAPH, editable)
    }

    sealed interface ParagraphContent {
        val id: Int
        val type: Type
        enum class Type {
            ITEM_LIST, LITERAL, VARIABLE, TABLE, FORM_TEXT, FORM_CHOICE, NEW_LINE
        }

        data class ItemList(override val id: Int, val items: List<Item>) : ParagraphContent {
            override val type = Type.ITEM_LIST

            data class Item(val id: Int, val content: List<Text>)
        }

        sealed interface Text : ParagraphContent {
            val text: String
            val fontType: FontType

            enum class FontType { PLAIN, BOLD, ITALIC }

            data class Literal(
                override val id: Int,
                override val text: String,
                override val fontType: FontType = FontType.PLAIN,
                val tags: Set<ElementTags> = emptySet(),
            ) : Text {
                override val type = Type.LITERAL
            }

            data class Variable(
                override val id: Int,
                override val text: String,
                override val fontType: FontType = FontType.PLAIN
            ) : Text {
                override val type = Type.VARIABLE
            }

            data class NewLine(override val id: Int) : Text {
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

        sealed class Form(override val id: Int, override val type: Type) : ParagraphContent {
            data class Text(override val id: Int, val prompt: List<ParagraphContent.Text>, val size: Size, val vspace: Boolean) : Form(id, Type.FORM_TEXT) {
                enum class Size { NONE, SHORT, LONG }
            }

            data class MultipleChoice(override val id: Int, val prompt: List<ParagraphContent.Text>, val choices: List<Choice>, val vspace: Boolean) :
                Form(id, Type.FORM_CHOICE) {
                data class Choice(val id: Int, val text: List<ParagraphContent.Text>)
            }
        }
    }
}
