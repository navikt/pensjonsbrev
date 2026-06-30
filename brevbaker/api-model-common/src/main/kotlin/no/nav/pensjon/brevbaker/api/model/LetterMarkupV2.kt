package no.nav.pensjon.brevbaker.api.model

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Foedselsnummer
import java.time.LocalDate

interface LetterMarkupWithDataUsageV2 {
    val markup: LetterMarkupV2
    val letterDataUsage: Set<Property>
    val brevtype: LetterMetadata.Brevtype

    interface Property {
        val typeName: String
        val propertyName: String
    }
}

interface LetterMarkupV2 {
    val title1: List<Text>
    val sakspart: Sakspart
    val blocks: List<Block>
    val signatur: Signatur

    interface Attachment : AttachmentTitleV2 {
        override val title: List<Text>
        val blocks: List<Block>
        val includeSakspart: Boolean
    }

    interface Sakspart {
        val gjelderNavn: String
        val gjelderFoedselsnummer: Foedselsnummer
        val annenMottakerNavn: String?
        val saksnummer: String
        val dokumentDato: LocalDate
    }

    interface Signatur {
        val hilsenTekst: String
        val saksbehandlerSignatur: SaksbehandlerSignatur?
        val navAvsenderEnhet: String
    }

    interface SaksbehandlerSignatur {
        val saksbehandlerNavn: String
        val attesterendeSaksbehandlerNavn: String?
    }

    sealed interface Block {
        val id: Int
        val type: Type

        enum class Type {
            TITLE2, TITLE3, TITLE4, PARAGRAPH, ITEM_LIST, NUMBERED_LIST, TABLE,
        }

        sealed interface Title : Block {
            val content: List<Text>

            interface Title2 : Title {
                override val type: Type get() = Type.TITLE2
            }

            interface Title3 : Title {
                override val type: Type get() = Type.TITLE3
            }

            interface Title4 : Title {
                override val type: Type get() = Type.TITLE4
            }
        }

        interface Paragraph : Block {
            val content: List<Text>
            override val type: Type
                get() = Type.PARAGRAPH
        }

        sealed interface ListContent : Block {
            val items: List<Item>

            interface Item {
                val id: Int
                val content: List<Text>
            }

            interface ItemList : ListContent {
                override val type: Type get() = Type.ITEM_LIST
            }

            interface NumberedList : ListContent {
                override val type: Type get() = Type.NUMBERED_LIST
            }
        }

        interface Table : Block {
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
    }

    sealed interface Text {
        val id: Int
        val type: Type
        enum class Type { LITERAL, VARIABLE, NEW_LINE }
        val text: String
        val fontType: FontType

        enum class FontType { PLAIN, BOLD, ITALIC }

        interface Literal : Text {
            val tags: Set<ElementTags>
            override val type: Type
                get() = Type.LITERAL
        }

        interface Variable : Text {
            val tags: Set<ElementTags>
            override val type: Type
                get() = Type.VARIABLE
        }

        interface NewLine : Text {
            override val type: Type
                get() = Type.NEW_LINE
        }
    }
}