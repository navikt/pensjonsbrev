package no.nav.pensjon.brevbaker.api.model

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
        interface ItemList : ParagraphContent {
            interface Item
        }

        /*sealed*/ interface Text : ParagraphContent {
            interface Literal : Text
            interface Variable : Text
            interface NewLine : Text
        }

        interface Table {
            interface Row
            interface Cell
            interface Header
            interface ColumnSpec
        }

        /*sealed*/ interface Form : ParagraphContent {
            interface Text : Form
            interface MultipleChoice : Form {
                interface Choice
            }
        }
    }
}