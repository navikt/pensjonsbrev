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
        val edited = editedAttachment(deleted = listOf(1, 2)) {
            title1(id = 3) { literal(id = 31, text = "Egen tittel", editedText = "Egen tittel redigert") }
        }

        val result = edited.updateEditedAttachment(rendered)

        assertEquals(listOf(1), result.deletedBlocks)
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

    @Test
    fun `blocks that are new or changed since previous render are marked newFromTemplate`() {
        val previous = attachment(
            ParagraphImpl(1, true, listOf(LiteralImpl(11, "Uendret"))),
            ParagraphImpl(2, true, listOf(VariableImpl(21, "100 kr"))),
        )
        val next = attachment(
            ParagraphImpl(1, true, listOf(LiteralImpl(11, "Uendret"))),
            ParagraphImpl(3, true, listOf(LiteralImpl(31, "Ny før"))),
            ParagraphImpl(2, true, listOf(VariableImpl(21, "200 kr"))),
            ParagraphImpl(4, true, listOf(LiteralImpl(41, "Ny etter"))),
        )

        val result = previous.toEdit().updateEditedAttachment(next, previousRender = previous.toEdit())

        assertEquals(next.toEdit().blocks.map { it.id to it.content }, result.blocks.map { it.id to it.content })
        assertEquals(listOf(false, true, true, true), result.blocks.map { it.newFromTemplate })
    }

    @Test
    fun `additional identical attachment block occurrences are marked newFromTemplate`() {
        val paragraph = ParagraphImpl(1, true, listOf(LiteralImpl(11, "Repeated")))
        val previous = attachment(paragraph)
        val next = attachment(paragraph, paragraph)

        val result = previous.toEdit().updateEditedAttachment(next, previousRender = previous.toEdit())

        assertEquals(listOf(false, true), result.blocks.map { it.newFromTemplate })
        assertEquals(result, result.updateEditedAttachment(next, previousRender = next.toEdit()))
    }

    @Test
    fun `edited block changed since previous render keeps edits and is marked newFromTemplate`() {
        val previous = attachment(ParagraphImpl(1, true, listOf(LiteralImpl(11, "Du får "), VariableImpl(12, "100 kr"))))
        val next = attachment(ParagraphImpl(1, true, listOf(LiteralImpl(11, "Du får "), VariableImpl(12, "200 kr"))))
        val edited = editedAttachment {
            paragraph(id = 1) {
                literal(id = 11, text = "Du får ", editedText = "Du mottar ")
                variable(id = 12, text = "100 kr")
            }
        }
        val expected = editedAttachment {
            paragraph(id = 1, newFromTemplate = true) {
                literal(id = 11, text = "Du får ", editedText = "Du mottar ")
                variable(id = 12, text = "200 kr")
            }
        }

        assertEquals(expected, edited.updateEditedAttachment(next, previousRender = previous.toEdit()))
    }

    @Test
    fun `newFromTemplate is kept on subsequent merges without changes in template`() {
        val rendered = attachment(
            ParagraphImpl(1, true, listOf(LiteralImpl(11, "Uendret"))),
            ParagraphImpl(2, true, listOf(LiteralImpl(21, "Var ny"))),
        )
        val edited = editedAttachment {
            paragraph(id = 1) { literal(id = 11, text = "Uendret") }
            paragraph(id = 2, newFromTemplate = true) { literal(id = 21, text = "Var ny", editedText = "Redigert") }
        }

        assertEquals(edited, edited.updateEditedAttachment(rendered, previousRender = rendered.toEdit()))
        assertEquals(edited, edited.updateEditedAttachment(rendered))
    }

    @Test
    fun `nothing is marked newFromTemplate when render is unchanged or previous render is unknown`() {
        val previous = attachment(ParagraphImpl(1, true, listOf(LiteralImpl(11, "Uendret"))))
        val next = attachment(
            ParagraphImpl(1, true, listOf(LiteralImpl(11, "Uendret"))),
            ParagraphImpl(2, true, listOf(LiteralImpl(21, "Ny"))),
        )
        val edited = previous.toEdit()

        assertEquals(edited, edited.updateEditedAttachment(previous, previousRender = previous.toEdit()))
        assertEquals(next.toEdit(), edited.updateEditedAttachment(next, previousRender = null))
    }
}
