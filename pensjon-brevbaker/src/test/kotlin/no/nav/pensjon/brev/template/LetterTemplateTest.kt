package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.something.Fagdelen
import no.nav.pensjon.brev.template.dsl.argument
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException

class LetterTemplateTest {

    val returAdresse: Fagdelen.ReturAdresse = Fagdelen.ReturAdresse(
        "NAV Familie- og pensjonsytelser Steinkjer",
        "Postboks 6600 Etterstad",
        "0607",
        "OSLO"
    )
    val title = title(Language.Bokmal to "test")


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
            createTemplate("test", title, MasterMal, languages(Language.Bokmal)) {
                outline {
                    eval(argument(KortNavn))
                }
            }
        }
    }

    @Test
    fun `constructor allows use of required arguments`() {
        createTemplate("test", title, MasterMal, languages(Language.Bokmal)) {
            parameters { required { KortNavn } }
            outline {
                eval(argument(KortNavn))
            }
        }
    }
}