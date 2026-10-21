package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Variable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UpdateVariablesTest {

    @Test
    fun `oppdaterer variabler uten aa ta inn strukturelle endringer fra rendret brev`() {
        val edited = editedLetter(deleted = listOf(3)) {
            title1(id = 1) {
                literal(id = 11, text = "Tittel ")
                variable(id = 12, text = "gammel tittelverdi")
            }
            paragraph(id = 2, deletedContent = listOf(23)) {
                literal(id = 21, text = "Uendret ")
                variable(id = 22, text = "100 kr")
            }
            paragraph {
                literal(text = "Ny fritekst ")
                variable(id = 51, text = "gammel")
            }
        }
        val rendered = editedLetter {
            title1(id = 1) {
                literal(id = 11, text = "Endret maltekst ")
                variable(id = 12, text = "ny tittelverdi")
            }
            paragraph(id = 2) {
                literal(id = 21, text = "Uendret ")
                variable(id = 22, text = "200 kr")
                literal(id = 23, text = "slettet innhold")
                literal(id = 24, text = "nytt innhold i malen")
            }
            paragraph(id = 3) { literal(id = 31, text = "slettet avsnitt") }
            paragraph(id = 4) { literal(id = 41, text = "nytt avsnitt i malen") }
            paragraph(id = 5) { variable(id = 51, text = "ny") }
        }.toMarkup()

        val expected = editedLetter(deleted = listOf(3)) {
            title1(id = 1) {
                literal(id = 11, text = "Tittel ")
                variable(id = 12, text = "ny tittelverdi")
            }
            paragraph(id = 2, deletedContent = listOf(23)) {
                literal(id = 21, text = "Uendret ")
                variable(id = 22, text = "200 kr")
            }
            paragraph {
                literal(text = "Ny fritekst ")
                variable(id = 51, text = "ny")
            }
        }

        assertEquals(expected, edited.updateVariables(rendered))
    }

    @Test
    fun `oppdaterer variabler i brevtittel, punktlister og tabeller`() {
        fun brev(tittel: String, punkt: String, header: String, celle: String) =
            editedLetter {
                paragraph(id = 1) {
                    itemList(id = 11) {
                        item(id = 111) { variable(id = 1111, text = punkt) }
                    }
                    table(id = 12) {
                        header(id = 121) { colSpec(id = 1211, cellId = 12111) { variable(id = 121111, text = header) } }
                        row(id = 122) { cell(id = 1221) { variable(id = 12211, text = celle) } }
                    }
                }
            }.let { it.copy(title = Edit.Title(listOf(Variable(id = 9, text = tittel)))) }

        val edited = brev("gammel tittel", "gammelt punkt", "gammel header", "gammel celle")
        val rendered = brev("ny tittel", "nytt punkt", "ny header", "ny celle").toMarkup()

        assertEquals(brev("ny tittel", "nytt punkt", "ny header", "ny celle"), edited.updateVariables(rendered))
    }

    @Test
    fun `variabel som mangler i rendret brev beholdes med gammel verdi`() {
        val edited = editedLetter {
            paragraph(id = 1) { variable(id = 11, text = "gammel verdi") }
        }
        val rendered = editedLetter {
            paragraph(id = 1) { literal(id = 12, text = "ny maltekst") }
        }.toMarkup()

        assertEquals(edited, edited.updateVariables(rendered))
    }
}
