package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Paragraph
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Title1
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Title2
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class EditModelTest {

    @Test
    fun `kan ikke ha tom tittel1-linje`() {
        assertThrows<IllegalArgumentException> {
            EditModelValidator.validate(
                Title1(
                    id = 1,
                    editable = true,
                    content = listOf(
                        Edit.ParagraphContent.Text.Literal(
                            id = 2,
                            text = "hei",
                            editedText = "",
                        )
                    ),
                    deletedContent = emptySet(),
                    originalType = null,
                    parentId = null
                )
            )
        }
    }

    @Test
    fun `kan ikke ha tom tittel1-linje uten editedText`() {
        assertThrows<IllegalArgumentException> {
            EditModelValidator.validate(
                Title1(
                    id = 1,
                    editable = true,
                    content = listOf(
                        Edit.ParagraphContent.Text.Literal(
                            id = 2,
                            text = "",
                            editedText = null,
                        )
                    ),
                    deletedContent = emptySet(),
                    originalType = null,
                    parentId = null
                )
            )
        }
    }


    @Test
    fun `kan ikke ha tom tittel2-linje`() {
        assertThrows<IllegalArgumentException> {
            EditModelValidator.validate(
                Title2(
                    id = 1,
                    editable = true,
                    content = listOf(
                        Edit.ParagraphContent.Text.Literal(
                            id = 2,
                            text = "hei",
                            editedText = "",
                        )
                    ),
                    deletedContent = emptySet(),
                    originalType = null,
                    parentId = null
                )
            )
        }
    }

    @Test
    fun `tolererer tomme avsnitt`() {
        assertDoesNotThrow {
            EditModelValidator.validate(
                Paragraph(
                    id = 1,
                    editable = true,
                    content = listOf(),
                )
            )
        }
    }

    @Disabled("Usikker på om denne skal med")
    @Test
    fun `feiler ved avsnitt med tomt tekstinnhold`() {
        assertThrows<java.lang.IllegalArgumentException> {
            EditModelValidator.validate(
                Paragraph(
                    id = 1,
                    editable = false,
                    content = listOf(
                        Edit.ParagraphContent.Text.Literal(
                            id = 2,
                            text = ""
                        )
                    )
                )
            )
        }
    }

    @Test
    fun `feiler ved to newlines paa rad i starten av avsnittet`() {
        assertThrows<IllegalArgumentException> {
            EditModelValidator.validate(
                Paragraph(
                    id = 1,
                    editable = true,
                    content = listOf(
                        Edit.ParagraphContent.Text.NewLine(2),
                        Edit.ParagraphContent.Text.NewLine(3),
                        Edit.ParagraphContent.Text.Literal(
                            id = 4,
                            text = "hei",
                            editedText = null,
                        )
                    ),
                )
            )
        }
    }

    @Test
    fun `feiler ved to newlines paa rad på slutten av avsnittet`() {
        assertThrows<IllegalArgumentException> {
            EditModelValidator.validate(
                Paragraph(
                    id = 1,
                    editable = true,
                    content = listOf(
                        Edit.ParagraphContent.Text.Literal(
                            id = 2,
                            text = "hei",
                            editedText = null,
                        ),
                        Edit.ParagraphContent.Text.NewLine(2),
                        Edit.ParagraphContent.Text.NewLine(4),
                    ),
                )
            )
        }
    }

}