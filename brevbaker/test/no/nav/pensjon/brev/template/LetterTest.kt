package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.something.Fagdelen
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.OutputStream
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class LetterTest {

    val returAdresse: Fagdelen.ReturAdresse = Fagdelen.ReturAdresse(
        "NAV Familie- og pensjonsytelser Steinkjer",
        "Postboks 6600 Etterstad",
        "0607",
        "OSLO",
        123
    )

    object TestMaster : BaseTemplate() {
        override val parameters = setOf(
            RequiredParameter(ReturAdresse)
        )

        override fun render(letter: Letter, out: OutputStream) {
            TODO("Not yet implemented")
        }

    }

    val template = createTemplate("test", TestMaster) { }

    @Test
    fun `constructor validates required parameters are present`() {
        assertThrows<IllegalArgumentException> {
            Letter(template, emptyMap())
        }
    }

    @Test
    fun `untypedArg fetching argument that is not part of template throws exception`() {
        val letter = Letter(template, mapOf(KortNavn to "mitt kort navn", ReturAdresse to returAdresse))

        assertThrows<IllegalArgumentException> {
            letter.untypedArg(KortNavn)
        }
    }

    @Test
    fun `requiredArg fetching argument that is part of base-template is successful`() {
        val letter = Letter(template, mapOf(ReturAdresse to returAdresse))

        letter.untypedArg(ReturAdresse)
    }

    @Test
    fun `requiredArg fetching argument that is part of template is successful`() {
        val template = createTemplate("test", TestMaster) {
            parameters { required { KortNavn } }
        }
        val letter = Letter(template, mapOf(KortNavn to "mitt navn", ReturAdresse to returAdresse))

        letter.untypedArg(KortNavn)
    }

    @Test
    fun `requiredArg passed arguments can be fetched`() {

        val actual: Fagdelen.ReturAdresse =
            Letter(template, mapOf(ReturAdresse to returAdresse)).requiredArg(ReturAdresse)

        assertEquals(returAdresse, actual)
    }

    @Test
    fun `requiredArg passed arguments with incorrect type throws exception`() {
        val letter = Letter(template, mapOf(ReturAdresse to "returAdresse"))

        assertThrows<IllegalStateException> {
            letter.requiredArg(ReturAdresse)
        }
    }

    @Test
    fun `requiredArg fetching missing required argument throws exception`() {
        val args = mutableMapOf<Parameter, Any>(
            ReturAdresse to returAdresse
        )
        val letter = Letter(template, args)
        args.remove(ReturAdresse)

        assertThrows<IllegalStateException> {
            letter.requiredArg(ReturAdresse)
        }
    }

    @Test
    fun `optionalArg passed arguments can be fetched`() {
        val template = createTemplate("test", TestMaster) {
            parameters { optional { KortNavn } }
        }
        val letter = Letter(template, mapOf(ReturAdresse to returAdresse))

        assertEquals(returAdresse, letter.optionalArg(ReturAdresse))
    }

    @Test
    fun `optionalArg will not fail for missing optional argument`() {
        val template = createTemplate("test", TestMaster) {
            parameters { optional { KortNavn } }
        }
        val letter = Letter(template, mapOf(ReturAdresse to returAdresse))

        letter.optionalArg(KortNavn)
    }

    @Test
    fun `optionalArg passed arguments with incorrect type throws exception`() {
        val letter = Letter(template, mapOf(ReturAdresse to "returAdresse"))

        assertThrows<IllegalStateException> {
            letter.requiredArg(ReturAdresse)
        }
    }


}