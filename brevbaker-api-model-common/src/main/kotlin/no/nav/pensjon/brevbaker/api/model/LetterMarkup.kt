package no.nav.pensjon.brevbaker.api.model

interface LetterMarkup {
    val title: String
    val sakspart: Sakspart
    val blocks: List<Block>
    val signatur: Signatur

    fun validate() {
        // Åpner bevisst for tom tittel her, med etterlatte sine redigerbare deler har de med vilje tom tittel
        // require(title.isNotEmpty()) { "Title cannot be empty" }
        blocks.forEach { it.validate() }
    }

    interface Attachment : LetterStructure.Attachment {
        val title: List<ParagraphContent.Text>
        val blocks: List<Block>
        val includeSakspart: Boolean
    }

    interface Sakspart : LetterStructure.Sakspart {
        val gjelderNavn: String
        val gjelderFoedselsnummer: String
        val saksnummer: String
        val dokumentDato: String
    }

    interface Signatur : LetterStructure.Signatur {
        val hilsenTekst: String
        val saksbehandlerRolleTekst: String
        val saksbehandlerNavn: String
        val attesterendeSaksbehandlerNavn: String?
        val navAvsenderEnhet: String
    }

    sealed interface Block : LetterStructure.Block {
        val id: Int
        val type: Type
        val editable: Boolean

        enum class Type {
            TITLE1, TITLE2, PARAGRAPH,
        }

        interface Title1 : Block, LetterStructure.Block.Title1 {
            val content: List<ParagraphContent.Text>
            override val type: Type
                get() = Type.TITLE1
        }

        interface Title2 : Block, LetterStructure.Block.Title2 {
            val content: List<ParagraphContent.Text>
            override val type: Type
                get() = Type.TITLE2
        }

        interface Paragraph : Block, LetterStructure.Block.Paragraph {
            val content: List<ParagraphContent>
            override val type: Type
                get() = Type.PARAGRAPH

            override fun validate() = content.forEach { it.validate() }
        }

        fun validate() {}
    }

    sealed interface ParagraphContent : LetterStructure.ParagraphContent {
        val id: Int
        val type: Type
        enum class Type {
            ITEM_LIST, LITERAL, VARIABLE, TABLE, FORM_TEXT, FORM_CHOICE, NEW_LINE
        }

        fun validate() {}

        interface ItemList : ParagraphContent, LetterStructure.ParagraphContent.ItemList {
            val items: List<Item>
            interface Item {
                val id: Int
                val content: List<Text>
            }

            override fun validate() = require(items.isNotEmpty()) { "Items must not be empty" }
        }

        sealed interface Text : ParagraphContent, LetterStructure.ParagraphContent.Text {
            val text: String
            val fontType: FontType

            enum class FontType { PLAIN, BOLD, ITALIC }

            interface Literal : Text, LetterStructure.ParagraphContent.Text.Literal {
                val tags: Set<ElementTags>
                override val type: Type
                    get() = Type.LITERAL
            }

            interface Variable : Text, LetterStructure.ParagraphContent.Text.Variable {
                override val type: Type
                    get() = Type.VARIABLE
            }

            interface NewLine : Text, LetterStructure.ParagraphContent.Text.NewLine {
                override val type: Type
                    get() = Type.NEW_LINE
            }
        }

        interface Table : ParagraphContent, LetterStructure.ParagraphContent.Table {
            val rows: List<Row>
            val header: Header

            override fun validate() {
                require(rows.isNotEmpty()) { "Must have at least one row" }
                require(rows.map { it.cells.size }.all { it == header.colSpec.size }) { "Must have at least one row" }
                rows.forEach { it.validate() }
                header.validate()
            }

            override val type: Type
                get() = Type.TABLE

            interface Row : LetterStructure.ParagraphContent.Table.Row {
                val id: Int
                val cells: List<Cell>

                fun validate() {
                    require(cells.isNotEmpty()) { "Must have at least one cell" }
                }
            }

            interface Cell : LetterStructure.ParagraphContent.Table.Cell {
                val id: Int
                val text: List<Text>
            }

            interface Header : LetterStructure.ParagraphContent.Table.Header {
                val id: Int
                val colSpec: List<ColumnSpec>

                fun validate() = require(colSpec.isNotEmpty()) { "Must have at least one header" }
            }

            interface ColumnSpec : LetterStructure.ParagraphContent.Table.ColumnSpec {
                val id: Int
                val headerContent: Cell
                val alignment: ColumnAlignment
                val span: Int
            }

            enum class ColumnAlignment { LEFT, RIGHT }
        }

        sealed interface Form : ParagraphContent, LetterStructure.ParagraphContent.Form {

            interface Text : Form, LetterStructure.ParagraphContent.Form.Text {
                val prompt: List<ParagraphContent.Text>
                val size: Size
                val vspace: Boolean
                override val type: Type
                    get() = Type.FORM_TEXT

                enum class Size { NONE, SHORT, LONG }
            }

            interface MultipleChoice : Form, LetterStructure.ParagraphContent.Form.MultipleChoice {
                val prompt: List<ParagraphContent.Text>
                val choices: List<Choice>
                val vspace: Boolean
                override val type: Type
                    get() = Type.FORM_CHOICE

                interface Choice : LetterStructure.ParagraphContent.Form.MultipleChoice.Choice {
                    val id: Int
                    val text: List<ParagraphContent.Text>
                }
            }
        }
    }
}