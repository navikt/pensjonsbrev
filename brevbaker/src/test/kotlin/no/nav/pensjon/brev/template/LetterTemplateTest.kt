package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.something.Fagdelen
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.OutputStream
import java.lang.IllegalArgumentException

class LetterTemplateTest {

    val returAdresse: Fagdelen.ReturAdresse = Fagdelen.ReturAdresse(
        "NAV Familie- og pensjonsytelser Steinkjer",
        "Postboks 6600 Etterstad",
        "0607",
        "OSLO",
        123
    )

    object MasterMal : BaseTemplate() {

        override val parameters = setOf(
            RequiredParameter(ReturAdresse)
        )

        override fun render(letter: Letter): RenderedLetter {
            TODO()
        }

    }


    @Test
    fun `constructor validates that outline doesnt use non-required arguments as required`() {
        assertThrows<IllegalArgumentException> {
            createTemplate("test", MasterMal, languages(Language.Bokmal)) {
                outline {
                    eval(argument(KortNavn))
                }
            }
        }
    }

    @Test
    fun `constructor allows use of required arguments`() {
        createTemplate("test", MasterMal, languages(Language.Bokmal)) {
            parameters { required { KortNavn } }
            outline {
                eval(argument(KortNavn))
            }
        }
    }
}