package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class BaseTest {

    @Test
    fun `ifElse creates a BinaryInvoke with branches as a tuple`() {
        val actual = ifElse(true.expr(), "hei".expr(), "hade bra".expr())
        val expected = Expression.BinaryInvoke(
            first = true.expr(),
            second = Expression.BinaryInvoke("hei".expr(), "hade bra".expr(), BinaryOperation.Tuple()),
            operation = BinaryOperation.IfElse()
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `ifElse expression evaluates to correct branch`() {
        val expr = ifElse(Expression.FromScope(ExpressionScope<Int, *>::argument) equalTo 2, "hei", "hade bra")

        val scope = ExpressionScope(2, Fixtures.felles, Language.Bokmal)

        assertEquals("hei", expr.eval(scope))
        assertEquals("hade bra", expr.eval(ExpressionScope(3, scope.felles, scope.language)))
    }

    enum class TestEnum{
        YES, NO, MAYBE
    }

    @Test
    fun `isOneOf positive match`() {
        val expr = TestEnum.YES.expr().isOneOf(TestEnum.YES)
        val scope = ExpressionScope(2, Fixtures.felles, Language.Bokmal)

        assertTrue(expr.eval(ExpressionScope(3, scope.felles, scope.language)))
    }

    @Test
    fun `isOneOf positive match with multiple values`() {
        val expr = TestEnum.YES.expr().isOneOf(TestEnum.NO, TestEnum.YES, TestEnum.MAYBE)
        val scope = ExpressionScope(2, Fixtures.felles, Language.Bokmal)

        assertTrue(expr.eval(ExpressionScope(3, scope.felles, scope.language)))
    }

    @Test
    fun `isOneOf negative match`() {
        val expr = TestEnum.YES.expr().isOneOf(TestEnum.NO)
        val scope = ExpressionScope(2, Fixtures.felles, Language.Bokmal)

        assertFalse(expr.eval(ExpressionScope(3, scope.felles, scope.language)))
    }

    @Test
    fun `isNotAnyOf positive match`() {
        val expr = TestEnum.YES.expr().isNotAnyOf(TestEnum.NO)
        val scope = ExpressionScope(2, Fixtures.felles, Language.Bokmal)

        assertTrue(expr.eval(ExpressionScope(3, scope.felles, scope.language)))
    }

    @Test
    fun `isNotAnyOf positive match with multiple values`() {
        val expr = TestEnum.YES.expr().isNotAnyOf(TestEnum.NO, TestEnum.MAYBE)
        val scope = ExpressionScope(2, Fixtures.felles, Language.Bokmal)

        assertTrue(expr.eval(ExpressionScope(3, scope.felles, scope.language)))
    }

    @Test
    fun `isNotAnyOf negative match`() {
        val expr = TestEnum.YES.expr().isNotAnyOf(TestEnum.YES)
        val scope = ExpressionScope(2, Fixtures.felles, Language.Bokmal)

        assertFalse(expr.eval(ExpressionScope(3, scope.felles, scope.language)))
    }

    @Test
    fun `EqualTo positive match for expression`() {
        val expr = 55.expr().equalTo(55.expr())
        val scope = ExpressionScope(2, Fixtures.felles, Language.Bokmal)

        assertTrue(expr.eval(ExpressionScope(3, scope.felles, scope.language)))
    }

    @Test
    fun `EqualTo negative match for expression`() {
        val expr = 44.expr().equalTo(55.expr())
        val scope = ExpressionScope(2, Fixtures.felles, Language.Bokmal)

        assertFalse(expr.eval(ExpressionScope(3, scope.felles, scope.language)))
    }

    @Test
    fun `EqualTo positive match for literal`() {
        val expr = 55.expr().equalTo(55)
        val scope = ExpressionScope(2, Fixtures.felles, Language.Bokmal)

        assertTrue(expr.eval(ExpressionScope(3, scope.felles, scope.language)))
    }

    @Test
    fun `EqualTo negative match for literal`() {
        val expr = 44.expr().equalTo(55)
        val scope = ExpressionScope(2, Fixtures.felles, Language.Bokmal)

        assertFalse(expr.eval(ExpressionScope(3, scope.felles, scope.language)))
    }

    @Test
    fun `NotEqualTo positive match for expression`() {
        val expr = 44.expr().notEqualTo(55.expr())
        val scope = ExpressionScope(2, Fixtures.felles, Language.Bokmal)

        assertTrue(expr.eval(ExpressionScope(3, scope.felles, scope.language)))
    }

    @Test
    fun `NotEqualTo negative match for expression`() {
        val expr = 55.expr().notEqualTo(55.expr())
        val scope = ExpressionScope(2, Fixtures.felles, Language.Bokmal)

        assertFalse(expr.eval(ExpressionScope(3, scope.felles, scope.language)))
    }

    @Test
    fun `NotEqualTo positive match for literal`() {
        val expr = 44.expr().notEqualTo(55)
        val scope = ExpressionScope(2, Fixtures.felles, Language.Bokmal)

        assertTrue(expr.eval(ExpressionScope(3, scope.felles, scope.language)))
    }

    @Test
    fun `NotEqualTo negative match for literal`() {
        val expr = 55.expr().notEqualTo(55)
        val scope = ExpressionScope(2, Fixtures.felles, Language.Bokmal)

        assertFalse(expr.eval(ExpressionScope(3, scope.felles, scope.language)))
    }


}