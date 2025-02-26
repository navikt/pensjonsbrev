package no.nav.pensjon.brevbaker.api.model

interface LetterMarkup {
    val title: String
    val sakspart: Sakspart
    val blocks: List<Block>
    val signatur: Signatur

    interface Attachment {
        val title: List<ParagraphContent.Text>
        val blocks: List<Block>
        val includeSakspart: Boolean
    }

    interface Sakspart {
        val gjelderNavn: String
        val gjelderFoedselsnummer: String
        val saksnummer: String
        val dokumentDato: String
    }

    interface Signatur {
        val hilsenTekst: String
        val saksbehandlerRolleTekst: String
        val saksbehandlerNavn: String
        val attesterendeSaksbehandlerNavn: String?
        val navAvsenderEnhet: String
    }

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

        interface Title2 : Block {
            val content: List<ParagraphContent.Text>
            override val type: Type
                get() = Type.TITLE2
        }

        interface Paragraph : Block {
            val content: List<ParagraphContent>
            override val type: Type
                get() = Type.PARAGRAPH
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

        sealed interface Text : ParagraphContent {
            val text: String
            val fontType: FontType

            enum class FontType { PLAIN, BOLD, ITALIC }

            interface Literal : Text {
                val tags: Set<ElementTags>
                override val type: Type
                    get() = Type.LITERAL
            }

            interface Variable : Text {
                override val type: Type
                    get() = Type.VARIABLE
            }

            interface NewLine : Text {
                override val type: Type
                    get() = Type.NEW_LINE
            }
        }

        sealed interface Table : ParagraphContent {
            val rows: List<Row>
            val header: Header

            override val type: Type
                get() = Type.TABLE

            interface Row {
                val id: Int
                val cells: List<Cell>
            }

            interface Cell {
                val id: Int
                val text: List<Text>
            }

            interface Header {
                val id: Int
                val colSpec: List<ColumnSpec>
            }

            interface ColumnSpec {
                val id: Int
                val headerContent: Cell
                val alignment: ColumnAlignment
                val span: Int
            }

            enum class ColumnAlignment { LEFT, RIGHT }
        }

        sealed interface Form : ParagraphContent {

            interface Text : Form {
                val prompt: List<ParagraphContent.Text>
                val size: Size
                val vspace: Boolean
                override val type: Type
                    get() = Type.FORM_TEXT

                enum class Size { NONE, SHORT, LONG }
            }

            interface MultipleChoice : Form {
                val prompt: List<ParagraphContent.Text>
                val choices: List<Choice>
                val vspace: Boolean
                override val type: Type
                    get() = Type.FORM_CHOICE

                interface Choice {
                    val id: Int
                    val text: List<ParagraphContent.Text>
                }
            }
        }
    }
}