package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.dto.Felles
import no.nav.pensjon.brev.template.base.DummyBase
import no.nav.pensjon.brev.template.dsl.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class ExpressionEvalTest {
    object TestMaster : DummyBase()

    data class SomeDto(val name: String, val kortNavn: String?)

    val template = createTemplate("test", TestMaster, SomeDto::class, languages(Language.Bokmal), newText(Language.Bokmal to "test")) { }

    val letter = Letter(template, SomeDto("Ole", null), Language.Bokmal, Fixtures.felles)
    val argumentExpr = Expression.LetterProperty(Letter<SomeDto>::argument)
    val fellesExpr = Expression.LetterProperty(Letter<SomeDto>::felles)

    @Test
    fun `eval Literal returns literal`() {
        val evaluated: Int = Expression.Literal(5).eval(letter)

        assertEquals(5, evaluated)
    }

    @Test
    fun `eval Argument returns argument value`() {
        val evaluated: String = argumentExpr.select(SomeDto::name).eval(letter)

        assertEquals(letter.argument.name, evaluated)
    }

    @Test
    fun `eval optional argument field returns argument value`() {
        val evaluated: String? = argumentExpr.select(SomeDto::kortNavn).eval(letter.copy(argument = letter.argument.copy(kortNavn = "O")))

        assertEquals("O", evaluated)
    }

    @Test
    fun `eval optional argument field without value returns null`() {
        val evaluated: String? = argumentExpr.select(SomeDto::kortNavn).eval(letter)

        assertNull(letter.argument.kortNavn) // tester ingenting om kortNavn har en verdi
        assertNull(evaluated)
    }

    @Test
    fun `eval can give default value for optional argument field`() {
        val evaluated = argumentExpr.select(SomeDto::kortNavn).ifNull("J").eval(letter)
        argumentExpr.select(SomeDto::name).ifNull("J").eval(letter)

        assertEquals("J", evaluated)
    }

    @Test
    fun `eval BinaryInvoke returns expected value`() {
        val evaluated: String = Expression.BinaryInvoke(
            Expression.Literal("h"),
            Expression.Literal("ei"),
            BinaryOperation.Concat
        ).eval(letter)

        assertEquals("hei", evaluated)
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
    fun `eval can select fields from felles`() {
        val evaluated = fellesExpr.select(Felles::saksnummer).eval(letter)
        assertEquals(Fixtures.felles.saksnummer, evaluated)
    }
}