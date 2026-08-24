package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.Title1Impl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.VariableImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class UpdateEditedAttachmentTest {

    @Test
    fun `no edits and no template changes returns the same attachment`() {
        val rendered = attachment(ParagraphImpl(1, true, listOf(LiteralImpl(11, "Noe tekst"))))
        val edited = rendered.toEdit()

        assertEquals(edited, edited.updateEditedAttachment(rendered))
    }

    @Test
    fun `new block from template is included`() {
        val edited = attachment(ParagraphImpl(1, true, listOf(LiteralImpl(11, "Foerste")))).toEdit()
        val rendered = attachment(
            ParagraphImpl(1, true, listOf(LiteralImpl(11, "Foerste"))),
            ParagraphImpl(2, true, listOf(LiteralImpl(21, "Andre"))),
        )

        assertEquals(rendered.toEdit(), edited.updateEditedAttachment(rendered))
    }

    @Test
    fun `edited text is preserved when template block still exists`() {
        val rendered = attachment(ParagraphImpl(1, true, listOf(LiteralImpl(11, "Mal-tekst"))))
        val edited = editedAttachment {
            paragraph(id = 1) { literal(id = 11, text = "Mal-tekst", editedText = "Saksbehandlers tekst") }
        }

        val result = edited.updateEditedAttachment(rendered)

        val literal = (result.blocks.single() as Edit.Block.Paragraph).content.single() as Edit.ParagraphContent.Text.Literal
        assertEquals("Saksbehandlers tekst", literal.editedText)
    }

    @Test
    fun `edited block missing from template is kept and marked missingFromTemplate`() {
        val rendered = attachment(ParagraphImpl(2, true, listOf(LiteralImpl(21, "Andre"))))
        val edited = editedAttachment {
            paragraph(id = 1) { literal(id = 11, text = "Foerste", editedText = "Foerste redigert") }
            paragraph(id = 2) { literal(id = 21, text = "Andre") }
        }

        val result = edited.updateEditedAttachment(rendered)

        assertEquals(2, result.blocks.size)
        assertTrue(result.blocks.first().missingFromTemplate == true)
    }

    @Test
    fun `variable values are refreshed from the template render`() {
        val rendered = attachment(
            ParagraphImpl(1, true, listOf(LiteralImpl(11, "Belop: "), VariableImpl(12, "2000"))),
            ParagraphImpl(2, true, listOf(LiteralImpl(21, "Fjernet fra mal"))),
        )
        val edited = editedAttachment {
            paragraph(id = 1) {
                literal(id = 11, text = "Belop: ")
                variable(id = 12, text = "1000")
            }
            paragraph(id = 2) { literal(id = 21, text = "Fjernet fra mal", editedText = "Redigert") }
        }

        val result = edited.updateEditedAttachment(rendered)

        val variable = (result.blocks.first() as Edit.Block.Paragraph).content.last() as Edit.ParagraphContent.Text.Variable
        assertEquals("2000", variable.text)
    }

    @Test
    fun `title is merged from the template render`() {
        val rendered = attachment(ParagraphImpl(1, true, listOf(LiteralImpl(11, "Tekst"))), title = "Ny tittel")
        val edited = attachment(ParagraphImpl(1, true, listOf(LiteralImpl(11, "Tekst"))), title = "Gammel tittel").toEdit()

        val result = edited.updateEditedAttachment(rendered)

        assertEquals("Ny tittel", (result.title.text.single() as Edit.ParagraphContent.Text.Literal).text)
    }

    @Test
    fun `includeSakspart is taken from the template render`() {
        val rendered = attachment(ParagraphImpl(1, true, listOf(LiteralImpl(11, "Tekst"))), includeSakspart = true)
        val edited = editedAttachment(includeSakspart = false) {
            paragraph(id = 1) { literal(id = 11, text = "Tekst") }
        }

        assertTrue(edited.updateEditedAttachment(rendered).includeSakspart)
    }

    @Test
    fun `deletedBlocks are filtered against blocks still present in the template`() {
        val rendered = attachment(ParagraphImpl(1, true, listOf(LiteralImpl(11, "Beholdt"))))
        val edited = editedAttachment(deleted = setOf(1, 2)) {
            title1(id = 3) { literal(id = 31, text = "Egen tittel", editedText = "Egen tittel redigert") }
        }

        val result = edited.updateEditedAttachment(rendered)

        assertEquals(setOf(1), result.deletedBlocks)
    }

    @Test
    fun `new block added by saksbehandler is kept`() {
        val rendered = attachment(ParagraphImpl(1, true, listOf(LiteralImpl(11, "Mal"))))
        val edited = editedAttachment {
            paragraph(id = 1) { literal(id = 11, text = "Mal") }
            paragraph(id = null) { literal(id = null, text = "Nytt avsnitt") }
        }

        val result = edited.updateEditedAttachment(rendered)

        assertEquals(2, result.blocks.size)
        assertEquals(null, result.blocks.last().id)
    }

    @Test
    fun `title1 block from template is merged`() {
        val rendered = attachment(
            Title1Impl(1, true, listOf(LiteralImpl(11, "Ny overskrift"))),
            ParagraphImpl(2, true, listOf(LiteralImpl(21, "Tekst"))),
        )
        val edited = editedAttachment {
            paragraph(id = 2) { literal(id = 21, text = "Tekst") }
        }

        assertEquals(rendered.toEdit(), edited.updateEditedAttachment(rendered))
    }
}
