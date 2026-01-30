package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brevbaker.api.model.ElementTags
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull

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
        val markup: LetterMarkup.ParagraphContent.Text.Variable = variabel.toMarkup() as LetterMarkup.ParagraphContent.Text.Variable
        assertThat(markup.tags).isEmpty()
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