package no.nav.pensjon.brevbaker.api.model

import no.nav.pensjon.brev.template.InvalidListDeclarationException
import no.nav.pensjon.brev.template.InvalidTableDeclarationException

interface LetterStructure {
    interface Attachment

    interface Sakspart

    interface Signatur

    sealed interface Block {
        interface Title1 : Block
        interface Title2 : Block
        interface Paragraph : Block
    }

    /*sealed*/ interface ParagraphContent {
        fun validate() {}

        interface ItemList : ParagraphContent {
            fun listItems(): List<Item>
            override fun validate() {
                if (listItems().isEmpty()) throw InvalidListDeclarationException("List has no items")
            }

            interface Item
        }

        /*sealed*/ interface Text : ParagraphContent {
            interface Literal : Text
            interface Variable : Text
            interface NewLine : Text
        }

        interface Table : ParagraphContent {
            fun listRows(): List<Row>
            val header: Header

            interface Row {
                val cells: List<Cell>

                fun validate() {
                    if (cells.isEmpty()) {
                        throw InvalidTableDeclarationException("Rows need at least one cell")
                    }
                }
            }
            interface Cell
            interface Header {
                val colSpec: List<ColumnSpec>

                fun validate() {
                    if (colSpec.isEmpty()) {
                        throw InvalidTableDeclarationException("Table column specification needs at least one column")
                    }
                }
            }
            interface ColumnSpec

            override fun validate() {
                if (listRows().isEmpty()) {
                    throw InvalidTableDeclarationException("A table must have at least one row")
                }
                listRows().map { it.cells.size }.filterNot { it == header.colSpec.size }.map {
                    throw InvalidTableDeclarationException("The number of cells in the row($it) does not match the number of InfoCmp.Capability.columns in the specification(${header.colSpec.size})")
                }
                listRows().forEach { it.validate() }
                header.validate()
            }
        }

        /*sealed*/ interface Form : ParagraphContent {
            interface Text : Form
            interface MultipleChoice : Form {
                interface Choice
            }
        }
    }
}