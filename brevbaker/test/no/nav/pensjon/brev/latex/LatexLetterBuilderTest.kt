package no.nav.pensjon.brev.latex

import no.nav.pensjon.brev.dto.Letter
import no.nav.pensjon.brev.dto.LetterTemplate
import no.nav.pensjon.brev.dto.StandardFields
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class LatexLetterBuilderTest {
    @Tag("integration")
    @Test
    fun `basic integration with container`() {
        val latexLetterBuilder = LatexLetterBuilder(LaTeXCompilerService())
        val result = latexLetterBuilder.buildLatex(
            Letter(
                StandardFields(
                    returAdresse = "",
                    postnummer = "",
                    poststed = "",
                    land = "",
                    mottakerNavn = "",
                    verge = "",
                    adresseLinje1 = "",
                    adresseLinje2 = "",
                    adresseLinje3 = "",
                    dokumentDato = "",
                    saksnummer = "",
                    sakspartNavn = "",
                    sakspartId = "",
                    kontakTelefonnummer = "",
                ),
                LetterTemplate("test")
            )
        )
        assertNotNull(result.pdf)
    }
}