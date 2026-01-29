package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brevbaker.api.model.ElementTags
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertThrows

class EditModelTest {
    
    @Test
    fun `kan ikke ha fritekst paa variabler`() {
        val variabel = Edit.ParagraphContent.Text.Variable(
            id = 1,
            text = "hei",
            fontType = Edit.ParagraphContent.Text.FontType.PLAIN,
            parentId = 0,
            tags = setOf(ElementTags.FRITEKST)
        )
        assertThrows<IllegalArgumentException> {
            variabel.toMarkup()
        }
    }

    @Test
    fun `kan ha redigerbar data paa variabler`() {
        val variabel = Edit.ParagraphContent.Text.Variable(
            id = 1,
            text = "hei",
            fontType = Edit.ParagraphContent.Text.FontType.PLAIN,
            parentId = 0,
            tags = setOf(ElementTags.REDIGERBAR_DATA)
        )
        assertNotNull(variabel.toMarkup())
    }
}