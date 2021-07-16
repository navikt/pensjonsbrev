package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.something.Fagdelen
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.OutputStream

class ExpressionEvalTest {
    object TestMaster : BaseTemplate() {
        override val parameters = setOf(
            RequiredParameter(SaksNr)
        )

        override fun render(letter: Letter, out: OutputStream) {
            TODO("Not yet implemented")
        }

    }
    val template = createTemplate("test", TestMaster) {
        parameters {
            required { KortNavn }
            optional { Penger }
        }
    }
    val letter = Letter(template, mapOf(
        SaksNr to 123,
        KortNavn to "mitt navn",
        Penger to 1337,
    ))

    @Test
    fun `eval Literal returns literal`() {
        val evaluated: Int = Expression.Literal(5).eval(letter)

        assertEquals(5, evaluated)
    }

    @Test
    fun `eval Argument returns argument value`() {
        val evaluated: String = Expression.Argument(KortNavn).eval(letter)

        assertEquals("mitt navn", evaluated)
    }

    @Test
    fun `eval OptionalArgument returns argument value`() {
        val evaluated: Int? = Expression.OptionalArgument(Penger).eval(letter)

        assertEquals(1337, evaluated)
    }

    @Test
    fun `eval BinaryInvoke returns expected value`() {
        val evaluated: Boolean = Expression.BinaryInvoke(
            Expression.Literal(1),
            Expression.Literal(2),
            BinaryOperation.GreaterThan()
        ).eval(letter)

        assertEquals(false, evaluated)
    }

    @Test
    fun `eval UnaryInvoke returns expected value`() {
        val evaluated: String = Expression.UnaryInvoke(
            Expression.Literal(4),
            UnaryOperation.ToString()
        ).eval(letter)

        assertEquals("4", evaluated)
    }

    @Test
    fun `eval Select returns field`() {
        val returAdresse = Fagdelen.ReturAdresse("navn", "addr1", "postnr", "poststed", 2)
        val evaluated: String = Expression.Select(
            Expression.Literal(returAdresse),
            Fagdelen.ReturAdresse::navEnhetsNavn
        ).eval(letter)

        assertEquals(returAdresse.navEnhetsNavn, evaluated)
    }
}