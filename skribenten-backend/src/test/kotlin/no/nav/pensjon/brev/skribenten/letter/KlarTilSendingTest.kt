package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Paragraph
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Literal
import no.nav.pensjon.brevbaker.api.model.ElementTags
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class KlarTilSendingTest {

    private val fritekst = setOf(ElementTags.FRITEKST)

    @Test
    fun `tomt brev er klar til sending`() {
        assertTrue(editedLetter().klarTilSending())
    }

    @Test
    fun `brev uten fritekst er klar til sending`() {
        val letter = editedLetter(Paragraph(null, true, listOf(Literal(null, "lit1"))))
        assertTrue(letter.klarTilSending())
    }

    @Test
    fun `brev med redigert fritekst er klar til sending`() {
        val letter = editedLetter(Paragraph(null, true, listOf(Literal(null, "lit1", editedText = "hei", tags = fritekst))))
        assertTrue(letter.klarTilSending())
    }

    @Test
    fun `brev med ikke redigert fritekst er ikke klar til sending`() {
        val letter = editedLetter(Paragraph(null, true, listOf(Literal(null, "lit1", tags = fritekst), Literal(null, "lit2"))))
        assertFalse(letter.klarTilSending())
    }

}