package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.Felles
import no.nav.pensjon.brev.template.dsl.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class ExpressionEvalTest {

    data class SomeDto(val name: String, val kortNavn: String?)

    val scope = ExpressionScope(SomeDto("Ole", null), Fixtures.felles, Language.Bokmal)
    val argumentExpr = Expression.FromScope(ExpressionScope<SomeDto, *>::argument)
    val fellesExpr = Expression.FromScope(ExpressionScope<SomeDto, *>::felles)

    @Test
    fun `eval Literal returns literal`() {
        val evaluated: Int = Expression.Literal(5).eval(scope)

        assertEquals(5, evaluated)
    }

    @Test
    fun `eval Argument returns argument value`() {
        val evaluated: String = argumentExpr.select(SomeDto::name).eval(scope)

        assertEquals(scope.argument.name, evaluated)
    }

    @Test
    fun `eval optional argument field returns argument value`() {
        val evaluated: String? = argumentExpr.select(SomeDto::kortNavn).eval(scope.copy(argument = scope.argument.copy(kortNavn = "O")))

        assertEquals("O", evaluated)
    }

    @Test
    fun `eval optional argument field without value returns null`() {
        val evaluated: String? = argumentExpr.select(SomeDto::kortNavn).eval(scope)

        assertNull(scope.argument.kortNavn) // tester ingenting om kortNavn har en verdi
        assertNull(evaluated)
    }

    @Test
    fun `eval can give default value for optional argument field`() {
        val evaluated = argumentExpr.select(SomeDto::kortNavn).ifNull("J").eval(scope)
        argumentExpr.select(SomeDto::name).ifNull("J").eval(scope)

        assertEquals("J", evaluated)
    }

    @Test
    fun `eval BinaryInvoke returns expected value`() {
        val evaluated: String = Expression.BinaryInvoke(
            Expression.Literal("h"),
            Expression.Literal("ei"),
            BinaryOperation.Concat
        ).eval(scope)

        assertEquals("hei", evaluated)
    }

    @Test
    fun `eval UnaryInvoke returns expected value`() {
        val evaluated: String = Expression.UnaryInvoke(
            Expression.Literal(4),
            UnaryOperation.ToString()
        ).eval(scope)

        assertEquals("4", evaluated)
    }

    @Test
    fun `eval can select fields from felles`() {
        val evaluated = fellesExpr.select(Felles::saksnummer).eval(scope)
        assertEquals(Fixtures.felles.saksnummer, evaluated)
    }

    @Test
    fun `eval FromScope will select a value from scope`() {
        val evaluated: Language = Expression.FromScope(ExpressionScope<SomeDto, *>::language).eval(scope)
        assertEquals(scope.language, evaluated)
    }

}