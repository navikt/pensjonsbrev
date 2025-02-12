package no.nav.pensjon.brevbaker.api.model

@Suppress("unused")
data class LetterMarkup(val title: String, val sakspart: Sakspart, val blocks: List<Block>, val signatur: Signatur) {
    data class Attachment(
        val title: List<ParagraphContent.Text>,
        val blocks: List<Block>,
        val includeSakspart: Boolean,
    )

    data class Sakspart(val gjelderNavn: String, val gjelderFoedselsnummer: String, val saksnummer: String, val dokumentDato: String)

    data class Signatur(
        val hilsenTekst: String,
        val saksbehandlerRolleTekst: String,
        val saksbehandlerNavn: String,
        val attesterendeSaksbehandlerNavn: String?,
        val navAvsenderEnhet: String,
    )

    sealed class Block(open val id: Int, open val type: Type, open val editable: Boolean = true) {
        enum class Type {
            TITLE1,
            TITLE2,
            PARAGRAPH,
        }

        data class Title1(override val id: Int, override val editable: Boolean, val content: List<ParagraphContent.Text>) : Block(id, Type.TITLE1, editable)

        data class Title2(override val id: Int, override val editable: Boolean, val content: List<ParagraphContent.Text>) : Block(id, Type.TITLE2, editable)

        data class Paragraph(override val id: Int, override val editable: Boolean, val content: List<ParagraphContent>) : Block(id, Type.PARAGRAPH, editable)
    }

    sealed class ParagraphContent(open val id: Int, open val type: Type) {
        enum class Type {
            ITEM_LIST,
            LITERAL,
            VARIABLE,
            TABLE,
            FORM_TEXT,
            FORM_CHOICE,
            NEW_LINE,
        }

        data class ItemList(override val id: Int, val items: List<Item>) : ParagraphContent(id, Type.ITEM_LIST) {
            data class Item(val id: Int, val content: List<Text>)
        }

        sealed class Text(id: Int, type: Type) : ParagraphContent(id, type) {
            abstract val text: String
            abstract val fontType: FontType

            enum class FontType { PLAIN, BOLD, ITALIC }

            data class Literal(
                override val id: Int,
                override val text: String,
                override val fontType: FontType = FontType.PLAIN,
                val tags: Set<ElementTags> = emptySet(),
            ) : Text(id, Type.LITERAL)

            data class Variable(override val id: Int, override val text: String, override val fontType: FontType = FontType.PLAIN) : Text(id, Type.VARIABLE)

            data class NewLine(override val id: Int) : Text(id, Type.NEW_LINE) {
                override val fontType = FontType.PLAIN
                override val text: String = ""
            }
        }

        data class Table(override val id: Int, val rows: List<Row>, val header: Header) : ParagraphContent(id, Type.TABLE) {
            data class Row(val id: Int, val cells: List<Cell>)

            data class Cell(val id: Int, val text: List<Text>)

            data class Header(val id: Int, val colSpec: List<ColumnSpec>)

            data class ColumnSpec(val id: Int, val headerContent: Cell, val alignment: ColumnAlignment, val span: Int)

            enum class ColumnAlignment { LEFT, RIGHT }
        }

        sealed class Form(id: Int, type: Type) : ParagraphContent(id, type) {
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
