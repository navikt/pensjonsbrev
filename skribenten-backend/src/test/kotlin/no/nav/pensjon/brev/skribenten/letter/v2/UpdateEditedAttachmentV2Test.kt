package no.nav.pensjon.brev.skribenten.letter.v2

import no.nav.pensjon.brev.skribenten.letter.v2.MarkupV2.literal
import no.nav.pensjon.brev.skribenten.letter.v2.MarkupV2.paragraph
import no.nav.pensjon.brev.skribenten.letter.v2.MarkupV2.title2
import no.nav.pensjon.brev.skribenten.letter.v2.MarkupV2.variable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class UpdateEditedAttachmentV2Test {

    @Test
    fun `no edits and no template changes returns the same attachment`() {
        val rendered = attachment(paragraph(1, literal(11, "Noe tekst")))
        val edited = EditV2.Attachment.from(rendered)

        assertEquals(edited, edited.merge(rendered))
    }

    @Test
    fun `new block from template is included`() {
        val edited = EditV2.Attachment.from(attachment(paragraph(1, literal(11, "Foerste"))))
        val rendered = attachment(
            paragraph(1, literal(11, "Foerste")),
            paragraph(2, literal(21, "Andre")),
        )

        assertEquals(EditV2.Attachment.from(rendered), edited.merge(rendered))
    }

    @Test
    fun `edited text is preserved when template block still exists`() {
        val rendered = attachment(paragraph(1, literal(11, "Mal-tekst")))
        val edited = editedAttachment {
            paragraph(id = 1) { literal(id = 11, text = "Mal-tekst", editedText = "Saksbehandlers tekst") }
        }

        val result = edited.merge(rendered)

        val literal = (result.blocks.single() as EditV2.Block.Paragraph).content.single() as EditV2.Text.Literal
        assertEquals("Saksbehandlers tekst", literal.editedText)
    }

    @Test
    fun `edited block missing from template is kept and marked missingFromTemplate`() {
        val rendered = attachment(paragraph(2, literal(21, "Andre")))
        val edited = editedAttachment {
            paragraph(id = 1) { literal(id = 11, text = "Foerste", editedText = "Foerste redigert") }
            paragraph(id = 2) { literal(id = 21, text = "Andre") }
        }

        val result = edited.merge(rendered)

        assertEquals(2, result.blocks.size)
        assertTrue(result.blocks.first().missingFromTemplate)
    }

    @Test
    fun `variable values are refreshed from the template render`() {
        val rendered = attachment(
            paragraph(1, literal(11, "Belop: "), variable(12, "2000")),
            paragraph(2, literal(21, "Fjernet fra mal")),
        )
        val edited = editedAttachment {
            paragraph(id = 1) {
                literal(id = 11, text = "Belop: ")
                variable(id = 12, text = "1000")
            }
            paragraph(id = 2) { literal(id = 21, text = "Fjernet fra mal", editedText = "Redigert") }
        }

        val result = edited.merge(rendered)

        val variable = (result.blocks.first() as EditV2.Block.Paragraph).content.last() as EditV2.Text.Variable
        assertEquals("2000", variable.text)
    }

    @Test
    fun `title is merged from the template render`() {
        val rendered = attachment(paragraph(1, literal(11, "Tekst")), title = "Ny tittel")
        val edited = EditV2.Attachment.from(attachment(paragraph(1, literal(11, "Tekst")), title = "Gammel tittel"))

        val result = edited.merge(rendered)

        assertEquals("Ny tittel", (result.title.text.single() as EditV2.Text.Literal).text)
    }

    @Test
    fun `inkluderSaksinformasjon is taken from the template render`() {
        val rendered = attachment(paragraph(1, literal(11, "Tekst")), inkluderSaksinformasjon = true)
        val edited = editedAttachment(inkluderSaksinformasjon = false) {
            paragraph(id = 1) { literal(id = 11, text = "Tekst") }
        }

        assertTrue(edited.merge(rendered).inkluderSaksinformasjon)
    }

    @Test
    fun `deletedBlocks are filtered against blocks still present in the template`() {
        val rendered = attachment(paragraph(1, literal(11, "Beholdt")))
        val edited = editedAttachment(deleted = listOf(1, 2)) {
            title2(id = 3) { literal(id = 31, text = "Egen tittel", editedText = "Egen tittel redigert") }
        }

        val result = edited.merge(rendered)

        assertEquals(listOf(1), result.deletedBlocks)
    }

    @Test
    fun `new block added by saksbehandler is kept`() {
        val rendered = attachment(paragraph(1, literal(11, "Mal")))
        val edited = editedAttachment {
            paragraph(id = 1) { literal(id = 11, text = "Mal") }
            paragraph(id = null) { literal(id = null, text = "Nytt avsnitt") }
        }

        val result = edited.merge(rendered)

        assertEquals(2, result.blocks.size)
        assertEquals(null, result.blocks.last().id)
    }

    @Test
    fun `title2 block from template is merged`() {
        val rendered = attachment(
            title2(1, literal(11, "Ny overskrift")),
            paragraph(2, literal(21, "Tekst")),
        )
        val edited = editedAttachment {
            paragraph(id = 2) { literal(id = 21, text = "Tekst") }
        }

        assertEquals(EditV2.Attachment.from(rendered), edited.merge(rendered))
    }
}
